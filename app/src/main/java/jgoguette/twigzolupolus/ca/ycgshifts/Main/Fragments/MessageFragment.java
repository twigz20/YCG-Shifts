package jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import jgoguette.twigzolupolus.ca.ycgshifts.Main.Adapters.MessageAdapter;
import jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.Messages.MessagePresenter;
import jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.Messages.MessagePresenterImpl;
import jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.Messages.MessageView;
import jgoguette.twigzolupolus.ca.ycgshifts.Main.MainActivity;
import jgoguette.twigzolupolus.ca.ycgshifts.Model.Message;
import jgoguette.twigzolupolus.ca.ycgshifts.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment implements MessageView, MessageAdapter.ViewHolder.ClickListener {

    private Context context;

    RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    MessagePresenter presenter;

    private MessageAdapter messageAdapter;
    private ActionModeCallback actionModeCallback = new ActionModeCallback();
    private ActionMode actionMode;
    private ArrayList<Message> messages = new ArrayList<>();

    public MessageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        presenter = new MessagePresenterImpl(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        setTitle();

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToSendMessage();
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadMessages();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        presenter.loadMessages();
        presenter.clearNotifications();

        return view;
    }

    @Override
    public void setTitle() {
        ((MainActivity)context).setTitle(R.string.Messages_Title);
    }

    @Override
    public void onMessagesLoadedSuccess(ArrayList<Message> messages, DatabaseReference notificationRef) {
        this.messages = messages;
        messageAdapter = new MessageAdapter(messages, notificationRef, this);
        recyclerView.setAdapter(messageAdapter);
    }

    @Override
    public void onMessagesLoadedFailure() {

    }

    @Override
    public void navigateToSendMessage() {
        SendMessageFragment fragment =  new SendMessageFragment();
        FragmentManager manager =
                ((MainActivity)context).getSupportFragmentManager();
        manager.beginTransaction().replace(
                R.id.fragmentContainer,
                fragment,
                fragment.getTag()
        ).addToBackStack(null).commit();
    }

    @Override
    public void navigateToReadMessage(Message message) {
        ReadMessageFragment fragment = ReadMessageFragment.newInstance(message);
        FragmentManager manager = ((MainActivity)context).getSupportFragmentManager();
        manager.beginTransaction().replace(
                R.id.fragmentContainer,
                fragment,
                fragment.getTag()
        ).addToBackStack(null).commit();
    }

    @Override
    public void navigateToReadShiftTradeMessage(Message message) {
        ReadShiftTradeFragment fragment = ReadShiftTradeFragment.newInstance(message);
        FragmentManager manager = ((MainActivity)context).getSupportFragmentManager();
        manager.beginTransaction().replace(
                R.id.fragmentContainer,
                fragment,
                fragment.getTag()
        ).addToBackStack(null).commit();
    }

    @Override
    public void onItemClicked(int position) {
        if (actionMode != null) {
            toggleSelection(position);
            return;
        }

        messageAdapter.markItemRead(position);
        if(messages.get(position).getType().equals(messages.get(position)
                .convertTypeToString(Message.Type.SHIFT_SWAP_NOTIF))) {

            navigateToReadShiftTradeMessage(messages.get(position));
        } else {
            navigateToReadMessage(messages.get(position));
        }
    }

    @Override
    public boolean onItemLongClicked(int position) {
        if (actionMode == null) {
            actionMode = ((MainActivity)context)
                    .startSupportActionMode(actionModeCallback);
        }

        toggleSelection(position);

        return true;
    }

    /**
     * Toggle the selection state of an item.
     *
     * If the item was the last one in the selection and is unselected, the selection is stopped.
     * Note that the selection must already be started (actionMode must not be null).
     *
     * @param position Position of the item to toggle the selection state
     */
    private void toggleSelection(int position) {
        messageAdapter.toggleSelection(position);
        int count = messageAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count) + " item(s) selected");
            actionMode.invalidate();
        }
    }

    private class ActionModeCallback implements ActionMode.Callback {
        @SuppressWarnings("unused")
        private final String TAG = ActionModeCallback.class.getSimpleName();

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate (R.menu.menu_selected, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_delete_items:
                    messageAdapter.removeItems(messageAdapter.getSelectedItems());
                    mode.finish();
                    return true;

                case R.id.menu_mark_read:
                    messageAdapter.markItemsRead(messageAdapter.getSelectedItems());
                    mode.finish();
                    return true;

                case R.id.menu_mark_unread:
                    messageAdapter.markItemsUnread(messageAdapter.getSelectedItems());
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            messageAdapter.clearSelection();
            actionMode = null;
        }
    }
}

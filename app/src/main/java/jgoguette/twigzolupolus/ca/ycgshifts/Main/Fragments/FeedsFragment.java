package jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;

import jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.Feeds.FeedsPresenter;
import jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.Feeds.FeedsPresenterImpl;
import jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.Feeds.FeedsView;
import jgoguette.twigzolupolus.ca.ycgshifts.Main.MainActivity;
import jgoguette.twigzolupolus.ca.ycgshifts.Main.Views.FeedHolder;
import jgoguette.twigzolupolus.ca.ycgshifts.Model.Feed;
import jgoguette.twigzolupolus.ca.ycgshifts.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class FeedsFragment extends Fragment implements FeedsView {
    private Context context;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private FeedsPresenter presenter;

    // FireBase Variables
    FirebaseAuth firebaseAuth;

    public FeedsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        presenter = new FeedsPresenterImpl(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        setTitle();

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(linearLayoutManager);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadFeeds();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        loadFeeds();

        return view;
    }

    @Override
    public void loadFeeds() {
        presenter.loadFeeds();
    }

    @Override
    public void onFeedsLoadedSuccess(FirebaseRecyclerAdapter<Feed, FeedHolder> firebaseRecyclerAdapter) {
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onFeedsLoadedFailure() {

    }

    @Override
    public void setTitle() {
        ((MainActivity)context).setTitle(((MainActivity)context).user.getName());
    }
}

package jgoguette.twigzolupolus.ca.ycgshifts.Main.Adapters;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import jgoguette.twigzolupolus.ca.ycgshifts.Model.Message;
import jgoguette.twigzolupolus.ca.ycgshifts.R;

/**
 * Created by jerry on 2016-11-15.
 */

public class MessageAdapter extends SelectableAdapter<MessageAdapter.ViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = MessageAdapter.class.getSimpleName();

    private static final int TYPE_UNREAD = 0;
    private static final int TYPE_READ = 1;

    private ViewHolder.ClickListener clickListener;

    List<Message> messages = new ArrayList<>();

    private DatabaseReference mFeedRef;

    public MessageAdapter(List<Message> messages, DatabaseReference mFeedRef,
                          ViewHolder.ClickListener clickListener) {
        super();

        this.mFeedRef = mFeedRef;
        this.messages = messages;
        this.clickListener = clickListener;
    }

    public void removeItem(int position) {
        mFeedRef.child(messages.get(position).getKey()).removeValue();
        messages.remove(position);
        notifyItemRemoved(position);
    }

    public void removeItems(List<Integer> positions) {
        // Reverse-sort the list
        Collections.sort(positions, new Comparator<Integer>() {
            @Override
            public int compare(Integer lhs, Integer rhs) {
                return rhs - lhs;
            }
        });

        // Split the list in ranges
        while (!positions.isEmpty()) {
            if (positions.size() == 1) {
                removeItem(positions.get(0));
                positions.remove(0);
            } else {
                int count = 1;
                while (positions.size() > count && positions.get(count).equals(positions.get(count - 1) - 1)) {
                    ++count;
                }

                if (count == 1) {
                    removeItem(positions.get(0));
                } else {
                    removeRange(positions.get(count - 1), count);
                }

                for (int i = 0; i < count; ++i) {
                    positions.remove(0);
                }
            }
        }
    }

    private void removeRange(int positionStart, int itemCount) {
        for (int i = 0; i < itemCount; ++i) {
            mFeedRef.child(messages.get(positionStart).getKey()).removeValue();
            messages.remove(positionStart);
        }
        notifyItemRangeRemoved(positionStart, itemCount);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final int layout = viewType == TYPE_READ ? R.layout.message_read : R.layout.message_unread;

        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(v, clickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Message message = this.messages.get(position);

        holder.sender.setText(message.getSender());
        holder.subject.setText(message.getSubject());
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                message.getTimeStamp(),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        holder.timeStamp.setText(timeAgo);

        // Highlight the item if it's selected
        holder.selectedOverlay.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        final Message messages = this.messages.get(position);

        return messages.getRead() ? TYPE_READ : TYPE_UNREAD;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {
        @SuppressWarnings("unused")
        private static final String TAG = ViewHolder.class.getSimpleName();

        TextView sender;
        TextView subject;
        TextView timeStamp;
        View selectedOverlay;

        private ClickListener listener;

        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);

            sender = (TextView) itemView.findViewById(R.id.messages_sender);
            subject = (TextView) itemView.findViewById(R.id.messages_subject);
            timeStamp = (TextView) itemView.findViewById(R.id.messages_timeStamp);
            selectedOverlay = itemView.findViewById(R.id.selected_overlay);

            this.listener = listener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClicked(getLayoutPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (listener != null) {
                return listener.onItemLongClicked(getLayoutPosition());
            }

            return false;
        }

        public interface ClickListener {
            void onItemClicked(int position);

            boolean onItemLongClicked(int position);
        }
    }
}

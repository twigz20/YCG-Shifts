package jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.Feeds;

import android.content.Context;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import jgoguette.twigzolupolus.ca.ycgshifts.Main.Views.FeedHolder;
import jgoguette.twigzolupolus.ca.ycgshifts.Model.Feed;
import jgoguette.twigzolupolus.ca.ycgshifts.R;

/**
 * Created by jerry on 2016-11-14.
 */

public class FeedsInteractorImpl implements FeedsInteractor {

    private Context context;
    private OnFeedsFetchedListener listener;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    public FeedsInteractorImpl(Context context, OnFeedsFetchedListener listener) {
        this.context = context;
        this.listener = listener;

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void getFeeds() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Query lastFifty = databaseReference.child(context.getString(R.string.feeds_table))
                .limitToLast(Integer.parseInt(context.getString(R.string.feeds_table_view_limit)));
        FirebaseRecyclerAdapter<Feed, FeedHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Feed, FeedHolder>(
                Feed.class, R.layout.feed, FeedHolder.class, lastFifty) {

            @Override
            public void populateViewHolder(FeedHolder FeedView, Feed Feed, int position) {
                FeedView.setName(Feed.getName());
                FeedView.setStatus(Feed.getStatus());
                FeedView.setTimeStamp(Feed.getTimeStamp());
                FeedView.setProfilePic(context, Feed.getFirebaseId());
            }
        };

        listener.onSuccess(firebaseRecyclerAdapter);
    }
}

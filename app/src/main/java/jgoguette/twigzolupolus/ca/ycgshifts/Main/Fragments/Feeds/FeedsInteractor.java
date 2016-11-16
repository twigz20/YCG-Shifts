package jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.Feeds;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

import jgoguette.twigzolupolus.ca.ycgshifts.Main.Views.FeedHolder;
import jgoguette.twigzolupolus.ca.ycgshifts.Model.Feed;

/**
 * Created by jerry on 2016-11-14.
 */

public interface FeedsInteractor {
    interface OnFeedsFetchedListener {
        void onFailure();
        void onSuccess(FirebaseRecyclerAdapter<Feed, FeedHolder> firebaseRecyclerAdapter);
    }

    void getFeeds();
}

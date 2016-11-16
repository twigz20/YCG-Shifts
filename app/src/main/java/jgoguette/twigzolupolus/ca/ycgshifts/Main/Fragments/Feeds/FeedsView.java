package jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.Feeds;

import android.content.Context;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

import jgoguette.twigzolupolus.ca.ycgshifts.Main.Views.FeedHolder;
import jgoguette.twigzolupolus.ca.ycgshifts.Model.Feed;

/**
 * Created by jerry on 2016-11-14.
 */

public interface FeedsView {

    Context getContext();
    void setTitle();
    void loadFeeds();
    void onFeedsLoadedSuccess(FirebaseRecyclerAdapter<Feed, FeedHolder> firebaseRecyclerAdapter);
    void onFeedsLoadedFailure();
}

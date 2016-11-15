package jgoguette.twigzolupolus.ca.mvptest.Main.Fragments.Feeds;

import android.content.Context;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

import jgoguette.twigzolupolus.ca.mvptest.Main.Views.FeedHolder;
import jgoguette.twigzolupolus.ca.mvptest.Model.Feed;

/**
 * Created by jerry on 2016-11-14.
 */

public interface FeedsView {

    Context getMainContext();
    void loadFeeds();
    void onFeedsLoadedSuccess(FirebaseRecyclerAdapter<Feed, FeedHolder> firebaseRecyclerAdapter);
    void onFeedsLoadedFailure();
}

package jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.Feeds;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

import jgoguette.twigzolupolus.ca.ycgshifts.Main.Views.FeedHolder;
import jgoguette.twigzolupolus.ca.ycgshifts.Model.Feed;

/**
 * Created by jerry on 2016-11-14.
 */

public class FeedsPresenterImpl implements FeedsPresenter, FeedsInteractor.OnFeedsFetchedListener {

    private FeedsView feedsView;
    private FeedsInteractor feedsInteractor;

    public FeedsPresenterImpl(FeedsView feedsView) {
        this.feedsView = feedsView;
        feedsInteractor = new FeedsInteractorImpl(feedsView.getContext(), this);
    }

    @Override
    public void loadFeeds() {
        feedsInteractor.getFeeds();
    }

    @Override
    public void onDestroy() {
        feedsView = null;
    }

    @Override
    public void onFailure() {
        feedsView.onFeedsLoadedFailure();
    }

    @Override
    public void onSuccess(FirebaseRecyclerAdapter<Feed, FeedHolder> firebaseRecyclerAdapter) {
        feedsView.onFeedsLoadedSuccess(firebaseRecyclerAdapter);
    }
}

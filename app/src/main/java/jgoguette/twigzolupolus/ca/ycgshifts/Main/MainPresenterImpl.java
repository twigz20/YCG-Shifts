package jgoguette.twigzolupolus.ca.ycgshifts.Main;

import jgoguette.twigzolupolus.ca.ycgshifts.Model.User;

/**
 * Created by jerry on 2016-11-14.
 */

public class MainPresenterImpl implements MainPresenter, MainInteractor.OnUserProfileFetchedFinishedListener {

    private MainView mainView;
    private MainInteractor mainInteractor;

    public MainPresenterImpl(MainView mainView) {
        this.mainView = mainView;
        mainInteractor = new MainInteractorImpl(mainView.getContext(), this);
    }

    @Override
    public void onSuccess(User user) {
        if(mainView != null) {
            mainView.setUserProfile(user);
            mainView.hideProgress();
            mainView.onProfileSetComplete();
        }
    }

    @Override
    public void onFailure(String message) {
        if(mainView != null) {

        }
    }

    @Override
    public void getUserProfile() {
        if (mainView != null) {
            mainView.showProgress();
        }

        mainInteractor.getUserProfile();
    }

    @Override
    public void onDestroy() {
        mainView = null;
    }
}

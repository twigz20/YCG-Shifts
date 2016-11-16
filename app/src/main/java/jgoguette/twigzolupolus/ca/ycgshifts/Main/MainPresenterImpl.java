package jgoguette.twigzolupolus.ca.ycgshifts.Main;

import android.net.Uri;

import java.util.ArrayList;

import jgoguette.twigzolupolus.ca.ycgshifts.Model.User;

/**
 * Created by jerry on 2016-11-14.
 */

public class MainPresenterImpl implements MainPresenter,
        MainInteractor.OnUserProfileFetchedFinishedListener, MainInteractor.OnProfilePicFetchedListener {

    private MainView mainView;
    private MainInteractor mainInteractor;

    public MainPresenterImpl(MainView mainView) {
        this.mainView = mainView;
        mainInteractor = new MainInteractorImpl(mainView.getContext(), this, this);
    }

    @Override
    public void onSuccess(User user) {
        if(mainView != null) {
            mainView.hideProgress();
            mainView.onProfileSuccessfullyRetrieved(user);
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
    public void getProfilePic() {
        mainInteractor.getProfilePicUri();
    }

    @Override
    public void uploadProfilePic(Uri mFileUri, Uri fileUri) {
        mainInteractor.uploadProfilePic(mFileUri,fileUri);
    }

    @Override
    public void removeNotification(String notificationKey) {
        mainInteractor.removeNotification(notificationKey);
    }

    @Override
    public void removeNotifications(ArrayList<String> notificationKeys) {
        mainInteractor.removeNotifications(notificationKeys);
    }

    @Override
    public void startNotificationService() {
        mainInteractor.startNotificationService();
    }

    @Override
    public void stopNotificationService() {
        mainInteractor.stopNotificationService();
    }

    @Override
    public void onDestroy() {
        mainView = null;
    }

    @Override
    public void onSuccessProfilePic(Uri uri) {
        if (mainView != null) {
            mainView.setProfilePic(uri);
        }
    }

    @Override
    public void onFailureProfilePic(Uri uri) {
        if (mainView != null) {
            mainView.setProfilePic(uri);
        }
    }
}

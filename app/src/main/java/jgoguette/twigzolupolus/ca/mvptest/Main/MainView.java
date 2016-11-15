package jgoguette.twigzolupolus.ca.mvptest.Main;

import android.content.Context;

import jgoguette.twigzolupolus.ca.mvptest.Model.User;

/**
 * Created by jerry on 2016-11-14.
 */

public interface MainView {
    Context getContext();

    void setUserProfile(User user);

    void setProfilePic();

    void showProgress();

    void hideProgress();

    void onProfileSetComplete();

    void navigateToHome();

    void navigateToMessages();

    void navigateToSendBlast();

    void navigateToSchedule();

    void navigateToSettings();

    void logOut();
}

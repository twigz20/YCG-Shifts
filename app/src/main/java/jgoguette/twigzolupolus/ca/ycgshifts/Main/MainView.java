package jgoguette.twigzolupolus.ca.ycgshifts.Main;

import android.content.Context;

import jgoguette.twigzolupolus.ca.ycgshifts.Model.User;

/**
 * Created by jerry on 2016-11-14.
 */

public interface MainView {
    Context getContext();

    void setUserProfile(User user);

    void setProfileInformation();

    void getProfilePic();

    void showProgress();

    void hideProgress();

    void onProfileSetComplete();

    void navigateToLogin();

    void navigateToHome();

    void navigateToMessages();

    void navigateToSendBlast();

    void navigateToSchedule();

    void navigateToSettings();

    void logOut();
}

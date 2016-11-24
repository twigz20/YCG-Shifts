package jgoguette.twigzolupolus.ca.ycgshifts.Main;

import android.content.Context;
import android.net.Uri;

import jgoguette.twigzolupolus.ca.ycgshifts.Model.Message;
import jgoguette.twigzolupolus.ca.ycgshifts.Model.User;

/**
 * Created by jerry on 2016-11-14.
 */

public interface MainView {
    Context getContext();

    void setProfilePic(Uri uri);

    void onProfileSuccessfullyRetrieved(User user);

    void showProgress();

    void hideProgress();

    void navigateToLogin();

    void navigateToHome();

    void navigateToProfile();

    void navigateToMessages();
    void navigateToReadMessage(Message message);
    void navigateToReadShiftTradeMessage(Message message);

    void navigateToSendBlast();

    void navigateToSchedule();

    void navigateToSettings();

    void logOut();
}

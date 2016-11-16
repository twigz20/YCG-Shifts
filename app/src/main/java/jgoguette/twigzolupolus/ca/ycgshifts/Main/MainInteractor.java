package jgoguette.twigzolupolus.ca.ycgshifts.Main;

import android.net.Uri;

import java.util.ArrayList;

import jgoguette.twigzolupolus.ca.ycgshifts.Model.User;

/**
 * Created by jerry on 2016-11-14.
 */
public interface MainInteractor {

    interface OnUserProfileFetchedFinishedListener {
        void onSuccess(User user);
        void onFailure(String message);
    }

    interface OnProfilePicFetchedListener {
        void onSuccessProfilePic(Uri uri);
        void onFailureProfilePic(Uri uri);
    }

    void getUserProfile();
    void getProfilePicUri();

    void uploadProfilePic(Uri mFileUri, Uri fileUri);

    void removeNotification(String notificationKey);
    void removeNotifications(ArrayList<String> notificationKeys);

    void startNotificationService();
    void stopNotificationService();
}

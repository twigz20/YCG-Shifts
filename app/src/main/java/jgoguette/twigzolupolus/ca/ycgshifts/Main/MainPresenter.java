package jgoguette.twigzolupolus.ca.ycgshifts.Main;

import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by jerry on 2016-11-14.
 */

public interface MainPresenter {
    void getUserProfile();
    void getProfilePic();

    void uploadProfilePic(Uri mFileUri, Uri fileUri);

    void removeNotification(String notificationKey);
    void removeNotifications(ArrayList<String> notificationKeys);

    void startNotificationService();
    void stopNotificationService();

    void onDestroy();
}

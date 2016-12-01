package jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.Messages;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import jgoguette.twigzolupolus.ca.ycgshifts.Model.Message;

/**
 * Created by jerry on 2016-11-15.
 */

public interface MessageInteractor {
    void clearNotifications();

    interface onMessagesLoadedListener {
        void onSuccess(ArrayList<Message> messages, DatabaseReference notificationRef);
        void onFailure(String message);
    }

    void loadMessages();
}

package jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.Messages;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import jgoguette.twigzolupolus.ca.ycgshifts.Model.Message;

/**
 * Created by jerry on 2016-11-15.
 */

public interface MessageView {
    void setTitle();
    Context getContext();

    void onMessagesLoadedSuccess(ArrayList<Message> messages, DatabaseReference notificationRef);
    void onMessagesLoadedFailure();

    void navigateToSendMessage();
    void navigateToReadMessage(Message message);
    void navigateToReadShiftTradeMessage(Message message);
}

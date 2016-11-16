package jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.SendMessage;

import java.util.ArrayList;

import jgoguette.twigzolupolus.ca.ycgshifts.Model.User;

/**
 * Created by jerry on 2016-11-15.
 */

public interface SendMessageInteractor {
    interface onMessageSentListener {
        void onToError();
        void onSubjectError();
        void onMessegeError();
        void onSuccess();
        void onFailure();
    }

    interface onFetchUsernamesListener {
        void onUserNamesFetched(ArrayList<String> userNames);
        void onFailedToFetch(String message);
    }

    void sendMessage(String to, String subject, String message);
    void getUserNames();
    Boolean isParamsValid(String to, String subject, String message);

    void sendMessage(ArrayList<User> users, String sender, String to, String subject, String message);
}

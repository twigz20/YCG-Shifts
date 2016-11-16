package jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.SendMessage;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by jerry on 2016-11-15.
 */

public interface SendMessageView {
    void setToError();

    void setSubjectError();

    void setMessageError();

    void onSuccess();

    void onFailure();

    void navigateBackToMessages();

    Context getContext();

    void getUserNames();

    void onUserNamesFetchedSuccessful(ArrayList<String> userNames);

    void setTitle();
}

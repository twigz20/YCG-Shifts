package jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.Messages;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import jgoguette.twigzolupolus.ca.ycgshifts.Model.Message;

/**
 * Created by jerry on 2016-11-15.
 */

public class MessagePresenterImpl implements MessagePresenter, MessageInteractor.onMessagesLoadedListener {

    MessageView messageView;
    MessageInteractor messageInteractor;

    public MessagePresenterImpl(MessageView messageView) {
        this.messageView = messageView;

        messageInteractor = new MessageInteractorImpl(messageView.getContext(), this);
    }

    @Override
    public void onSuccess(ArrayList<Message> messages, DatabaseReference notificationRef) {
        if(messageView != null) {
            messageView.onMessagesLoadedSuccess(messages, notificationRef);
        }
    }

    @Override
    public void onFailure(String message) {

    }

    @Override
    public void loadMessages() {
        messageInteractor.loadMessages();
    }

    @Override
    public void onDestroy() {

    }
}

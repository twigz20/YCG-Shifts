package jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.SendMessage;

import java.util.ArrayList;

/**
 * Created by jerry on 2016-11-15.
 */

public class SendMessagePresenterImpl implements SendMessagePresenter, SendMessageInteractor.onMessageSentListener, SendMessageInteractor.onFetchUsernamesListener {

    SendMessageView sendMessageView;
    SendMessageInteractor sendMessageInteractor;

    public SendMessagePresenterImpl(SendMessageView sendMessageView) {
        this.sendMessageView = sendMessageView;

        sendMessageInteractor = new SendMessageInteractorImpl(sendMessageView.getContext(), this, this);
    }

    @Override
    public void sendMessage(String to, String subject, String message) {
        sendMessageInteractor.sendMessage(to,subject,message);
    }

    @Override
    public void getUserNames() {
        sendMessageInteractor.getUserNames();
    }

    @Override
    public void onDestroy() {
        sendMessageView = null;
    }

    @Override
    public void onToError() {
        if(sendMessageView != null) {
            sendMessageView.setToError();
        }
    }

    @Override
    public void onSubjectError() {
        if(sendMessageView != null) {
            sendMessageView.setSubjectError();
        }
    }

    @Override
    public void onMessegeError() {
        if(sendMessageView != null) {
            sendMessageView.setMessageError();
        }
    }

    @Override
    public void onSuccess() {
        if(sendMessageView != null) {
            sendMessageView.onSuccess();
        }
    }

    @Override
    public void onFailure() {
        if(sendMessageView != null) {
            sendMessageView.onFailure();
        }
    }

    @Override
    public void onUserNamesFetched(ArrayList<String> userNames) {
        if(sendMessageView != null) {
            sendMessageView.onUserNamesFetchedSuccessful(userNames);
        }
    }

    @Override
    public void onFailedToFetch(String message) {
        if(sendMessageView != null) {
            sendMessageView.onFailure();
        }
    }
}

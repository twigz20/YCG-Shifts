package jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.SendBlast;

/**
 * Created by jerry on 2016-11-15.
 */

public class SendBlastPresenterImpl implements SendBlastPresenter, SendBlastInteractor.OnBlastSentListener {

    private SendBlastView sendBlastView;
    private SendBlastInteractor sendBlastInteractor;

    public SendBlastPresenterImpl(SendBlastView sendBlastView) {
        this.sendBlastView = sendBlastView;

        sendBlastInteractor = new SendBlastInteractorImpl(sendBlastView.getContext(),this);
    }

    @Override
    public void onMessegeError() {
        if(sendBlastView != null) {
            sendBlastView.setMessageError();
        }
    }

    @Override
    public void onSuccess() {
        if(sendBlastView != null) {
            sendBlastView.onSuccess();
        }
    }

    @Override
    public void onFailure() {
        if(sendBlastView != null) {
            sendBlastView.onFailure();
        }
    }

    @Override
    public void sendBlast(String name, String message) {
        sendBlastInteractor.sendBlast(name, message);
    }

    @Override
    public void onDestroy() {
        sendBlastView = null;
    }
}

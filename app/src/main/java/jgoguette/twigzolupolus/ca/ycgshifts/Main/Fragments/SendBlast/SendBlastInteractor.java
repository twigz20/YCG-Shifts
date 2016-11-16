package jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.SendBlast;

/**
 * Created by jerry on 2016-11-15.
 */

public interface SendBlastInteractor {

    interface OnBlastSentListener {
        void onMessegeError();
        void onSuccess();
        void onFailure();
    }

    void sendBlast(String name, String message);

    Boolean isMessageValid(String message);
}

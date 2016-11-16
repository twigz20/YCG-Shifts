package jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.SendMessage;

/**
 * Created by jerry on 2016-11-15.
 */

public interface SendMessagePresenter {
    void sendMessage(String to, String subject, String message);
    void getUserNames();
    void onDestroy();
}

package jgoguette.twigzolupolus.ca.mvptest.Main.Fragments.SendBlast;

/**
 * Created by jerry on 2016-11-15.
 */

public interface SendBlastPresenter {
    void sendBlast(String name, String message);

    void onDestroy();
}

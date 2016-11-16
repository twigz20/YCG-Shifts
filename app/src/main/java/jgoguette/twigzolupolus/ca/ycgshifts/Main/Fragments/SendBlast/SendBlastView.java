package jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.SendBlast;

import android.content.Context;

/**
 * Created by jerry on 2016-11-15.
 */

public interface SendBlastView {

    void setTitle();

    void setMessageError();

    void onSuccess();

    void onFailure();

    void navigateToHome();

    Context getContext();
}

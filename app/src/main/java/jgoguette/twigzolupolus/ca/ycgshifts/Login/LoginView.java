package jgoguette.twigzolupolus.ca.ycgshifts.Login;

import android.content.Context;

/**
 * Created by jerry on 2016-11-13.
 */

public interface LoginView {
    void showProgress();

    void hideProgress();

    void setEmailError();

    void setPasswordError();

    void showMessageDialog(String title, String message);

    void navigateToHome();

    void navigateToSignUp();

    Context getContext();
}

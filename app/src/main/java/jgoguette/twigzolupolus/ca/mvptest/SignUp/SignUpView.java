package jgoguette.twigzolupolus.ca.mvptest.SignUp;

import android.content.Context;

import jgoguette.twigzolupolus.ca.mvptest.Models.User;

/**
 * Created by jerry on 2016-11-14.
 */

public interface SignUpView {
    void showProgress();

    void hideProgress();

    void setEmailError();

    void setPasswordError();

    void setNameError();

    void setDepartmentError();

    void showMessageDialog(String title, String message);

    void navigateToHome();

    Context getContext();

    void setUser();

    User getUser();
}

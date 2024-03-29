package jgoguette.twigzolupolus.ca.ycgshifts.SignUp;

import android.content.Context;

import jgoguette.twigzolupolus.ca.ycgshifts.Model.User;

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

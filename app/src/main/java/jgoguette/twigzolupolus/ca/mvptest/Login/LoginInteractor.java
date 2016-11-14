package jgoguette.twigzolupolus.ca.mvptest.Login;

/**
 * Created by jerry on 2016-11-13.
 */

public interface LoginInteractor {

    interface OnLoginFinishedListener {
        void onEmailError();

        void onPasswordError();

        void onCredentialsError(String title, String message);

        void onSuccess();
    }

    void login(String email, String password);

    Boolean isValidCredentials(String email, String password);
}

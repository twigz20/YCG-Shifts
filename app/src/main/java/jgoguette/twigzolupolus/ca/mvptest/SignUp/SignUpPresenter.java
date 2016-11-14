package jgoguette.twigzolupolus.ca.mvptest.SignUp;

/**
 * Created by jerry on 2016-11-14.
 */

public interface SignUpPresenter {
    void signUp(String username, String password, String name, String department);

    void onDestroy();
}

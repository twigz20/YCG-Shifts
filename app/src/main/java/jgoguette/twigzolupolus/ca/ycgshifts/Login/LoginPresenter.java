package jgoguette.twigzolupolus.ca.ycgshifts.Login;

/**
 * Created by jerry on 2016-11-13.
 */

public interface LoginPresenter {
    void validateCredentials(String username, String password);

    void onDestroy();
}

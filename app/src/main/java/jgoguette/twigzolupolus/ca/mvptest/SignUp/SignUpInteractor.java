package jgoguette.twigzolupolus.ca.mvptest.SignUp;

import jgoguette.twigzolupolus.ca.mvptest.Models.User;

/**
 * Created by jerry on 2016-11-14.
 */

public interface SignUpInteractor {

    interface OnSignUpFinishedListener {
        void onEmailError();

        void onPasswordError();

        void onNameError();

        void onDepartmentError();

        void onUserAlreadyExists(String title, String message);

        void onSuccess();
    }

    Boolean isValidCredentials(String username, String password,
                               String name, String department);

    void signUp(String email, String password,
                String name, String department);

    void addUserToDatabase(User user);

}

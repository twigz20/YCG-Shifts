package jgoguette.twigzolupolus.ca.ycgshifts.Login;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by jerry on 2016-11-13.
 */

public class LoginPresenterImpl implements LoginPresenter, LoginInteractor.OnLoginFinishedListener {

    private LoginView loginView;
    private LoginInteractor loginInteractor;

    public LoginPresenterImpl(LoginView loginView) {
        this.loginView = loginView;
        this.loginInteractor = new LoginInteractorImpl(loginView.getContext(), this);

        if(isLoggedIn())
            loginView.navigateToHome();
    }

    private Boolean isLoggedIn() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        return firebaseAuth.getCurrentUser() != null;
    }

    @Override public void validateCredentials(String username, String password) {
        if (loginView != null) {
            loginView.showProgress();
        }

        loginInteractor.login(username, password);
    }

    @Override public void onDestroy() {
        loginView = null;
    }

    @Override public void onEmailError() {
        if (loginView != null) {
            loginView.setEmailError();
            loginView.hideProgress();
        }
    }

    @Override public void onPasswordError() {
        if (loginView != null) {
            loginView.setPasswordError();
            loginView.hideProgress();
        }
    }

    @Override
    public void onCredentialsError(String title, String message) {
        if (loginView != null) {
            loginView.showMessageDialog(title,message);
            loginView.hideProgress();
        }
    }

    @Override public void onSuccess() {
        if (loginView != null) {
            loginView.navigateToHome();
        }
    }
}

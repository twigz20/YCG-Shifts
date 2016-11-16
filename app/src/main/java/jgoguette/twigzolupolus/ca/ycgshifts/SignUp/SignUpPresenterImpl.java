package jgoguette.twigzolupolus.ca.ycgshifts.SignUp;

/**
 * Created by jerry on 2016-11-14.
 */

public class SignUpPresenterImpl implements SignUpPresenter, SignUpInteractor.OnSignUpFinishedListener{

    private SignUpView signUpView;
    private SignUpInteractor signUpInteractor;

    public SignUpPresenterImpl(SignUpView signUpView) {
        this.signUpView = signUpView;
        this.signUpInteractor = new SignUpInteractorImpl(signUpView.getContext(), this);
    }

    @Override
    public void signUp(String username, String password, String name, String department) {
        if (signUpView != null) {
            signUpView.showProgress();
        }

        signUpInteractor.signUp(username, password, name, department);
    }

    @Override
    public void onDestroy() {
        signUpView = null;
    }

    @Override
    public void onEmailError() {
        if (signUpView != null) {
            signUpView.setEmailError();
            signUpView.hideProgress();
        }
    }

    @Override
    public void onPasswordError() {
        if (signUpView != null) {
            signUpView.setPasswordError();
            signUpView.hideProgress();
        }
    }

    @Override
    public void onNameError() {
        if (signUpView != null) {
            signUpView.setNameError();
            signUpView.hideProgress();
        }
    }

    @Override
    public void onDepartmentError() {
        if (signUpView != null) {
            signUpView.setDepartmentError();
            signUpView.hideProgress();
        }
    }

    @Override
    public void onUserAlreadyExists(String title, String message) {
        if (signUpView != null) {
            signUpView.showMessageDialog(title,message);
            signUpView.hideProgress();
        }
    }

    @Override
    public void onSuccess() {
        if (signUpView != null) {
            signUpView.setUser();
            signUpInteractor.addUserToDatabase(signUpView.getUser());
            signUpView.navigateToHome();
        }
    }
}

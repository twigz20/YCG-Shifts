package jgoguette.twigzolupolus.ca.ycgshifts.Login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jgoguette.twigzolupolus.ca.ycgshifts.BuildConfig;
import jgoguette.twigzolupolus.ca.ycgshifts.Main.MainActivity;
import jgoguette.twigzolupolus.ca.ycgshifts.R;
import jgoguette.twigzolupolus.ca.ycgshifts.SignUp.SignUpActivity;

public class LoginActivity extends AppCompatActivity implements LoginView {

    private ProgressDialog progressDialog;

    @Bind(R.id.username)
    EditText username;

    @Bind(R.id.password)
    EditText password;

    private LoginPresenter presenter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        // Used for quick testing in Debug Mode
        if(BuildConfig.DEBUG) {
            username.setText(getString(R.string.debug_email));
            password.setText(getString(R.string.debug_password));
        }

        presenter = new LoginPresenterImpl(this);
    }

    @Override protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @OnClick(R.id.buttonLogin)
    public void loginTapped(View view) {
        presenter.validateCredentials(username.getText().toString(), password.getText().toString());
    }

    @OnClick(R.id.buttonSignup)
    public void signUpTapped(View view) {
        navigateToSignUp();
    }

    @Override public void showProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.login_progressDialog_message));
            progressDialog.setIndeterminate(true);
        }

        progressDialog.show();
    }

    @Override public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override public void setEmailError() {
        username.setError(getString(R.string.email_error));
    }

    @Override public void setPasswordError() {
        password.setError(getString(R.string.password_error));
    }

    @Override
    public void showMessageDialog(String title, String message) {
        AlertDialog ad = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .create();
        ad.show();
    }

    @Override public void navigateToHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void navigateToSignUp() {
        startActivity(new Intent(this, SignUpActivity.class));
        finish();
    }

    @Override
    public Context getContext() {
        return LoginActivity.this;
    }
}

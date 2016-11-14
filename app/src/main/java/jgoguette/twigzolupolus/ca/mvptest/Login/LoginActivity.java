package jgoguette.twigzolupolus.ca.mvptest.Login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import jgoguette.twigzolupolus.ca.mvptest.Main.MainActivity;
import jgoguette.twigzolupolus.ca.mvptest.R;
import jgoguette.twigzolupolus.ca.mvptest.SignUp.SignUpActivity;

public class LoginActivity extends AppCompatActivity implements LoginView, View.OnClickListener {

    private ProgressDialog progressDialog;
    private EditText username;
    private EditText password;
    private LoginPresenter presenter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        findViewById(R.id.buttonLogin).setOnClickListener(this);
        findViewById(R.id.buttonSignup).setOnClickListener(this);

        presenter = new LoginPresenterImpl(this);
    }

    @Override protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
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

    @Override public void onClick(View v) {
        if(v == findViewById(R.id.buttonLogin))
            presenter.validateCredentials(username.getText().toString(), password.getText().toString());

        if(v == findViewById(R.id.buttonSignup))
            navigateToSignUp();
    }
}

package jgoguette.twigzolupolus.ca.ycgshifts.SignUp;

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
import jgoguette.twigzolupolus.ca.ycgshifts.Login.LoginActivity;
import jgoguette.twigzolupolus.ca.ycgshifts.Main.MainActivity;
import jgoguette.twigzolupolus.ca.ycgshifts.Model.User;
import jgoguette.twigzolupolus.ca.ycgshifts.R;

public class SignUpActivity extends AppCompatActivity implements SignUpView {

    @Bind(R.id.editTextEmail)
    EditText email;

    @Bind(R.id.editTextPassword)
    EditText password;

    @Bind(R.id.editTextName)
    EditText name;

    @Bind(R.id.editTextDepartment)
    EditText department;

    private User user;
    private ProgressDialog progressDialog;

    private SignUpPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        presenter = new SignUpPresenterImpl(this);
    }

    @Override protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, LoginActivity.class));
        super.onBackPressed();
    }

    @OnClick(R.id.buttonSignup)
    public void signUpTapped(View view) {
        presenter.signUp(email.getText().toString(),
                password.getText().toString(),
                name.getText().toString(),
                department.getText().toString()
        );
    }

    @Override
    public void showProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Creating User Profile...");
            progressDialog.setIndeterminate(true);
        }

        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void setEmailError() {
        email.setError(getString(R.string.email_error));
    }

    @Override
    public void setPasswordError() {
        password.setError(getString(R.string.password_error));
    }

    @Override
    public void setNameError() {
        name.setError(getString(R.string.name_error));
    }

    @Override
    public void setDepartmentError() {
        department.setError(getString(R.string.department_error));
    }

    @Override
    public void showMessageDialog(String title, String message) {
        AlertDialog ad = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .create();
        ad.show();
    }

    @Override
    public void navigateToHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public Context getContext() {
        return SignUpActivity.this;
    }

    @Override
    public void setUser() {
        user = new User();
        user.setName(name.getText().toString());
        user.setEmail(email.getText().toString());
        user.setDepartment(department.getText().toString());
        user.setAuthorizationLevel(getString(R.string.low_level_authorization));
    }

    @Override
    public User getUser() {
        return user;
    }
}



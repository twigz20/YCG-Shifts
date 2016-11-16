package jgoguette.twigzolupolus.ca.ycgshifts.Login;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import jgoguette.twigzolupolus.ca.ycgshifts.R;

/**
 * Created by jerry on 2016-11-13.
 */

public class LoginInteractorImpl implements LoginInteractor  {

    Context context;
    OnLoginFinishedListener listener;

    public LoginInteractorImpl(Context context, OnLoginFinishedListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    public void login(final String email, final String password) {
        this.listener = listener;

        if(isValidCredentials(email,password)) {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        listener.onCredentialsError(
                                context.getString(R.string.credential_error_title),
                                context.getString(R.string.credential_error_message));
                    } else {
                        listener.onSuccess();
                    }
                }
            });
        }
    }

    @Override
    public Boolean isValidCredentials(String email, String password) {
        boolean valid = true;
        if (TextUtils.isEmpty(email)){
            listener.onEmailError();
            valid = false;
        }
        if (TextUtils.isEmpty(password)){
            listener.onPasswordError();
            valid = false;
        }

        return valid;
    }
}

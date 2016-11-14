package jgoguette.twigzolupolus.ca.mvptest.SignUp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import jgoguette.twigzolupolus.ca.mvptest.Models.User;
import jgoguette.twigzolupolus.ca.mvptest.R;

/**
 * Created by jerry on 2016-11-14.
 */

public class SignUpInteractorImpl implements SignUpInteractor {

    Context context;
    OnSignUpFinishedListener listener;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public SignUpInteractorImpl(Context context, OnSignUpFinishedListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    public Boolean isValidCredentials(String email, String password,
                                      String name, String department) {
        boolean valid = true;
        if (TextUtils.isEmpty(email)){
            listener.onEmailError();
            valid = false;
        }
        if (TextUtils.isEmpty(password)){
            listener.onPasswordError();
            valid = false;
        }

        if (TextUtils.isEmpty(name)){
            listener.onNameError();
            valid = false;
        }
        if (TextUtils.isEmpty(department)){
            listener.onDepartmentError();
            valid = false;
        }

        return valid;
    }

    @Override
    public void addUserToDatabase(User user) {
        databaseReference.child(context.getString(R.string.users_table))
                .child(firebaseAuth.getCurrentUser().getUid())
                .setValue(user);
    }

    @Override
    public void signUp(final String email, final String password,
                       final String name, final String department) {
        if(isValidCredentials(email, password, name, department)) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                listener.onUserAlreadyExists(
                                        context.getString(R.string.user_exists_error_title),
                                        context.getString(R.string.user_exists_error_message));
                            } else {
                                listener.onSuccess();
                            }
                        }
                    });
        }
    }
}

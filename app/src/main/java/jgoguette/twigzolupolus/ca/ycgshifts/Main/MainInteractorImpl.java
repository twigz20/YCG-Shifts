package jgoguette.twigzolupolus.ca.ycgshifts.Main;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import jgoguette.twigzolupolus.ca.ycgshifts.Model.User;
import jgoguette.twigzolupolus.ca.ycgshifts.R;

/**
 * Created by jerry on 2016-11-14.
 */

public class MainInteractorImpl implements MainInteractor{

    private Context context;
    private OnUserProfileFetchedFinishedListener listener;

    public MainInteractorImpl(Context context, OnUserProfileFetchedFinishedListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    public void getUserProfile() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        //Referring to the name of the User who has logged in currently and adding a valueChangeListener
        databaseReference.child(context.getString(R.string.users_table))
                .child(firebaseAuth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                //onDataChange is called every time the name of the User changes in your Firebase Database
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    listener.onSuccess(user);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    listener.onFailure(databaseError.getMessage());
                }
        });
    }
}

package jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.ReadShiftTrade;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import jgoguette.twigzolupolus.ca.ycgshifts.R;

/**
 * Created by jerry on 2016-11-15.
 */

public class ReadShiftTradeInteractorImpl implements ReadShiftTradeInteractor {

    private Context context;
    private OnShiftTradeResponseListener listener;
    private OnProfilePicLoadedListener profilePicLoadedListener;

    public ReadShiftTradeInteractorImpl(Context context, OnShiftTradeResponseListener listener, OnProfilePicLoadedListener profilePicLoadedListener) {
        this.context = context;
        this.listener = listener;
        this.profilePicLoadedListener = profilePicLoadedListener;
    }

    @Override
    public void setRead(String key) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(context.getString(R.string.messages_table))
                .child(firebaseAuth.getCurrentUser().getUid())
                .child(key).child("read").setValue(true);
    }

    @Override
    public void loadProfilePic(String profilePicOwnerID) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference(profilePicOwnerID
                + context.getString(R.string.profilePicPath));
        final Task task = storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

            @Override
            public void onSuccess(Uri uri) {
                profilePicLoadedListener.onProfilePicLoadedSuccess(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                // TODO: Load temporary image
            }
        });
    }
}

package jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.ReadShiftTrade;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import jgoguette.twigzolupolus.ca.ycgshifts.Model.Shift;
import jgoguette.twigzolupolus.ca.ycgshifts.Model.User;
import jgoguette.twigzolupolus.ca.ycgshifts.R;

/**
 * Created by jerry on 2016-11-15.
 */

public class ReadShiftTradeInteractorImpl implements ReadShiftTradeInteractor {

    private Context context;
    private OnShiftTradeResponseListener listener;
    private OnProfilePicLoadedListener profilePicLoadedListener;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    public ReadShiftTradeInteractorImpl(Context context, OnShiftTradeResponseListener listener, OnProfilePicLoadedListener profilePicLoadedListener) {
        this.context = context;
        this.listener = listener;
        this.profilePicLoadedListener = profilePicLoadedListener;

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
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
                profilePicLoadedListener.onProfilePicLoadedFailure();
            }
        });
    }

    @Override
    public void acceptShiftTrade(final String messageKey, final Shift shift) {
        databaseReference.child(context.getString(R.string.users_table)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot userSnap : dataSnapshot.getChildren()) {
                    User user = userSnap.getValue(User.class);

                    removeShiftTradeMessage(user.getFirebaseId(), messageKey);
                    removeShiftTradeNotification(user.getFirebaseId(), messageKey);
                    removeShiftFromPreviousOwner(user.getFirebaseId(), shift.getKey());
                }

                addShiftToCurrentUserSchedule(shift);
                listener.onShiftTradeSuccessful();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void rejectShiftTrade(String messageKey) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(context.getString(R.string.messages_table))
                .child(firebaseAuth.getCurrentUser().getUid())
                .child(messageKey).removeValue();

        databaseReference.child(context.getString(R.string.notifications_requests_table))
                .child(firebaseAuth.getCurrentUser().getUid())
                .child(messageKey).removeValue();
    }

    private void removeShiftTradeMessage(String firebaseId, String messageKey) {
        databaseReference.child(context.getString(R.string.messages_table)).child(firebaseId)
                .child(messageKey).removeValue();
    }

    private void removeShiftTradeNotification(String firebaseId, String messageKey) {
        databaseReference.child(context.getString(R.string.notifications_requests_table)).child(firebaseId)
                .child(messageKey).removeValue();
    }

    private void removeShiftFromPreviousOwner(String firebaseId, String shiftKey) {
        databaseReference.child(context.getString(R.string.shifts_table))
                .child(firebaseId)
                .child(shiftKey).removeValue();
    }

    private void addShiftToCurrentUserSchedule(Shift shift) {
        DatabaseReference newRef = databaseReference.child(context.getString(R.string.shifts_table))
                .child(firebaseAuth.getCurrentUser().getUid())
                .push();
        shift.setKey(newRef.getKey());
        shift.setOwner(firebaseAuth.getCurrentUser().getUid());
        shift.setShiftTradeRequestSent(false);
        newRef.setValue(shift);
    }
}

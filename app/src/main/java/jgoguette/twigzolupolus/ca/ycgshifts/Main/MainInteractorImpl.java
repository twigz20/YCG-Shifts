package jgoguette.twigzolupolus.ca.ycgshifts.Main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.AnyRes;
import android.support.annotation.NonNull;
import android.widget.Toast;

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

import java.util.ArrayList;

import jgoguette.twigzolupolus.ca.ycgshifts.Main.Receivers.AlarmReceiver;
import jgoguette.twigzolupolus.ca.ycgshifts.Main.Services.MyUploadService;
import jgoguette.twigzolupolus.ca.ycgshifts.Main.Services.NotificationService;
import jgoguette.twigzolupolus.ca.ycgshifts.Model.User;
import jgoguette.twigzolupolus.ca.ycgshifts.R;

/**
 * Created by jerry on 2016-11-14.
 */

public class MainInteractorImpl implements MainInteractor{

    private Context context;
    private OnUserProfileFetchedFinishedListener listener;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private OnProfilePicFetchedListener profilePicFetchedListener;
    private PendingIntent pendingIntent;
    private AlarmManager manager;
    Intent alarmIntent;

    public MainInteractorImpl(Context context, OnUserProfileFetchedFinishedListener listener, OnProfilePicFetchedListener profilePicFetchedListener) {
        this.context = context;
        this.listener = listener;
        this.profilePicFetchedListener = profilePicFetchedListener;

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void getUserProfile() {
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

    @Override
    public void getProfilePicUri() {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference(firebaseAuth.getCurrentUser().getUid()
                + context.getString(R.string.profilePicPath));
        final Task task = storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

            @Override
            public void onSuccess(Uri uri) {
                profilePicFetchedListener.onSuccessProfilePic(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                profilePicFetchedListener
                        .onFailureProfilePic(getUriToDrawable(context, R.drawable.no_picture));
            }
        });
    }

    @Override
    public void uploadProfilePic(Uri mFileUri, Uri fileUri) {
        // Save the File URI
        mFileUri = fileUri;

        // Toast message in case the user does not see the notification
        Toast.makeText(context, "Profile Picture Updated", Toast.LENGTH_SHORT).show();

        // Start MyUploadService to upload the file, so that the file is uploaded
        // even if this Activity is killed or put in the background

        context.startService(new Intent(context, MyUploadService.class)
                .putExtra(MyUploadService.EXTRA_FILE_URI, fileUri)
                .setAction(MyUploadService.ACTION_UPLOAD));
    }

    @Override
    public void removeNotification(String notificationKey) {
        databaseReference.child("notificationsRequests")
                .child(firebaseAuth.getCurrentUser().getUid())
                .child(notificationKey)
                .removeValue();
    }

    @Override
    public void removeNotifications(ArrayList<String> notificationKeys) {
        for (String key : notificationKeys) {
            databaseReference.child("notificationsRequests")
                    .child(firebaseAuth.getCurrentUser().getUid())
                    .child(key)
                    .removeValue();
        }
    }

    @Override
    public void startNotificationService() {
        manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        int interval = 60000;

        // Retrieve a PendingIntent that will perform a broadcast
        alarmIntent = new Intent(context, AlarmReceiver.class)
                .putExtra("FireBaseUID", firebaseAuth.getCurrentUser().getUid())
                .setAction(NotificationService.ACTION_START);
        pendingIntent = PendingIntent.getBroadcast(context,
                0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
    }

    @Override
    public void stopNotificationService() {
        manager.cancel(pendingIntent);
        alarmIntent = new Intent(context, AlarmReceiver.class)
                .putExtra("FireBaseUID", firebaseAuth.getCurrentUser().getUid())
                .setAction(NotificationService.ACTION_STOP);
        pendingIntent = PendingIntent.getBroadcast(context,
                0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),pendingIntent);
    }

    /**
     * get uri to drawable or any other resource type if u wish
     * @param context - context
     * @param drawableId - drawable res id
     * @return - uri
     */
    public static final Uri getUriToDrawable(@NonNull Context context, @AnyRes int drawableId) {
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + context.getResources().getResourcePackageName(drawableId)
                + '/' + context.getResources().getResourceTypeName(drawableId)
                + '/' + context.getResources().getResourceEntryName(drawableId) );
        return imageUri;
    }
}

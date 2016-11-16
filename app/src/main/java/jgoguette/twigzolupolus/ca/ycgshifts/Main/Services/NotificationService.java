package jgoguette.twigzolupolus.ca.ycgshifts.Main.Services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import jgoguette.twigzolupolus.ca.ycgshifts.Main.MainActivity;
import jgoguette.twigzolupolus.ca.ycgshifts.Model.Message;
import jgoguette.twigzolupolus.ca.ycgshifts.Model.Shift;
import jgoguette.twigzolupolus.ca.ycgshifts.R;

/**
 * Created by jerry on 2016-11-16.
 */

public class NotificationService extends Service {
    private static final String TAG = "NotificationService";

    /** Intent Actions **/
    public static final String ACTION_NOTIFICATION_SEARCH = "action_notification_search";
    public static final String ACTION_START = "action_start";
    public static final String ACTION_STOP = "action_stop";

    /** Intent Extras **/
    public static final String NOTIFICATION_FOUND = "action_notification_found";

    // [START declare_ref]
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    // [END declare_ref]


    public NotificationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // [START get_storage_ref]
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("notificationsRequests");
        // [END get_storage_ref]
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: Notif" + intent + ":" + startId);

        if (ACTION_NOTIFICATION_SEARCH.equals(intent.getAction())) {
            getNotifications(intent.getStringExtra("FireBaseUID"));
        }

        return START_NOT_STICKY;
    }

    private void getNotifications(final String uid){
        Log.d(TAG, "UserNotifications: " + uid);


        databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Message> notificationRequests = new ArrayList<Message>();
                Boolean displayNotification = false;
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message notification =
                            snapshot.getValue(Message.class);

                    if(notification.getType().contains(notification.convertTypeToString(Message.Type.SHIFT_SWAP_NOTIF))) {
                        Shift shift = snapshot.child("shift").getValue(Shift.class);
                        notification.setShift(shift);
                    }

                    notificationRequests.add(notification);
                    if(!notification.getNotificationDisplayed())
                        displayNotification = true;
                }

                if(displayNotification) {
                    if (notificationRequests.size() > 1)
                        showUploadFinishedNotificationGroup(uid,notificationRequests);
                    else if (notificationRequests.size() == 1)
                        showUploadFinishedNotificationSingle(uid,notificationRequests.get(0));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Show a notification for a finished upload.
     */
    private void showUploadFinishedNotificationGroup(final String uid, List<Message> notificationRequest) {
        // Make Intent to MainActivity
        ArrayList<String> keys = new ArrayList<>();
        for(Message m : notificationRequest)
            keys.add(m.getKey());

        Intent intent = new Intent(this, MainActivity.class)
                .putExtra(NOTIFICATION_FOUND, true)
                .putExtra("Grouped", true)
                .putStringArrayListExtra("Keys", keys)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        // Make PendingIntent for notification
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* requestCode */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // for every previously sent notification that met our above requirements,
        // add a new line containing its title to the inbox style notification extender
        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();

        // Sets a title for the Inbox in expanded layout
        inboxStyle.setSummaryText(notificationRequest.get(0).getReceiver());
        inboxStyle.setBigContentTitle(notificationRequest.size() + " Message(s) Received");
        // Moves events into the expanded layout
        for (int i=0; i < notificationRequest.size(); i++) {
            String string = notificationRequest.get(i).getSender()
                    + " - " + notificationRequest.get(i).getSubject();
            inboxStyle.addLine(string);
        }

        NotificationCompat.Builder notif = (NotificationCompat.Builder) new NotificationCompat.Builder(this)

                .setSmallIcon(R.drawable.ic_check_white_24)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(notificationRequest.get(0).getReceiver())
                .setStyle(inboxStyle)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);
        notificationManager.notify(1, notif.build());

        for(Message messages : notificationRequest) {
            databaseReference.child(uid)
                    .child(messages.getKey())
                    .child("notificationDisplayed")
                    .setValue(true);
        }
    }

    /**
     * Show a notification for a finished upload.
     */
    private void showUploadFinishedNotificationSingle(final String uid, Message notificationRequest) {
        // Make Intent to MainActivity
        Intent intent = new Intent(this, MainActivity.class)
                .putExtra(NOTIFICATION_FOUND, true)
                .putExtra("Grouped", false)
                .putExtra("Message", notificationRequest)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        // Make PendingIntent for notification
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* requestCode */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notif = (NotificationCompat.Builder) new NotificationCompat.Builder(this)

                .setSmallIcon(R.drawable.ic_check_white_24)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(notificationRequest.getSender()
                        + " - " + notificationRequest.getSubject())
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);


        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);
        notificationManager.notify(1, notif.build());

        databaseReference.child(uid)
                .child(notificationRequest.getKey())
                .child("notificationDisplayed")
                .setValue(true);
    }

    @Override
    public void onDestroy() {
        Log.v("Screen:","Service killed");
        firebaseAuth.signOut();
        firebaseAuth = null;
        super.onDestroy();
    }
}

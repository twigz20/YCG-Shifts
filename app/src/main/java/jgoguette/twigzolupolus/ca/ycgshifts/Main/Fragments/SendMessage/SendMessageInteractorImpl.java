package jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.SendMessage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;

import jgoguette.twigzolupolus.ca.ycgshifts.Model.Message;
import jgoguette.twigzolupolus.ca.ycgshifts.Model.User;
import jgoguette.twigzolupolus.ca.ycgshifts.R;

/**
 * Created by jerry on 2016-11-15.
 */

public class SendMessageInteractorImpl implements SendMessageInteractor {

    private Context context;
    private onMessageSentListener listener;
    private onFetchUsernamesListener fetchUsernamesListener;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    public SendMessageInteractorImpl(Context context, onMessageSentListener listener, onFetchUsernamesListener fetchUsernamesListener) {
        this.context = context;
        this.listener = listener;
        this.fetchUsernamesListener = fetchUsernamesListener;

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void sendMessage(final String to, final String subject, final String message) {

        if(isParamsValid(to, subject, message)) {
            databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                //onDataChange is called every time the name of the User changes in your Firebase Database
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //Inside onDataChange we can get the data as an Object from the dataSnapshot
                    //getValue returns an Object. We can specify the type by passing the type expected as a parameter
                    //Iterable<DataSnapshot> list = dataSnapshot.getChildren();

                    try {
                        ArrayList<User> users = new ArrayList<>();
                        String sender = new String();
                        for (DataSnapshot usersSnapShot : dataSnapshot.getChildren()) {
                            User user = usersSnapShot.getValue(User.class);
                            if (!user.getFirebaseId().equals(firebaseAuth
                                    .getCurrentUser()
                                    .getUid())) {

                                users.add(user);
                            } else {
                                sender = user.getName();
                            }
                        }

                        sendMessage(users, sender, to, subject, message);

                    } catch (DatabaseException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //Toast.makeText(getActivity(), "" + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public void getUserNames() {
        databaseReference.child(context.getString(R.string.users_table)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    ArrayList<String> userNames = new ArrayList<>();
                    for (DataSnapshot usersSnapShot : dataSnapshot.getChildren()) {
                        User user = usersSnapShot.getValue(User.class);
                        if(!user.getFirebaseId().equals(firebaseAuth
                                .getCurrentUser()
                                .getUid())) {

                            userNames.add(user.getName());
                        }
                    }

                    Collections.addAll(userNames, context.getResources().getStringArray(R.array.departments));
                    Collections.addAll(userNames, context.getResources().getStringArray(R.array.predefinedSendTargets));

                    fetchUsernamesListener.onUserNamesFetched(userNames);
                } catch(DatabaseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fetchUsernamesListener.onFailedToFetch(databaseError.getMessage());
            }
        });
    }

    @Override
    public Boolean isParamsValid(String to, String subject, String message) {
        boolean valid = true;

        if (TextUtils.isEmpty(to)){
            listener.onToError();
            valid = false;
        }
        if (TextUtils.isEmpty(subject)){
            listener.onSubjectError();
            valid = false;
        }
        if (TextUtils.isEmpty(message)){
            listener.onMessegeError();
            valid = false;
        }

        return valid;
    }

    @Override
    public void sendMessage(ArrayList<User> users, String sender, String to, String subject, String message) {

        String toString = to.substring(0, to.length()-2);
        String[] departments = context.getResources().getStringArray(R.array.departments);
        // Mass send to users of various departments
        for(String dptmt : departments) {
            if(toString.trim().equals(dptmt.trim())) {
                for(User user : users) {
                    if(user.getDepartment().contains(dptmt)) {
                        createNotification(sender, user.getFirebaseId(),user.getName(),
                                subject, message);
                    }
                }
            }
        }

        String[] targets = context.getResources().getStringArray(R.array.predefinedSendTargets);
        // Mass send to users of predefined Targets
        for(String target : targets) {
            if(toString.trim().equals(target)) {
                if(target.contains("All")) {
                    for(User user : users) {
                        createNotification(sender, user.getFirebaseId(),user.getName(),
                                subject, message);
                    }
                }
                if(target.contains("Managers")) {
                    for(User user : users) {
                        if(user.getAuthorizationLevel()
                                .contains(context.getResources().getString(R.string.manager))) {
                            createNotification(sender, user.getFirebaseId(),user.getName(),
                                    subject, message);
                        }
                    }
                }
                if(target.contains("Produce Manager")) {
                    for(User user : users) {
                        if(user.getAuthorizationLevel()
                                .contains(context.getResources().getString(R.string.manager))) {
                            if (user.getDepartment().contains("Produce")) {
                                createNotification(sender, user.getFirebaseId(), user.getName(),
                                        subject, message);
                            }
                        }
                    }
                }
                if(target.contains("Grocery Manager")) {
                    for(User user : users) {
                        if(user.getAuthorizationLevel()
                                .contains(context.getResources().getString(R.string.manager))) {
                            if (user.getDepartment().contains("Grocery")) {
                                createNotification(sender, user.getFirebaseId(), user.getName(),
                                        subject, message);
                            }
                        }
                    }
                }
                if(target.contains("Deli Manager")) {
                    for(User user : users) {
                        if(user.getAuthorizationLevel()
                                .contains(context.getResources().getString(R.string.manager))) {
                            if (user.getDepartment().contains("Deli")) {
                                createNotification(sender, user.getFirebaseId(), user.getName(),
                                        subject, message);
                            }
                        }
                    }
                }
                if(target.contains("Cash Manager")) {
                    for(User user : users) {
                        if(user.getAuthorizationLevel()
                                .contains(context.getResources().getString(R.string.manager))) {
                            if (user.getDepartment().contains("Cash")) {
                                createNotification(sender, user.getFirebaseId(), user.getName(),
                                        subject, message);
                            }
                        }
                    }
                }
            }
        }

        // Send to only Users listed
        for(User user : users) {
            if(toString.trim().equals(user.getName())) {
                createNotification(sender, user.getFirebaseId(),user.getName(),
                        subject, message);
            }
        }

    }

    private void createNotification(String sender, String firebaseId, String receiver, String subject, String msg) {
        Message message = new Message();
        message.setReceiver(receiver);
        message.setSender(sender);
        message.setSubject(subject);
        message.setMessage(msg);
        message.setRead(false);
        message.setType(
                message.convertTypeToString(Message.Type.NORMAL_NOTIF));
        message.setNotificationDisplayed(false);

        // Get Current TimeStamp
        java.util.Date date= new java.util.Date();
        Timestamp ts_now = new Timestamp(date.getTime());
        long tsTime2 = ts_now.getTime();

        message.setTimeStamp(tsTime2);

        DatabaseReference newRef = databaseReference.child(context.getString(R.string.messages_table)).child(firebaseId).push();
        String key = newRef.getKey();
        message.setKey(key);
        newRef.setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                listener.onSuccess();
            }
        });
        databaseReference.child("notificationsRequests").child(firebaseId).child(key).setValue(message);
    }
}

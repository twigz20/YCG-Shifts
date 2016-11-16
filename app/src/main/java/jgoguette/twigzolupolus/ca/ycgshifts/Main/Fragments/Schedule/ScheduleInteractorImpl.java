package jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.Schedule;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.LocalDateTime;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import jgoguette.twigzolupolus.ca.ycgshifts.Model.Message;
import jgoguette.twigzolupolus.ca.ycgshifts.Model.Shift;
import jgoguette.twigzolupolus.ca.ycgshifts.Model.User;
import jgoguette.twigzolupolus.ca.ycgshifts.R;

/**
 * Created by jerry on 2016-11-15.
 */

public class ScheduleInteractorImpl implements ScheduleInteractor{
    private Context context;
    private onScheduleLoadedListener scheduleLoadedListener;
    private onShiftTradeRequestSentListener tradeRequestSentListener;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private final int NULL_HOUR = -1;

    public ScheduleInteractorImpl(Context context, onShiftTradeRequestSentListener tradeRequestSentListener, onScheduleLoadedListener scheduleLoadedListener) {
        this.context = context;
        this.scheduleLoadedListener = scheduleLoadedListener;
        this.tradeRequestSentListener = tradeRequestSentListener;

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }


    @Override
    public void getSchedule() {
        databaseReference.child(context.getString(R.string.shifts_table))
                .child(firebaseAuth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    ArrayList<Shift> shifts = new ArrayList<>();
                    SimpleDateFormat dt = new SimpleDateFormat(context
                            .getResources().getString(R.string.date_format));
                    for (DataSnapshot shiftSnapShot : dataSnapshot.getChildren()) {
                        Shift shift = shiftSnapShot.getValue(Shift.class);
                        // Check to make sure data is valid date format
                        try {
                            Date start = dt.parse(shift.getStart_time());
                            Date end = dt.parse(shift.getEnd_time());
                            shifts.add(shift);
                            Log.d("Shift: ", shift.getOwner());
                        } catch (ParseException e) {
                            Log.d("Parse Error: ", e.toString());
                        }
                    }

                    int hour = getTodayShiftStartHour(shifts);
                    scheduleLoadedListener.onScheduleLoaded(shifts, hour);
                } catch(DatabaseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Toast.makeText(getActivity(), "" + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public int getTodayShiftStartHour(ArrayList<Shift> shifts) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(context.getString(R.string.date_format), Locale.CANADA);
        try {
            for(Shift shift : shifts) {
                Date shiftDate = dateFormat.parse(shift.getStart_time());
                Date today = dateFormat.parse(dateFormat.format(cal.getTime()));

                DateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy (z)");
                Date shiftD = targetFormat.parse(targetFormat.format(shiftDate));
                Date todayD = targetFormat.parse(targetFormat.format(today));

                if(shiftD.equals(todayD)) {
                    LocalDateTime localDate = new LocalDateTime(shiftDate);
                    return localDate.getHourOfDay();
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return NULL_HOUR;
    }

    @Override
    public void sendShiftTradeRequest(final Message message) {
        final String uID = firebaseAuth.getCurrentUser().getUid();

        databaseReference.child(context.getString(R.string.users_table))
                .addListenerForSingleValueEvent(new ValueEventListener() {
            //onDataChange is called every time the name of the User changes in your Firebase Database
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    User currentUser = new User();
                    ArrayList<User> users = new ArrayList<User>();
                    for (DataSnapshot usersSnapShot : dataSnapshot.getChildren()) {
                        User user = usersSnapShot.getValue(User.class);
                        if(!user.getFirebaseId().equals(uID)) {
                            users.add(user);
                        } else {
                            currentUser = new User(user);
                        }
                    }

                    final String key = UUID.randomUUID().toString();
                    for(User user: users) {
                        if(user.getDepartment().equals(currentUser.getDepartment())) {
                            sendShiftTradeRequest(key, message, currentUser, user);
                        }
                    }
                } catch(DatabaseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "" + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public void sendShiftTradeRequest(final String key, final Message message, User currentUser, User otherUser) {
        message.setReceiver(otherUser.getName());
        message.setSender(currentUser.getName());
        message.setSubject("Shift Trade Request");
        message.setRead(false);
        message.setApproval(false);
        message.setType(
                message.convertTypeToString(Message.Type.SHIFT_SWAP_NOTIF));
        message.setNotificationDisplayed(false);

        // Get Current TimeStamp
        java.util.Date date = new java.util.Date();
        Timestamp ts_now = new Timestamp(date.getTime());
        long tsTime2 = ts_now.getTime();

        message.setTimeStamp(tsTime2);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference newRef =
                databaseReference.child("messages").child(otherUser.getFirebaseId()).child(key);

        message.setKey(key);
        newRef.setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                setTradeRequestSent(message.getShift().getKey());
                tradeRequestSentListener.onSuccess("Request Successfully Sent!");
            }
        });
        databaseReference.child("notificationsRequests")
                .child(otherUser.getFirebaseId())
                .child(key)
                .setValue(message);
    }

    @Override
    public void setTradeRequestSent(String key) {
        databaseReference.child(context.getString(R.string.shifts_table))
                    .child(firebaseAuth.getCurrentUser().getUid()).child(key)
                    .child("shiftTradeRequestSent")
                    .setValue(true);
    }
}

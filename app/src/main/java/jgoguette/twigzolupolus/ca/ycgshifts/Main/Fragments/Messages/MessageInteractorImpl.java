package jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.Messages;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import jgoguette.twigzolupolus.ca.ycgshifts.Model.Message;
import jgoguette.twigzolupolus.ca.ycgshifts.Model.Shift;
import jgoguette.twigzolupolus.ca.ycgshifts.R;

/**
 * Created by jerry on 2016-11-15.
 */

public class MessageInteractorImpl implements MessageInteractor {
    Context context;
    onMessagesLoadedListener listener;

    public MessageInteractorImpl(Context context, onMessagesLoadedListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    public void loadMessages() {
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        //Referring to the name of the User who has logged in currently and adding a valueChangeListener
        databaseReference.child(context.getString(R.string.messages_table))
                .child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            //onDataChange is called every time the name of the User changes in your Firebase Database
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Inside onDataChange we can get the data as an Object from the dataSnapshot
                //getValue returns an Object. We can specify the type by passing the type expected as a parameter
                ArrayList<Message> messages = new ArrayList<>();
                for(DataSnapshot snap : dataSnapshot.getChildren()) {
                    Message message = snap.getValue(Message.class);

                    if(message.getType().contains(message.convertTypeToString(Message.Type.SHIFT_SWAP_NOTIF))) {
                        Shift shift = snap.child(context.getString(R.string.shift)).getValue(Shift.class);
                        message.setShift(shift);
                    }
                    messages.add(message);
                }

                Collections.sort(messages, new Comparator<Message>() {
                    public int compare(Message o1, Message o2) {
                        if (o1.getTimeStamp() == null || o2.getTimeStamp() == null)
                            return 0;
                        // Descending , for ascending just switch 02 and 01
                        return o2.getTimeStamp().compareTo(o1.getTimeStamp());
                    }
                });
                DatabaseReference notificationRef = databaseReference.child(context.getString(R.string.messages_table))
                        .child(firebaseAuth.getCurrentUser().getUid());
                listener.onSuccess(messages, notificationRef);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Toast.makeText(MessageFragment.this.getActivity(), "" + databaseError.getMessage(), Toast.LENGTH_LONG).show();

                listener.onFailure(databaseError.getMessage());
            }
        });
    }
}

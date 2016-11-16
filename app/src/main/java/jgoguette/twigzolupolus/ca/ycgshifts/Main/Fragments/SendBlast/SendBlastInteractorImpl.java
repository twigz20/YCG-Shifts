package jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.SendBlast;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Timestamp;

import jgoguette.twigzolupolus.ca.ycgshifts.Model.Feed;
import jgoguette.twigzolupolus.ca.ycgshifts.R;

/**
 * Created by jerry on 2016-11-15.
 */

public class SendBlastInteractorImpl implements SendBlastInteractor{

    private Context context;
    private OnBlastSentListener listener;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;


    public SendBlastInteractorImpl(Context context, SendBlastInteractor.OnBlastSentListener listener) {
        this.context = context;
        this.listener = listener;

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void sendBlast(String name, String message) {
        if(isMessageValid(message)) {
            String uid = firebaseAuth.getCurrentUser().getUid();

            // Get Current TimeStamp
            java.util.Date date= new java.util.Date();
            Timestamp ts_now = new Timestamp(date.getTime());
            long tsTime2 = ts_now.getTime();
            String timeStamp = Long.toString(tsTime2);

            Feed feed = new Feed(name,message,tsTime2, uid);
            databaseReference.child(context.getString(R.string.feeds_table))
                    .push().setValue(feed)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    listener.onSuccess();
                }
            });
        } else {
            listener.onFailure();
        }
    }

    @Override
    public Boolean isMessageValid(String message) {
        boolean valid = true;

        if (TextUtils.isEmpty(message)){
            listener.onMessegeError();
            valid = false;
        }

        return valid;
    }
}

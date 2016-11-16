package jgoguette.twigzolupolus.ca.ycgshifts.Main.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import jgoguette.twigzolupolus.ca.ycgshifts.Main.Services.NotificationService;

/**
 * Created by jerry on 2016-11-16.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(NotificationService.ACTION_STOP)) {
            Toast.makeText(context, "Stopping Service....", Toast.LENGTH_SHORT).show();

            Log.d("Screen:", "In Method:  ACTION_SERVICE_STOP");
            context.stopService(
                    new Intent(context, NotificationService.class)
                            .putExtra("FireBaseUID", intent.getStringExtra("FireBaseUID"))
                            .setAction(NotificationService.ACTION_NOTIFICATION_SEARCH)
            );
        }
        else if (intent.getAction().equals(NotificationService.ACTION_START)) {
            //Toast.makeText(context, "Starting Service....", Toast.LENGTH_SHORT).show();

            Log.d("Screen:", "In Method:  ACTION_SERVICE_START");
            context.startService(
                    new Intent(context, NotificationService.class)
                            .putExtra("FireBaseUID", intent.getStringExtra("FireBaseUID"))
                            .setAction(NotificationService.ACTION_NOTIFICATION_SEARCH)
            );

        }
    }
}


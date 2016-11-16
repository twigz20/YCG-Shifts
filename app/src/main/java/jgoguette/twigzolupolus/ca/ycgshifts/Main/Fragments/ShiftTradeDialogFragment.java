package jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;

import jgoguette.twigzolupolus.ca.ycgshifts.R;

/**
 * Created by jerry on 2016-11-15.
 */

public class ShiftTradeDialogFragment extends android.support.v4.app.DialogFragment {

    //Interface created for communicating this dialog fragment events to called fragment
    public interface ScheduleMessageDialogFragmentEvents {
        void onMessageEntered(String message);
    }

    ScheduleMessageDialogFragmentEvents fragmentEvents;

    public void setScheduleMessageFragmentEvents(ScheduleMessageDialogFragmentEvents fragmentEvents){
        this.fragmentEvents = fragmentEvents;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        // Set Dialog Icon
        alertDialog.setIcon(R.drawable.no_picture);
        // Set Dialog Title
        alertDialog.setTitle("Shift Swap Request");
        // Set Dialog Message
        alertDialog.setMessage("Reason For Request:");

        final EditText input = new EditText(getActivity());

        //input.setBackgroundResource(R.drawable.rounded_edittext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        // Positive button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Do something else
                //((ScheduleActivity)getActivity()).sendShiftSwapRequest(input.getText().toString());
                fragmentEvents.onMessageEntered(input.getText().toString());
            }
        });

        // Negative Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,	int which) {
                // Do something else
            }
        });

        return alertDialog.create();
    }
}

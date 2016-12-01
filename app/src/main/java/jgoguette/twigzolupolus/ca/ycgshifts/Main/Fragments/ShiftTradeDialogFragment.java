package jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
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
         alertDialog.setIcon(resizeImage(R.drawable.no_picture));
        // Set Dialog Title
        alertDialog.setTitle("Shift Trade Request");
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

    public BitmapDrawable resizeImage(int resourceID) {
        // load the origial BitMap (500 x 500 px)
        Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(),
                resourceID);

        int width = bitmapOrg.getWidth();
        int height = bitmapOrg.getHeight();
        int newWidth = 400;
        int newHeight = 400;

        // calculate the scale - in this case = 0.4f
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // create matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // rotate the Bitmap
        // matrix.postRotate(45);

        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0,
                width, height, matrix, true);

        // make a Drawable from Bitmap to allow to set the BitMap
        // to the ImageView, ImageButton or what ever

        return new BitmapDrawable(resizedBitmap);
    }
}

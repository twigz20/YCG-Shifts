package jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.Schedule;

import android.content.Context;

import java.util.ArrayList;

import jgoguette.twigzolupolus.ca.ycgshifts.Model.Shift;

/**
 * Created by jerry on 2016-11-15.
 */

public interface ScheduleView {
    Context getContext();
    void setTitle();
    void getSchedule();
    void sendShiftTradeRequest();

    void onScheduleLoaded(ArrayList<Shift> shifts);
    void onTradeRequestSent(String message);
    void onTradeRequestNotSent(String message);

    void showMessageDialog(String title, String message);
}

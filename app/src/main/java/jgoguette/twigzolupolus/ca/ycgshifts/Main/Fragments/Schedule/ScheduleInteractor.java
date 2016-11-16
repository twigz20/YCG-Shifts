package jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.Schedule;

import java.util.ArrayList;

import jgoguette.twigzolupolus.ca.ycgshifts.Model.Message;
import jgoguette.twigzolupolus.ca.ycgshifts.Model.Shift;
import jgoguette.twigzolupolus.ca.ycgshifts.Model.User;

/**
 * Created by jerry on 2016-11-15.
 */

public interface ScheduleInteractor {
    interface onScheduleLoadedListener {
        void onScheduleLoaded(ArrayList<Shift> shifts, int hour);
        void onFailedToLoad();
    }

    interface onShiftTradeRequestSentListener {
        void onSuccess(String message);
        void onFailure(String message);
    }

    void getSchedule();
    void sendShiftTradeRequest(Message message);
    void sendShiftTradeRequest(String key, Message message, User currentUser, User otherUser);

    void setTradeRequestSent(String key);
}

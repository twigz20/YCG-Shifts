package jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.Schedule;

import jgoguette.twigzolupolus.ca.ycgshifts.Model.Message;

/**
 * Created by jerry on 2016-11-15.
 */

public interface SchedulePresenter {
    void getSchedule();
    void sendShiftTradeRequest(Message message);
}

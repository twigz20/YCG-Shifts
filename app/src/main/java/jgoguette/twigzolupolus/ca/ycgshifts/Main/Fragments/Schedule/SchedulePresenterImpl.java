package jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.Schedule;

import java.util.ArrayList;

import jgoguette.twigzolupolus.ca.ycgshifts.Model.Message;
import jgoguette.twigzolupolus.ca.ycgshifts.Model.Shift;

/**
 * Created by jerry on 2016-11-15.
 */

public class SchedulePresenterImpl implements SchedulePresenter, ScheduleInteractor.onScheduleLoadedListener, ScheduleInteractor.onShiftTradeRequestSentListener {

    ScheduleView scheduleView;
    ScheduleInteractor scheduleInteractor;

    public SchedulePresenterImpl(ScheduleView scheduleView) {
        this.scheduleView = scheduleView;

        scheduleInteractor = new ScheduleInteractorImpl(scheduleView.getContext(),this,this);
    }

    @Override
    public void getSchedule() {
        scheduleInteractor.getSchedule();
    }

    @Override
    public void sendShiftTradeRequest(Message message) {
        scheduleInteractor.sendShiftTradeRequest(message);
    }

    @Override
    public void onScheduleLoaded(ArrayList<Shift> shifts) {
        scheduleView.onScheduleLoaded(shifts);
    }

    @Override
    public void onFailedToLoad() {

    }

    @Override
    public void onSuccess(String message) {
        if(scheduleView != null) {
            scheduleView.onTradeRequestSent(message);
        }
    }

    @Override
    public void onFailure(String message) {
        if(scheduleView != null) {
            scheduleView.onTradeRequestNotSent(message);
        }
    }
}

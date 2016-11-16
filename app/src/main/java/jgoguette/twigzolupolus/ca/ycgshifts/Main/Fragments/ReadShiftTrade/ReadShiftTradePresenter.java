package jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.ReadShiftTrade;

/**
 * Created by jerry on 2016-11-15.
 */

public interface ReadShiftTradePresenter {
    void setRead();
    void acceptShiftTrade();
    void rejectShiftTrade();

    void onDestroy();
}

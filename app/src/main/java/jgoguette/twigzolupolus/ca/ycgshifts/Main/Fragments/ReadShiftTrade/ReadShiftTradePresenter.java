package jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.ReadShiftTrade;

import jgoguette.twigzolupolus.ca.ycgshifts.Model.Shift;

/**
 * Created by jerry on 2016-11-15.
 */

public interface ReadShiftTradePresenter {
    void setRead(String key);
    void acceptShiftTrade(String messageKey, Shift shift);
    void rejectShiftTrade(String messageKey);

    void loadProfilePic(String profilePicOwnerID);

    void onDestroy();
}

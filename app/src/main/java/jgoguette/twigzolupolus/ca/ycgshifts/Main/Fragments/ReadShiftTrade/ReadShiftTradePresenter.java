package jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.ReadShiftTrade;

/**
 * Created by jerry on 2016-11-15.
 */

public interface ReadShiftTradePresenter {
    void setRead(String key);
    void acceptShiftTrade();
    void rejectShiftTrade();

    void loadProfilePic(String profilePicOwnerID);

    void onDestroy();
}

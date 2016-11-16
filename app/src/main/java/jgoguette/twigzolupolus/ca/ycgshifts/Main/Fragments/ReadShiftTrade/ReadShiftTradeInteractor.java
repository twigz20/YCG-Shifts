package jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.ReadShiftTrade;

import android.net.Uri;

import jgoguette.twigzolupolus.ca.ycgshifts.Model.Shift;

/**
 * Created by jerry on 2016-11-15.
 */

public interface ReadShiftTradeInteractor {

    interface OnShiftTradeResponseListener {
        void onShiftTradeSuccessful();
        void onShiftTradeFailed();
    }

    interface OnProfilePicLoadedListener {
        void onProfilePicLoadedSuccess(Uri uri);
        void onProfilePicLoadedFailure();
    }

    void setRead(String key);
    void loadProfilePic(String profilePicOwnerID);

    void acceptShiftTrade(String messageKey, Shift shift);
    void rejectShiftTrade(String messageKey);
}

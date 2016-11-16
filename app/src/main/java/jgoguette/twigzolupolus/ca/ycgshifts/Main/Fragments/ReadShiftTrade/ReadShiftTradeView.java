package jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.ReadShiftTrade;

import android.content.Context;
import android.net.Uri;

/**
 * Created by jerry on 2016-11-15.
 */

public interface ReadShiftTradeView {
    void setTitle();

    void onShiftTradedSuccess();
    void onShiftTradedFailed();
    void loadProfilePic();
    Context getContext();

    void onProfilePicLoadedFailure();
    void setProfilePic(Uri uri);

    void navigateBackToMessages();
}

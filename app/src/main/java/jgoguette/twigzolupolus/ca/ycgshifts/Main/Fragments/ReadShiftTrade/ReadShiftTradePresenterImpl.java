package jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.ReadShiftTrade;

import android.net.Uri;

/**
 * Created by jerry on 2016-11-15.
 */

public class ReadShiftTradePresenterImpl implements ReadShiftTradePresenter,
        ReadShiftTradeInteractor.OnShiftTradeResponseListener, ReadShiftTradeInteractor.OnProfilePicLoadedListener {

    ReadShiftTradeView readShiftTradeView;
    ReadShiftTradeInteractor readShiftTradeInteractor;

    public ReadShiftTradePresenterImpl(ReadShiftTradeView readShiftTradeView) {
        this.readShiftTradeView = readShiftTradeView;

        readShiftTradeInteractor = new ReadShiftTradeInteractorImpl(readShiftTradeView.getContext(), this, this);
    }

    @Override
    public void setRead(String key) {
        readShiftTradeInteractor.setRead(key);
    }

    @Override
    public void acceptShiftTrade() {

    }

    @Override
    public void rejectShiftTrade() {

    }

    @Override
    public void loadProfilePic(String profilePicOwnerID) {
        readShiftTradeInteractor.loadProfilePic(profilePicOwnerID);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onShiftTradeSuccessful() {

    }

    @Override
    public void onShiftTradeFailed() {

    }

    @Override
    public void onProfilePicLoadedSuccess(Uri uri) {
        if(readShiftTradeView != null) {
            readShiftTradeView.setProfilePic(uri);
        }
    }

    @Override
    public void onProfilePicLoadedFailure() {

    }
}

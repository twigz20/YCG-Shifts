package jgoguette.twigzolupolus.ca.ycgshifts.Main.Fragments.ReadShiftTrade;

import android.net.Uri;

import jgoguette.twigzolupolus.ca.ycgshifts.Model.Shift;

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
    public void acceptShiftTrade(String messageKey, Shift shift) {
        readShiftTradeInteractor.acceptShiftTrade(messageKey, shift);
    }

    @Override
    public void rejectShiftTrade(String messageKey) {
        readShiftTradeInteractor.rejectShiftTrade(messageKey);
    }

    @Override
    public void loadProfilePic(String profilePicOwnerID) {
        readShiftTradeInteractor.loadProfilePic(profilePicOwnerID);
    }

    @Override
    public void onDestroy() {
        readShiftTradeView = null;
    }

    @Override
    public void onShiftTradeSuccessful() {
        if(readShiftTradeView != null) {
            readShiftTradeView.onShiftTradedSuccess();
        }
    }

    @Override
    public void onShiftTradeFailed() {
        if(readShiftTradeView != null) {
            readShiftTradeView.onShiftTradedFailed();
        }
    }

    @Override
    public void onProfilePicLoadedSuccess(Uri uri) {
        if(readShiftTradeView != null) {
            readShiftTradeView.setProfilePic(uri);
        }
    }

    @Override
    public void onProfilePicLoadedFailure() {
        if(readShiftTradeView != null) {
            readShiftTradeView.onProfilePicLoadedFailure();
        }
    }
}

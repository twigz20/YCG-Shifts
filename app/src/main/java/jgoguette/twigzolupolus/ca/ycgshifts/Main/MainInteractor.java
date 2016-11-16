package jgoguette.twigzolupolus.ca.ycgshifts.Main;

import jgoguette.twigzolupolus.ca.ycgshifts.Model.User;

/**
 * Created by jerry on 2016-11-14.
 */
public interface MainInteractor {

    interface OnUserProfileFetchedFinishedListener {
        void onSuccess(User user);
        void onFailure(String message);
    }

    void getUserProfile();
}

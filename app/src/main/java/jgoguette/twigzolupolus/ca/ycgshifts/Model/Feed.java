package jgoguette.twigzolupolus.ca.ycgshifts.Model;

/**
 * Created by jerry on 2016-11-14.
 */

public class Feed {

    private String name;

    private String status;

    private Long timeStamp;

    private String firebaseId;

    public Feed() {
    }

    public Feed(String name, String status, Long timeStamp, String firebaseId) {
        this.name = name;
        this.status = status;
        this.timeStamp = timeStamp;
        this.firebaseId = firebaseId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }
}



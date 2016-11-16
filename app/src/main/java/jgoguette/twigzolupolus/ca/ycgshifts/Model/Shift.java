package jgoguette.twigzolupolus.ca.ycgshifts.Model;

import java.io.Serializable;

/**
 * Created by jerry on 2016-11-15.
 */

public class Shift implements Serializable {

    private String end_time;
    private String start_time;

    private String key;
    private String owner;

    private Boolean shiftTradeRequestSent;

    public Shift() {
    }

    public Shift(String end_time, String start_time) {
        this.end_time = end_time;
        this.start_time = start_time;
    }

    public Shift(String end_time, String start_time, String key, String owner, Boolean shiftTradeRequestSent) {
        this.end_time = end_time;
        this.start_time = start_time;
        this.key = key;
        this.owner = owner;
        this.shiftTradeRequestSent = shiftTradeRequestSent;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Boolean getShiftTradeRequestSent() {
        return shiftTradeRequestSent;
    }

    public void setShiftTradeRequestSent(Boolean shiftTradeRequestSent) {
        this.shiftTradeRequestSent = shiftTradeRequestSent;
    }
}


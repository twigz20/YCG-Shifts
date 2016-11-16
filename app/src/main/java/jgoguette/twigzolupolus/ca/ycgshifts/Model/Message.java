package jgoguette.twigzolupolus.ca.ycgshifts.Model;

import java.io.Serializable;

/**
 * Created by jerry on 2016-11-15.
 */

public class Message implements Serializable {

    public enum Type {
        NORMAL_NOTIF, SHIFT_SWAP_NOTIF;
    }

    private String receiver;
    private String sender;
    private String subject;
    private String message;
    private Long timeStamp;
    private String key;
    private Boolean read;
    private String type;

    // For Shift Swap
    private String date;
    private String time;
    private Boolean approval;
    private Shift shift;

    private Boolean notificationDisplayed;

    public Message() {

    }

    public Message(String receiver, String sender, String subject,
                    String message, Long timeStamp, Boolean read, String type) {
        this.receiver = receiver;
        this.sender = sender;
        this.message = message;
        this.subject = subject;
        this.timeStamp = timeStamp;
        this.read = read;
        this.type = type;
    }

    public Message(String receiver, String sender, String subject,
                    String message, Long timeStamp, Boolean read, String type, String date, String time, Boolean approval, Boolean notificationDisplayed) {
        this.receiver = receiver;
        this.sender = sender;
        this.message = message;
        this.subject = subject;
        this.timeStamp = timeStamp;
        this.read = read;
        this.type = type;
        this.date = date;
        this.time = time;
        this.approval = approval;
        this.notificationDisplayed = notificationDisplayed;
    }

    public Message(String receiver, String sender, String subject,
                    String message, Long timeStamp, Boolean read, String type,
                    String date, String time, Boolean approval, Shift shift, Boolean notificationDisplayed) {
        this.receiver = receiver;
        this.sender = sender;
        this.message = message;
        this.subject = subject;
        this.timeStamp = timeStamp;
        this.read = read;
        this.type = type;
        this.date = date;
        this.time = time;
        this.approval = approval;
        this.shift = shift;
        this.notificationDisplayed = notificationDisplayed;
    }

    public Message(Message message) {
        this.receiver = message.receiver;
        this.sender = message.sender;
        this.message = message.message;
        this.subject = message.subject;
        this.timeStamp = message.timeStamp;
        this.read = message.read;
        this.type = message.type;
        this.date = message.date;
        this.time = message.time;
        this.approval = message.approval;
        this.shift = message.shift;
        this.notificationDisplayed = message.notificationDisplayed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Type convertStringToType(String type_) {
        Type type = Type.NORMAL_NOTIF;
        if(type_ == "shift_swap") {
            type = Type.SHIFT_SWAP_NOTIF;
        }

        return type;
    }

    public String convertTypeToString(Type type_) {
        String type = "normal";
        if(type_ == Type.SHIFT_SWAP_NOTIF) {
            type = "shift_swap";
        }

        return type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getApproval() {
        return approval;
    }

    public void setApproval(Boolean approval) {
        this.approval = approval;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public Boolean getNotificationDisplayed() {
        return notificationDisplayed;
    }

    public void setNotificationDisplayed(Boolean notificationDisplayed) {
        this.notificationDisplayed = notificationDisplayed;
    }

}



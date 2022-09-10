package com.kalash.mywhatsapplite.model;

import android.util.Log;

public class MessagesModel {

    String uId ,message,meesageId;
    long timestamp;

    public MessagesModel(String uId, String message, long timestamp) {
        this.uId = uId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public MessagesModel(String uId, String message) {
        this.uId = uId;
        this.message = message;
    }
    public MessagesModel(){}

    public String getMeesageId() {
        return meesageId;
    }

    public void setMeesageId(String meesageId) {
        this.meesageId = meesageId;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getMessage() {
        return message;

    }

    public void setMessage(String message) {
        this.message = message;

    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

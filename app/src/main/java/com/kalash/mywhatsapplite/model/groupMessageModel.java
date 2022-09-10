package com.kalash.mywhatsapplite.model;

public class groupMessageModel {
    String uId ,message,name;
    long timestamp;

    public groupMessageModel(String uId, String message, String name, long timestamp) {
        this.uId = uId;
        this.message = message;
        this.name = name;
        this.timestamp = timestamp;
    }

    public groupMessageModel(String uId, String message, String name) {
        this.uId = uId;
        this.message = message;
        this.name = name;
    }

    public groupMessageModel() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

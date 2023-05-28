package com.cemcoma.myapplication.listings;

public class MessageRequest {
    private String kanalID;
    private String userID;

    public MessageRequest(String kanalID, String userID) {
        this.kanalID = kanalID;
        this.userID = userID;
    }

    public MessageRequest() {
    }

    public void setKanalID(String kanalID) {
        this.kanalID = kanalID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getKanalID() {
        return kanalID;
    }

    public String getUserID() {
        return userID;
    }
}

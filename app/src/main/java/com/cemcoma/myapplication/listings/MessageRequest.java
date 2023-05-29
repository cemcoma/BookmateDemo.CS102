package com.cemcoma.myapplication.listings;

public class MessageRequest {
    private String kanalID;
    private String userID;
    private String userName;
    private String userProfile;

    public MessageRequest(String kanalID, String userID, String userName, String userProfile) {
        this.kanalID = kanalID;
        this.userID = userID;
        this.userName = userName;
        this.userProfile = userProfile;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

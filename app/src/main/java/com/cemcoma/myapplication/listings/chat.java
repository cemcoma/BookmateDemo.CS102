package com.cemcoma.myapplication.listings;

public class chat {
    private String messageContent, sender, receiver, messageType, docId;

    public chat(String messageContent, String sender, String receiver, String messageType, String docId) {
        this.messageContent = messageContent;
        this.sender = sender;
        this.receiver = receiver;
        this.messageType = messageType;
        this.docId = docId;
    }

    public chat() {
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }
}

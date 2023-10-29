package com.vat.icare.chats.fcm;

public class Sender {
    private final Data notification;
    private final Data data;
    private final String to;

    public Sender(Data data,Data notification, String to) {
        this.data = data;
        this.notification = notification;
        this.to = to;
    }
}
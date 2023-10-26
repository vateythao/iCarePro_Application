package com.vat.icare.pojo;

public class Message {
    private String id, message, type, from, to;
    private long timestamp;

    public Message() {

    }

    public Message(String id, String message, String type, String from, String to, long timestamp) {
        this.id = id;
        this.message = message;
        this.type = type;
        this.from = from;
        this.to = to;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

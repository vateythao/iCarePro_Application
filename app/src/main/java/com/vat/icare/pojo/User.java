package com.vat.icare.pojo;

public class User {

    private String token, name, email, status, image;
    private long date;

    public User() {

    }

    public User(String token, String name, String email, String status, String image, long date) {
        this.token = token;
        this.name = name;
        this.email = email;
        this.status = status;
        this.image = image;
        this.date = date;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}

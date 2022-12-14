package com.alex.chatapp.models;

public class User {
    private String id;
    private String username;
    private String phone;
    private String image;

    public User(){

    }

    public User(String id, String username, String phone, String image) {
        this.id = id;
        this.username = username;
        this.phone = phone;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

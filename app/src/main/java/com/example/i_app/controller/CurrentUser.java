package com.example.i_app.controller;

import android.widget.ImageView;

public class CurrentUser {
    private String username;
    private ImageView userImage;
    private String userEmail;

    public CurrentUser(String username, ImageView userImage, String userEmail) {
        this.username = username;
        this.userImage = userImage;
        this.userEmail = userEmail;
    }

    public CurrentUser(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ImageView getUserImage() {
        return userImage;
    }

    public void setUserImage(ImageView userImage) {
        this.userImage = userImage;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}

package com.example.tinpet.Entity;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class Solicitudes implements Serializable {

    @Exclude
    String iudFriend;
    String iudMe;
    String status;

    public Solicitudes(){
    }

    public Solicitudes(String iudFriend, String iudMe, String status) {
        this.iudFriend = iudFriend;
        this.iudMe = iudMe;
        this.status = status;
    }

    public String getIudMe() {
        return iudMe;
    }

    public void setIudMe(String iudMe) {
        this.iudMe = iudMe;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIudFriend() {
        return iudFriend;
    }

    public void setIudFriend(String iudFriend) {
        this.iudFriend = iudFriend;
    }
}

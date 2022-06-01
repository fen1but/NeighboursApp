package com.example.neighbours;

import java.util.ArrayList;

public class User {
    private ArrayList<String> liked;
    private String name, email, phone, uImgId;
    public User(){

    }

    public User(String name, String email, String phone, ArrayList<String> liked){
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.liked = liked;
    }

    public String getuImgId() {
        return uImgId;
    }

    public void setuImgId(String uImgId) {
        this.uImgId = uImgId;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ArrayList<String> getLiked() {
        return liked;
    }

    public void setLiked(ArrayList<String> liked) {
        this.liked = liked;
    }
}

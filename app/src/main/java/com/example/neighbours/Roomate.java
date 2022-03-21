package com.example.neighbours;

public class Roomate {
    private String imgId, id, name, email, phone, pay, sex, bio;
    private boolean pet;
    private int age;

    public Roomate(){

    }

    public Roomate(String imgId, String id, String name, String email, String phone, String pay, String sex, String bio, boolean pet, int age) {
        this.imgId = imgId;
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.pay = pay;
        this.sex = sex;
        this.bio = bio;
        this.pet = pet;
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
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

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public boolean isPet() {
        return pet;
    }

    public void setPet(boolean pet) {
        this.pet = pet;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

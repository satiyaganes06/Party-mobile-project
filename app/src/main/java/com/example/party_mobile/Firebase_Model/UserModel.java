package com.example.party_mobile.Firebase_Model;

public class UserModel {
    String email, name, image, userID;
    int age, phoneNum;



    public UserModel(String email, String name, String image, String userID, int age, int phoneNum) {
        this.email = email;
        this.name = name;
        this.image = image;
        this.userID = userID;
        this.phoneNum = phoneNum;
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return name;
    }

    public void setUsername(String username) {
        this.name = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getphoneNum() {
        return phoneNum;
    }

    public void setphoneNum(int phoneNum) {
        this.phoneNum = phoneNum;
    }
}

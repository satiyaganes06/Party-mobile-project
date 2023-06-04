package com.example.party_mobile.Firebase_Model;

public class UserModel {
    String email, name, image, userID, std_id, user_type;
    int age, phoneNum;



    public UserModel(String email, String name, String image, String std_id, String userID, String user_type,boolean isNotificationEnable) {
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

    public String getstd_id() {
        return std_id;
    }

    public void setStd_id(String std_id) {
        this.std_id = std_id;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
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

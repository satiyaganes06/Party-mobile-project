package com.example.party_mobile.Firebase_Model;

public class PartyDetailsModel {
    private String party_id;
    private String user_id;
    private String party_name;
    private String party_category;
    private String party_type;
    private String user_telNum;
    private String party_date;
    private String party_time;
    private int party_max_capacity;
    private int party_current_capacity;
    private String party_address;
    private String party_city;
    private String party_state;
    private String party_img;
    private String party_join_code;

    public PartyDetailsModel() {
        // Empty constructor needed for Firestore toObject() method
    }


    public PartyDetailsModel(String party_id, String user_id, String party_name, String party_category,
                             String party_type, String user_telNum, String party_date, String party_time,
                             int party_max_capacity, int party_current_capacity,String party_address, String party_city,
                             String party_state, String party_img, String party_join_code) {

        this.party_id = party_id;
        this.user_id = user_id;
        this.party_name = party_name;
        this.party_category = party_category;
        this.party_type = party_type;
        this.user_telNum = user_telNum;
        this.party_date = party_date;
        this.party_time = party_time;
        this.party_max_capacity = party_max_capacity;
        this.setParty_current_capacity(party_current_capacity);
        this.party_address = party_address;
        this.party_city = party_city;
        this.party_state = party_state;
        this.party_img = party_img;
        this.party_join_code = party_join_code;
    }


    public String getParty_id() {
        return party_id;
    }

    public void setParty_id(String party_id) {
        this.party_id = party_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getParty_name() {
        return party_name;
    }

    public void setParty_name(String party_name) {
        this.party_name = party_name;
    }

    public String getParty_category() {
        return party_category;
    }

    public void setParty_category(String party_category) {
        this.party_category = party_category;
    }

    public String getParty_type() {
        return party_type;
    }

    public void setParty_type(String party_type) {
        this.party_type = party_type;
    }

    public String getUser_telNum() {
        return user_telNum;
    }

    public void setUser_telNum(String user_telNum) {
        this.user_telNum = user_telNum;
    }

    public String getParty_date() {
        return party_date;
    }

    public void setParty_date(String party_date) {
        this.party_date = party_date;
    }

    public String getParty_time() {
        return party_time;
    }

    public void setParty_time(String party_time) {
        this.party_time = party_time;
    }

    public int getParty_max_capacity() {
        return party_max_capacity;
    }

    public void setParty_max_capacity(int party_max_capacity) {
        this.party_max_capacity = party_max_capacity;
    }

    public String getParty_address() {
        return party_address;
    }

    public void setParty_address(String party_address) {
        this.party_address = party_address;
    }

    public String getParty_city() {
        return party_city;
    }

    public void setParty_city(String party_city) {
        this.party_city = party_city;
    }

    public String getParty_state() {
        return party_state;
    }

    public void setParty_state(String party_state) {
        this.party_state = party_state;
    }

    public String getParty_img() {
        return party_img;
    }

    public void setParty_img(String party_img) {
        this.party_img = party_img;
    }

    public String getParty_join_code() {
        return party_join_code;
    }

    public void setParty_join_code(String party_join_code) {
        this.party_join_code = party_join_code;
    }

    public int getParty_current_capacity() {
        return party_current_capacity;
    }

    public void setParty_current_capacity(int party_current_capacity) {
        this.party_current_capacity = party_current_capacity;
    }
}

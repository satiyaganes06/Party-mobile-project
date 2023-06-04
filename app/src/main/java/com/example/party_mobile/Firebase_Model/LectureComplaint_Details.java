package com.example.party_mobile.Firebase_Model;

public class LectureComplaint_Details {
    private String image;
    private String email;
    private String user_name;
    private String user_id_num;
    private String user_id;
    private String reported_time;
    private String reported_date;
    private String campus_location;
    private String faculty_name;
    private String user_phone_num;
    private String complaint_category;
    private String complaint;
    private String happen_date;
    private String happen_time;
    private String remark;
    private String address;
    private String complaint_id;
    private String complaint_status;

    public LectureComplaint_Details() {

    }

    public LectureComplaint_Details(String email, String image, String user_name, String user_id_num, String user_id,
                                    String reported_time, String reported_date, String campus_location,
                                    String faculty_name, String user_phone_num, String complaint_category, String complaint,
                                    String happen_date, String happen_time, String remark, String address,String complaint_id, String complaint_status) {

        this.email = email;
        this.image = image;
        this.user_name = user_name;
        this.user_id_num = user_id_num;
        this.user_id = user_id;
        this.reported_time = reported_time;
        this.reported_date = reported_date;
        this.campus_location = campus_location;
        this.faculty_name = faculty_name;
        this.user_phone_num = user_phone_num;
        this.complaint_category = complaint_category;
        this.complaint = complaint;
        this.happen_date = happen_date;
        this.happen_time = happen_time;
        this.remark = remark;
        this.address = address;
        this.complaint_id = complaint_id;
        this.complaint_status = complaint_status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_id_num() {
        return user_id_num;
    }

    public void setUser_id_num(String user_id_num) {
        this.user_id_num = user_id_num;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getReported_time() {
        return reported_time;
    }

    public void setReported_time(String reported_time) {
        this.reported_time = reported_time;
    }

    public String getReported_date() {
        return reported_date;
    }

    public void setReported_date(String reported_date) {
        this.reported_date = reported_date;
    }

    public String getCampus_location() {
        return campus_location;
    }

    public void setCampus_location(String campus_location) {
        this.campus_location = campus_location;
    }

    public String getFaculty_name() {
        return faculty_name;
    }

    public void setFaculty_name(String faculty_name) {
        this.faculty_name = faculty_name;
    }

    public String getUser_phone_num() {
        return user_phone_num;
    }

    public void setUser_phone_num(String user_phone_num) {
        this.user_phone_num = user_phone_num;
    }

    public String getComplaint_category() {
        return complaint_category;
    }

    public void setComplaint_category(String complaint_category) {
        this.complaint_category = complaint_category;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public String getHappen_date() {
        return happen_date;
    }

    public void setHappen_date(String happen_date) {
        this.happen_date = happen_date;
    }

    public String getHappen_time() {
        return happen_time;
    }

    public void setHappen_time(String happen_time) {
        this.happen_time = happen_time;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getComplaint_id() {
        return complaint_id;
    }

    public void setComplaint_id(String complaint_id) {
        this.complaint_id = complaint_id;
    }

    public String getComplaint_status() {
        return complaint_status;
    }

    public void setComplaint_status(String complaint_status) {
        this.complaint_status = complaint_status;
    }
}

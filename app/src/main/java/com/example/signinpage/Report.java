package com.example.signinpage;

import android.os.Parcel;

import java.io.Serializable;

public class Report implements Serializable {
    private String id;
    private String type;
    private String information;
    private String imgOfReport;
    private Double latitude;
    private Double longitude;
    private String userId;
    private String fullName;
    private boolean status;
    private long date;
    private String phoneNum;
    private String address;


    public Report(String id, String type, String information, String imgOfReport, Double latitude, Double longitude, String userId, String fullName, boolean status, long date, String phoneNum, String address) {
        this.id = id;
        this.type = type;
        this.information = information;
        this.imgOfReport = imgOfReport;
        this.latitude = latitude;
        this.longitude = longitude;
        this.userId = userId;
        this.fullName = fullName;
        this.status = status;
        this.date = date;
        this.phoneNum = phoneNum;
        this.address = address;
    }

    public Report() {
    }



    public String getImgOfReport() {
        return imgOfReport;
    }

    public void setImgOfReport(String imgOfReport) {
        this.imgOfReport = imgOfReport;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}

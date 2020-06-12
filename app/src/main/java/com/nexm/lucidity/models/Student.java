package com.nexm.lucidity.models;

public class Student {

    private String uid;
    private String std;
    private String name;
    private String motherName = "x";
    private String address;
    private String moNo;
    private String altMoNo;
    private long date;
    private boolean paid = false;

    public Student(){}

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getDate() {
        return date;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setStd(String std) {
        this.std = std;
    }

    public String getStd() {
        return std;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAltMoNo(String altMoNo) {
        this.altMoNo = altMoNo;
    }

    public String getAltMoNo() {
        return altMoNo;
    }

    public void setMoNo(String moNo) {
        this.moNo = moNo;
    }

    public String getMoNo() {
        return moNo;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }
}

package com.nexm.lucidity.models;

public class LiveClass {
    private String subject;
    private String std;
    private String unitNo;
    private String unitName;
    private String name;
    private String stime;
    private String etime;
    private String date;
    private String teacherName;
    private String videoID;
    private String videoDuration;
    private String unitID;
    private String id;

    public LiveClass(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnitID() {
        return unitID;
    }

    public void setUnitID(String unitID) {
        this.unitID = unitID;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(String videoDuration) {
        this.videoDuration = videoDuration;
    }

    public String getDate() {
        return date;
    }

    public String getVideoID() {
        return videoID;
    }

    public void setVideoID(String videoID) {
        this.videoID = videoID;
    }

    public String getEtime() {
        return etime;
    }

    public String getStime() {
        return stime;
    }

    public void setEtime(String etime) {
        this.etime = etime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }

    public void setStd(String std) {
        this.std = std;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getSubject() {
        return subject;
    }

    public String getUnitNo() {
        return unitNo;
    }

    public String getStd() {
        return std;
    }

    public String getUnitName() {
        return unitName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public String getName() {
        return name;
    }
}

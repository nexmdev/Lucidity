package com.nexm.lucidity.models;

public class Topic {
    private String id;
    private String name;
    private int notes;
    private int questions;
    private String videoDuration;
    private String unitID;
    private String unitName;
    private String unitNo;
    private String videoID;

    public Topic(){}

    public void setUnitID(String unitID) {
        this.unitID = unitID;
    }

    public String getUnitID() {
        return unitID;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }

    public String getUnitNo() {
        return unitNo;
    }

    public void setVideoID(String videoID) {
        this.videoID = videoID;
    }

    public String getVideoID() {
        return videoID;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setNotes(int notes) {
        this.notes = notes;
    }

    public int getNotes() {
        return notes;
    }

    public void setQuestions(int questions) {
        this.questions = questions;
    }

    public int getQuestions() {
        return questions;
    }

    public void setVideoDuration(String videoDuration) {
        this.videoDuration = videoDuration;
    }

    public String getVideoDuration() {
        return videoDuration;
    }
}

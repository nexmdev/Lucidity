package com.nexm.lucidity.models;

public class CurrentTopic {
    String id;
    int progress;
    String unitid="x";

    public CurrentTopic(){}

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getUnitid() {
        return unitid;
    }

    public void setUnitid(String unitid) {
        this.unitid = unitid;
    }
}

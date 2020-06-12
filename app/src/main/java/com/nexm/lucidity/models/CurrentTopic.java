package com.nexm.lucidity.models;

public class CurrentTopic {
    String id;
    int progress;

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
}

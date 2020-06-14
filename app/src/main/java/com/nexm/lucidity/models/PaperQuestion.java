package com.nexm.lucidity.models;

public class PaperQuestion {
    String id;
    String imageUrl;
    String marks;
    String text;

    public PaperQuestion(){}

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public String getMarks() {
        return marks;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}

package com.nexm.lucidity.models;

public class Notes {
    private String text;
    private String id;
    private String imageUrl;

    public Notes(){}

    public String getImageUrl() {
        return imageUrl;
    }

    public String getText() {
        return text;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

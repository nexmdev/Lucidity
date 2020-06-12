package com.nexm.lucidity.models;

public class Subject {
    private String id ;
    private String name ;
    private String number ;
    private String description ;

    public Subject(){}

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getId() {
        return id;
    }

    public void setNumber(String color) {
        this.number = color;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

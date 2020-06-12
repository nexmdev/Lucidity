package com.nexm.lucidity.models;

public class Test {
    String description;
    String id;
    String marks;
    String subject;
    String time;
    String unit;
    String unitNo;

    public Test(){}

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getUnitNo() {
        return unitNo;
    }

    public String getMarks() {
        return marks;
    }

    public String getSubject() {
        return subject;
    }

    public String getTime() {
        return time;
    }

    public String getUnit() {
        return unit;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

}


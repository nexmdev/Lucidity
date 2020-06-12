package com.nexm.lucidity.models;

public class Question {
    String questionText;
    String questionImage;
    String id;
    String correctOption;
    String answer1;
    String answer1Image;
    String answer2;
    String answer2Image;
    String answer3;
    String answer3Image;

    public Question(){}

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionImage() {
        return questionImage;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getId() {
        return id;
    }

    public void setQuestionImage(String questionImage) {
        this.questionImage = questionImage;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getAnswer1() {
        return answer1;
    }

    public String getAnswer1Image() {
        return answer1Image;
    }

    public String getAnswer2() {
        return answer2;
    }

    public String getAnswer2Image() {
        return answer2Image;
    }

    public String getAnswer3() {
        return answer3;
    }

    public String getAnswer3Image() {
        return answer3Image;
    }

    public String getCorrectOption() {
        return correctOption;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public void setAnswer1Image(String answer1Image) {
        this.answer1Image = answer1Image;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public void setAnswer2Image(String answer2Image) {
        this.answer2Image = answer2Image;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public void setAnswer3Image(String answer3Image) {
        this.answer3Image = answer3Image;
    }

    public void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }

}

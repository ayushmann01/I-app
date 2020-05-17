package com.example.i_app.model;

public class QuestionDownModel {
    private String uploader, question;

    public QuestionDownModel(String uploader, String question) {
        this.uploader = uploader;
        this.question = question;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}

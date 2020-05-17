package com.example.i_app.model;

public class NotesDownModel {

    private String pdf_name, link, uploader;

    public NotesDownModel(String pdf_name, String link, String uploader) {
        this.pdf_name = pdf_name;
        this.link = link;
        this.uploader = uploader;
    }

    public String getPdfName() {
        return pdf_name;
    }

    public void setPdfName(String name) {
        this.pdf_name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }
}

package com.dania.scheduler;

public class DataModel {

    private String date;
    private String title;
    private String content;
    private String timestamp;
    private String status;
    private boolean selected;
    private String dateTimestamp;

    public DataModel(String date, String title, String content, String timestamp, String status, boolean selected, String dateTimestamp) {
        this.date = date;
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
        this.status = status;
        this.selected = selected;
        this.dateTimestamp = dateTimestamp;
    }

    public String getDateTimestamp() {
        return dateTimestamp;
    }

    public void setDateTimestamp(String dateTimestamp) {
        this.dateTimestamp = dateTimestamp;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

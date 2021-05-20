package com.dania.scheduler;

import java.util.Date;

public class TimeModel {
    private String dateLable;
    private String dateTimestamp;
    private String date;
    private String month;
    private String day;
    Boolean isSelected = false;

    public TimeModel(String dateLable, String dateTimestamp, String date, String month, String day, Boolean isSelected) {
        this.dateLable = dateLable;
        this.dateTimestamp = dateTimestamp;
        this.date = date;
        this.month = month;
        this.day = day;
        this.isSelected = isSelected;
    }

    public String getDateLable() {
        return dateLable;
    }

    public void setDateLable(String dateLable) {
        this.dateLable = dateLable;
    }

    public String getDateTimestamp() {
        return dateTimestamp;
    }

    public void setDateTimestamp(String dateTimestamp) {
        this.dateTimestamp = dateTimestamp;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}

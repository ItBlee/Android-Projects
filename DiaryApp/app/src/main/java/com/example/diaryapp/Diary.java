package com.example.diaryapp;

public class Diary {
    private String ID;
    private String title;
    private String content;
    private String dateTime;
    private String filePath;
    private String weather;
    private String location;

    public Diary() {}

    public Diary(String title, String content, String dateTime, String filePath, String weather, String location) {
        this.title = title;
        this.content = content;
        this.dateTime = dateTime;
        this.filePath = filePath;
        this.weather = weather;
        this.location = location;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

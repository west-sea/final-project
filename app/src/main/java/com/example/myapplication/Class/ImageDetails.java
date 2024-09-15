package com.example.myapplication.Class;

import org.json.JSONObject;

import java.util.Date;

public class ImageDetails {
    private String imageName;
    private Date selectedDate;
    private String description;
    private static int idCounter = 0;

    private int id;

    public ImageDetails(String imageName, Date selectedDate, String description) {
        this.imageName = imageName;
        this.selectedDate = selectedDate;
        this.description = description;
        this.id = idCounter++;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdFromJson(JSONObject jsonObject) {
        this.id = jsonObject.optInt("id", idCounter++);
    }
    public int getId() {
        return id;
    }
    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }


    public Date getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
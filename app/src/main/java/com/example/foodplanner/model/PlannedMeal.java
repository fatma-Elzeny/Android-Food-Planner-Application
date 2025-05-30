package com.example.foodplanner.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;



import androidx.annotation.NonNull;


@Entity(tableName = "planned_meals")
public class PlannedMeal {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String mealId;
    private String mealName;
    private String mealThumb;
    private String day;
    private String date;
    private String userId;
    private long eventId;
    private long dateTimeInMillis;

    public void setDateTimeInMillis(long dateTimeInMillis) {
        this.dateTimeInMillis = dateTimeInMillis;
    }

    public long getDateTimeInMillis() { return dateTimeInMillis; }

    public long getEventId() { return eventId; }
    public void setEventId(long eventId) { this.eventId = eventId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public PlannedMeal() {
    }

    public PlannedMeal(String mealId, String mealName, String mealThumb, String day) {
        this.mealId = mealId;
        this.mealName = mealName;
        this.mealThumb = mealThumb;
        this.day = day;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMealId() {
        return mealId;
    }

    public void setMealId(String mealId) {
        this.mealId = mealId;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getMealThumb() {
        return mealThumb;
    }

    public void setMealThumb(String mealThumb) {
        this.mealThumb = mealThumb;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

}

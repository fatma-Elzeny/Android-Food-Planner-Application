package com.example.foodplanner.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "planned_meals")
public class PlannedMeal {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String mealId;
    private String day; // e.g., "Monday"
    private String strMeal;
    private String strMealThumb;

    public PlannedMeal(String mealId, String day, String strMeal, String strMealThumb) {
        this.mealId = mealId;
        this.day = day;
        this.strMeal = strMeal;
        this.strMealThumb = strMealThumb;
    }

    public int getId() {
        return id;
    }

    public String getMealId() {
        return mealId;
    }

    public String getDay() {
        return day;
    }

    public String getStrMeal() {
        return strMeal;
    }

    public String getStrMealThumb() {
        return strMealThumb;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMealId(String mealId) {
        this.mealId = mealId;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setStrMeal(String strMeal) {
        this.strMeal = strMeal;
    }

    public void setStrMealThumb(String strMealThumb) {
        this.strMealThumb = strMealThumb;
    }
}

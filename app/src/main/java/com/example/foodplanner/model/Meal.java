package com.example.foodplanner.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Meals_table")
public class Meal {
    @NonNull
    @PrimaryKey
    private String idMeal;
    private String strMeal;
    private String strCategory;
    private String strArea;
    private String strMealThumb;
    private String strInstructions;
    private String strYoutube;

    public Meal(String idMeal, String strMeal, String strCategory, String strArea, String strMealThumb, String strInstructions, String strYoutube) {
        this.idMeal = idMeal;
        this.strMeal = strMeal;
        this.strCategory = strCategory;
        this.strArea = strArea;
        this.strMealThumb = strMealThumb;
        this.strInstructions = strInstructions;
        this.strYoutube = strYoutube;
    }

    public String getIdMeal() {
        return idMeal;
    }

    public String getStrMeal() {
        return strMeal;
    }

    public String getStrCategory() {
        return strCategory;
    }

    public String getStrArea() {
        return strArea;
    }

    public String getStrMealThumb() {
        return strMealThumb;
    }

    public String getStrInstructions() {
        return strInstructions;
    }

    public String getStrYoutube() {
        return strYoutube;
    }

    public void setStrYoutube(String strYoutube) {
        this.strYoutube = strYoutube;
    }

    public void setStrInstructions(String strInstructions) {
        this.strInstructions = strInstructions;
    }

    public void setStrMealThumb(String strMealThumb) {
        this.strMealThumb = strMealThumb;
    }

    public void setStrArea(String strArea) {
        this.strArea = strArea;
    }

    public void setStrCategory(String strCategory) {
        this.strCategory = strCategory;
    }

    public void setStrMeal(String strMeal) {
        this.strMeal = strMeal;
    }

    public void setIdMeal(String idMeal) {
        this.idMeal = idMeal;
    }
}

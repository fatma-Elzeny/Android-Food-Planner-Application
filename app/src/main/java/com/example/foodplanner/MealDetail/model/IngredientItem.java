package com.example.foodplanner.MealDetail.model;

public class IngredientItem {

    private String name;
    private String measure;
    private String imageUrl;

    public IngredientItem(String name, String measure, String imageUrl) {
        this.name = name;
        this.measure = measure;
        this.imageUrl = imageUrl;
    }

    public String getName() { return name; }
    public String getMeasure() { return measure; }
    public String getImageUrl() { return imageUrl; }

}

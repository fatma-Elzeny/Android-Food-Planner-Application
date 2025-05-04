package com.example.foodplanner.model;

public class Ingredient {
    private String strIngredient;
    private String strDescription;
    private String strIngredientThumb;

    public void setStrIngredient(String strIngredient) {
        this.strIngredient = strIngredient;
    }

    public void setStrDescription(String strDescription) {
        this.strDescription = strDescription;
    }

    public void setStrIngredientThumb(String strIngredientThumb) {
        this.strIngredientThumb = strIngredientThumb;
    }

    public String getStrIngredient() {
        return strIngredient;
    }

    public String getStrDescription() {
        return strDescription;
    }

    public String getStrIngredientThumb() {
        return strIngredientThumb;
    }
}
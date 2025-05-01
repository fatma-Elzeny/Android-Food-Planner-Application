package com.example.foodplanner.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.foodplanner.MealDetail.model.IngredientItem;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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
    public List<IngredientItem> getIngredientItemList() {
        List<IngredientItem> ingredients = new ArrayList<>();
        try {
            for (int i = 1; i <= 20; i++) {
                Field ingredientField = this.getClass().getDeclaredField("strIngredient" + i);
                Field measureField = this.getClass().getDeclaredField("strMeasure" + i);

                ingredientField.setAccessible(true);
                measureField.setAccessible(true);

                String ingredient = (String) ingredientField.get(this);
                String measure = (String) measureField.get(this);

                if (ingredient != null && !ingredient.trim().isEmpty()) {
                    String imageUrl = "https://www.themealdb.com/images/ingredients/" + ingredient.trim() + ".png";
                    ingredients.add(new IngredientItem(ingredient.trim(), measure != null ? measure.trim() : "", imageUrl));
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Avoid crashing app
        }
        return ingredients;
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

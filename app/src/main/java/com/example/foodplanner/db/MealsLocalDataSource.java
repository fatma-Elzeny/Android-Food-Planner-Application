package com.example.foodplanner.db;

import com.example.foodplanner.model.FavoriteMeal;
import com.example.foodplanner.model.PlannedMeal;

import java.util.List;

public interface MealsLocalDataSource {
    void insertFavorite(FavoriteMeal meal);
    void deleteFavorite(FavoriteMeal meal);
    FavoriteMeal getFavoriteById(String id);
    List<FavoriteMeal> getAllFavorites();

    // Planned Meals
    void insertPlannedMeal(PlannedMeal meal);
    void deletePlannedMeal(PlannedMeal meal);
    List<PlannedMeal> getMealsByDay(String day);
    List<PlannedMeal> getAllPlannedMeals();
}

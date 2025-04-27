package com.example.foodplanner.model;

import java.util.List;

public interface MealsRepository {
    void insertFavorite(FavoriteMeal meal);
    void deleteFavorite(FavoriteMeal meal);
    List<FavoriteMeal> getAllFavorites();
    FavoriteMeal getFavoriteById(String id);

    // Weekly Plan
    void insertPlannedMeal(PlannedMeal meal);
    void deletePlannedMeal(PlannedMeal meal);
    List<PlannedMeal> getMealsByDay(String day);
    List<PlannedMeal> getAllPlannedMeals();
}

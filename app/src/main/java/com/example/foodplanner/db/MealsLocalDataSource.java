package com.example.foodplanner.db;

import androidx.lifecycle.LiveData;

import com.example.foodplanner.model.FavoriteMeal;
import com.example.foodplanner.model.Meal;
import com.example.foodplanner.model.PlannedMeal;

import java.util.List;

public interface MealsLocalDataSource {



    // Favourite Meals

    void setCurrentUserId(String userId);

    LiveData<List<FavoriteMeal>> getAllFavorites();

    void insertMeal(FavoriteMeal meal);

    void deleteMeal(FavoriteMeal meal);


    // planned meals
    LiveData<List<PlannedMeal>> getPlannedFood(String date);
    void insertFoodPlan(PlannedMeal foodPlan);
    void deleteFoodPlan(PlannedMeal foodPlan);
    void updateFoodPlan(PlannedMeal foodPlan);
}

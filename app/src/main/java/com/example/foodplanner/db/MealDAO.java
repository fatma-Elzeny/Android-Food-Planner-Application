package com.example.foodplanner.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.foodplanner.model.FavoriteMeal;
import com.example.foodplanner.model.PlannedMeal;

import java.util.List;
@Dao
public interface MealDAO {
    // Favorite meals
    @Insert
    void insertFavorite(FavoriteMeal meal);

    @Delete
    void deleteFavorite(FavoriteMeal meal);

    @Query("SELECT * FROM favorite_meals")
    LiveData<List<FavoriteMeal>> getAllFavorites(); // ✅ works with Room + LiveData


    @Query("SELECT * FROM favorite_meals WHERE idMeal = :mealId")
    LiveData<FavoriteMeal> getFavoriteById(String mealId);

    // Planned meals
    @Insert
    void insertPlanned(PlannedMeal meal);

    @Delete
    void deletePlanned(PlannedMeal meal);

    @Query("SELECT * FROM planned_meals WHERE day = :day")
    List<PlannedMeal> getMealsByDay(String day);

    @Query("SELECT * FROM planned_meals")
    List<PlannedMeal> getAllPlannedMeals();

























}

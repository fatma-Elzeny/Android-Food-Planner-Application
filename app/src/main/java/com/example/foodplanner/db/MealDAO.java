package com.example.foodplanner.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foodplanner.model.FavoriteMeal;
import com.example.foodplanner.model.PlannedMeal;

import java.util.List;
@Dao
public interface MealDAO {
    // Favorite meals
    @Insert
    void insertFavoriteMeal(FavoriteMeal meal);

    @Delete
    void deleteFavoriteMeal(FavoriteMeal meal);

    @Query("SELECT * FROM favorite_meals")
    LiveData<List<FavoriteMeal>> getAllFavorites();

    // Planned meals
    @Insert
    void insertPlannedMeal(PlannedMeal meal);

    @Delete
    void deletePlannedMeal(PlannedMeal meal);

    @Query("SELECT * FROM planned_meals WHERE day = :day")
    LiveData<List<PlannedMeal>>getMealsByDay(String day);

    @Query("SELECT * FROM planned_meals")
    List<PlannedMeal> getAllPlannedMeals();

    @Update
    void updatePlannedMeals(PlannedMeal plannedMeal);

























}

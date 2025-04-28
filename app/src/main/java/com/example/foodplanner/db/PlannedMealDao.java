package com.example.foodplanner.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.foodplanner.model.PlannedMeal;

import java.util.List;
@Dao
public interface PlannedMealDao {

    @Insert
    void insertPlannedMeal(PlannedMeal meal);

    @Delete
    void deletePlannedMeal(PlannedMeal meal);

    @Query("SELECT * FROM planned_meals WHERE day = :day")
    List<PlannedMeal> getMealsByDay(String day);

    @Query("SELECT * FROM planned_meals")
    List<PlannedMeal> getAllPlannedMeals();
}

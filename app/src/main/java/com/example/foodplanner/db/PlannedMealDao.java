package com.example.foodplanner.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foodplanner.model.PlannedMeal;

import java.util.List;
@Dao
public interface PlannedMealDao {

    @Insert
    void insertPlannedMeal(PlannedMeal meal);

    @Delete
    void deletePlannedMeal(PlannedMeal meal);

    @Query("SELECT * FROM planned_meals WHERE day = :day AND userId = :userId")
    LiveData<List<PlannedMeal>> getMealsByDay(String day, String userId);


    @Query("SELECT * FROM planned_meals")
    List<PlannedMeal> getAllPlannedMeals();

    @Update
    void updatePlannedMeals(PlannedMeal plannedMeal);

    @Query("SELECT * FROM planned_meals WHERE mealId = :mealId")
    LiveData<PlannedMeal> getPlannedMealById(String mealId);

}

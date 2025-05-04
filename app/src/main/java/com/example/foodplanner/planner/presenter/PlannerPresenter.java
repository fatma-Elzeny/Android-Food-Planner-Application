package com.example.foodplanner.planner.presenter;

import androidx.lifecycle.LiveData;

import com.example.foodplanner.model.PlannedMeal;

import java.util.List;

public interface PlannerPresenter {
    LiveData<List<PlannedMeal>> loadMealsForDay(String day);
    void deletePlannedMeal(PlannedMeal meal);


}

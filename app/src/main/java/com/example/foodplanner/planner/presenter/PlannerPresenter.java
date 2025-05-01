package com.example.foodplanner.planner.presenter;

import com.example.foodplanner.model.PlannedMeal;

public interface PlannerPresenter {
    void loadMealsForDay(String day);
    void deletePlannedMeal(PlannedMeal meal);


}

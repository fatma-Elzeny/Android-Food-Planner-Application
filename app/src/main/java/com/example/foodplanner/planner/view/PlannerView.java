package com.example.foodplanner.planner.view;

import com.example.foodplanner.model.PlannedMeal;

import java.util.List;

public interface PlannerView {
    void showMeals(List<PlannedMeal> meals);
    void showEmpty();

    void showError(String message);
}

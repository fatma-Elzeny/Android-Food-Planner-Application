package com.example.foodplanner.planner.view;

import com.example.foodplanner.model.PlannedMeal;

public interface OnPlannerClickListener {
    void onMealClick(PlannedMeal meal);
    void onDeleteClick(PlannedMeal meal);
}

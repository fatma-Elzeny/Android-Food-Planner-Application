package com.example.foodplanner.MealDetail.view;

import com.example.foodplanner.model.Meal;

public interface MealDetailsView {
    void showLoading();
    void hideLoading();
    void showMealDetails(Meal meal);
    void showError(String message);
}

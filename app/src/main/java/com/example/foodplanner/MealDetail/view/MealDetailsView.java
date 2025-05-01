package com.example.foodplanner.MealDetail.view;

import com.example.foodplanner.model.FavoriteMeal;
import com.example.foodplanner.model.Meal;

import java.lang.reflect.InvocationTargetException;

public interface MealDetailsView {
    void showLoading();
    void hideLoading();
    void showMealDetails(Meal meal);
    void showError(String message);
}

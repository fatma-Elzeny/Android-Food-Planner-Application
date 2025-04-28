package com.example.foodplanner.home.view;

import com.example.foodplanner.model.Meal;

import java.util.List;

public interface HomeView {

    void showLoading();
    void hideLoading();
    void showSuggestedMeal(Meal meal);
    void showLazyMeals(List<Meal> meals);
    void showError(String message);
}

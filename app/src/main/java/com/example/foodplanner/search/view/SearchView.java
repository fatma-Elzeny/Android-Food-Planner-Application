package com.example.foodplanner.search.view;

import com.example.foodplanner.model.Category;
import com.example.foodplanner.model.Country;
import com.example.foodplanner.model.Ingredient;
import com.example.foodplanner.model.Meal;

import java.util.List;

public interface SearchView {
    void showMeals(List<Meal> meals);
    void showEmptyState(String message);
    void showError(String errorMessage);
    void showLoading();
    void hideLoading();


}

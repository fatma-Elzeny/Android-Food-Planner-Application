package com.example.foodplanner.search.view;

import android.widget.SearchView;

import com.example.foodplanner.model.Category;
import com.example.foodplanner.model.Country;
import com.example.foodplanner.model.Ingredient;
import com.example.foodplanner.model.Meal;

import java.util.List;

public interface SearchScreen {

    void displayMeals(List<Meal> meals);
    void displayError(String errMsg);

    void showMeals(List<Meal> meals);

    void displayEmptyState(String message);

    // Show loading indicator
    void showLoading();

    // Hide loading indicator
    void hideLoading();

    void showEmptyState(String s);

    void showError(String errorMessage);
}



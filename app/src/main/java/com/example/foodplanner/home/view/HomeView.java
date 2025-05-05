package com.example.foodplanner.home.view;

import com.example.foodplanner.model.Category;
import com.example.foodplanner.model.CategoryResponse;
import com.example.foodplanner.model.Country;
import com.example.foodplanner.model.Meal;

import java.util.List;

public interface HomeView {


    void showLoading();
    void hideLoading();
    void showSuggestedMeal(Meal meal);
    void showLazyMeals(List<Meal> meals);
    void showError(String message);

    // For categories section
    void showCategories(List<Category> categories);
    void showCategoryLoading();
    void hideCategoryLoading();

    // For meals by category section
    void showMealsByCategory(String categoryName, List<Meal> meals);

    // For area-based meals
    void showMealsByArea(String areaName, List<Meal> meals);

    void showAreas(List<Country> areas);
    void showAreaLoading();
    void hideAreaLoading();


}

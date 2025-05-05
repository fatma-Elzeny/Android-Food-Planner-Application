package com.example.foodplanner.search.presenter;

public interface searchPresenter {
    void getMealsByCategory(String category);
    void getMealsById(String id);
    void getMealsByName(String name);
    void getMealsByCountry(String country);
    void getMealsByIngredient(String ingredient);
    void getAllCategories();
    void getAllCountries();

    void getAllIngredients();
}

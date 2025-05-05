package com.example.foodplanner.home.presenter;

public interface HomePresenter {

    void getMealOfTheDay();
    void getMealsByCategory(String category);
    void onDestroy();

    void loadMealsByCategory(String category);

    void getAllCategories();

    void getAllAreas();
    void getMealsByArea(String area);
}

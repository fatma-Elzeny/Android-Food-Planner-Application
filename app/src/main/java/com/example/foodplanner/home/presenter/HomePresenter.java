package com.example.foodplanner.home.presenter;

public interface HomePresenter {

    void getMealOfTheDay();
    void getMealsByCategory(String category);
    void onDestroy();

    void loadMealsByCategory(String category);

}

package com.example.foodplanner.Favorite.presenter;

import com.example.foodplanner.model.FavoriteMeal;

public interface Favpresenter {
    void getFavoriteMeals();
    void deleteMeal(FavoriteMeal meal);
    void onDestroy();
}

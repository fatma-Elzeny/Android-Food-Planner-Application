package com.example.foodplanner.Favorite.presenter;

import com.example.foodplanner.model.FavoriteMeal;
import com.example.foodplanner.model.Meal;

public interface Favpresenter {
    void getFavoriteMeals();
    void deleteMeal(FavoriteMeal meal);
    void onDestroy();
}

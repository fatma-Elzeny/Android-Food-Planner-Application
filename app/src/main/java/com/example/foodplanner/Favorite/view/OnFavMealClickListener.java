package com.example.foodplanner.Favorite.view;

import com.example.foodplanner.model.FavoriteMeal;
import com.example.foodplanner.model.Meal;

public interface OnFavMealClickListener {
    void onFavoriteMealClick(FavoriteMeal meal);

    void onDeleteClick(FavoriteMeal meal);
}

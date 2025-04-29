package com.example.foodplanner.Favorite.view;

import com.example.foodplanner.model.FavoriteMeal;

import java.util.List;

public interface FavoritesView {

    void showFavorites(List<FavoriteMeal> meals);
    void showEmptyState();
    void showDeleteConfirmation(FavoriteMeal meal);
}

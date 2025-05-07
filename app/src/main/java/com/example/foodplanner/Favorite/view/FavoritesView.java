package com.example.foodplanner.Favorite.view;

import androidx.lifecycle.LiveData;

import com.example.foodplanner.model.FavoriteMeal;
import com.example.foodplanner.model.Meal;

import java.util.List;

public interface FavoritesView {
    void observeFavorites(LiveData<List<FavoriteMeal>> liveData);
    void showEmptyState();

    // public void showDeleteConfirmation(FavoriteMeal meal);
}

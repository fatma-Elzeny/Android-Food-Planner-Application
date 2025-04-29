package com.example.foodplanner.Favorite.presenter;

import com.example.foodplanner.Favorite.view.FavoritesView;
import com.example.foodplanner.model.FavoriteMeal;
import com.example.foodplanner.model.MealsRepository;

import java.util.List;

public class FavPresenterImpl implements Favpresenter {
    private FavoritesView view;
    private MealsRepository repository;

    public FavPresenterImpl(FavoritesView view, MealsRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void getFavoriteMeals() {
        List<FavoriteMeal> meals = repository.getAllFavorites();
        if (meals == null || meals.isEmpty()) {
            view.showEmptyState();
        } else {
            view.showFavorites(meals);
        }
    }

    @Override
    public void deleteMeal(FavoriteMeal meal) {
        repository.deleteFavorite(meal);
        getFavoriteMeals(); // Refresh list
    }

    @Override
    public void onDestroy() {
        view = null;
    }
}

package com.example.foodplanner.db;

import com.example.foodplanner.model.FavoriteMeal;
import com.example.foodplanner.model.PlannedMeal;

import java.util.Collections;
import java.util.List;

public class MealsLocalDataSourceImpl implements MealsLocalDataSource{
    @Override
    public void insertFavorite(FavoriteMeal meal) {

    }

    @Override
    public void deleteFavorite(FavoriteMeal meal) {

    }

    @Override
    public FavoriteMeal getFavoriteById(String id) {
        return null;
    }

    @Override
    public List<FavoriteMeal> getAllFavorites() {
        return Collections.emptyList();
    }

    @Override
    public void insertPlannedMeal(PlannedMeal meal) {

    }

    @Override
    public void deletePlannedMeal(PlannedMeal meal) {

    }

    @Override
    public List<PlannedMeal> getMealsByDay(String day) {
        return Collections.emptyList();
    }

    @Override
    public List<PlannedMeal> getAllPlannedMeals() {
        return Collections.emptyList();
    }
}

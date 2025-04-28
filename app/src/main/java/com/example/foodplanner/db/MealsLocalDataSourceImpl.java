package com.example.foodplanner.db;

import android.content.Context;

import com.example.foodplanner.model.FavoriteMeal;
import com.example.foodplanner.model.PlannedMeal;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MealsLocalDataSourceImpl implements MealsLocalDataSource{
    private FavoriteMealDao favoriteMealDao;
    private PlannedMealDao plannedMealDao;
    private Executor executor;

    public LocalDataSourceImpl(Context context) {
        AppDataBase db = AppDataBase.getInstance(context);
        favoriteMealDao = db.favoriteMealDao();
        plannedMealDao = db.plannedMealDao();
        executor = Executors.newSingleThreadExecutor();
    }

    // --------- Favorites ---------

    @Override
    public void insertFavorite(FavoriteMeal meal) {
        executor.execute(() -> favoriteMealDao.insertFavoriteMeal(meal));
    }

    @Override
    public void deleteFavorite(FavoriteMeal meal) {
        executor.execute(() -> favoriteMealDao.deleteFavoriteMeal(meal));
    }

    @Override
    public FavoriteMeal getFavoriteById(String id) {
        return favoriteMealDao.getFavoriteById(id);
    }

    @Override
    public List<FavoriteMeal> getAllFavorites() {
        return favoriteMealDao.getAllFavorites();
    }

    // --------- Planned Meals ---------

    @Override
    public void insertPlannedMeal(PlannedMeal meal) {
        executor.execute(() -> plannedMealDao.insertPlannedMeal(meal));
    }

    @Override
    public void deletePlannedMeal(PlannedMeal meal) {
        executor.execute(() -> plannedMealDao.deletePlannedMeal(meal));
    }

    @Override
    public List<PlannedMeal> getMealsByDay(String day) {
        return plannedMealDao.getMealsByDay(day);
    }

    @Override
    public List<PlannedMeal> getAllPlannedMeals() {
        return plannedMealDao.getAllPlannedMeals();
    }
}

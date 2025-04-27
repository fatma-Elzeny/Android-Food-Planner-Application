package com.example.foodplanner.model;

import android.content.Context;

import com.example.foodplanner.db.AppDataBase;
import com.example.foodplanner.db.MealDAO;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MealsRepositoryImpl implements MealsRepository {
    private final MealDAO mealDao;
    private final Executor executor;

    public MealsRepositoryImpl(Context context) {
        AppDataBase db = AppDataBase.getInstance(context);
        mealDao = db.mealDao();
        executor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void insertFavorite(FavoriteMeal meal) {
        executor.execute(() -> mealDao.insertFavorite(meal));
    }

    @Override
    public void deleteFavorite(FavoriteMeal meal) {
        executor.execute(() -> mealDao.deleteFavorite(meal));
    }

    @Override
    public List<FavoriteMeal> getAllFavorites() {
        return mealDao.getAllFavorites();
    }

    @Override
    public FavoriteMeal getFavoriteById(String id) {
        return mealDao.getFavoriteById(id);
    }

    @Override
    public void insertPlannedMeal(PlannedMeal meal) {
        executor.execute(() -> mealDao.insertPlanned(meal));
    }

    @Override
    public void deletePlannedMeal(PlannedMeal meal) {
        executor.execute(() -> mealDao.deletePlanned(meal));
    }

    @Override
    public List<PlannedMeal> getMealsByDay(String day) {
        return mealDao.getMealsByDay(day);
    }

    @Override
    public List<PlannedMeal> getAllPlannedMeals() {
        return mealDao.getAllPlannedMeals();
    }
}


package com.example.foodplanner.model;

import android.content.Context;

import com.example.foodplanner.db.AppDataBase;
import com.example.foodplanner.db.MealDAO;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.foodplanner.db.MealsLocalDataSource;
import com.example.foodplanner.db.MealsLocalDataSourceImpl;
import com.example.foodplanner.model.FavoriteMeal;
import com.example.foodplanner.model.PlannedMeal;
import com.example.foodplanner.network.MealsRemoteDataSource;
import com.example.foodplanner.network.MealsRemoteDataSourceImpl;
import com.example.foodplanner.network.NetworkCallback;

import java.util.List;

public class MealsRepositoryImpl implements MealsRepository {
    private final MealDAO mealDao;
    private final Executor executor;

    private final MealsRemoteDataSource remoteDataSource;

    public MealsRepositoryImpl(Context context) {
        AppDataBase db = AppDataBase.getInstance(context);
        mealDao = db.mealDao();
        remoteDataSource = MealsRemoteDataSourceImpl.getInstance();
        executor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void getMealOfTheDay(NetworkCallback<Object> callback) {
        remoteDataSource.getMealOfTheDay(callback);
    }

    @Override
    public void searchMealsByName(String name, NetworkCallback<Object> callback) {
        remoteDataSource.searchMealsByName(name, callback);
    }

    @Override
    public void getMealsByCategory(String category, NetworkCallback<Object> callback) {
        remoteDataSource.getMealsByCategory(category, callback);
    }

    @Override
    public void getMealsByIngredient(String ingredient, NetworkCallback<Object> callback) {
        remoteDataSource.getMealsByIngredient(ingredient, callback);
    }

    @Override
    public void getMealsByCountry(String country, NetworkCallback<Object> callback) {
        remoteDataSource.getMealsByCountry(country, callback);
    }

    @Override
    public void getMealDetails(String mealId, NetworkCallback<Object> callback) {
        remoteDataSource.getMealDetails(mealId, callback);
    }

    @Override
    public void getAllCategories(NetworkCallback<Object> callback) {
        remoteDataSource.getAllCategories(callback);
    }

    @Override
    public void getAllCountries(NetworkCallback<Object> callback) {
        remoteDataSource.getAllCountries(callback);
    }


    // ===== Favorites =====

    @Override
    public void insertFavorite(FavoriteMeal meal) {
        executor.execute(() -> mealDao.insertFavorite(meal));
    }

    @Override
    public void deleteFavorite(FavoriteMeal meal) {
        executor.execute(() -> mealDao.deleteFavorite(meal));
    }

    @Override
    public LiveData<List<FavoriteMeal>> getAllFavorites() {
        return mealDao.getAllFavorites();
    }

    @Override
    public FavoriteMeal getFavoriteById(String id) {

        return mealDao.getFavoriteById(id);
    }

    // ===== Planned Meals =====

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
        // Optional: should also be exposed via LiveData for UI safety
        return mealDao.getMealsByDay(day);
    }

    @Override
    public List<PlannedMeal> getAllPlannedMeals() {
        return mealDao.getAllPlannedMeals();
    }
}

package com.example.foodplanner.model;

import android.content.Context;

import com.example.foodplanner.db.AppDataBase;
import com.example.foodplanner.db.MealDAO;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;

import com.example.foodplanner.network.MealsRemoteDataSource;
import com.example.foodplanner.network.MealsRemoteDataSourceImpl;
import com.example.foodplanner.network.NetworkCallback;

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
    public void getMealOfTheDay(NetworkCallback<MealResponse> callback) {
        remoteDataSource.getMealOfTheDay(callback);
    }

    @Override
    public void searchMealsByName(String name, NetworkCallback<MealResponse> callback) {
        remoteDataSource.searchMealsByName(name, callback);
    }

    @Override
    public void getMealsByCategory(String category, NetworkCallback<MealResponse> callback) {
        remoteDataSource.getMealsByCategory(category, callback);
    }

    @Override
    public void getMealsByIngredient(String ingredient, NetworkCallback<MealResponse> callback) {
        remoteDataSource.getMealsByIngredient(ingredient, callback);
    }

    @Override
    public void getMealsByCountry(String country, NetworkCallback<MealResponse> callback) {
        remoteDataSource.getMealsByCountry(country, callback);
    }

    @Override
    public void getMealDetails(String mealId, NetworkCallback<MealResponse> callback) {
        remoteDataSource.getMealDetails(mealId, callback);
    }

    @Override
    public void getAllCategories(NetworkCallback<CategoryResponse> callback) {
        remoteDataSource.getAllCategories(callback);
    }

    @Override
    public void getAllIngredients(NetworkCallback<IngredientResponse> callback) {
        remoteDataSource.getAllIngredients(callback);
    }

    @Override
    public void getAllCountries(NetworkCallback<CountryResponse> callback) {
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
    public LiveData<FavoriteMeal> getFavoriteById(String id) {
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
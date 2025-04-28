package com.example.foodplanner.model;

import android.content.Context;

import com.example.foodplanner.db.AppDataBase;
import com.example.foodplanner.db.MealDAO;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import android.content.Context;

import com.example.foodplanner.db.MealsLocalDataSource;
import com.example.foodplanner.db.MealsLocalDataSourceImpl;
import com.example.foodplanner.model.FavoriteMeal;
import com.example.foodplanner.model.PlannedMeal;
import com.example.foodplanner.network.MealsRemoteDataSource;
import com.example.foodplanner.network.MealsRemoteDataSourceImpl;
import com.example.foodplanner.network.NetworkCallback;

import java.util.List;

public class MealsRepositoryImpl implements MealsRepository {

    private final MealsRemoteDataSource remoteDataSource;
    private final MealsLocalDataSource localDataSource;

    public MealsRepositoryImpl(Context context) {
        remoteDataSource = MealsRemoteDataSourceImpl.getInstance();
        localDataSource = new MealsLocalDataSourceImpl(context);
    }

    // --------- Remote Methods ---------

    @Override
    public void getMealOfTheDay(NetworkCallback<Object> callback) {
        remoteDataSource.getMealOfTheDay(callback);
    }

    @Override
    public void getMealsByCategory(String category, NetworkCallback<Object> callback) {
        remoteDataSource.getMealsByCategory(category, callback);
    }

    @Override
    public void getMealDetails(String mealId, NetworkCallback<Object> callback) {
        remoteDataSource.getMealDetails(mealId, callback);
    }

    @Override
    public void searchMealsByName(String name, NetworkCallback<Object> callback) {
        remoteDataSource.searchMealsByName(name, callback);
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
    public void getAllCategories(NetworkCallback<Object> callback) {
        remoteDataSource.getAllCategories(callback);
    }

    @Override
    public void getAllCountries(NetworkCallback<Object> callback) {
        remoteDataSource.getAllCountries(callback);
    }

    // --------- Local Methods ---------

    @Override
    public void insertFavorite(FavoriteMeal meal) {
        localDataSource.insertFavorite(meal);
    }

    @Override
    public void deleteFavorite(FavoriteMeal meal) {
        localDataSource.deleteFavorite(meal);
    }

    @Override
    public List<FavoriteMeal> getAllFavorites() {
        return localDataSource.getAllFavorites();
    }

    @Override
    public FavoriteMeal getFavoriteById(String id) {
        return localDataSource.getFavoriteById(id);
    }

    @Override
    public void insertPlannedMeal(PlannedMeal meal) {
        localDataSource.insertPlannedMeal(meal);
    }

    @Override
    public void deletePlannedMeal(PlannedMeal meal) {
        localDataSource.deletePlannedMeal(meal);
    }

    @Override
    public List<PlannedMeal> getMealsByDay(String day) {
        return localDataSource.getMealsByDay(day);
    }

    @Override
    public List<PlannedMeal> getAllPlannedMeals() {
        return localDataSource.getAllPlannedMeals();
    }
}

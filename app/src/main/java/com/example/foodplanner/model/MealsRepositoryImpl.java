package com.example.foodplanner.model;

import java.util.List;

import androidx.lifecycle.LiveData;

import com.example.foodplanner.db.MealsLocalDataSource;
import com.example.foodplanner.network.MealsRemoteDataSource;
import com.example.foodplanner.network.NetworkCallback;

public class MealsRepositoryImpl implements MealsRepository {

    MealsRemoteDataSource mealRemoteDataSource;
    MealsLocalDataSource mealLocalDataSource;

    private static MealsRepositoryImpl mealRepo =null;

    public static MealsRepositoryImpl getInstance(MealsRemoteDataSource mealRemoteDataSource,MealsLocalDataSource mealLocalDataSource)
    {
        if(mealRepo ==null)
        {
            mealRepo =new MealsRepositoryImpl(mealRemoteDataSource,mealLocalDataSource);
        }
        return mealRepo;
    }

    public MealsRepositoryImpl(MealsRemoteDataSource mealRemoteDataSource,MealsLocalDataSource mealLocalDataSource)
    {
        this.mealRemoteDataSource = mealRemoteDataSource;
        this.mealLocalDataSource=mealLocalDataSource;
    }

    @Override
    public void getMealOfTheDay(NetworkCallback<MealResponse> callback) {
        mealRemoteDataSource.getMealOfTheDay(callback);
    }

    @Override
    public void searchMealsByName(String name, NetworkCallback<MealResponse> callback) {
        mealRemoteDataSource.searchMealsByName(name, callback);
    }

    @Override
    public void getMealsByCategory(String category, NetworkCallback<MealResponse> callback) {
        mealRemoteDataSource.getMealsByCategory(category, callback);
    }

    @Override
    public void getMealsByIngredient(String ingredient, NetworkCallback<MealResponse> callback) {
        mealRemoteDataSource.getMealsByIngredient(ingredient, callback);
    }

    @Override
    public void getMealsByCountry(String country, NetworkCallback<MealResponse> callback) {
        mealRemoteDataSource.getMealsByCountry(country, callback);
    }

    @Override
    public void getMealDetails(String mealId, NetworkCallback<MealResponse> callback) {
        mealRemoteDataSource.getMealDetails(mealId, callback);
    }

    @Override
    public void getAllCategories(NetworkCallback<CategoryResponse> callback) {
        mealRemoteDataSource.getAllCategories(callback);
    }

    @Override
    public void getAllIngredients(NetworkCallback<IngredientResponse> callback) {
        mealRemoteDataSource.getAllIngredients(callback);
    }

    @Override
    public void getAllCountries(NetworkCallback<CountryResponse> callback) {
        mealRemoteDataSource.getAllCountries(callback);
    }



    // ===== Favorites =====

    @Override
    public void insertFavorite(FavoriteMeal meal) {
       mealLocalDataSource.insertMeal(meal);
    }

    @Override
    public void deleteFavorite(FavoriteMeal meal) {
       mealLocalDataSource.deleteMeal(meal);
    }

    @Override
    public LiveData<List<FavoriteMeal>> getAllFavorites() {
      return   mealLocalDataSource.getAllFavorites();
    }



    // ===== Planned Meals =====

    @Override
    public void insertPlannedMeal(PlannedMeal meal) {
       mealLocalDataSource.insertFoodPlan(meal);
    }

    @Override
    public void deletePlannedMeal(PlannedMeal meal) {
        mealLocalDataSource.deleteFoodPlan(meal);
    }

    @Override
    public LiveData<List<PlannedMeal>> getMealsByDay(String day) {
        // Optional: should also be exposed via LiveData for UI safety
      return  mealLocalDataSource.getPlannedFood(day);
    }
    @Override
    public void updatePlannedMeal(PlannedMeal plannedMeal) {
        mealLocalDataSource.updateFoodPlan(plannedMeal);

    }




}
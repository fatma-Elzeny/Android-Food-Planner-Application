package com.example.foodplanner.model;

import androidx.lifecycle.LiveData;

import com.example.foodplanner.network.NetworkCallback;

import java.util.List;

public interface MealsRepository {
    void getMealOfTheDay(NetworkCallback<Object> callback);

    void searchMealsByName(String name, NetworkCallback<Object> callback);

    void getMealsByCategory(String category, NetworkCallback<Object> callback);

    void getMealsByIngredient(String ingredient, NetworkCallback<Object> callback);

    void getMealsByCountry(String country, NetworkCallback<Object> callback);

    void getMealDetails(String mealId, NetworkCallback<Object> callback);

    void getAllCategories(NetworkCallback<Object> callback);

    void getAllCountries(NetworkCallback<Object> callback);

    void insertFavorite(FavoriteMeal meal);

    void deleteFavorite(FavoriteMeal meal);

    LiveData<List<FavoriteMeal>> getAllFavorites();

    FavoriteMeal getFavoriteById(String id);

    void insertPlannedMeal(PlannedMeal meal);

    void deletePlannedMeal(PlannedMeal meal);

    List<PlannedMeal> getMealsByDay(String day);

    List<PlannedMeal> getAllPlannedMeals();
}

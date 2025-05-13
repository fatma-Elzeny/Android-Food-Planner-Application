package com.example.foodplanner.model;

import androidx.lifecycle.LiveData;

import com.example.foodplanner.db.LocalCallback;
import com.example.foodplanner.network.NetworkCallback;

import java.util.List;

public interface MealsRepository {
    void getMealOfTheDay(NetworkCallback<MealResponse> callback);

    void searchMealsByName(String name, NetworkCallback<MealResponse> callback);

    void getMealsByCategory(String category, NetworkCallback<MealResponse> callback);

    void getMealsByIngredient(String ingredient, NetworkCallback<MealResponse> callback);

    void getMealsByCountry(String country, NetworkCallback<MealResponse> callback);

    void getMealDetails(String mealId, NetworkCallback<MealResponse> callback);


    void getAllCategories(NetworkCallback<CategoryResponse> callback);

    void getAllIngredients(NetworkCallback<IngredientResponse> callback);

    void getAllCountries(NetworkCallback<CountryResponse> callback);

    void insertFavorite(FavoriteMeal meal);

    void deleteFavorite(FavoriteMeal meal);

    LiveData<List<FavoriteMeal>> getAllFavorites();



    void insertPlannedMeal(PlannedMeal meal);

    void deletePlannedMeal(PlannedMeal meal);

    LiveData<List<PlannedMeal>> getMealsByDay(String day, String userId);



    void updatePlannedMeal(PlannedMeal plannedMeal);

    LiveData<FavoriteMeal> getFavoriteMealById(String mealId);
    LiveData<PlannedMeal> getPlannedMealById(String mealId);


}

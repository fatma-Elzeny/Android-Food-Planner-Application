package com.example.foodplanner.network;

import com.example.foodplanner.model.CategoryResponse;
import com.example.foodplanner.model.CountryResponse;
import com.example.foodplanner.model.IngredientResponse;
import com.example.foodplanner.model.MealResponse;

public interface MealsRemoteDataSource {

    void getMealOfTheDay(NetworkCallback<MealResponse> callback);

    void searchMealsByName(String name, NetworkCallback<MealResponse> callback);

    void getMealsByCategory(String category, NetworkCallback<MealResponse> callback);

    void getMealsByIngredient(String ingredient, NetworkCallback<MealResponse> callback);

    void getMealsByCountry(String country, NetworkCallback<MealResponse> callback);

    void getMealDetails(String mealId, NetworkCallback<MealResponse> callback);

    void getAllCategories(NetworkCallback<CategoryResponse> callback);

    void getAllIngredients(NetworkCallback<IngredientResponse> callback);

    void getAllCountries(NetworkCallback<CountryResponse> callback);


}

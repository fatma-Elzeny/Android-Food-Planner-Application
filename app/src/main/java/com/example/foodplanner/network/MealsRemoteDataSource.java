package com.example.foodplanner.network;

public interface MealsRemoteDataSource {

    void getMealOfTheDay(NetworkCallback<Object> callback);

    void searchMealsByName(String name, NetworkCallback<Object> callback);

    void getMealsByCategory(String category, NetworkCallback<Object> callback);

    void getMealsByIngredient(String ingredient, NetworkCallback<Object> callback);

    void getMealsByCountry(String country, NetworkCallback<Object> callback);

    void getMealDetails(String mealId, NetworkCallback<Object> callback);

    void getAllCategories(NetworkCallback<Object> callback);

    void getAllCountries(NetworkCallback<Object> callback);

}

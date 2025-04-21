package com.example.foodplanner.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MealsRemoteDataSourceImpl implements MealsRemoteDataSource{

    public static final String TAG ="MealClient";
    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";

    private MealAPIService mealAPIService;

    private static MealsRemoteDataSourceImpl client = null;

    private MealsRemoteDataSourceImpl (){

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL).build();
        mealAPIService = retrofit.create(MealAPIService.class);

    }
    @Override
    public void getMealOfTheDay(NetworkCallback<Object> callback) {

    }

    @Override
    public void searchMealsByName(String name, NetworkCallback<Object> callback) {

    }

    @Override
    public void getMealsByCategory(String category, NetworkCallback<Object> callback) {

    }

    @Override
    public void getMealsByIngredient(String ingredient, NetworkCallback<Object> callback) {

    }

    @Override
    public void getMealsByCountry(String country, NetworkCallback<Object> callback) {

    }

    @Override
    public void getMealDetails(String mealId, NetworkCallback<Object> callback) {

    }

    @Override
    public void getAllCategories(NetworkCallback<Object> callback) {

    }

    @Override
    public void getAllCountries(NetworkCallback<Object> callback) {

    }
}

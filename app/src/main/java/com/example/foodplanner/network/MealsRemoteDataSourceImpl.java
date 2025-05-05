package com.example.foodplanner.network;

import com.example.foodplanner.model.CategoryResponse;
import com.example.foodplanner.model.CountryResponse;
import com.example.foodplanner.model.IngredientResponse;
import com.example.foodplanner.model.MealResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MealsRemoteDataSourceImpl implements MealsRemoteDataSource {

    public static final String TAG = "MealClient";
    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";

    private MealAPIService mealAPIService;

    private static MealsRemoteDataSourceImpl client = null;

    private MealsRemoteDataSourceImpl() {

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL).build();
        mealAPIService = retrofit.create(MealAPIService.class);

    }
    public static MealsRemoteDataSourceImpl getInstance() {
        if (client == null) {
            client = new MealsRemoteDataSourceImpl();
        }
        return client;
    }

    @Override
    public void getMealOfTheDay(NetworkCallback<MealResponse> callback) {
        mealAPIService.getMealOfTheDay().enqueue(wrapCallback(callback));
    }

    @Override
    public void searchMealsByName(String name, NetworkCallback<MealResponse> callback) {
        mealAPIService.searchMealsByName(name).enqueue(wrapCallback(callback));
    }

    @Override
    public void getMealsByCategory(String category, NetworkCallback<MealResponse> callback) {
        mealAPIService.getMealsByCategory(category).enqueue(wrapCallback(callback));
    }

    @Override
    public void getMealsByIngredient(String ingredient, NetworkCallback<MealResponse> callback) {
        mealAPIService.getMealsByIngredient(ingredient).enqueue(wrapCallback(callback));
    }

    @Override
    public void getMealsByCountry(String country, NetworkCallback<MealResponse> callback) {
        mealAPIService.getMealsByCountry(country).enqueue(wrapCallback(callback));
    }

    @Override
    public void getMealDetails(String mealId, NetworkCallback<MealResponse> callback) {
        mealAPIService.getMealDetails(mealId).enqueue(wrapCallback(callback));
    }

    @Override
    public void getAllCategories(NetworkCallback<CategoryResponse> callback) {
        mealAPIService.getAllCategories().enqueue(wrapCallback(callback));
    }

    @Override
    public void getAllIngredients(NetworkCallback<IngredientResponse> callback) {
        mealAPIService.getAllIngredients().enqueue(wrapCallback(callback));
    }

    @Override
    public void getAllCountries(NetworkCallback<CountryResponse> callback) {
        mealAPIService.getAllCountries().enqueue(wrapCallback(callback));
    }

    private <T> Callback<T> wrapCallback(NetworkCallback<T> callback) {
        return new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("API Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                callback.onFailure("Network Failure: " + t.getMessage());
            }
        };
    }


}

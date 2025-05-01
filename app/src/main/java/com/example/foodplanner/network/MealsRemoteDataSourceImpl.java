package com.example.foodplanner.network;

import androidx.annotation.NonNull;

import com.example.foodplanner.model.MealResponse;

import java.lang.reflect.InvocationTargetException;

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
    public void getMealOfTheDay(NetworkCallback<Object> callback) {
        mealAPIService.getMealOfTheDay().enqueue(wrapCallback(callback));
    }

    @Override
    public void searchMealsByName(String name, NetworkCallback<Object> callback) {
        mealAPIService.searchMealsByName(name).enqueue(wrapCallback(callback));
    }

    @Override
    public void getMealsByCategory(String category, NetworkCallback<Object> callback) {
        mealAPIService.getMealsByCategory(category).enqueue(wrapCallback(callback));
    }

    @Override
    public void getMealsByIngredient(String ingredient, NetworkCallback<Object> callback) {
        mealAPIService.getMealsByIngredient(ingredient).enqueue(wrapCallback(callback));
    }

    @Override
    public void getMealsByCountry(String country, NetworkCallback<Object> callback) {
        mealAPIService.getMealsByCountry(country).enqueue(wrapCallback(callback));
    }

    @Override
    public void getMealDetails(String mealId, NetworkCallback<Object> callback) {
        mealAPIService.getMealDetails(mealId).enqueue(wrapCallback(callback));
    }

    @Override
    public void getAllCategories(NetworkCallback<Object> callback) {
        mealAPIService.getAllCategories().enqueue(wrapCallback(callback));
    }

    @Override
    public void getAllCountries(NetworkCallback<Object> callback) {
        mealAPIService.getAllCountries().enqueue(wrapCallback(callback));
    }

    private Callback<MealResponse> wrapCallback(NetworkCallback<Object> callback) {
        return new Callback<MealResponse>() {
            @Override
            public void onResponse(@NonNull Call<MealResponse> call, @NonNull Response<MealResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    callback.onSuccess(response.body());

                } else {
                    callback.onFailure("No data found.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<MealResponse> call, @NonNull Throwable t) {
                callback.onFailure(t.getMessage() != null ? t.getMessage() : "Network error");
            }
        };
    }
}


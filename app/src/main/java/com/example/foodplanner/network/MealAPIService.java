package com.example.foodplanner.network;
import com.example.foodplanner.model.MealResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
public interface MealAPIService {
    @GET("random.php")
    Call<MealResponse> getMealOfTheDay();

    @GET("search.php")
    Call<MealResponse> searchMealsByName(@Query("s") String name);

    @GET("filter.php")
    Call<MealResponse> getMealsByCategory(@Query("c") String category);

    @GET("filter.php")
    Call<MealResponse> getMealsByIngredient(@Query("i") String ingredient);

    @GET("filter.php")
    Call<MealResponse> getMealsByCountry(@Query("a") String country);

    @GET("lookup.php")
    Call<MealResponse> getMealDetails(@Query("i") String mealId);

    @GET("list.php?c=list")
    Call<MealResponse> getAllCategories();

    @GET("list.php?a=list")
    Call<MealResponse> getAllCountries();

}

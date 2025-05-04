package com.example.foodplanner.home.presenter;

import com.example.foodplanner.home.view.HomeView;
import com.example.foodplanner.model.CategoryResponse;
import com.example.foodplanner.model.MealResponse;
import com.example.foodplanner.model.MealsRepository;
import com.example.foodplanner.network.MealsRemoteDataSource;
import com.example.foodplanner.network.MealsRemoteDataSourceImpl;
import com.example.foodplanner.network.NetworkCallback;

public class HomePresenterImpl implements HomePresenter {
    private HomeView view;
    private MealsRepository repository;

    public HomePresenterImpl(HomeView view, MealsRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void getMealOfTheDay() {
        view.showLoading();
        repository.getMealOfTheDay(new NetworkCallback<MealResponse>() {
            @Override
            public void onSuccess(MealResponse data) {
                view.hideLoading();
                MealResponse mealResponse =  data;
                if (!mealResponse.getMeals().isEmpty()) {
                    view.showSuggestedMeal(mealResponse.getMeals().get(0));
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                view.hideLoading();
                view.showError(errorMessage);
            }
        });
    }

    @Override
    public void getMealsByCategory(String category) {
        repository.getMealsByCategory(category, new NetworkCallback<MealResponse>() {
            @Override
            public void onSuccess(MealResponse data) {
                MealResponse mealResponse = data;
                view.showLazyMeals(mealResponse.getMeals());
            }

            @Override
            public void onFailure(String errorMessage) {
                view.showError(errorMessage);
            }
        });
    }

    @Override
    public void onDestroy() {
        view = null;
    }

    @Override
    public void loadMealsByCategory(String category) {
        view.showLoading();
        repository.getMealsByCategory(category, new NetworkCallback<MealResponse>() {
            @Override
            public void onSuccess(MealResponse data) {
                view.hideLoading();
                MealResponse response = data;
                if (response.getMeals() != null) {
                    view.showMealsByCategory(category, response.getMeals());
                } else {
                    view.showError("No meals found for " + category);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                view.hideLoading();
                view.showError(errorMessage);
            }
        });
    }

}
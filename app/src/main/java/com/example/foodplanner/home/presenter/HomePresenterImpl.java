package com.example.foodplanner.home.presenter;

import com.example.foodplanner.home.view.HomeView;
import com.example.foodplanner.model.CategoryResponse;
import com.example.foodplanner.model.CountryResponse;
import com.example.foodplanner.model.MealResponse;
import com.example.foodplanner.model.MealsRepository;
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
                if (data != null && data.getMeals() != null && !data.getMeals().isEmpty()) {  //  Null check added
                    view.showSuggestedMeal(data.getMeals().get(0));
                } else {
                    view.showError("No meal of the day available."); // ✅ Added fallback error
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
        view.showCategoryLoading();
        repository.getMealsByCategory(category, new NetworkCallback<MealResponse>() {
            @Override
            public void onSuccess(MealResponse data) {
                view.hideCategoryLoading();
                if (data != null && data.getMeals() != null && !data.getMeals().isEmpty()) {
                    view.showMealsByCategory(category, data.getMeals());
                } else {
                    view.showError("No meals found for category: " + category);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                view.hideCategoryLoading();
                view.showError(errorMessage);
            }
        });

    }
    @Override
    public void getAllCategories() {
        view.showCategoryLoading();
        repository.getAllCategories(new NetworkCallback<CategoryResponse>() {
            @Override
            public void onSuccess(CategoryResponse data) {
                view.hideCategoryLoading();
                view.showCategories(data.getCategoryResponse());
            }

            @Override
            public void onFailure(String errorMessage) {
                view.hideCategoryLoading();
                view.showError(errorMessage);
            }
        });
    }
    @Override
    public void getMealsByArea(String area) {
        view.showAreaLoading();
        repository.getMealsByCountry(area, new NetworkCallback<MealResponse>() {
            @Override
            public void onSuccess(MealResponse data) {
                view.hideAreaLoading();
                if (data != null && data.getMeals() != null && !data.getMeals().isEmpty()) {
                    view.showMealsByArea(area, data.getMeals());
                } else {
                    view.showError("No meals found for area: " + area);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                view.hideAreaLoading();
                view.showError(errorMessage);
            }
        });
    }
    @Override
    public void getAllAreas() {
        view.showAreaLoading();
        repository.getAllCountries(new NetworkCallback<CountryResponse>() {
            @Override
            public void onSuccess(CountryResponse data) {
                view.hideAreaLoading();
                view.showAreas(data.getAreas());
            }

            @Override
            public void onFailure(String errorMessage) {
                view.hideAreaLoading();
                view.showError(errorMessage);
            }
        });
    }

}
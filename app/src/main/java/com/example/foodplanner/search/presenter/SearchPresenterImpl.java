package com.example.foodplanner.search.presenter;

import com.example.foodplanner.model.Category;
import com.example.foodplanner.model.CategoryResponse;
import com.example.foodplanner.model.CountryResponse;
import com.example.foodplanner.model.Ingredient;
import com.example.foodplanner.model.IngredientResponse;
import com.example.foodplanner.model.MealResponse;
import com.example.foodplanner.model.MealsRepository;
import com.example.foodplanner.network.NetworkCallback;
import com.example.foodplanner.search.view.SearchView;

import java.util.List;

public class SearchPresenterImpl implements searchPresenter {
    private SearchView view;
    private MealsRepository repository;

    public SearchPresenterImpl(SearchView view, MealsRepository repository) {
        this.view = view;
        this.repository = repository;
    }
    @Override
    public void getMealsByCategory(String category) {
        view.showLoading();
        repository.getMealsByCategory(category, new NetworkCallback<MealResponse>() {
            @Override
            public void onSuccess(MealResponse data) {
                view.hideLoading();
                if (data != null && data.getMeals() != null) {
                    view.showMeals(data.getMeals());
                } else {
                    view.showEmptyState("No meals found in this category.");
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
    public void getMealsByIngredient(String ingredient) {
        view.showLoading();
        repository.getMealsByIngredient(ingredient, new NetworkCallback<MealResponse>() {
            @Override
            public void onSuccess(MealResponse data) {
                view.hideLoading();
                if (data != null && data.getMeals() != null) {
                    view.showMeals(data.getMeals());
                } else {
                    view.showEmptyState("No meals found with this ingredient.");
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
    public void getMealsByCountry(String country) {
        view.showLoading();
        repository.getMealsByCountry(country, new NetworkCallback<MealResponse>() {
            @Override
            public void onSuccess(MealResponse data) {
                view.hideLoading();
                if (data != null && data.getMeals() != null) {
                    view.showMeals(data.getMeals());
                } else {
                    view.showEmptyState("No meals found from this country.");
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
    public void getMealsByName(String name) {
        view.showLoading();
        repository.searchMealsByName(name, new NetworkCallback<MealResponse>() {
            @Override
            public void onSuccess(MealResponse data) {
                view.hideLoading();
                if (data != null && data.getMeals() != null) {
                    view.showMeals(data.getMeals());
                } else {
                    view.showEmptyState("No meals found with this name.");
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
    public void getMealsById(String id) {
        view.showLoading();
        repository.getMealDetails(id, new NetworkCallback<MealResponse>() {
            @Override
            public void onSuccess(MealResponse data) {
                view.hideLoading();
                if (data != null && data.getMeals() != null && !data.getMeals().isEmpty()) {
                    view.showMeals(data.getMeals()); // returns a list with one meal
                } else {
                    view.showEmptyState("No meal found with this ID.");
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
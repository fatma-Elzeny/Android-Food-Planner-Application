package com.example.foodplanner.search.presenter;

import com.example.foodplanner.CountryFlagHelper;
import com.example.foodplanner.model.Category;
import com.example.foodplanner.model.CategoryResponse;
import com.example.foodplanner.model.Country;
import com.example.foodplanner.model.CountryResponse;
import com.example.foodplanner.model.Ingredient;
import com.example.foodplanner.model.IngredientResponse;
import com.example.foodplanner.model.Meal;
import com.example.foodplanner.model.MealResponse;
import com.example.foodplanner.model.MealsRepository;
import com.example.foodplanner.network.NetworkCallback;
import com.example.foodplanner.search.view.SearchScreen;

import java.util.ArrayList;
import java.util.List;

public class SearchPresenterImpl implements searchPresenter {
    private final SearchScreen view;
    private final MealsRepository repository;

    public SearchPresenterImpl(SearchScreen view, MealsRepository repository) {
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
                    view.showMeals(data.getMeals());
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

    // SearchPresenterImpl.java
    @Override
    public void getAllCategories() {
        view.showLoading();
        repository.getAllCategories(new NetworkCallback<CategoryResponse>() {
            @Override
            public void onSuccess(CategoryResponse data) {
                view.hideLoading();
                if (data != null && data.getCategoryResponse() != null) {
                    // Convert categories to meals for display
                    List<Meal> categoryMeals = new ArrayList<>();
                    for (Category category : data.getCategoryResponse()) {
                        Meal meal = new Meal();
                        meal.setStrMeal(category.getStrCategory());
                        meal.setStrMealThumb(category.getStrCategoryThumb());
                        // You might want to set other fields or create a different model
                        meal.setType("category");
                        categoryMeals.add(meal);
                    }
                    view.showMeals(categoryMeals);
                } else {
                    view.showEmptyState("No categories found.");
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                view.hideLoading();
                view.showError(errorMessage);
            }
        });
    }


    public void getAllCountries() {
        view.showLoading();
        repository.getAllCountries(new NetworkCallback<CountryResponse>() {
            @Override
            public void onSuccess(CountryResponse data) {
                view.hideLoading();
                if (data != null && data.getAreas() != null) {
                    List<Meal> countryMeals = new ArrayList<>();
                    for (Country country : data.getAreas()) {
                        Meal meal = new Meal();
                        meal.setStrMeal(country.getStrArea());
                        meal.setStrMealThumb(CountryFlagHelper.getFlagUrl(country.getStrArea()));
                        // Set a special type if you want to handle countries differently
                        meal.setType("country");
                        countryMeals.add(meal);
                    }
                    view.showMeals(countryMeals);
                } else {
                    view.showEmptyState("No countries found.");
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
    public void getAllIngredients() {
        view.showLoading();
        repository.getAllIngredients(new NetworkCallback<IngredientResponse>() {
            @Override
            public void onSuccess(IngredientResponse data) {
                view.hideLoading();
                if (data != null && data.getMeals() != null) {
                    List<Meal> ingredientMeals = new ArrayList<>();
                    for (Ingredient ingredient : data.getMeals()) {
                        Meal meal = new Meal();
                        meal.setStrMeal(ingredient.getStrIngredient());
                        meal.setStrMealThumb(getIngredientImageUrl(ingredient.getStrIngredient()));
                        meal.setType("ingredient");
                        ingredientMeals.add(meal);
                    }
                    view.showMeals(ingredientMeals);
                } else {
                    view.showEmptyState("No ingredients found.");
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                view.hideLoading();
                view.showError(errorMessage);
            }
        });
    }

    private String getIngredientImageUrl(String ingredientName) {
        // The MealDB uses this format for ingredient images
        return "https://www.themealdb.com/images/ingredients/" +
                ingredientName + "-Small.png";
    }
}


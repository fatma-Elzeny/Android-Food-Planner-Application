package com.example.foodplanner.MealDetail.presenter;

import androidx.lifecycle.LifecycleOwner;

import com.example.foodplanner.MealDetail.view.MealDetailsView;
import com.example.foodplanner.NetworkUtil;
import com.example.foodplanner.db.LocalCallback;
import com.example.foodplanner.model.Meal;
import com.example.foodplanner.model.MealResponse;
import com.example.foodplanner.model.MealsRepository;
import com.example.foodplanner.network.NetworkCallback;

import java.lang.reflect.InvocationTargetException;

public class MealDetailPresenterImpl implements MealDetailPresenter {

    private MealDetailsView view;
    private MealsRepository repository;

    public MealDetailPresenterImpl(MealDetailsView view, MealsRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void getMealDetails(String mealId) {
        view.showLoading();
        repository.getMealDetails(mealId, new NetworkCallback<MealResponse>() {
            @Override
            public void onSuccess(MealResponse data) {
                view.hideLoading();
                MealResponse response = data;
                if (response.getMeals() != null && !response.getMeals().isEmpty()) {
                    view.showMealDetails(response.getMeals().get(0));
                } else {
                    view.showError("Meal not found");
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
    public void onDestroy() {
        view = null;
    }
}
package com.example.foodplanner.planner.presenter;

import com.example.foodplanner.model.MealsRepository;
import com.example.foodplanner.model.PlannedMeal;
import com.example.foodplanner.network.NetworkCallback;
import com.example.foodplanner.planner.view.PlannerView;

import java.util.List;

public class PlannerPresenterImpl implements PlannerPresenter {
    private PlannerView view;
    private MealsRepository repository;

    public PlannerPresenterImpl(PlannerView view, MealsRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void loadMealsForDay(String day) {
        repository.getMealsByDay(day, new NetworkCallback<List<PlannedMeal>>() {
            @Override
            public void onSuccess(List<PlannedMeal> meals) {
                if (meals.isEmpty()) {
                    view.showEmpty();
                } else {
                    view.showMeals(meals);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                view.showError(errorMessage);
            }
        });
    }

    @Override
    public void deletePlannedMeal(PlannedMeal meal) {
        repository.deletePlannedMeal(meal, new NetworkCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                loadMealsForDay(meal.getDay());
            }

            @Override
            public void onFailure(String errorMessage) {
                view.showError("Failed to delete: " + errorMessage);
            }
        });
    }
}

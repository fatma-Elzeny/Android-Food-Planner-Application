package com.example.foodplanner.planner.presenter;

import android.os.Handler;
import android.os.Looper;

import com.example.foodplanner.model.MealsRepository;
import com.example.foodplanner.model.PlannedMeal;
import com.example.foodplanner.network.NetworkCallback;
import com.example.foodplanner.planner.view.PlannerView;

import java.util.List;
import java.util.concurrent.Executors;

public class PlannerPresenterImpl implements PlannerPresenter {
    private final PlannerView view;
    private final MealsRepository repository;

    public PlannerPresenterImpl(PlannerView view, MealsRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void loadMealsForDay(String day) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<PlannedMeal> meals = repository.getMealsByDay(day);
            new Handler(Looper.getMainLooper()).post(() -> {
                if (meals == null || meals.isEmpty()) {
                    view.showEmpty();
                } else {
                    view.showMeals(meals);
                }
            });
        });
    }

    @Override
    public void deletePlannedMeal(PlannedMeal meal) {
        repository.deletePlannedMeal(meal);
        loadMealsForDay(meal.getDay());
    }
}
package com.example.foodplanner.planner.presenter;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.example.foodplanner.CalendarHelper;
import com.example.foodplanner.model.MealsRepository;
import com.example.foodplanner.model.PlannedMeal;
import com.example.foodplanner.planner.view.PlannerView;

import java.util.List;

public class PlannerPresenterImpl implements PlannerPresenter {
    private final PlannerView view;
    private final MealsRepository repository;
    private final String userId;

    public PlannerPresenterImpl(PlannerView view, MealsRepository repository, String userId) {
        this.view = view;
        this.repository = repository;
        this.userId = userId;
    }

    @Override
    public LiveData<List<PlannedMeal>> loadMealsForDay(String day) {
        LiveData<List<PlannedMeal>> meals = repository.getMealsByDay(day,userId);
        meals.observeForever(newMeals -> {
            new Handler(Looper.getMainLooper()).post(() -> {
                if (newMeals == null || newMeals.isEmpty()) {
                    view.showEmpty();
                } else {
                    view.showMeals(meals);
                }
            });
        });
        return meals;
    }

    @Override
    public void deletePlannedMeal(PlannedMeal meal) {
        repository.deletePlannedMeal(meal);
        new Handler(Looper.getMainLooper()).post(() -> {
            CalendarHelper helper = new CalendarHelper(((PlannerView) view).getContext(), "FoodPlanner");
            helper.deleteEvent(meal.getEventId());
        });
        loadMealsForDay(meal.getDay());
    }

}
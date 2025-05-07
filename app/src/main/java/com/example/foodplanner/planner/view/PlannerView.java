package com.example.foodplanner.planner.view;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.foodplanner.model.PlannedMeal;

import java.util.List;

public interface PlannerView {

    Context getContext();
    void showMeals(LiveData<List<PlannedMeal>> meals);
    void showEmpty();

    void showError(String message);


}

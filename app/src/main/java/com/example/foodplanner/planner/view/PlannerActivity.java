package com.example.foodplanner.planner.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.foodplanner.MealDetail.view.MealDetailsActivity;
import com.example.foodplanner.R;
import com.example.foodplanner.model.MealsRepositoryImpl;
import com.example.foodplanner.model.PlannedMeal;
import com.example.foodplanner.planner.presenter.PlannerPresenter;
import com.example.foodplanner.planner.presenter.PlannerPresenterImpl;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PlannerActivity extends AppCompatActivity implements PlannerView ,OnPlannerClickListener{

    private PlannerPresenter presenter;
    private TextView tvSelectedDay;
    private RecyclerView rvMeals;
    private PlannerAdapter adapter;
    private CalendarView calendarView;
    private LottieAnimationView noMealsAnimation;
    private TextView noMealsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);

        tvSelectedDay = findViewById(R.id.tv_selected_day);
        rvMeals = findViewById(R.id.rv_planned_meals);
        calendarView = findViewById(R.id.calendar_view);
        noMealsAnimation = findViewById(R.id.no_meals_animation);
        noMealsText = findViewById(R.id.no_meals_text);

        adapter = new PlannerAdapter(new ArrayList<>(), this);
        rvMeals.setLayoutManager(new LinearLayoutManager(this));
        rvMeals.setAdapter(adapter);


        rvMeals.setLayoutManager(new LinearLayoutManager(this));

        presenter = new PlannerPresenterImpl(this, new MealsRepositoryImpl(this));
        Calendar calendar = Calendar.getInstance();
        calendarView.setMinDate(calendar.getTimeInMillis());
        calendar.add(Calendar.DAY_OF_YEAR, 6);
        calendarView.setMaxDate(calendar.getTimeInMillis());
        calendarView.setDate(Calendar.getInstance().getTimeInMillis(), true, true);

        // Load today's meals by default
        String today = new SimpleDateFormat("EEEE", Locale.getDefault()).format(Calendar.getInstance().getTime());
        tvSelectedDay.setText("Meals for " + today);
        presenter.loadMealsForDay(today);

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar selected = Calendar.getInstance();
            selected.set(year, month, dayOfMonth);
            String selectedDay = new SimpleDateFormat("EEEE", Locale.getDefault()).format(selected.getTime());
            tvSelectedDay.setText("Meals for " + selectedDay);
            presenter.loadMealsForDay(selectedDay);
        });

    }

    @Override
    public void showMeals(List<PlannedMeal> meals) {
        if (meals.isEmpty()) {
            showEmpty();
        } else {
            rvMeals.setVisibility(View.VISIBLE);
            noMealsAnimation.setVisibility(View.GONE);
            noMealsText.setVisibility(View.GONE);
            adapter.updateMeals(meals);
        }
    }

    @Override
    public void showEmpty() {
        rvMeals.setVisibility(View.GONE);
        noMealsAnimation.setVisibility(View.VISIBLE);
        noMealsText.setVisibility(View.VISIBLE);
    }
    @Override
    public void onMealClick(PlannedMeal meal) {
        Intent intent = new Intent(this, MealDetailsActivity.class);
        intent.putExtra("MEAL_ID", meal.getMealId());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(PlannedMeal meal) {
        presenter.deletePlannedMeal(meal);
    }

    @Override
    public void showError(String message) {
        runOnUiThread(() -> {
            // Hide loading indicators if visible
            progressBar.setVisibility(View.GONE);

            // Show error message to user
            Snackbar snackbar = Snackbar.make(
                    findViewById(android.R.id.content),
                    message,
                    Snackbar.LENGTH_LONG
            );

            // Optional: Add action button to retry
            snackbar.setAction("Retry", v -> {
                String currentDay = tvSelectedDay.getText().toString()
                        .replace("Meals for ", "");
                presenter.loadMealsForDay(currentDay);
            });

            snackbar.show();
        });
    }
}
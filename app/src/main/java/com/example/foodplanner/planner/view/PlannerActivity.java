package com.example.foodplanner.planner.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ProgressBar;
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

public class PlannerActivity extends AppCompatActivity implements PlannerView, OnPlannerClickListener {

    private PlannerPresenter presenter;
    private TextView tvSelectedDay;
    private RecyclerView rvMeals;
    private PlannerAdapter adapter;
    private CalendarView calendarView;
    private LottieAnimationView noMealsAnimation;
    private TextView noMealsText;

    private final SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());

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

        presenter = new PlannerPresenterImpl(this, new MealsRepositoryImpl(this));

        setupCurrentWeekCalendar();

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar selected = Calendar.getInstance();
            selected.set(year, month, dayOfMonth);

            if (!isWithinCurrentWeek(selected)) {
                Toast.makeText(this, "Only current week is allowed", Toast.LENGTH_SHORT).show();
                calendarView.setDate(Calendar.getInstance().getTimeInMillis()); // reset to today
                return;
            }

            String selectedDay = dayFormat.format(selected.getTime());
            tvSelectedDay.setText("Meals on " + selectedDay);
            presenter.loadMealsForDay(selectedDay);
        });
    }

    private void setupCurrentWeekCalendar() {
        Calendar calendar = Calendar.getInstance();
        long today = calendar.getTimeInMillis();
        calendarView.setMinDate(today);

        calendar.add(Calendar.DAY_OF_YEAR, 6); // up to 6 days from today
        calendarView.setMaxDate(calendar.getTimeInMillis());

        calendarView.setDate(today, true, true);
    }

    private boolean isWithinCurrentWeek(Calendar date) {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.WEEK_OF_YEAR) == date.get(Calendar.WEEK_OF_YEAR) &&
                now.get(Calendar.YEAR) == date.get(Calendar.YEAR);
    }

    @Override
    public void showMeals(List<PlannedMeal> meals) {
        rvMeals.setVisibility(View.VISIBLE);
        noMealsAnimation.setVisibility(View.GONE);
        noMealsText.setVisibility(View.GONE);
        adapter.updateMeals(meals);
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
}


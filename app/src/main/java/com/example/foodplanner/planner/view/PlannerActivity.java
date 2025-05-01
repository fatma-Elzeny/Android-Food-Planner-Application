package com.example.foodplanner.planner.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
    private Calendar selectedCalendar = Calendar.getInstance();
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

        // Limit calendar to current week
        Calendar todayCalendar = Calendar.getInstance();
        long today = todayCalendar.getTimeInMillis();
        calendarView.setMinDate(today);
        todayCalendar.add(Calendar.DAY_OF_YEAR, 6);
        calendarView.setMaxDate(todayCalendar.getTimeInMillis());

        // Set default date and selected calendar
        calendarView.setDate(Calendar.getInstance().getTimeInMillis(), true, true);
        selectedCalendar.setTimeInMillis(calendarView.getDate());

        // Load meals for today by default
        String todayDay = dayFormat.format(selectedCalendar.getTime());
        tvSelectedDay.setText("Meals on " + todayDay);
        presenter.loadMealsForDay(todayDay);

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedCalendar.set(Calendar.YEAR, year);
            selectedCalendar.set(Calendar.MONTH, month);
            selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            if (!isWithinCurrentWeek(selectedCalendar)) {
                Toast.makeText(this, "Only current week is allowed", Toast.LENGTH_SHORT).show();
                calendarView.setDate(Calendar.getInstance().getTimeInMillis());
                return;
            }

            String selectedDay = dayFormat.format(selectedCalendar.getTime());
            tvSelectedDay.setText("Meals on " + selectedDay);
            presenter.loadMealsForDay(selectedDay);
        });
    }

    private boolean isWithinCurrentWeek(Calendar date) {
        Calendar now = Calendar.getInstance();
        int currentWeek = now.get(Calendar.WEEK_OF_YEAR);
        int currentYear = now.get(Calendar.YEAR);

        int selectedWeek = date.get(Calendar.WEEK_OF_YEAR);
        int selectedYear = date.get(Calendar.YEAR);

        return currentWeek == selectedWeek && currentYear == selectedYear;
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
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_confirm_delete_planned, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(dialogView);
        AlertDialog dialog = builder.create();

        Button btnConfirm = dialogView.findViewById(R.id.btn_confirm);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);

        btnConfirm.setOnClickListener(v -> {
            presenter.deletePlannedMeal(meal);
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}



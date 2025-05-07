package com.example.foodplanner.planner.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.foodplanner.Favorite.view.FavoritesActivity;
import com.example.foodplanner.MealDetail.view.MealDetailsActivity;
import com.example.foodplanner.R;

import com.example.foodplanner.db.MealsLocalDataSourceImpl;
import com.example.foodplanner.home.view.MainActivity;
import com.example.foodplanner.model.MealsRepositoryImpl;
import com.example.foodplanner.model.PlannedMeal;
import com.example.foodplanner.network.MealsRemoteDataSourceImpl;
import com.example.foodplanner.planner.presenter.PlannerPresenter;
import com.example.foodplanner.planner.presenter.PlannerPresenterImpl;
import com.example.foodplanner.profile.view.ProfileActivity;
import com.example.foodplanner.search.view.SearchActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
    private String userId;

    private static final int PERMISSION_REQUEST_CODE = 100;
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
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_planner);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.navigation_planner) {
                return true; // Stay here
            } else if (id == R.id.navigation_favorites) {
                startActivity(new Intent(this, FavoritesActivity.class));
                return true;
            } else if (id == R.id.navigation_home) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else if (id == R.id.navigation_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            } else if (id == R.id.navigation_search) {
                startActivity(new Intent(this, SearchActivity.class));
                return true;
            }

            return false;
        });
        SharedPreferences prefs = getSharedPreferences("FoodAppPrefs", MODE_PRIVATE);
        String userId = prefs.getString("USER_UID", null);


        MealsRepositoryImpl repository = new MealsRepositoryImpl(
                MealsRemoteDataSourceImpl.getInstance(),
                MealsLocalDataSourceImpl.getInstance(this)
        );
        repository.setCurrentUserId(userId);

        presenter = new PlannerPresenterImpl(this, repository, userId);
        // Limit calendar to current week
        Calendar minDateCalendar = Calendar.getInstance();
        calendarView.setMinDate(minDateCalendar.getTimeInMillis());

        Calendar maxDateCalendar = Calendar.getInstance();
        maxDateCalendar.add(Calendar.DAY_OF_YEAR, 6);
        calendarView.setMaxDate(maxDateCalendar.getTimeInMillis());


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
                runOnUiThread(() -> {
                    Toast.makeText(this, "Only current week is allowed", Toast.LENGTH_SHORT).show();
                    // Reset to today's date
                    calendarView.setDate(System.currentTimeMillis());
                });
                return;
            }

            String selectedDay = dayFormat.format(selectedCalendar.getTime());
            tvSelectedDay.setText("Meals on " + selectedDay);
            presenter.loadMealsForDay(selectedDay);
        });
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.READ_CALENDAR,
                    android.Manifest.permission.WRITE_CALENDAR
            }, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Calendar permissions are required to sync meals", Toast.LENGTH_LONG).show();
            }
        }
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
    public void showMeals(LiveData<List<PlannedMeal>> mealsLiveData) {
        mealsLiveData.observe(this, meals -> {
            rvMeals.setVisibility(View.VISIBLE);
            noMealsAnimation.setVisibility(View.GONE);
            noMealsText.setVisibility(View.GONE);
            adapter.updateMeals(meals);
        });
    }


    @Override
    public void showEmpty() {
        runOnUiThread(() -> {
            rvMeals.setVisibility(View.GONE);
            noMealsAnimation.setVisibility(View.VISIBLE);
            noMealsText.setVisibility(View.VISIBLE);

            // Make sure to play the animation
            if (noMealsAnimation.getProgress() == 0f) {
                noMealsAnimation.playAnimation();
            } else {
                noMealsAnimation.setProgress(0f);
                noMealsAnimation.playAnimation();
            }
        });
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

    @Override
    public void showError(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    }

    @Override
    public Context getContext() {
        return this; // Activity is a Context subclass
    }
}



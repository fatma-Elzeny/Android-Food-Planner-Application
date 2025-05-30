package com.example.foodplanner.home.view;

import static android.view.View.GONE;

import android.os.Bundle;
import com.google.gson.Gson;
import android.content.SharedPreferences;
import java.util.Calendar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodplanner.Favorite.view.FavoritesActivity;
import com.example.foodplanner.MealDetail.view.MealDetailsActivity;
import com.example.foodplanner.NetworkUtil;
import com.example.foodplanner.NoInternetDialog;
import com.example.foodplanner.db.MealsLocalDataSourceImpl;
import com.example.foodplanner.mainLogin.view.LoginActivity;
import com.example.foodplanner.model.Category;
import com.example.foodplanner.model.Country;
import com.example.foodplanner.network.MealsRemoteDataSourceImpl;
import com.example.foodplanner.planner.view.PlannerActivity;
import com.example.foodplanner.profile.view.ProfileActivity;
import com.example.foodplanner.R;

import android.content.Intent;
import android.view.View;
import android.widget.*;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import com.example.foodplanner.home.presenter.HomePresenter;
import com.example.foodplanner.home.presenter.HomePresenterImpl;
import com.example.foodplanner.model.Meal;
import com.example.foodplanner.model.MealsRepository;
import com.example.foodplanner.model.MealsRepositoryImpl;
import com.example.foodplanner.search.view.SearchActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements HomeView,OnMealClickListener ,
        CategoriesAdapter.OnCategoryClickListener, AreaAdapter.OnAreaClickListener {
    private HomePresenter presenter;
    private ImageView mealImage;
    private TextView mealName, mealInstructions, lazyMealsTitle;
    private RecyclerView lazyMealsRecycler;
    private ProgressBar progressBar;
    private LazyMealAdapter adapter;

    private RecyclerView categoriesRecycler;
    private CategoriesAdapter categoriesAdapter;
    private RecyclerView areasRecycler;
    private AreaAdapter areaAdapter;
    private RecyclerView mealsByCategoryRecycler;

    private MealsByCategoryAdapter mealsByCategoryAdapter;
    private RecyclerView mealsByAreaRecycler;
    private MealsByCategoryAdapter mealsByAreaAdapter;
    private ProgressBar areaProgressBar;
    private ProgressBar categoryProgressBar;
    private TextView mealsByAreaTitle, categoryMealsTitle;
    private static final String PREFS_NAME = "MealOfTheDayPrefs";
    private static final String KEY_MEAL_JSON = "meal_json";
    private static final String KEY_SAVED_TIME = "saved_time";

    int value = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Initialize UID and Repository FIRST
        SharedPreferences prefs = getSharedPreferences("FoodAppPrefs", MODE_PRIVATE);
        String currentUserUid = prefs.getString("USER_UID", null);
        value = getIntent().getIntExtra("key", 0); // Fix: Safe guest flag retrieval

        MealsRepositoryImpl repository = new MealsRepositoryImpl(
                MealsRemoteDataSourceImpl.getInstance(),
                MealsLocalDataSourceImpl.getInstance(this)
        );
        repository.setCurrentUserId(currentUserUid);
        presenter = new HomePresenterImpl(this, repository);

        // Initialize UI Components
        mealImage = findViewById(R.id.mealImage);
        mealName = findViewById(R.id.mealName);
        mealInstructions = findViewById(R.id.mealInstructions);
        lazyMealsRecycler = findViewById(R.id.lazyMealsRecycler);
        progressBar = findViewById(R.id.progressBarMain);
        lazyMealsTitle = findViewById(R.id.lazyMealsTitle);
        areaProgressBar = findViewById(R.id.areaProgressBar);
        categoryProgressBar = findViewById(R.id.categoryProgressBar);
        mealsByAreaTitle = findViewById(R.id.areaMealsTitle);
        categoryMealsTitle = findViewById(R.id.categoryMealsTitle);

        // Setup Adapters
        adapter = new LazyMealAdapter(this, this);
        lazyMealsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        lazyMealsRecycler.setAdapter(adapter);

        // 2. Network Check and Data Loading
        if (!NetworkUtil.isOnline(this)) {
            NoInternetDialog.show(this);
            SharedPreferences offlinePrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE); // Fix: Renamed variable
            String mealJson = offlinePrefs.getString(KEY_MEAL_JSON, null);
            if (mealJson != null) {
                Gson gson = new Gson();
                Meal savedMeal = gson.fromJson(mealJson, Meal.class);
                showSuggestedMeal(savedMeal);
            } else {
                showError("No internet connection");
            }
        } else {
            SharedPreferences onlinePrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE); // Fix: Renamed variable
            String mealJson = onlinePrefs.getString(KEY_MEAL_JSON, null);
            long savedTime = onlinePrefs.getLong(KEY_SAVED_TIME, 0);
            boolean shouldFetchNew = true;

            if (mealJson != null && savedTime != 0) {
                Gson gson = new Gson();
                Meal savedMeal = gson.fromJson(mealJson, Meal.class);
                long currentTime = System.currentTimeMillis();
                boolean isNewDay = isNewDay(savedTime, currentTime);
                boolean is24HoursPassed = (currentTime - savedTime) >= 24 * 60 * 60 * 1000;

                if (!isNewDay && !is24HoursPassed) {
                    showSuggestedMeal(savedMeal);
                    shouldFetchNew = false;
                }
            }

            if (shouldFetchNew) {
                presenter.getMealOfTheDay();
            }
        }

        // Handle Preferred Category
        String preferredCategory = getIntent().getStringExtra("CATEGORY_PREF");
        if (preferredCategory == null || preferredCategory.isEmpty()) {
            preferredCategory = "Beef";
        }
        presenter.getMealsByCategory(preferredCategory);
        lazyMealsTitle.setText("Meals for " + preferredCategory);

        // Initialize RecyclerViews
        setupCategoriesRecyclerView();
        setupAreasRecyclerView();
        setupMealsByCategoryRecyclerView();
        setupMealsByAreaRecyclerView();

        // Load Data
        presenter.getAllCategories();
        presenter.getAllAreas();

        // 3. Bottom Navigation with Guest Mode Fixes
        setupBottomNavigation();
    }

    // region Helper Methods
    private void setupCategoriesRecyclerView() {
        categoriesRecycler = findViewById(R.id.categoriesRecycler);
        categoriesAdapter = new CategoriesAdapter();
        categoriesAdapter.setOnCategoryClickListener(this);
        categoriesRecycler.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        categoriesRecycler.setAdapter(categoriesAdapter);
    }

    private void setupAreasRecyclerView() {
        areasRecycler = findViewById(R.id.areasRecycler);
        areaAdapter = new AreaAdapter();
        areaAdapter.setOnAreaClickListener(this);
        areasRecycler.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        areasRecycler.setAdapter(areaAdapter);
    }

    private void setupMealsByCategoryRecyclerView() {
        mealsByCategoryRecycler = findViewById(R.id.mealsByCategoryRecycler);
        mealsByCategoryAdapter = new MealsByCategoryAdapter(this, this);
        mealsByCategoryRecycler.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        mealsByCategoryRecycler.setAdapter(mealsByCategoryAdapter);
        mealsByCategoryRecycler.setVisibility(View.GONE);
    }

    private void setupMealsByAreaRecyclerView() {
        mealsByAreaRecycler = findViewById(R.id.mealsByAreaRecycler);
        mealsByAreaAdapter = new MealsByCategoryAdapter(this, this);
        mealsByAreaRecycler.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        mealsByAreaRecycler.setAdapter(mealsByAreaAdapter);
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.navigation_home) {
                return true;
            } else if (id == R.id.navigation_favorites) {
                handleFavoritesNavigation();
                return true;
            } else if (id == R.id.navigation_planner) {
                handlePlannerNavigation();
                return true;
            } else if (id == R.id.navigation_profile) {
                navigateToProfile();
                return true;
            } else if (id == R.id.navigation_search) {
                navigateToSearch();
                return true;
            }
            return false;
        });
    }

    private void handleFavoritesNavigation() {
        if (value == 1) {
            showLoginDialog("access favorites");
        } else {
            Intent intent = new Intent(this, FavoritesActivity.class);
            intent.putExtra("key", value);
            startActivity(intent);
        }
    }

    private void handlePlannerNavigation() {
        if (value == 1) {
            showLoginDialog("access the planner");
        } else {
            startActivity(new Intent(this, PlannerActivity.class));
        }
    }

    private void showLoginDialog(String feature) {
        new AlertDialog.Builder(this)
                .setTitle("Login Required")
                .setMessage("You need to log in to " + feature + ". Would you like to log in now?")
                .setPositiveButton("Log In", (dialog, which) -> startActivity(new Intent(this, LoginActivity.class)))
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void navigateToProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("key", value);
        startActivity(intent);
    }

    private void navigateToSearch() {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("key", value);
        startActivity(intent);
    }
    // endregion

    // region Existing Implementations (Unchanged)
    @Override
    public void onMealClick(Meal meal) {
        Intent intent = new Intent(MainActivity.this, MealDetailsActivity.class);
        intent.putExtra("MEAL_ID", meal.getIdMeal());
        intent.putExtra("key", value);
        startActivity(intent);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showSuggestedMeal(Meal meal) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String mealJson = gson.toJson(meal);
        editor.putString(KEY_MEAL_JSON, mealJson);
        editor.putLong(KEY_SAVED_TIME, System.currentTimeMillis());
        editor.apply();

        mealName.setText(meal.getStrMeal());
        mealInstructions.setText(meal.getStrInstructions());
        Glide.with(this).load(meal.getStrMealThumb()).into(mealImage);

        mealImage.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MealDetailsActivity.class);
            intent.putExtra("MEAL_ID", meal.getIdMeal());
            intent.putExtra("key", value);
            startActivity(intent);
        });
    }

    @Override
    public void showLazyMeals(List<Meal> meals) {
        adapter.setMeals(meals);
        lazyMealsRecycler.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, "Error: " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        categoryMealsTitle.setVisibility(View.GONE);
        mealsByAreaTitle.setVisibility(View.GONE);
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void showMealsByCategory(String category, List<Meal> meals) {
        mealsByCategoryAdapter.setMeals(meals);
        categoryMealsTitle.setText("Meals For " + category);
        mealsByCategoryRecycler.setVisibility(View.VISIBLE);
        if (meals.isEmpty()) {
            categoryMealsTitle.setText("No meals found for " + category);
        }
    }

    @Override
    public void showMealsByArea(String areaName, List<Meal> meals) {
        mealsByAreaTitle.setText("Meals from " + areaName);
        mealsByAreaAdapter.setMeals(meals);
        mealsByAreaRecycler.setVisibility(View.VISIBLE);
        if (meals.isEmpty()) {
            mealsByAreaTitle.setText("No meals found from " + areaName);
        }
    }

    @Override
    public void showAreas(List<Country> areas) {
        if (areas != null) {
            areaAdapter.setAreas(areas);
            findViewById(R.id.areasRecycler).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.areasRecycler).setVisibility(View.GONE);
            showError("No countries available");
        }
    }

    @Override
    public void showAreaLoading() {
        areaProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideAreaLoading() {
        areaProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showCategories(List<Category> categories) {
        if (categories != null) {
            categoriesAdapter.setCategories(categories);
            categoriesRecycler.setVisibility(View.VISIBLE);
        } else {
            showError("No categories available");
        }
    }

    @Override
    public void showCategoryLoading() {
        categoryProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideCategoryLoading() {
        categoryProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onCategoryClick(String category) {
        presenter.loadMealsByCategory(category);
        categoryMealsTitle.setVisibility(View.VISIBLE);
        categoryMealsTitle.setText("Meals For " + category);
        mealsByCategoryRecycler.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAreaClick(String area) {
        presenter.getMealsByArea(area);
        mealsByAreaTitle.setVisibility(View.VISIBLE);
        mealsByAreaTitle.setText("Loading meals from " + area + "...");
        mealsByAreaRecycler.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEmptyState(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    private boolean isNewDay(long savedTimeMillis, long currentTimeMillis) {
        Calendar savedCal = Calendar.getInstance();
        savedCal.setTimeInMillis(savedTimeMillis);
        Calendar currentCal = Calendar.getInstance();
        currentCal.setTimeInMillis(currentTimeMillis);

        return savedCal.get(Calendar.YEAR) != currentCal.get(Calendar.YEAR) ||
                savedCal.get(Calendar.DAY_OF_YEAR) != currentCal.get(Calendar.DAY_OF_YEAR);
    }
}
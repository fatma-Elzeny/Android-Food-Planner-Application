package com.example.foodplanner.home.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodplanner.Favorite.view.FavoritesActivity;
import com.example.foodplanner.MealDetail.view.MealDetailsActivity;
import com.example.foodplanner.NetworkUtil;
import com.example.foodplanner.NoInternetDialog;
import com.example.foodplanner.UserSessionManager;
import com.example.foodplanner.db.MealsLocalDataSourceImpl;
import com.example.foodplanner.mainLogin.view.LoginActivity;
import com.example.foodplanner.model.Category;
import com.example.foodplanner.model.Country;
import com.example.foodplanner.network.MealsRemoteDataSourceImpl;
import com.example.foodplanner.planner.view.PlannerActivity;
import com.example.foodplanner.profile.view.ProfileActivity;
import com.example.foodplanner.R;

import android.content.Intent;
import android.view.MenuItem;
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
        CategoriesAdapter.OnCategoryClickListener, AreaAdapter.OnAreaClickListener ,MealsByCategoryAdapter.OnMealClickListener{
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
    private RecyclerView mealsByAreaRecycler;
    private MealsByCategoryAdapter mealsByAreaAdapter;

    private ProgressBar areaProgressBar;
    private ProgressBar categoryProgressBar;
    private TextView mealsByAreaTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mealImage = findViewById(R.id.mealImage);
        mealName = findViewById(R.id.mealName);
        mealInstructions = findViewById(R.id.mealInstructions);
        lazyMealsRecycler = findViewById(R.id.lazyMealsRecycler);
        progressBar = findViewById(R.id.progressBarMain);
        lazyMealsTitle = findViewById(R.id.lazyMealsTitle);
        areaProgressBar = findViewById(R.id.areaProgressBar);
        categoryProgressBar = findViewById(R.id.categoryProgressBar);
        mealsByAreaTitle = findViewById(R.id.areaMealsTitle);
        MealsRepository repository = new MealsRepositoryImpl(MealsRemoteDataSourceImpl.getInstance(), MealsLocalDataSourceImpl.getInstance(this));
        presenter = new HomePresenterImpl(this, repository);

        adapter = new LazyMealAdapter(this, this);
        lazyMealsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        lazyMealsRecycler.setAdapter(adapter);
        if (!NetworkUtil.isOnline(this)) {
            NoInternetDialog.show(this);
            return; // Skip calling Presenter/Remote
        }
        presenter.getMealOfTheDay();


        String preferredCategory = getIntent().getStringExtra("CATEGORY_PREF");

        if (preferredCategory == null || preferredCategory.isEmpty()) {
            preferredCategory = "Beef"; // 🟣 Default category if not set (e.g., guest or Google)
        }

        presenter.loadMealsByCategory(preferredCategory);
        lazyMealsTitle.setText("Meals for " + preferredCategory);

        // Initialize Categories RecyclerView
        categoriesRecycler = findViewById(R.id.categoriesRecycler);
        categoriesAdapter = new CategoriesAdapter();
        categoriesAdapter.setOnCategoryClickListener(this);
        categoriesRecycler.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        categoriesRecycler.setAdapter(categoriesAdapter);

        // Initialize Areas RecyclerView
        areasRecycler = findViewById(R.id.areasRecycler);
        areaAdapter = new AreaAdapter();
        areaAdapter.setOnAreaClickListener(this);
        areasRecycler.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        areasRecycler.setAdapter(areaAdapter);

        // Initialize Meals by Area RecyclerView
        mealsByAreaRecycler = findViewById(R.id.mealsByAreaRecycler);
        mealsByAreaAdapter = new MealsByCategoryAdapter();
        mealsByAreaAdapter.setOnMealClickListener(this);
        mealsByAreaRecycler.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        mealsByAreaRecycler.setAdapter(mealsByAreaAdapter);
        presenter.getAllCategories();
        presenter.getAllAreas();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.navigation_home) {
                return true; // Stay here
            } else if (id == R.id.navigation_favorites) {
                startActivity(new Intent(this, FavoritesActivity.class));
                return true;
            } else if (id == R.id.navigation_planner) {
                startActivity(new Intent(this, PlannerActivity.class));
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


    }

    public void onMealClick(Meal meal) {
        // Handle click
        Intent intent = new Intent(MainActivity.this, MealDetailsActivity.class);
        intent.putExtra("MEAL_ID", meal.getIdMeal());
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
        mealName.setText(meal.getStrMeal());
        mealInstructions.setText(meal.getStrInstructions());
        Glide.with(this).load(meal.getStrMealThumb()).into(mealImage);

        mealImage.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MealDetailsActivity.class);
            intent.putExtra("MEAL_ID", meal.getIdMeal());
            startActivity(intent);
        });
    }

    @Override
    public void showLazyMeals(List<Meal> meals) {
        adapter.setMeals(meals);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, "Error: " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void showMealsByCategory(String category, List<Meal> meals) {
        adapter.setMeals(meals);
    }

    @Override
    public void showMealsByArea(String areaName, List<Meal> meals) {
        mealsByAreaTitle.setText("Meals from " + areaName);
        mealsByAreaAdapter.setMeals(meals);
        mealsByAreaRecycler.setVisibility(View.VISIBLE);
    }

    @Override
    public void showAreas(List<Country> areas) {
        if (areas != null) {
            areaAdapter.setAreas(areas);
            findViewById(R.id.areasRecycler).setVisibility(View.VISIBLE);
        } else {
            // Handle null case - show error or empty state
            findViewById(R.id.areasRecycler).setVisibility(View.GONE);
            showError("No countries available");
        }

    }

    @Override
    public void showAreaLoading() {
        areaProgressBar.setVisibility(View.VISIBLE);
        findViewById(R.id.areasRecycler).setVisibility(View.GONE);
    }

    @Override
    public void hideAreaLoading() {
        areaProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showCategories(List<Category> categories) {
        if (categories != null) {
            categoriesAdapter.setCategories(categories);
            findViewById(R.id.categoriesRecycler).setVisibility(View.VISIBLE);
        } else {
            // Handle null case - show error or empty state
            findViewById(R.id.categoriesRecycler).setVisibility(View.GONE);
            showError("No categories available");
        }

    }

    @Override
    public void showCategoryLoading() {
        categoryProgressBar.setVisibility(View.VISIBLE);
        findViewById(R.id.categoriesRecycler).setVisibility(View.GONE);
    }

    @Override
    public void hideCategoryLoading() {
        categoryProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onCategoryClick(String category) {
        presenter.loadMealsByCategory(category);
        lazyMealsTitle.setText("Meals for " + category);
    }

    @Override
    public void onAreaClick(String area) {
        presenter.getMealsByArea(area);
        mealsByAreaTitle.setText("Loading meals from " + area + "...");
        mealsByAreaRecycler.setVisibility(View.GONE);
    }
}
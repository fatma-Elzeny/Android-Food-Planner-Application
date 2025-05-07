package com.example.foodplanner.search.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.Favorite.view.FavoritesActivity;
import com.example.foodplanner.MealDetail.view.MealDetailsActivity;
import com.example.foodplanner.NetworkUtil;
import com.example.foodplanner.NoInternetDialog;
import com.example.foodplanner.R;
import com.example.foodplanner.db.MealsLocalDataSourceImpl;
import com.example.foodplanner.home.view.MainActivity;
import com.example.foodplanner.mainLogin.view.LoginActivity;
import com.example.foodplanner.model.Category;
import com.example.foodplanner.model.Meal;
import com.example.foodplanner.model.MealsRepositoryImpl;
import com.example.foodplanner.network.MealsRemoteDataSourceImpl;
import com.example.foodplanner.planner.view.PlannerActivity;
import com.example.foodplanner.profile.view.ProfileActivity;
import com.example.foodplanner.search.presenter.searchPresenter;
import com.example.foodplanner.search.presenter.SearchPresenterImpl;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements SearchScreen ,SearchAdapter.OnItemClickListener{
    private searchPresenter searchPresenter;
    private SearchAdapter searchAdapter;
    private RecyclerView mealsRecyclerView;
    private SearchView searchView;
    private Spinner searchTypeSpinner;
    private List<Meal> mealList = new ArrayList<>();

    private ProgressBar progressBar2;
    int value ;
    private final String[] searchOptions = {"Meal", "Countries", "Categories", "Ingredients"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mealsRecyclerView = findViewById(R.id.mealsRecyclerViewID);
        searchView = findViewById(R.id.searchViewID);
        searchTypeSpinner = findViewById(R.id.searchTypeSpinner);
        progressBar2=findViewById(R.id.progressBar2);
        mealsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchAdapter = new SearchAdapter(this, mealList,this);
        mealsRecyclerView.setAdapter(searchAdapter);
        if (!NetworkUtil.isOnline(this)) {
            NoInternetDialog.show(this);
            return; // Skip calling Presenter/Remote
        }
        searchPresenter = new SearchPresenterImpl(this,
                MealsRepositoryImpl.getInstance(
                        MealsRemoteDataSourceImpl.getInstance(),
                        MealsLocalDataSourceImpl.getInstance(this)));

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, searchOptions);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchTypeSpinner.setAdapter(spinnerAdapter);

        // SearchActivity.java
        searchTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedSearchType = searchOptions[position];
                updateSearchHint(selectedSearchType);

                if (selectedSearchType.equals("Categories")) {
                    searchPresenter.getAllCategories();
                } else if (selectedSearchType.equals("Countries")) {
                    searchPresenter.getAllCountries();
                } else if (selectedSearchType.equals("Ingredients")) {
                    searchPresenter.getAllIngredients(); // Add this line
                } else {
                    mealList.clear();
                    searchAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.isEmpty()) {
                    Toast.makeText(SearchActivity.this, "Please enter a query", Toast.LENGTH_SHORT).show();
                    return false;
                }
                String selectedSearchType = searchTypeSpinner.getSelectedItem().toString();
                if (selectedSearchType.equals("Meal")) {
                    searchPresenter.getMealsByName(query);
                } else if (selectedSearchType.equals("Countries")) {
                    searchPresenter.getMealsByCountry(query);
                } else if (selectedSearchType.equals("Categories")) {
                    searchPresenter.getMealsByCategory(query);
                } else if (selectedSearchType.equals("Ingredients")) {
                    searchPresenter.getMealsByIngredient(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void updateSearchHint(String selectedSearchType) {
        switch (selectedSearchType) {
            case "Meal":
                searchView.setQueryHint("Search By Meal Name . . .");
                break;
            case "Countries":
                searchView.setQueryHint("Search By Cuisine Name . . .");
                break;
            case "Categories":
                searchView.setQueryHint("Search By Category Name . . .");
                break;
            case "Ingredients":
                searchView.setQueryHint("Search By Ingredient Name . . .");
                break;
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                value = extras.getInt("key");
            }

            if (id == R.id.navigation_search) {
                return true; // Stay here
            } else if (id == R.id.navigation_favorites) {
                if (value == 1 ){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Login Required");
                    builder.setMessage("You need to log in to access favorites. Would you like to log in now?");
                    builder.setPositiveButton("Log In", (dialog, which) -> {
                        // Redirect to LoginActivity
                        startActivity(new Intent(this, LoginActivity.class));
                    });
                    builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
                    builder.create().show();
                    return true;

                }else {
                    startActivity(new Intent(this, FavoritesActivity.class));
                    return true;
                }
            } else if (id == R.id.navigation_planner) {
                if (value == 1 ){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Login Required");
                    builder.setMessage("You need to log in to access planner. Would you like to log in now?");
                    builder.setPositiveButton("Log In", (dialog, which) -> {
                        // Redirect to LoginActivity
                        startActivity(new Intent(this, LoginActivity.class));
                    });
                    builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
                    builder.create().show();
                    return true;

                }else {
                    startActivity(new Intent(this, PlannerActivity.class));
                    return true;
                }
            } else if (id == R.id.navigation_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            } else if (id == R.id.navigation_home) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            }

            return false;
        });


    }

    @Override
    public void displayMeals(List<Meal> meals) {
        if (meals != null) {
            mealList.clear();
            mealList.addAll(meals);
            searchAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "No Meals Found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void displayError(String errMsg) {
        Toast.makeText(this, "Error: " + errMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(Meal item) {
        if ("country".equals(item.getType())) {
            searchPresenter.getMealsByCountry(item.getStrMeal());
        } else if ("category".equals(item.getType())) {
            searchPresenter.getMealsByCategory(item.getStrMeal());
        } else if ("ingredient".equals(item.getType())) {
            searchPresenter.getMealsByIngredient(item.getStrMeal());
        } else {
            Intent intent = new Intent(this, MealDetailsActivity.class);
            intent.putExtra("MEAL_ID", item.getIdMeal());
            startActivity(intent);
        }
    }

    @Override
    public void showMeals(List<Meal> meals) {
        displayMeals(meals);
    }

    @Override
    public void displayEmptyState(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        // Show loading indicator (implement loading indicator view in XML)
        // For example, using a ProgressBar

        progressBar2.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        // Hide loading indicator

        progressBar2.setVisibility(View.GONE);
    }


    @Override
    public void showEmptyState(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
    }
}

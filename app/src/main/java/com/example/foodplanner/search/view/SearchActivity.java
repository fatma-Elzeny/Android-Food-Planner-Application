package com.example.foodplanner.search.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.MealDetail.view.MealDetailsActivity;
import com.example.foodplanner.R;
import com.example.foodplanner.db.MealsLocalDataSourceImpl;
import com.example.foodplanner.model.Meal;
import com.example.foodplanner.model.MealsRepositoryImpl;
import com.example.foodplanner.network.MealsRemoteDataSourceImpl;
import com.example.foodplanner.search.presenter.searchPresenter;
import com.example.foodplanner.search.presenter.SearchPresenterImpl;

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

        searchPresenter = new SearchPresenterImpl(this,
                MealsRepositoryImpl.getInstance(
                        MealsRemoteDataSourceImpl.getInstance(),
                        MealsLocalDataSourceImpl.getInstance(this)));

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, searchOptions);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchTypeSpinner.setAdapter(spinnerAdapter);

        searchTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedSearchType = searchOptions[position];
                updateSearchHint(selectedSearchType);
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
    public void onItemClick(Meal meal) {
        // Handle meal item click, for example, navigate to a detailed activity
        Intent intent = new Intent(this, MealDetailsActivity.class);
        intent.putExtra("mealId", meal.getIdMeal());
        startActivity(intent);
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

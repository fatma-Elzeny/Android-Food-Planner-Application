package com.example.foodplanner.search.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.R;
import com.example.foodplanner.db.MealsLocalDataSourceImpl;
import com.example.foodplanner.model.Category;
import com.example.foodplanner.model.Ingredient;
import com.example.foodplanner.model.Meal;
import com.example.foodplanner.model.MealsRepository;
import com.example.foodplanner.model.MealsRepositoryImpl;
import com.example.foodplanner.network.MealsRemoteDataSource;
import com.example.foodplanner.network.MealsRemoteDataSourceImpl;
import com.example.foodplanner.search.presenter.searchPresenter;
import com.example.foodplanner.search.presenter.SearchPresenterImpl;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements SearchView {
    private SearchPresenterImpl presenter;
    private SearchAdapter adapter;
    private ProgressBar progressBar;
    private TextView emptyStateText;
    private RecyclerView recyclerView;
    private EditText searchInput;
    private ImageButton searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Initialize views
        progressBar = findViewById(R.id.progressBar);
        emptyStateText = findViewById(R.id.emptyStateText);
        recyclerView = findViewById(R.id.mealsRecyclerView);
        searchInput = findViewById(R.id.searchInput);
        searchButton = findViewById(R.id.searchButton);

        // Setup RecyclerView
        adapter = new SearchAdapter(new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Initialize presenter
        presenter = new SearchPresenterImpl(this, new MealsRepositoryImpl(MealsRemoteDataSourceImpl.getInstance(),MealsLocalDataSourceImpl.getInstance(this)));

        // Set up search button click listener
        searchButton.setOnClickListener(v -> performSearch());
    }

    private void performSearch() {
        String query = searchInput.getText().toString().trim();
        if (!query.isEmpty()) {
            // Determine search type (could be enhanced with search filters)
            presenter.getMealsByName(query);
        } else {
            Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showMeals(List<Meal> meals) {
        emptyStateText.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        adapter.updateMeals(meals);
    }

    @Override
    public void showEmptyState(String message) {
        recyclerView.setVisibility(View.GONE);
        emptyStateText.setVisibility(View.VISIBLE);
        emptyStateText.setText(message);
    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }
}

package com.example.foodplanner.home.view;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodplanner.MealDetailsActivity;
import com.example.foodplanner.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.foodplanner.R;
import com.example.foodplanner.db.MealsLocalDataSource;
import com.example.foodplanner.db.MealsLocalDataSourceImpl;
import com.example.foodplanner.home.presenter.HomePresenter;
import com.example.foodplanner.home.presenter.HomePresenterImpl;
import com.example.foodplanner.model.Meal;
import com.example.foodplanner.model.MealsRepository;
import com.example.foodplanner.model.MealsRepositoryImpl;
import com.example.foodplanner.network.MealsRemoteDataSourceImpl;

import java.util.List;

public class MainActivity extends AppCompatActivity implements HomeView,OnMealClickListener {

    private HomePresenter presenter;
    private ImageView mealImage;
    private TextView mealName, mealInstructions;
    private RecyclerView lazyMealsRecycler;
    private ProgressBar progressBar;
    private LazyMealAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mealImage = findViewById(R.id.mealImage);
        mealName = findViewById(R.id.mealName);
        mealInstructions = findViewById(R.id.mealInstructions);
        lazyMealsRecycler = findViewById(R.id.lazyMealsRecycler);
        progressBar = findViewById(R.id.progressBarMain);

        MealsRepository repository = new MealsRepositoryImpl(this);
        presenter = new HomePresenterImpl(this, repository);

        adapter = new LazyMealAdapter(this, this);
        lazyMealsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        lazyMealsRecycler.setAdapter(adapter);

        presenter.getMealOfTheDay();
        presenter.getMealsByCategory("Beef");


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
}

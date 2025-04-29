package com.example.foodplanner.Favorite.view;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.Favorite.presenter.FavPresenterImpl;
import com.example.foodplanner.Favorite.presenter.Favpresenter;
import com.example.foodplanner.R;
import com.example.foodplanner.model.FavoriteMeal;
import com.example.foodplanner.model.MealsRepositoryImpl;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class FavoritesActivity extends AppCompatActivity implements FavoritesView,OnFavMealClickListener{

    private Favpresenter presenter;
    private RecyclerView recyclerView;
    private FavoritesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        recyclerView = findViewById(R.id.recycler_favorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FavoritesAdapter(this);
        recyclerView.setAdapter(adapter);

        presenter = new FavPresenterImpl(this, new MealsRepositoryImpl(this));
        presenter.getFavoriteMeals();
    }

    private void onMealClicked(FavoriteMeal meal) {
        showDeleteConfirmation(meal);
    }


    @Override
    public void showEmptyState() {
        Toast.makeText(this, "No favorite meals yet.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDeleteConfirmation(FavoriteMeal meal) {
        new MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle("Delete Favorite")
                .setMessage("Are you sure you want to remove \"" + meal.getStrMeal() + "\" from favorites?")
                .setPositiveButton("Yes", (dialog, which) -> presenter.deleteMeal(meal))
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }



    @Override
    public void onFavoriteMealClick(FavoriteMeal meal) {
        showDeleteConfirmation(meal);
    }

    @Override
    public void observeFavorites(LiveData<List<FavoriteMeal>> liveData) {
        liveData.observe(this, meals -> {
            if (meals == null || meals.isEmpty()) {
                showEmptyState();
            } else {
                adapter.setMeals(meals);
            }
        });
    }

}
package com.example.foodplanner.Favorite.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
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

import com.example.foodplanner.ConnectionLiveData;
import com.example.foodplanner.Favorite.presenter.FavPresenterImpl;
import com.example.foodplanner.Favorite.presenter.Favpresenter;
import com.example.foodplanner.MealDetail.view.MealDetailsActivity;
import com.example.foodplanner.NoInternetDialog;
import com.example.foodplanner.R;
import com.example.foodplanner.model.FavoriteMeal;
import com.example.foodplanner.model.MealsRepositoryImpl;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class FavoritesActivity extends AppCompatActivity implements FavoritesView,OnFavMealClickListener {

    private Favpresenter presenter;
    private RecyclerView recyclerView;
    private FavoritesAdapter adapter;
    private LinearLayout emptyStateLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        recyclerView = findViewById(R.id.recycler_favorites);
        emptyStateLayout = findViewById(R.id.empty_state_layout);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FavoritesAdapter(this);
        recyclerView.setAdapter(adapter);

        presenter = new FavPresenterImpl(this, new MealsRepositoryImpl(this));
        presenter.getFavoriteMeals();
    }




    @Override
    public void showEmptyState() {
        recyclerView.setVisibility(View.GONE);
        emptyStateLayout.setVisibility(View.VISIBLE);
        Toast.makeText(this, "No favorite meals yet.", Toast.LENGTH_SHORT).show();
    }

    //@Override
   /* public void showDeleteConfirmation(FavoriteMeal meal) {
        new MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle("Delete Favorite")
                .setMessage("Are you sure you want to remove \"" + meal.getStrMeal() + "\" from favorites?")
                .setPositiveButton("Yes", (dialog, which) -> presenter.deleteMeal(meal))
                .setNegativeButton("Cancel", null)
                .show();
    }*/
    private void showLottieDeleteDialog(FavoriteMeal meal) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_confirm_delete, null);

        AlertDialog dialog = new AlertDialog.Builder(this, R.style.AlertDialogTheme)
                .setView(dialogView)
                .setPositiveButton("Delete", (d, which) -> presenter.deleteMeal(meal))
                .setNegativeButton("Cancel", null)
                .create();

        dialog.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }


    @Override
    public void onFavoriteMealClick(FavoriteMeal meal) {
        Intent intent = new Intent(this, MealDetailsActivity.class);
        intent.putExtra("meal_id", meal.getIdMeal());
        startActivity(intent);
    }
    @Override
    public void onDeleteClick(FavoriteMeal meal) {
        showLottieDeleteDialog(meal);
    }
    @Override
    public void observeFavorites(LiveData<List<FavoriteMeal>> liveData) {
        liveData.observe(this, meals -> {
            if (meals == null || meals.isEmpty()) {
                // 🔻 Show empty state
                recyclerView.setVisibility(View.GONE);
                emptyStateLayout.setVisibility(View.VISIBLE);
            } else {
                // ✅ Show list
                emptyStateLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                adapter.setMeals(meals);
            }
        });

    }


}
package com.example.foodplanner.Favorite.view;

import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
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

import com.airbnb.lottie.LottieAnimationView;
import com.example.foodplanner.ConnectionLiveData;
import com.example.foodplanner.Favorite.presenter.FavPresenterImpl;
import com.example.foodplanner.Favorite.presenter.Favpresenter;
import com.example.foodplanner.MealDetail.view.MealDetailsActivity;
import com.example.foodplanner.NoInternetDialog;
import com.example.foodplanner.R;
import com.example.foodplanner.SearchActivity;
import com.example.foodplanner.home.view.MainActivity;
import com.example.foodplanner.model.FavoriteMeal;
import com.example.foodplanner.model.MealsRepositoryImpl;
import com.example.foodplanner.planner.view.PlannerActivity;
import com.example.foodplanner.profile.view.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class FavoritesActivity extends AppCompatActivity implements FavoritesView,OnFavMealClickListener {

    private Favpresenter presenter;
    private RecyclerView recyclerView;
    private FavoritesAdapter adapter;

   private TextView no_fav_text;
   private LottieAnimationView no_fav_animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        recyclerView = findViewById(R.id.rv_favorites);
        no_fav_text = findViewById(R.id.no_fav_text);
        no_fav_animation =findViewById(R.id.no_fav_animation);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FavoritesAdapter(this);
        recyclerView.setAdapter(adapter);

        presenter = new FavPresenterImpl(this, new MealsRepositoryImpl(this));
        presenter.getFavoriteMeals();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_favorites);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.navigation_favorites) {
                return true; // Stay here
            } else if (id == R.id.navigation_home) {
                startActivity(new Intent(this, MainActivity.class));
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




    @Override
    public void showEmptyState() {
        recyclerView.setVisibility(View.GONE);
        no_fav_text.setVisibility(VISIBLE);
        no_fav_animation.setVisibility(VISIBLE);
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
  /*  private void showLottieDeleteDialog(FavoriteMeal meal) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_confirm_delete, null);

        AlertDialog dialog = new AlertDialog.Builder(this, R.style.AlertDialogTheme)
                .setView(dialogView)
                .setPositiveButton("Delete", (d, which) -> presenter.deleteMeal(meal))
                .setNegativeButton("Cancel", null)
                .create();

        dialog.show();
    }*/


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
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_confirm_delete, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(dialogView);
        AlertDialog dialog = builder.create();

        Button btnConfirm = dialogView.findViewById(R.id.btn_confirm);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);

        btnConfirm.setOnClickListener(v -> {
            presenter.deleteMeal(meal);
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    @Override
    public void observeFavorites(LiveData<List<FavoriteMeal>> liveData) {
        liveData.observe(this, meals -> {
            if (meals == null || meals.isEmpty()) {
                // 🔻 Show empty state
                recyclerView.setVisibility(View.GONE);
                 no_fav_text.setVisibility(VISIBLE);
                no_fav_animation.setVisibility(VISIBLE);
            } else {
                // ✅ Show list
                no_fav_text.setVisibility(VISIBLE);
                no_fav_animation.setVisibility(VISIBLE);
                recyclerView.setVisibility(VISIBLE);
                adapter.setMeals(meals);
            }
        });

    }


}
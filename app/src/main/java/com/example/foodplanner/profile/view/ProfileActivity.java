package com.example.foodplanner.profile.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.foodplanner.Favorite.view.FavoritesActivity;
import com.example.foodplanner.R;

import com.example.foodplanner.home.view.MainActivity;
import com.example.foodplanner.mainLogin.view.LoginActivity;
import com.example.foodplanner.planner.view.PlannerActivity;
import com.example.foodplanner.profile.presenter.ProfilePresenter;
import com.example.foodplanner.profile.presenter.ProfilePresenterImpl;
import com.example.foodplanner.search.view.SearchActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity implements ProfileView {

    private TextView tvUserName;
    private Button btnFavorites, btnPlanner, btnLogout;
    private LottieAnimationView animationView;
    private ProfilePresenter presenter;

    int value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvUserName = findViewById(R.id.tv_greeting);
        btnFavorites = findViewById(R.id.btn_favorites);
        btnPlanner = findViewById(R.id.btn_planner);
        btnLogout = findViewById(R.id.btn_logout);
        animationView = findViewById(R.id.profile_animation);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userName = (user != null && user.getDisplayName() != null) ? user.getDisplayName() : "Guest";

        presenter = new ProfilePresenterImpl(this, userName);
        presenter.onViewLoaded();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                value = extras.getInt("key");
            }

            if (id == R.id.navigation_profile) {
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
                if (value == 1 ) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Login Required");
                    builder.setMessage("You need to log in to access Planned. Would you like to log in now?");
                    builder.setPositiveButton("Log In", (dialog, which) -> {
                        // Redirect to LoginActivity
                        startActivity(new Intent(this, LoginActivity.class));
                    });
                    builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
                    builder.create().show();
                    return true;
                } else{
                    startActivity(new Intent(this, PlannerActivity.class));
                    return true;
                }
            } else if (id == R.id.navigation_home) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else if (id == R.id.navigation_search) {
                Intent intent = new Intent(this, SearchActivity.class);
                intent.putExtra("key", 1); // guest
                startActivity(intent);
                return true;
            }

            return false;
        });


        btnFavorites.setOnClickListener(v -> presenter.onFavoritesClicked());
        btnPlanner.setOnClickListener(v -> presenter.onPlannerClicked());
        btnLogout.setOnClickListener(v -> presenter.onLogoutClicked());
    }

    @Override
    public void showUserName(String name) {
        tvUserName.setText("Hello, " + name + "!");
    }

    @Override
    public void navigateToFavorites() {;
        if (value == 1 ) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Login Required");
            builder.setMessage("You need to log in to access favorites. Would you like to log in now?");
            builder.setPositiveButton("Log In", (dialog, which) -> {
                // Redirect to LoginActivity
                startActivity(new Intent(this, LoginActivity.class));
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
            builder.create().show();

        }else {
            startActivity(new Intent(this, FavoritesActivity.class));
        }
    }

    @Override
    public void navigateToPlanner() {
        if (value == 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Login Required");
            builder.setMessage("You need to log in to access Planner. Would you like to log in now?");
            builder.setPositiveButton("Log In", (dialog, which) -> {
                // Redirect to LoginActivity
                startActivity(new Intent(this, LoginActivity.class));
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
            builder.create().show();

        } else {

            startActivity(new Intent(this, PlannerActivity.class));
        }
    }
    @Override
    public void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}

package com.example.foodplanner.profile.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.foodplanner.Favorite.view.FavoritesActivity;
import com.example.foodplanner.R;
import com.example.foodplanner.SearchActivity;
import com.example.foodplanner.home.view.MainActivity;
import com.example.foodplanner.mainLogin.view.LoginActivity;
import com.example.foodplanner.planner.view.PlannerActivity;
import com.example.foodplanner.profile.presenter.ProfilePresenter;
import com.example.foodplanner.profile.presenter.ProfilePresenterImpl;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity implements ProfileView {

    private TextView tvUserName;
    private Button btnFavorites, btnPlanner, btnLogout;
    private LottieAnimationView animationView;
    private ProfilePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvUserName = findViewById(R.id.tv_greeting);
        btnFavorites = findViewById(R.id.btn_favorites);
        btnPlanner = findViewById(R.id.btn_planner);
        btnLogout = findViewById(R.id.btn_logout);
        animationView = findViewById(R.id.profile_animation);

        String userName = "Guest"; // or retrieve from Firebase/Auth
        presenter = new ProfilePresenterImpl(this, userName);
        presenter.onViewLoaded();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.navigation_profile) {
                return true; // Stay here
            } else if (id == R.id.navigation_favorites) {
                startActivity(new Intent(this, FavoritesActivity.class));
                return true;
            } else if (id == R.id.navigation_planner) {
                startActivity(new Intent(this, PlannerActivity.class));
                return true;
            } else if (id == R.id.navigation_home) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else if (id == R.id.navigation_search) {
                startActivity(new Intent(this, SearchActivity.class));
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
    public void navigateToFavorites() {
        startActivity(new Intent(this, FavoritesActivity.class));
    }

    @Override
    public void navigateToPlanner() {
        startActivity(new Intent(this, PlannerActivity.class));
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

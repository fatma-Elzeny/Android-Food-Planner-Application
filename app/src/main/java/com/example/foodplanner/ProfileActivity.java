package com.example.foodplanner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodplanner.Favorite.view.FavoritesActivity;
import com.example.foodplanner.mainLogin.view.LoginActivity;
import com.example.foodplanner.planner.view.PlannerActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvGreeting;
    private Button btnFavorites, btnPlanner, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvGreeting = findViewById(R.id.tv_greeting);
        btnFavorites = findViewById(R.id.btn_favorites);
        btnPlanner = findViewById(R.id.btn_planner);
        btnLogout = findViewById(R.id.btn_logout);

        // Assuming name is passed through intent or shared preferences
        String userName = getIntent().getStringExtra("USER_NAME");
        if (userName == null) userName = "Chef";
        tvGreeting.setText("Hello, " + userName + "!");

        btnFavorites.setOnClickListener(v -> {
            startActivity(new Intent(this, FavoritesActivity.class));
        });

        btnPlanner.setOnClickListener(v -> {
            startActivity(new Intent(this, PlannerActivity.class));
        });

        btnLogout.setOnClickListener(v -> {
            // Clear session (e.g., FirebaseAuth.signOut(), clear shared prefs, etc.)
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}
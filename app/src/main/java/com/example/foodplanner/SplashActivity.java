package com.example.foodplanner;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Handler;


public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME = 5000; // 5 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            // Navigate to Login screen
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_TIME);
    }
}

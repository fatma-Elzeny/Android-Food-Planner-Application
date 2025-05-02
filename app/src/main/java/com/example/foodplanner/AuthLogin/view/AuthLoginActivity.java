package com.example.foodplanner.AuthLogin.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.foodplanner.AuthLogin.presenter.AuthLoginPresenter;
import com.example.foodplanner.AuthLogin.presenter.AuthLoginPresenterImpl;
import com.example.foodplanner.R;
import com.example.foodplanner.home.view.MainActivity;

public class AuthLoginActivity extends AppCompatActivity implements AuthLoginView {

    private EditText emailField, passwordField;
    private ProgressBar progressBar;
    private Button loginButton;
    private AuthLoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_login);

        emailField = findViewById(R.id.editTextEmail);
        passwordField = findViewById(R.id.editTextPassword);
        progressBar = findViewById(R.id.progressBar);
        loginButton = findViewById(R.id.buttonLogin);

        presenter = new AuthLoginPresenterImpl(this);

        loginButton.setOnClickListener(v -> {
            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();
            presenter.login(email, password);
        });
    }

    @Override
    public void showLoginSuccess(String userName) {
        Toast.makeText(this, "Welcome, " + userName, Toast.LENGTH_SHORT).show();
        // Navigate to next screen
        Intent intent = new Intent(AuthLoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Optional: Prevent going back to login
    }

    @Override
    public void showLoginError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}

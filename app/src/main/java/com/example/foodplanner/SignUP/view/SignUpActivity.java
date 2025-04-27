package com.example.foodplanner.SignUP.view;



import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodplanner.MainActivity;
import com.example.foodplanner.R;
import com.example.foodplanner.SignUP.presenter.SignUpPresenter;
import com.example.foodplanner.SignUP.presenter.SignUpPresenterImpl;


public class SignUpActivity extends AppCompatActivity implements SignUpView {

    private EditText emailEditText, passwordEditText;
    private Button createAccountBtn;
    private ProgressBar progressBar;
    private SignUpPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        presenter = new SignUpPresenterImpl(this);

        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        createAccountBtn = findViewById(R.id.btnSignUp);

        createAccountBtn.setOnClickListener(view -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                presenter.signUp(email, password);
            }
        });
    }

    @Override
    public void showLoading() {
        createAccountBtn.setEnabled(false);
    }

    @Override
    public void hideLoading() {
        createAccountBtn.setEnabled(true);
    }

    @Override
    public void onSignUpSuccess() {
        Toast.makeText(this, "Account Created!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onSignUpFailure(String message) {
        Toast.makeText(this, "Error: " + message, Toast.LENGTH_LONG).show();
    }
}

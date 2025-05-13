package com.example.foodplanner.SignUP.view;



import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodplanner.db.MealsLocalDataSourceImpl;
import com.example.foodplanner.home.view.MainActivity;
import com.example.foodplanner.R;
import com.example.foodplanner.SignUP.presenter.SignUpPresenter;
import com.example.foodplanner.SignUP.presenter.SignUpPresenterImpl;
import com.example.foodplanner.model.MealsRepositoryImpl;
import com.example.foodplanner.network.MealsRemoteDataSourceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class SignUpActivity extends AppCompatActivity implements SignUpView {

    private EditText etEmail, etPassword, etConfirmPassword, etUserName;
    private AutoCompleteTextView etFoodPref,etCountry;
    private ProgressBar progressBar;
    private Button btnSignUp;
    private SignUpPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        etUserName = findViewById(R.id.et_user_name);

        etFoodPref = findViewById(R.id.et_food_pref);
        String[] categories = {
                "Beef", "Chicken", "Dessert", "Lamb", "Miscellaneous", "Pasta",
                "Pork", "Seafood", "Side", "Starter", "Vegan", "Vegetarian", "Breakfast", "Goat"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                categories
        );
        etFoodPref.setAdapter(adapter);
        etFoodPref.setDropDownBackgroundResource(R.color.white);

        AutoCompleteTextView etCountry = findViewById(R.id.et_country);

        List<String> countries = new ArrayList<>();
        for (Locale locale : Locale.getAvailableLocales()) {
            String country = locale.getDisplayCountry();
            if (!TextUtils.isEmpty(country) && !countries.contains(country)) {
                countries.add(country);
            }
        }
        Collections.sort(countries); // Optional: sort alphabetically

        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                countries
        );
        etCountry.setAdapter(countryAdapter);


        progressBar = findViewById(R.id.progress_bar);
        btnSignUp = findViewById(R.id.btn_sign_up);

        presenter = new SignUpPresenterImpl(this);

        btnSignUp.setOnClickListener(v -> presenter.signUp(
                etEmail.getText().toString(),
                etPassword.getText().toString(),
                etConfirmPassword.getText().toString(),
                etUserName.getText().toString(),
                etCountry.getText().toString(),
                etFoodPref.getText().toString()
        ));
    }

    @Override
    public void showSignUpSuccess(String userName,String preferredCategory) {
        Toast.makeText(this, "Welcome, " + userName, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("CATEGORY_PREF", preferredCategory);
        startActivity(intent);
        finish();
    }

    @Override
    public void showSignUpError(String message) {
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
    @Override
    public void persistUser(String uid) {
        SharedPreferences prefs = getSharedPreferences("FoodAppPrefs", MODE_PRIVATE);
        prefs.edit().putString("USER_UID", uid).apply();

        MealsRepositoryImpl repository = new MealsRepositoryImpl(
                MealsRemoteDataSourceImpl.getInstance(),
                MealsLocalDataSourceImpl.getInstance(this)
        );
        repository.setCurrentUserId(uid);

    }
}

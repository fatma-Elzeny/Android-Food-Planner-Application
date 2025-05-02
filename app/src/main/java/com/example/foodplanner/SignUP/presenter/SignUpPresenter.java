package com.example.foodplanner.SignUP.presenter;

public interface SignUpPresenter {
    void signUp(String email, String password, String confirmPassword, String userName, String country, String foodPref);
    void onDestroy();
}

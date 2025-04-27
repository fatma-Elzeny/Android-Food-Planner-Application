package com.example.foodplanner.SignUP.view;

public interface SignUpView {

    void showLoading();
    void hideLoading();
    void onSignUpSuccess();
    void onSignUpFailure(String message);
}

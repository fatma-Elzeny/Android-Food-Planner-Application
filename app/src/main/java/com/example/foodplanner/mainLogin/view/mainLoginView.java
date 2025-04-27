package com.example.foodplanner.mainLogin.view;

public interface mainLoginView {
    void showLoading();
    void hideLoading();
    void onLoginSuccess();
    void onLoginFailure(String message);
}

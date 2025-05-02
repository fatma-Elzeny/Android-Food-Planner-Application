package com.example.foodplanner.AuthLogin.view;

public interface AuthLoginView {

    void showLoginSuccess(String userName);
    void showLoginError(String message);
    void showLoading();
    void hideLoading();
}

package com.example.foodplanner.AuthLogin.presenter;

public interface AuthLoginPresenter {
    void login(String email, String password);
    void onDestroy();
}

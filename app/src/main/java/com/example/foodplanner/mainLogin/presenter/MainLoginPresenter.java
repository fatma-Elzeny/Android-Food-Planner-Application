package com.example.foodplanner.mainLogin.presenter;

public interface MainLoginPresenter {
    void loginWithGoogle(String idToken);
    void loginWithFacebook(String token);
    void loginAsGuest();
}

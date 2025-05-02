package com.example.foodplanner.SignUP.view;

public interface SignUpView {

    void showSignUpSuccess(String userName);
    void showSignUpError(String message);
    void showLoading();
    void hideLoading();
}

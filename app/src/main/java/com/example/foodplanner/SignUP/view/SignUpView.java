package com.example.foodplanner.SignUP.view;

public interface SignUpView {

    void showSignUpSuccess(String userName,String preferredCategory);
    void showSignUpError(String message);
    void showLoading();
    void hideLoading();
}

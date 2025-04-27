package com.example.foodplanner.SignUP.presenter;

import com.example.foodplanner.SignUP.view.SignUpView;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpPresenterImpl implements SignUpPresenter{

    private SignUpView view;
    private FirebaseAuth auth;

    public SignUpPresenterImpl(SignUpView view) {
        this.view = view;
        auth = FirebaseAuth.getInstance();
    }
    @Override
    public void signUp(String email, String password) {
        view.showLoading();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    view.hideLoading();
                    if (task.isSuccessful()) {
                        view.onSignUpSuccess();
                    } else {
                        view.onSignUpFailure(task.getException().getMessage());
                    }
                });
    }
    }


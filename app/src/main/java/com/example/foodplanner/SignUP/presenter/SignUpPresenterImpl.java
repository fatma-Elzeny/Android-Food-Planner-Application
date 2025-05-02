package com.example.foodplanner.SignUP.presenter;

import android.text.TextUtils;

import com.example.foodplanner.SignUP.view.SignUpView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpPresenterImpl implements SignUpPresenter{

    private SignUpView view;
    private FirebaseAuth auth;

    public SignUpPresenterImpl(SignUpView view) {
        this.view = view;
        this.auth = FirebaseAuth.getInstance();
    }

    @Override
    public void signUp(String email, String password, String confirmPassword, String userName, String country, String foodPref) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword) ||
                TextUtils.isEmpty(userName) || TextUtils.isEmpty(country) || TextUtils.isEmpty(foodPref)) {
            view.showSignUpError("All fields are required");
            return;
        }

        if (!password.equals(confirmPassword)) {
            view.showSignUpError("Passwords do not match");
            return;
        }

        view.showLoading();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    view.hideLoading();
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(userName)
                                    .build();

                            user.updateProfile(profileUpdates).addOnCompleteListener(profileTask -> {
                                if (profileTask.isSuccessful()) {
                                    view.showSignUpSuccess(userName);
                                } else {
                                    view.showSignUpError("Failed to update user profile");
                                }
                            });
                        }
                    } else {
                        view.showSignUpError(task.getException() != null ? task.getException().getMessage() : "Sign-up failed");
                    }
                });
    }

    @Override
    public void onDestroy() {
        view = null;
    }
}




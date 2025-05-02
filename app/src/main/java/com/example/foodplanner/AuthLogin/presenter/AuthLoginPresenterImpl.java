package com.example.foodplanner.AuthLogin.presenter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.foodplanner.AuthLogin.view.AuthLoginActivity;
import com.example.foodplanner.AuthLogin.view.AuthLoginView;
import com.example.foodplanner.home.view.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

public class AuthLoginPresenterImpl implements AuthLoginPresenter {

    private AuthLoginView view;
    private FirebaseAuth auth;

    public AuthLoginPresenterImpl(AuthLoginView view) {
        this.view = view;
        this.auth = FirebaseAuth.getInstance();
    }

    @Override
    
    public void login(String email, String password) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            view.showLoginError("Email and Password are required");
            return;
        }

        view.showLoading();

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    view.hideLoading();
                    if (task.isSuccessful() && task.getResult() != null) {
                        String userName = task.getResult().getUser().getDisplayName();
                        if (userName == null) userName = "User";
                        view.showLoginSuccess(userName);
                    } else {
                        String error = task.getException() != null ? task.getException().getMessage() : "Login failed";
                        view.showLoginError(error);
                    }
                });
    }

    @Override
    public void onDestroy() {
        view = null;
    }
}

package com.example.foodplanner.mainLogin.presenter;



import com.example.foodplanner.mainLogin.view.mainLoginView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;

public class MainLoginPresenterImpl implements MainLoginPresenter {

    private mainLoginView view;
    private FirebaseAuth auth;

    public MainLoginPresenterImpl(mainLoginView view) {
        this.view = view;
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void loginWithGoogle(String idToken) {
        view.showLoading();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    view.hideLoading();
                    if (task.isSuccessful()) {
                        handleUserPersistence();
                        view.onLoginSuccess();
                    } else {
                        view.onLoginFailure(task.getException().getMessage());
                    }
                });
    }

    @Override
    public void loginWithFacebook(String token) {
        view.showLoading();
        AuthCredential credential = FacebookAuthProvider.getCredential(token);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    view.hideLoading();
                    if (task.isSuccessful()) {
                        handleUserPersistence();
                        view.onLoginSuccess();
                    } else {
                        view.onLoginFailure(task.getException().getMessage());
                    }
                });
    }

    @Override
    public void checkExistingUser() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            view.persistUser(currentUser.getUid());
            view.onLoginSuccess();
        }
    }

    private void handleUserPersistence() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            view.persistUser(user.getUid());
        }
    }

}



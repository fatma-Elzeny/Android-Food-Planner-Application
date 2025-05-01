package com.example.foodplanner.profile.presenter;

import com.example.foodplanner.profile.view.ProfileView;

public class ProfilePresenterImpl implements ProfilePresenter {
    private ProfileView view;
    private final String userName; // passed from storage/auth

    public ProfilePresenterImpl(ProfileView view, String userName) {
        this.view = view;
        this.userName = userName;
    }

    @Override
    public void onViewLoaded() {
        view.showUserName(userName);
    }

    @Override
    public void onFavoritesClicked() {
        view.navigateToFavorites();
    }

    @Override
    public void onPlannerClicked() {
        view.navigateToPlanner();
    }

    @Override
    public void onLogoutClicked() {
        // Perform FirebaseAuth.getInstance().signOut(); if needed
        view.logout();
    }

    @Override
    public void onDestroy() {
        view = null;
    }
}


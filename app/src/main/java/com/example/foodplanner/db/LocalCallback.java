package com.example.foodplanner.db;

public interface LocalCallback<T> {
    void onSuccess(T data);     // Called when data is retrieved successfully
    void onFailure(String errorMessage); // Called on errors (e.g., no data found)
}
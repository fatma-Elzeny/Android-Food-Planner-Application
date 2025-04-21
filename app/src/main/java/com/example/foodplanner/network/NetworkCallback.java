package com.example.foodplanner.network;

public interface NetworkCallback<T> {
    void onSuccess(T data);
    void onFailure(String message);
}

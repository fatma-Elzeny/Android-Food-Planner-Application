package com.example.foodplanner.network;

import java.lang.reflect.InvocationTargetException;

public interface NetworkCallback<T> {
    void onSuccess(T data) ;
    void onFailure(String message);
}

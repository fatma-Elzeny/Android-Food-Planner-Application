package com.example.foodplanner.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryResponse {
    List<Category> categories;

    public List<Category> getCategoryResponse()
    {
        return categories;
    }
}

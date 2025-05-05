package com.example.foodplanner.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CountryResponse {
    @SerializedName("meals") // This must match the API response key
    private List<Country> areas;

    public List<Country> getAreas() {
        return areas;
    }
}
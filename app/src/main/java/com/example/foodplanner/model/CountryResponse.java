package com.example.foodplanner.model;

import java.util.List;

public class CountryResponse {
    private List<Country> meals;

    public CountryResponse() {
    }

    public CountryResponse(List<Country> meals) {
        this.meals = meals;
    }

    public List<Country> getMeals() {
        return meals;
    }

    public void setMeals(List<Country> meals) {
        this.meals = meals;
    }

    public static class Country {
        private String strArea;

        public Country() {
        }

        public Country(String strArea) {
            this.strArea = strArea;
        }

        // Getters and Setters
        public String getStrArea() {
            return strArea;
        }

        public void setStrArea(String strArea) {
            this.strArea = strArea;
        }
    }
}
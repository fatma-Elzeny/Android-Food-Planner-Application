package com.example.foodplanner.search.presenter;

public interface searchPresenter {
    public void getMealsByCategory(String category);
    public void getMealsById(String id);
    public void getMealsByName(String name);
    public void getMealsByCountry(String country);
    public void getMealsByIngredient(String ingredent);

}

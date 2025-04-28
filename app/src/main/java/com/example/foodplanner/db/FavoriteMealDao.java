package com.example.foodplanner.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.foodplanner.model.FavoriteMeal;

import java.util.List;

@Dao
public interface FavoriteMealDao {
    @Insert
    void insertFavoriteMeal(FavoriteMeal meal);

    @Delete
    void deleteFavoriteMeal(FavoriteMeal meal);

    @Query("SELECT * FROM favorite_meals WHERE idMeal = :id LIMIT 1")
    FavoriteMeal getFavoriteById(String id);

    @Query("SELECT * FROM favorite_meals")
    List<FavoriteMeal> getAllFavorites();
}

package com.example.foodplanner.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.foodplanner.model.FavoriteMeal;
import com.example.foodplanner.model.Meal;

import java.util.List;

@Dao
public interface FavoriteMealDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertFavoriteMeal(FavoriteMeal meal);

    @Delete
    void deleteFavoriteMeal(FavoriteMeal meal);

    @Query("SELECT * FROM favorite_meals WHERE userId = :userId")
    LiveData<List<FavoriteMeal>> getAllFavorites(String userId);
    @Query("SELECT * FROM favorite_meals WHERE idMeal = :mealId")
    LiveData<FavoriteMeal> getFavoriteById(String mealId);

}

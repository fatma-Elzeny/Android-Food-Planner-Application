package com.example.foodplanner.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.foodplanner.model.FavoriteMeal;
import com.example.foodplanner.model.Meal;
import com.example.foodplanner.model.PlannedMeal;


@Database(
        entities = {FavoriteMeal.class, PlannedMeal.class,Meal.class},
        version =4
)

public abstract class AppDataBase extends RoomDatabase {
    private static AppDataBase instance;

    public abstract MealDAO mealDao();
    public abstract FavoriteMealDao favoriteMealDao();
    public abstract PlannedMealDao plannedMealDao();

    public static  AppDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDataBase.class, "food_database")
                   .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}

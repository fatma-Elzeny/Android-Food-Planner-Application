package com.example.foodplanner.db;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

public abstract class AppDataBase extends RoomDatabase {
    private static AppDataBase instance;

    public abstract MealDAO mealDao();

    public static  AppDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDataBase.class, "meal_database")

                    .build();
        }
        return instance;
    }
}

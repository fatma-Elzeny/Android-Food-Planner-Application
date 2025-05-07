package com.example.foodplanner.db;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.foodplanner.model.FavoriteMeal;
import com.example.foodplanner.model.Meal;
import com.example.foodplanner.model.PlannedMeal;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MealsLocalDataSourceImpl implements MealsLocalDataSource{
    private FavoriteMealDao favoriteMealDao;
    private PlannedMealDao plannedMealDao;
    private static MealsLocalDataSource mealLocalDataSource=null;
    private LiveData<List<FavoriteMeal>> storedMeals;

    private LiveData<List<Meal>> plannedMeals;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public MealsLocalDataSourceImpl(Context context) {

        AppDataBase db =AppDataBase.getInstance(context.getApplicationContext());
        favoriteMealDao = db.favoriteMealDao();
        plannedMealDao = db.plannedMealDao();
        storedMeals=favoriteMealDao.getAllFavorites();
    }

    public  static MealsLocalDataSource getInstance(Context context)
    {
        if(mealLocalDataSource==null)
        {
            mealLocalDataSource=new MealsLocalDataSourceImpl(context);
        }
        return mealLocalDataSource;
    }

    @Override
    public LiveData<List<FavoriteMeal>> getAllFavorites() {
        return storedMeals;
    }

    @Override
    public void insertMeal(FavoriteMeal meal) {

        new Thread(){
            @Override
            public void run() {
                super.run();
                favoriteMealDao.insertFavoriteMeal(meal);
                Log.i("MealInsert", "Inserted meal: " + meal.getStrMeal());

            }
        }.start();
    }

    @Override
    public void deleteMeal(FavoriteMeal meal) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                favoriteMealDao.deleteFavoriteMeal(meal);
            }
        }.start();
    }

    @Override
    public LiveData<List<PlannedMeal>> getPlannedFood(String date) {
        return plannedMealDao.getMealsByDay(date);
    }

    @Override
    public void insertFoodPlan( PlannedMeal plannedMeal) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                plannedMealDao.insertPlannedMeal(plannedMeal);
            }
        }).start();
    }

    @Override
    public void deleteFoodPlan(PlannedMeal plannedMeal) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                plannedMealDao.deletePlannedMeal(plannedMeal);
            }
        }).start();

    }

    @Override
    public void updateFoodPlan(PlannedMeal plannedMeal) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                plannedMealDao.updatePlannedMeals(plannedMeal);
            }
        }).start();
    }


}


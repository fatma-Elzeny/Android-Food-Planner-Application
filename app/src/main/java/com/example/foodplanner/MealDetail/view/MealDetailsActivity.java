package com.example.foodplanner.MealDetail.view;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.MealDetail.model.IngredientItem;
import com.example.foodplanner.MealDetail.presenter.MealDetailPresenter;
import com.example.foodplanner.MealDetail.presenter.MealDetailPresenterImpl;
import com.example.foodplanner.R;
import com.example.foodplanner.Utils;
import com.example.foodplanner.model.FavoriteMeal;
import com.example.foodplanner.model.Meal;
import com.example.foodplanner.model.MealsRepositoryImpl;
import com.example.foodplanner.model.PlannedMeal;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MealDetailsActivity extends AppCompatActivity implements MealDetailsView {

    private MealDetailPresenter presenter;

    private ImageView mealImage;
    private TextView mealName;
    private TextView mealInstructions;
    private ProgressBar progressBar;

    private TextView categoryText, countryText;

    private ImageButton btnFavorite;
    private ImageButton btnPlanner;
    private Meal currentMeal;
    private YouTubePlayerView youtubePlayerView;

    RecyclerView ingredientsRecycler;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_details);
        youtubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youtubePlayerView);
        mealImage = findViewById(R.id.meal_image);
        mealName = findViewById(R.id.meal_name);
        mealInstructions = findViewById(R.id.meal_instructions);
        progressBar = findViewById(R.id.progress_bar);
        categoryText = findViewById(R.id.meal_category);
        countryText = findViewById(R.id.meal_country);
        btnPlanner = findViewById(R.id.btn_add_planner);
        ingredientsRecycler = findViewById(R.id.ingredients_recycler);
        ingredientsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        String mealId = getIntent().getStringExtra("MEAL_ID");
        presenter = new MealDetailPresenterImpl(this, new MealsRepositoryImpl(this));
        presenter.getMealDetails(mealId);

        btnFavorite = findViewById(R.id.btn_add_favorite);
        btnPlanner = findViewById(R.id.btn_add_planner);

        btnFavorite.setOnClickListener(v -> {
            if (currentMeal != null) {
                FavoriteMeal fav = new FavoriteMeal();
                fav.setIdMeal(currentMeal.getIdMeal());
                fav.setStrMeal(currentMeal.getStrMeal());
                fav.setStrMealThumb(currentMeal.getStrMealThumb());

                new MealsRepositoryImpl(this).insertFavorite(fav);
                Toast.makeText(this, "Added to Favorites", Toast.LENGTH_SHORT).show();
            }
        });

        btnPlanner.setOnClickListener(v -> {
            if (currentMeal != null) {
                showPlannerCalendarDialog();
            }
        });
    }

    private void showPlannerCalendarDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_planner_calendar, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        CalendarView calendarView = dialogView.findViewById(R.id.calendar_view);
        Button btnConfirm = dialogView.findViewById(R.id.btn_confirm);


        // Get current date
        Calendar calendar = Calendar.getInstance();
        long today = calendar.getTimeInMillis();

        // Set min date to today
        calendarView.setMinDate(today);

        // Set max date to end of current week (6 days from today)
        calendar.add(Calendar.DAY_OF_YEAR, 6);
        long maxDate = calendar.getTimeInMillis();
        calendarView.setMaxDate(maxDate);

        // Set initial selection to today
        calendarView.setDate(today, true, true);

        btnConfirm.setOnClickListener(v -> {
            long selectedDate = calendarView.getDate();
            Calendar selectedCalendar = Calendar.getInstance();
            selectedCalendar.setTimeInMillis(selectedDate);

            String dayName = new SimpleDateFormat("EEEE", Locale.getDefault()).format(selectedCalendar.getTime());
            String formattedDate = new SimpleDateFormat("MMM dd", Locale.getDefault()).format(selectedCalendar.getTime());

            if (currentMeal != null) {
                PlannedMeal planned = new PlannedMeal();
                planned.setMealId(currentMeal.getIdMeal());
                planned.setMealName(currentMeal.getStrMeal());
                planned.setDay(dayName);
                planned.setDate(formattedDate);
                planned.setMealThumb(currentMeal.getStrMealThumb());

                new MealsRepositoryImpl(this).insertPlannedMeal(planned);
                Toast.makeText(this, "Meal planned for " + dayName + " (" + formattedDate + ")",
                        Toast.LENGTH_SHORT).show();
            }

            dialog.dismiss();
        });

        dialog.show();
    }
    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showMealDetails(Meal meal) {
        this.currentMeal = meal;

        mealName.setText(meal.getStrMeal());
        mealInstructions.setText(meal.getStrInstructions());
        Glide.with(this).load(meal.getStrMealThumb()).into(mealImage);

        categoryText.setText("Category: " + meal.getStrCategory());
        countryText.setText("Country: " + meal.getStrArea());

        // ✅ Use helper method instead of reflection
        List<IngredientItem> ingredientItems = meal.getIngredientItemList();
        ingredientsRecycler.setAdapter(new IngredientAdapter(ingredientItems));

        // ✅ Safe video loading
        String videoId = extractYoutubeVideoId(meal.getStrYoutube());
        if (videoId != null) {
            youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(YouTubePlayer youTubePlayer) {
                    youTubePlayer.loadVideo(videoId, 0);
                }
            });
        }
    }


    private String extractYoutubeVideoId(String url) {
        Uri uri = Uri.parse(url);
        return uri.getQueryParameter("v");
    }




    @Override
    public void showError(String message) {
        Toast.makeText(this, "Error: " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        youtubePlayerView.release();
        presenter.onDestroy();
    }
}
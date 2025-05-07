package com.example.foodplanner.Favorite.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.R;
import com.example.foodplanner.home.view.OnMealClickListener;
import com.example.foodplanner.model.FavoriteMeal;
import com.example.foodplanner.model.Meal;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {
    private List<FavoriteMeal> meals;
    private final OnFavMealClickListener listener;

    public FavoritesAdapter(OnFavMealClickListener listener) {
        this.listener = listener;
    }

    public void setMeals(List<FavoriteMeal> meals) {
        this.meals = meals;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite_meal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavoriteMeal meal = meals.get(position);
        holder.title.setText(meal.getStrMeal());
        Glide.with(holder.itemView.getContext()).load(meal.getStrMealThumb()).into(holder.image);
        holder.itemView.setOnClickListener(v -> listener.onFavoriteMealClick(meal));
        holder.delete.setOnClickListener(v -> listener.onDeleteClick(meal));
    }

    @Override
    public int getItemCount() {
        return meals != null ? meals.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;

        ImageButton delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.img_meal);
            title = itemView.findViewById(R.id.txt_meal_name);
            delete = itemView.findViewById(R.id.btn_remove_favorite);
        }
    }
}

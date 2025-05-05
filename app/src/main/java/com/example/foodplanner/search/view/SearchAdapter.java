package com.example.foodplanner.search.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.R;
import com.example.foodplanner.model.Category;
import com.example.foodplanner.model.Country;
import com.example.foodplanner.model.Ingredient;
import com.example.foodplanner.model.Meal;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MealViewHolder> {
    private static final String TAG = "SearchAdapter";
    private Context context;
    private List<Meal> mealList;
    private OnItemClickListener onItemClickListener;

    public SearchAdapter(Context context, List<Meal> mealList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.mealList = mealList != null ? mealList : new ArrayList<>();
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_meal, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        if (mealList == null || mealList.isEmpty() || position >= mealList.size()) {
            Log.e(TAG, "Invalid position or empty meal list");
            return;
        }

        Meal item = mealList.get(position);
        if (item == null) {
            Log.e(TAG, "Meal at position " + position + " is null");
            return;
        }

        if (holder.mealName != null) {
            holder.mealName.setText(item.getStrMeal() != null ? item.getStrMeal() : "No name available");
        }

        if ("country".equals(item.getType())) {
            // Country flag loading
            Glide.with(context)
                    .load(item.getStrMealThumb())
                    .placeholder(R.drawable.ic_placeholder)
                    .into(holder.mealImage);
        } else if ("ingredient".equals(item.getType())) {
            // Ingredient image loading
            Glide.with(context)
                    .load(item.getStrMealThumb())
                    .placeholder(R.drawable.ic_placeholder)
                    .into(holder.mealImage);
        } else {
            // Default meal loading
            Glide.with(context)
                    .load(item.getStrMealThumb())
                    .placeholder(R.drawable.ic_placeholder)
                    .into(holder.mealImage);
        }

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(item);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mealList != null ? mealList.size() : 0;
    }

    public static class MealViewHolder extends RecyclerView.ViewHolder {
        TextView mealName;
        ImageView mealImage;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            mealName = itemView.findViewById(R.id.textMealName);
            mealImage = itemView.findViewById(R.id.imageMeal);

            // Add null check for debugging
            if (mealName == null) {
                throw new IllegalStateException("TextView with ID 'textMealName' not found in item_meal.xml");
            }
            if (mealImage == null) {
                throw new IllegalStateException("ImageView with ID 'imageMeal' not found in item_meal.xml");
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Meal meal);
    }
}

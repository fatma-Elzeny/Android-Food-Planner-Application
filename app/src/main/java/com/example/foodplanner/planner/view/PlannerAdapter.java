package com.example.foodplanner.planner.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.R;
import com.example.foodplanner.model.PlannedMeal;

import java.util.List;
import java.util.function.Consumer;

public class PlannerAdapter extends RecyclerView.Adapter<PlannerAdapter.PlannerViewHolder> {

    private List<PlannedMeal> meals;
    private final OnPlannerClickListener listener;

    public PlannerAdapter(List<PlannedMeal> meals, OnPlannerClickListener listener) {
        this.meals = meals;
        this.listener = listener;
    }

    public void updateMeals(List<PlannedMeal> newMeals) {
        this.meals = newMeals;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_planned_meal, parent, false);
        return new PlannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlannerViewHolder holder, int position) {
        PlannedMeal meal = meals.get(position);
        holder.title.setText(meal.getMealName());
        holder.date.setText(meal.getDate()); // Add this TextView in your ViewHolder

        holder.itemView.setOnClickListener(v -> listener.onMealClick(meal));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(meal));
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    static class PlannerViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView date; // Add this
        ImageButton btnDelete;

        PlannerViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_meal_title);
            date = itemView.findViewById(R.id.tv_meal_date); // Add this
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
    }


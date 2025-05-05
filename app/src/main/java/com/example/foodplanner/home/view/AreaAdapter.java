package com.example.foodplanner.home.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.R;
import com.example.foodplanner.model.Country;

import java.util.ArrayList;
import java.util.List;

// AreaAdapter.java
public class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.ViewHolder> {
    private List<Country> areas = new ArrayList<>();
    private OnAreaClickListener listener;

    public interface OnAreaClickListener {
        void onAreaClick(String area);
    }

    public void setOnAreaClickListener(OnAreaClickListener listener) {
        this.listener = listener;
    }

    public void setAreas(List<Country> areas) {
        this.areas = areas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_country, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Country area = areas.get(position);
        holder.areaName.setText(area.getStrArea());
        Glide.with(holder.itemView.getContext())
                .load(area.getStrFlag())
                .into(holder.areaFlag);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAreaClick(area.getStrArea());
            }
        });
    }

    @Override
    public int getItemCount() {
        return areas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView areaFlag;
        TextView areaName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            areaFlag = itemView.findViewById(R.id.img_area_flag);
            areaName = itemView.findViewById(R.id.txt_area_name);
        }
    }
}

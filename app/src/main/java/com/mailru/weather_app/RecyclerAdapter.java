package com.mailru.weather_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<String> data;
    private int selectedPosition = -1;
    private Context context;
    private OnItemClickListener listener;

    public RecyclerAdapter(ArrayList<String> data, OnItemClickListener listener) {
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_list, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.buttonView.setText(data.get(position));
        holder.buttonView.setOnClickListener(v -> {
            selectedPosition = position;
            notifyDataSetChanged();
        });
        highLightSelectedPosition(holder, position);
        if (selectedPosition != -1 && position == selectedPosition) {
            holder.listener.onClick(selectedPosition);
        }
    }

    private void highLightSelectedPosition(@NonNull ViewHolder holder, int position) {
        if (position == selectedPosition) {
            int color = ContextCompat.getColor(context, R.color.colorCitySky);
            holder.buttonView.setBackgroundColor(color);
        } else {
            int color = ContextCompat.getColor(context, android.R.color.transparent);
            holder.buttonView.setBackgroundColor(color);
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        Button buttonView;
        OnItemClickListener listener;

        ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            buttonView = itemView.findViewById(R.id.item_city);
            this.listener = listener;
        }
    }

}

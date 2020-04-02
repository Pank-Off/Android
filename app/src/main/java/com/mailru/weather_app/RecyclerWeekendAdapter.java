package com.mailru.weather_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerWeekendAdapter extends RecyclerView.Adapter<RecyclerWeekendAdapter.MyViewHolder> {

    private ArrayList<DataWeather> dataWeathers;

    public RecyclerWeekendAdapter(ArrayList<DataWeather> dataWeathers) {
        this.dataWeathers = dataWeathers;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.week_weather_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textViewDay.setText(dataWeathers.get(position).getDay());
        holder.imageView.setImageDrawable(dataWeathers.get(position).getImg());
        holder.textViewGrad.setText(dataWeathers.get(position).getGrad());
    }

    @Override
    public int getItemCount() {
        return dataWeathers == null ? 0 : dataWeathers.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewDay;
        ImageView imageView;
        TextView textViewGrad;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDay = itemView.findViewById(R.id.weekday);
            imageView = itemView.findViewById(R.id.imageView);
            textViewGrad = itemView.findViewById(R.id.gradView);
        }
    }
}

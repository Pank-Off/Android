package com.mailru.weather_app;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerWeekendAdapter extends RecyclerView.Adapter<RecyclerWeekendAdapter.MyViewHolder> {

    private Typeface weatherFont;
    private ArrayList<DataWeather> dataWeathers;

    public RecyclerWeekendAdapter(ArrayList<DataWeather> dataWeathers, Typeface weatherFont) {
        this.dataWeathers = dataWeathers;
        this.weatherFont = weatherFont;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.week_weather_item, parent, false);
        return new MyViewHolder(view, weatherFont);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textViewDay.setText(dataWeathers.get(position).getDay());
        holder.imageView.setText(dataWeathers.get(position).getImg());
        holder.textViewGrad.setText(dataWeathers.get(position).getGrad());
    }

    @Override
    public int getItemCount() {
        return dataWeathers == null ? 0 : dataWeathers.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDay;
        TextView imageView;
        TextView textViewGrad;
        Typeface weatherFont;

        MyViewHolder(@NonNull View itemView, Typeface weatherFont) {
            super(itemView);
            textViewDay = itemView.findViewById(R.id.weekday);
            imageView = itemView.findViewById(R.id.imageView);
            textViewGrad = itemView.findViewById(R.id.gradView);
            this.weatherFont = weatherFont;
            initFonts();
        }

        private void initFonts() {
            imageView.setTypeface(weatherFont);
        }

    }
}

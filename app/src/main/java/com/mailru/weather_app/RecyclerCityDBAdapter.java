package com.mailru.weather_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mailru.weather_app.room.CityEntity;
import com.mailru.weather_app.room.CitySource;

import java.util.List;

public class RecyclerCityDBAdapter extends RecyclerView.Adapter<RecyclerCityDBAdapter.ViewHolder> {
    private CitySource data;
    private int selectedPosition = -1;
    private Context context;
    private OnItemClickListener listener;

    public RecyclerCityDBAdapter(CitySource data, OnItemClickListener listener) {
        this.data = data;
        this.listener = listener;
    }

    public int selectBtn(String selected_city) {
        CityEntity cityEntity = new CityEntity();
        cityEntity.city = selected_city;
        data.addCity(cityEntity);
        for (int i = 0; i < data.getCities().size(); i++) {
            if (selected_city.equals(data.getCities().get(i).city)) {
                selectedPosition = i;
                break;
            }
        }
        return selectedPosition;
    }

    @NonNull
    @Override
    public RecyclerCityDBAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_list, parent, false);
        return new RecyclerCityDBAdapter.ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerCityDBAdapter.ViewHolder holder, int position) {
        // Заполнение данными записи на экране

        List<CityEntity> cities = data.getCities();
        holder.buttonView.setText(cities.get(position).city);
        holder.buttonView.setOnClickListener(v -> {
            selectedPosition = position;
            notifyDataSetChanged();
        });
        highLightSelectedPosition(holder, position);
        if (selectedPosition != -1 && position == selectedPosition) {
            holder.listener.onClick(selectedPosition);
        }
    }

    private void highLightSelectedPosition(@NonNull RecyclerCityDBAdapter.ViewHolder holder, int position) {
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
        return data == null ? 0 : data.getCities().size();
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

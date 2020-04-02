package com.mailru.weather_app.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mailru.weather_app.DataWeather;
import com.mailru.weather_app.R;
import com.mailru.weather_app.RecyclerWeekendAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class WeatherFragment extends Fragment {
    private TextView textView;
    private CheckBox details_checkbox;
    private TextView detailsText;
    private LinearLayout weatherLay;
    private ArrayList<DataWeather> dataWeathers = new ArrayList<>();
    private ArrayList<DataWeather> dataWeathersHour = new ArrayList<>();

    static WeatherFragment create(int index) {
        WeatherFragment f = new WeatherFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);
        return f;
    }

    int getIndex() {
        return Objects.requireNonNull(getArguments()).getInt("index", 0);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.weather_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initViews(view);
        setTextCity();
        setOnCheckedChangeListener();
        setOnSearchLayClickListener();
    }

    private void initData() {
        dataWeathers.add(new DataWeather(getResources().getString(R.string.monday),
                getResources().getDrawable(R.drawable.rainy), getResources().getString(R.string._8_u00b0)));
        dataWeathers.add(new DataWeather(getResources().getString(R.string.tuesday),
                getResources().getDrawable(R.drawable.cloud_small), getResources().getString(R.string._7_u00b0)));
        dataWeathers.add(new DataWeather(getResources().getString(R.string.wednesday),
                getResources().getDrawable(R.drawable.sunny), getResources().getString(R.string._13_u00b0)));
        dataWeathers.add(new DataWeather(getResources().getString(R.string.thursrday),
                getResources().getDrawable(R.drawable.cloud_small), getResources().getString(R.string._6_u00b0)));
        dataWeathers.add(new DataWeather(getResources().getString(R.string.friday),
                getResources().getDrawable(R.drawable.cloud_small), getResources().getString(R.string._6_u00b0)));
        dataWeathers.add(new DataWeather(getResources().getString(R.string.saturday),
                getResources().getDrawable(R.drawable.rainy), getResources().getString(R.string._4_u00b0)));
        dataWeathers.add(new DataWeather(getResources().getString(R.string.sunday),
                getResources().getDrawable(R.drawable.wind), getResources().getString(R.string._6_u00b0)));


        dataWeathersHour.add(new DataWeather(getResources().getString(R.string._15_00),
                getResources().getDrawable(R.drawable.cloud_small), getResources().getString(R.string._8_u00b0)));
        dataWeathersHour.add(new DataWeather(getResources().getString(R.string._16_00),
                getResources().getDrawable(R.drawable.cloud_small), getResources().getString(R.string._7_u00b0)));
        dataWeathersHour.add(new DataWeather(getResources().getString(R.string._17_00),
                getResources().getDrawable(R.drawable.sunny), getResources().getString(R.string._7_u00b0)));
        dataWeathersHour.add(new DataWeather(getResources().getString(R.string._18_00),
                getResources().getDrawable(R.drawable.rainy), getResources().getString(R.string._6_u00b0)));
        dataWeathersHour.add(new DataWeather(getResources().getString(R.string._19_00),
                getResources().getDrawable(R.drawable.rainy), getResources().getString(R.string._6_u00b0)));
        dataWeathersHour.add(new DataWeather(getResources().getString(R.string._20_00),
                getResources().getDrawable(R.drawable.rainy), getResources().getString(R.string._4_u00b0)));
        dataWeathersHour.add(new DataWeather(getResources().getString(R.string._21_00),
                getResources().getDrawable(R.drawable.wind), getResources().getString(R.string._6_u00b0)));
    }

    private void setTextCity() {

        ArrayList<String> cities = CitiesFragment.city;
        textView.setText(cities.get(getIndex()));

    }

    private void initViews(View view) {
        textView = view.findViewById(R.id.city);
        details_checkbox = view.findViewById(R.id.details_checkbox);
        detailsText = view.findViewById(R.id.detailsText);
        weatherLay = view.findViewById(R.id.firstLay);

        RecyclerView recyclerViewWeek = view.findViewById(R.id.weekendRecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewWeek.setLayoutManager(linearLayoutManager);
        recyclerViewWeek.setAdapter(new RecyclerWeekendAdapter(dataWeathers));

        RecyclerView recyclerViewHour = view.findViewById(R.id.HourRecycler);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewHour.setLayoutManager(linearLayoutManager2);
        recyclerViewHour.setAdapter(new RecyclerWeekendAdapter(dataWeathersHour));
    }


    private void setOnCheckedChangeListener() {
        details_checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                String details_string = "Wind: 5 m/s\nPressure: 750 mm of mercury";
                detailsText.setVisibility(View.VISIBLE);
                detailsText.setText(details_string);
            } else {
                detailsText.setVisibility(View.GONE);
            }
        });
    }

    private void setOnSearchLayClickListener() {
        weatherLay.setOnClickListener(v -> {
            String url = getString(R.string.url_yandex_weather);
            Uri uri = Uri.parse(url);
            Intent linkIntent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(linkIntent);
        });

    }
}


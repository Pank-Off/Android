package com.mailru.weather_app.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mailru.weather_app.DataWeather;
import com.mailru.weather_app.ParseJsonClass;
import com.mailru.weather_app.R;
import com.mailru.weather_app.RecyclerWeekendAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WeatherFragment extends Fragment {

    private ParseJsonClass parseJsonClass;
    private Typeface weatherFont;
    private TextView textView;
    private TextView weatherTodayView;
    private CheckBox details_checkbox;
    private TextView detailsText;
    private TextView weatherText;
    private TextView iconWeather;
    private TextView currentTime;
    private RecyclerView recyclerViewWeek;
    private ArrayList<DataWeather> dataWeathers;
    private String currentCity;
    private final static String LOG_TAG = WeatherFragment.class.getSimpleName();

    static WeatherFragment create(int index, String currentCity) {
        WeatherFragment f = new WeatherFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        args.putString("index", currentCity);
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

        initViews(view);
        currentCity = Objects.requireNonNull(getArguments()).getString("index");
        parseJsonClass = new ParseJsonClass(currentCity, getContext());
        initFonts();
        updateWeatherWeekData();
        updateWeatherTodayData();
        Log.d(getClass().getSimpleName() + " - LifeCycle", "AfterEverything");
    }

    private void initFonts() {
        weatherFont = Typeface.createFromAsset(Objects.requireNonNull(getActivity()).getAssets(), "fonts/weather.ttf");
        iconWeather.setTypeface(weatherFont);
    }


    private void updateWeatherTodayData() {

        List<String> list = parseJsonClass.updateWeatherTodayData();
        //Это заглушка на время, чтобы пережить поворот!
        if(list.size()==0){
            for(int i = 0;i<=5;i++){
                list.add(null);
            }
        }
        textView.setText(list.get(0));
        currentTime.setText(list.get(1));
        setDetails(list.get(2));
        weatherTodayView.setText(list.get(3));
        weatherText.setText(list.get(4));
        iconWeather.setText(list.get(5));
    }

    private void updateWeatherWeekData() {

        dataWeathers = parseJsonClass.updateWeatherWeekData();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewWeek.setLayoutManager(linearLayoutManager);
        recyclerViewWeek.setAdapter(new RecyclerWeekendAdapter(dataWeathers, weatherFont));

    }

    private void setDetails(String details) {
        details_checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                detailsText.setText(details);
                detailsText.setVisibility(View.VISIBLE);
            } else {
                detailsText.setVisibility(View.GONE);
            }
        });
    }

    private void initViews(View view) {
        textView = view.findViewById(R.id.city);
        details_checkbox = view.findViewById(R.id.details_checkbox);
        detailsText = view.findViewById(R.id.detailsText);
        weatherTodayView = view.findViewById(R.id.weatherTodayView);
        weatherText = view.findViewById(R.id.weatherText);
        iconWeather = view.findViewById(R.id.icon_weather);
        currentTime = view.findViewById(R.id.currentTime);
        recyclerViewWeek = view.findViewById(R.id.weekendRecycler);
    }

}


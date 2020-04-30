package com.mailru.weather_app.fragments;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mailru.weather_app.DataWeather;
import com.mailru.weather_app.OpenWeatherRepo;
import com.mailru.weather_app.ParseJsonClass;
import com.mailru.weather_app.ParseJsonRestClass;
import com.mailru.weather_app.R;
import com.mailru.weather_app.RecyclerWeekendAdapter;
import com.mailru.weather_app.entities.WeatherRequestRestModel;

import androidx.preference.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherFragment extends Fragment {

    private ParseJsonClass parseJsonClass;
    private ParseJsonRestClass parseJsonRestClass;
    private Typeface weatherFont;
    private TextView textCityView;
    private TextView weatherTodayView;
    private CheckBox details_checkbox;
    private TextView detailsText;
    private TextView weatherText;
    private TextView iconWeather;
    private TextView currentTime;
    private RecyclerView recyclerViewWeek;
    private ArrayList<DataWeather> dataWeathers;
    private String currentCity;
    private SharedPreferences defalutPrefs;
    private String savePrefKey = "savePref";

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
        return requireArguments().getInt("index", 0);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(getClass().getSimpleName() + " - LifeCycle", "onCreateView");
        return inflater.inflate(R.layout.weather_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        defalutPrefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        try {
            currentCity = requireArguments().getString("index", null);
        } catch (IllegalStateException e) {
            currentCity = readFromPreference(defalutPrefs);
        }

        parseJsonClass = new ParseJsonClass(currentCity, getContext());
        parseJsonRestClass = new ParseJsonRestClass(currentCity, getContext());
        initFonts();
        updateWeatherWeekData();
        // updateWeatherTodayData();
        //updateWeatherTodayDataRest();
        updateWeatherData();
    }

    private void saveToPreference(SharedPreferences preferences) {

        SharedPreferences.Editor editor = preferences.edit();
        String text = currentCity;
        editor.putString(savePrefKey, text);
        editor.apply();
    }

    private String readFromPreference(SharedPreferences preferences) {
        return preferences.getString(savePrefKey, "Moscow");

    }

    private void initFonts() {
        weatherFont = Typeface.createFromAsset(requireActivity().getAssets(), "fonts/weather.ttf");
        iconWeather.setTypeface(weatherFont);
    }


    private void updateWeatherTodayData() {

        List<String> list = parseJsonClass.updateWeatherTodayData();
        //Это заглушка на время, чтобы пережить поворот!
        if (list.size() == 0) {
            for (int i = 0; i <= 5; i++) {
                list.add(null);
            }
        }
        textCityView.setText(list.get(0));
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


    private void updateWeatherTodayDataRest() {
        List<String> list = parseJsonRestClass.updateWeatherTodayData();

        //Это заглушка на время, чтобы пережить поворот!
        if (list.size() == 0) {
            for (int i = 0; i <= 5; i++) {
                list.add(null);
            }
        }
        textCityView.setText(list.get(0));
        currentTime.setText(list.get(1));
        setDetails(list.get(2));
        weatherTodayView.setText(list.get(3));
        weatherText.setText(list.get(4));
        iconWeather.setText(list.get(5));

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
        textCityView = view.findViewById(R.id.city);
        details_checkbox = view.findViewById(R.id.details_checkbox);
        detailsText = view.findViewById(R.id.detailsText);
        weatherTodayView = view.findViewById(R.id.weatherTodayView);
        weatherText = view.findViewById(R.id.weatherText);
        iconWeather = view.findViewById(R.id.icon_weather);
        currentTime = view.findViewById(R.id.currentTime);
        recyclerViewWeek = view.findViewById(R.id.weekendRecycler);
    }


    private void updateWeatherData() {
        OpenWeatherRepo.getSingleton().getAPI().loadWeather(currentCity,
                "8e4427442db813060908d56bee675cb7", "metric")
                .enqueue(new Callback<WeatherRequestRestModel>() {
                    @Override
                    public void onResponse(@NonNull Call<WeatherRequestRestModel> call,
                                           @NonNull Response<WeatherRequestRestModel> response) {
                        Log.d("response.body", response.body() + "");
                        Log.d("response.isSuccessful", response.isSuccessful() + "");
                        if (response.body() != null && response.isSuccessful()) {
                            renderWeather(response.body());

                        } else {
                            //Похоже, код у нас не в диапазоне [200..300) и случилась ошибка
                            //обрабатываем ее
                            Log.d("code ", response.code() + "");
                            if (response.code() == 500) {
                                Log.d("code 500", "Internal Server Error");
                                //ой, случился Internal Server Error. Решаем проблему
                            } else if (response.code() == 401) {
                                Log.d("code 401", "Auto");
                                //не авторизованы, что-то с этим делаем.
                                //например, открываем страницу с логинкой
                            }// и так далее
                        }
                    }

                    //сбой при интернет подключении
                    @Override
                    public void onFailure(Call<WeatherRequestRestModel> call, Throwable t) {
//                        Toast.makeText(getContext(), getString(R.string.unavailable_network),
//                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void renderWeather(WeatherRequestRestModel model) {

        textCityView.setText(model.name);
        currentTime.setText(setCurrentTime());
        String details = "Pressure: " + model.main.pressure + " hPa\nHumidity: " + model.main.humidity + "%";
        setDetails(details);
        String temp = setCurrentTemp(model.main.temp);
        weatherTodayView.setText(temp);
        weatherText.setText(model.weather[0].main);
        String icon = setWeatherIcon(model.weather[0].id, model.sys.sunrise * 1000, model.sys.sunset * 1000);
        iconWeather.setText(icon);
    }

    private String setCurrentTime() {
        Date date = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("E MM.dd hh:mm", Locale.US);
        return formatForDateNow.format(date);
    }

    private String setCurrentTemp(float temp) {
        String currentTextText = String.format(Locale.getDefault(), "%.0f",
                temp) + "\u2103";
        Log.d("currentText", currentTextText);
        return currentTextText;

    }

    private String setWeatherIcon(int actualId, long sunrise, long sunset) {
        int id = actualId / 100;
        String icon = "";

        if (actualId == 800) {
            long currentTime = new Date().getTime();
            if (currentTime >= sunrise && currentTime < sunset) {
                icon = getString(R.string.weather_sunny);
            } else {
                icon = getString(R.string.weather_clear_night);
            }
        } else {
            switch (id) {
                case 2: {
                    icon = getString(R.string.weather_thunder);
                    break;
                }
                case 3: {
                    icon = getString(R.string.weather_drizzle);
                    break;
                }
                case 5: {
                    icon = getString(R.string.weather_rainy);
                    break;
                }
                case 6: {
                    icon = getString(R.string.weather_snowy);
                    break;
                }
                case 7: {
                    icon = getString(R.string.weather_foggy);
                    break;
                }
                case 8: {
                    icon = getString(R.string.weather_cloudy);
                    break;
                }
            }
        }
        return icon;
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(getClass().getSimpleName() + " - LifeCycle", "onStopFrag");
        saveToPreference(defalutPrefs);
    }

}


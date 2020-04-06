package com.mailru.weather_app.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mailru.weather_app.DataWeather;
import com.mailru.weather_app.R;
import com.mailru.weather_app.RecyclerWeekendAdapter;
import com.mailru.weather_app.WeatherDataLoader;
import com.mailru.weather_app.WeatherDataWeekendLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class WeatherFragment extends Fragment {

    private Typeface weatherFont;
    private TextView textView;
    private TextView weatherTodayView;
    private CheckBox details_checkbox;
    private TextView detailsText;
    private TextView weatherText;
    private TextView iconWeather;
    private TextView currentTime;
    private RecyclerView recyclerViewWeek;
    private ArrayList<DataWeather> dataWeathers = new ArrayList<>();
    private final Handler handler = new Handler();
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
        updateWeatherWeekData();
        updateWeatherTodayData();
        initFonts();
    }

    private void initFonts() {
        weatherFont = Typeface.createFromAsset(Objects.requireNonNull(getActivity()).getAssets(), "fonts/weather.ttf");
        iconWeather.setTypeface(weatherFont);
    }


    private void updateWeatherTodayData() {
        currentCity = Objects.requireNonNull(getArguments()).getString("index");
        new Thread() {
            @Override
            public void run() {
                final JSONObject jsonObject = WeatherDataLoader.getJSONData(currentCity);
                if (jsonObject == null) {
                    handler.post(() ->
                            Toast.makeText(getContext(), R.string.place_not_found, Toast.LENGTH_LONG).show());
                } else {
                    handler.post(() -> renderWeather(jsonObject));
                }
            }
        }.start();
    }

    private void updateWeatherWeekData() {
        currentCity = Objects.requireNonNull(getArguments()).getString("index");
        new Thread() {
            @Override
            public void run() {
                final JSONObject jsonObject = WeatherDataWeekendLoader.getJSONData(currentCity);
                if (jsonObject == null) {
                    handler.post(() ->
                            Toast.makeText(getContext(), R.string.place_not_found, Toast.LENGTH_LONG).show());
                } else {
                    handler.post(() -> renderWeekWeather(jsonObject));
                }
            }
        }.start();
    }

    private void renderWeekWeather(JSONObject jsonObject) {
        Log.d(LOG_TAG, "json: " + jsonObject.toString());
        try {
            for (int i = 0; i < 5; i++) {
                JSONObject details = jsonObject.getJSONArray("list").getJSONObject(i).getJSONArray("weather").getJSONObject(0);
                JSONObject main = jsonObject.getJSONArray("list").getJSONObject(i).getJSONObject("main");

                String dt_txt = jsonObject.getJSONArray("list").getJSONObject(i).getString("dt_txt");
                String[] s = dt_txt.split(" ");

                dataWeathers.add(new DataWeather(s[1],
                        setWeatherIcon(details.getInt("id"),
                                0,
                                0), setCurrentTemp(main)));
            }
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            recyclerViewWeek.setLayoutManager(linearLayoutManager);
            recyclerViewWeek.setAdapter(new RecyclerWeekendAdapter(dataWeathers, weatherFont));
        } catch (Exception exc) {
            exc.printStackTrace();
            Log.e(LOG_TAG, "One or more fields not found in the JSON data");
        }
    }

    private void renderWeather(JSONObject jsonObject) {
        Log.d(LOG_TAG, "json: " + jsonObject.toString());
        try {
            JSONObject details = jsonObject.getJSONArray("weather").getJSONObject(0);
            JSONObject main = jsonObject.getJSONObject("main");

            setPlaceName(jsonObject);
            setCurrentTime();
            setDetails(main);

            String currentTextText = setCurrentTemp(main);
            weatherTodayView.setText(currentTextText);

            setTextWeather(details.getString("main"));

            String icon = setWeatherIcon(details.getInt("id"),
                    jsonObject.getJSONObject("sys").getLong("sunrise") * 1000,
                    jsonObject.getJSONObject("sys").getLong("sunset") * 1000);
            iconWeather.setText(icon);

        } catch (Exception exc) {
            exc.printStackTrace();
            Log.e(LOG_TAG, "One or more fields not found in the JSON data");
        }
    }

    private void setCurrentTime() {
        Date date = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("E MM.dd hh:mm", Locale.US);
        currentTime.setText(formatForDateNow.format(date));
    }

    private void setTextWeather(String main) {
        weatherText.setText(main);
    }

    private void setDetails(JSONObject main) {
        details_checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                StringBuilder builder = new StringBuilder();
                try {
                    builder.append("Pressure: ").append(main.getDouble("pressure")).append(" hPa\nHumidity: ").append(main.getDouble("humidity")).append("%");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                detailsText.setText(builder.toString());
                detailsText.setVisibility(View.VISIBLE);
            } else {
                detailsText.setVisibility(View.GONE);
            }
        });
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


    private void setPlaceName(JSONObject jsonObject) throws JSONException {
        String cityText = jsonObject.getString("name").toUpperCase() + ", "
                + jsonObject.getJSONObject("sys").getString("country");
        textView.setText(cityText);
    }

    private String setCurrentTemp(JSONObject main) throws JSONException {
        String currentTextText = String.format(Locale.getDefault(), "%.0f",
                main.getDouble("temp")) + "\u2103";
        Log.d("currentText", currentTextText);
        return currentTextText;

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


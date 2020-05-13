package com.mailru.weather_app;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.mailru.weather_app.fragments.WeatherFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static java.security.AccessController.getContext;

public class ParseJsonClass {

    private Context context;
    private String currentCity;
    private static final Handler handler = new Handler();

    private ArrayList<String> infoToday = new ArrayList<>();
    private ArrayList<DataWeather> infoWeek = new ArrayList<>();
    private final static String LOG_TAG = WeatherFragment.class.getSimpleName();

    public ParseJsonClass(String currentCity, Context context) {
        this.context = context;
        this.currentCity = currentCity;
    }

    public ArrayList<String> updateWeatherTodayData() {

        Thread t = new Thread() {
            @Override
            public void run() {
                final JSONObject jsonObject = WeatherDataLoader.getJSONData(currentCity);
                if (jsonObject == null) {
                    handler.post(() -> {
                        if (getContext() != null) {
                            Toast.makeText(context, R.string.place_not_found, Toast.LENGTH_LONG).show();
                        }
                    });

                } else {
                    renderWeather(jsonObject);
                }
            }
        };
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return infoToday;
    }

    private void renderWeather(JSONObject jsonObject) {
        Log.d(LOG_TAG, "json: " + jsonObject.toString());

        try {
            JSONObject details = jsonObject.getJSONArray("weather").getJSONObject(0);
            JSONObject main = jsonObject.getJSONObject("main");

            infoToday.add(setPlaceName(jsonObject));
            infoToday.add(setCurrentTime());
            infoToday.add(setDetails(main));

            infoToday.add(setCurrentTemp(main));

            infoToday.add(details.getString("main"));

            String icon = setWeatherIcon(details.getInt("id"),
                    jsonObject.getJSONObject("sys").getLong("sunrise") * 1000,
                    jsonObject.getJSONObject("sys").getLong("sunset") * 1000);
            infoToday.add(icon);


        } catch (Exception exc) {
            exc.printStackTrace();
            Log.e(LOG_TAG, "One or more fields not found in the JSON data");
        }
    }


    public ArrayList<DataWeather> updateWeatherWeekData() {
        Thread t = new Thread() {
            @Override
            public void run() {
                final JSONObject jsonObject = WeatherDataWeekendLoader.getJSONData(currentCity);
                if (jsonObject == null) {
                    handler.post(() ->
                            Log.d(LOG_TAG, "json: "));
                } else {
                    renderWeekWeather(jsonObject);
                }
            }
        };
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return infoWeek;
    }


    private void renderWeekWeather(JSONObject jsonObject) {
        Log.d(LOG_TAG, "json: " + jsonObject.toString());
        try {
            for (int i = 0; i < 5; i++) {
                JSONObject details = jsonObject.getJSONArray("list").getJSONObject(i).getJSONArray("weather").getJSONObject(0);
                JSONObject main = jsonObject.getJSONArray("list").getJSONObject(i).getJSONObject("main");

                String dt_txt = jsonObject.getJSONArray("list").getJSONObject(i).getString("dt_txt");
                String[] s = dt_txt.split(" ");

                infoWeek.add(new DataWeather(s[1],
                        setWeatherIcon(details.getInt("id"),
                                0, 0), setCurrentTemp(main)));

            }
        } catch (Exception exc) {
            exc.printStackTrace();
            Log.e(LOG_TAG, "One or more fields not found in the JSON data");
        }
    }

    private String setPlaceName(JSONObject jsonObject) throws JSONException {
        return jsonObject.getString("name").toUpperCase() + ", "
                + jsonObject.getJSONObject("sys").getString("country");
    }

    private String setCurrentTime() {
        Date date = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("E MM.dd hh:mm", Locale.US);
        return formatForDateNow.format(date);
    }

    private String setDetails(JSONObject main) {

        StringBuilder builder = new StringBuilder();
        try {
            builder.append("Pressure: ").append(main.getDouble("pressure")).append(" hPa\nHumidity: ").append(main.getDouble("humidity")).append("%");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    private String setCurrentTemp(JSONObject main) throws JSONException {
        String currentTextText = String.format(Locale.getDefault(), "%.0f",
                main.getDouble("temp")) + "\u2103";
        Log.d("currentText", currentTextText);
        return currentTextText;

    }

    private String setWeatherIcon(int actualId, long sunrise, long sunset) {
        int id = actualId / 100;
        String icon = "";

        if (actualId == 800) {
            long currentTime = new Date().getTime();
            if (currentTime >= sunrise && currentTime < sunset) {
                icon = context.getString(R.string.weather_sunny);
            } else {
                icon = context.getString(R.string.weather_clear_night);
            }
        } else {
            switch (id) {
                case 2: {
                    icon = context.getString(R.string.weather_thunder);
                    break;
                }
                case 3: {
                    icon = context.getString(R.string.weather_drizzle);
                    break;
                }
                case 5: {
                    icon = context.getString(R.string.weather_rainy);
                    break;
                }
                case 6: {
                    icon = context.getString(R.string.weather_snowy);
                    break;
                }
                case 7: {
                    icon = context.getString(R.string.weather_foggy);
                    break;
                }
                case 8: {
                    icon = context.getString(R.string.weather_cloudy);
                    break;
                }
            }
        }
        return icon;
    }

}

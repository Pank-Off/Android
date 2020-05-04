package com.mailru.weather_app;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.mailru.weather_app.entities.WeatherRequestRestModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParseJsonRestClass {

    private static final Handler handler = new Handler();
    private String currentCity;
    private Context context;
    private ArrayList<String> infoToday = new ArrayList<>();

    public ParseJsonRestClass(String currentCity, Context context) {
        this.currentCity = currentCity;
        this.context = context;
    }

    public ArrayList<String> updateWeatherTodayData() {
        Log.d("asds", infoToday.size() + "");
            OpenWeatherRepo.getSingleton().getAPI().loadWeather(currentCity,
                    "8e4427442db813060908d56bee675cb7", "metric")
                    .enqueue(new Callback<WeatherRequestRestModel>() {
                        @Override
                        public void onResponse(@NonNull Call<WeatherRequestRestModel> call,
                                               @NonNull Response<WeatherRequestRestModel> response) {
                            if (response.body() != null && response.isSuccessful()) {
                                renderWeather(response.body());
                            } else {
                                //Похоже, код у нас не в диапазоне [200..300) и случилась ошибка
                                //обрабатываем ее
                                if (response.code() == 500) {
                                    //ой, случился Internal Server Error. Решаем проблему
                                } else if (response.code() == 401) {
                                    //не авторизованы, что-то с этим делаем.
                                    //например, открываем страницу с логинкой
                                }// и так далее
                            }
                            Log.d("sizeInto", infoToday.size() + "");
                        }

                        //сбой при интернет подключении
                        @Override
                        public void onFailure(Call<WeatherRequestRestModel> call, Throwable t) {
                            Toast.makeText(context, context.getString(R.string.unavailable_network),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

        Log.d("sizeOut", infoToday.size() + "");
        return infoToday;
    }

    private ArrayList<String> renderWeather(WeatherRequestRestModel model) {
        Log.d("asds", infoToday.size() + "");
        infoToday.add(model.name);
        infoToday.add(setCurrentTime());
        String details = "Pressure: " + model.main.pressure + " hPa\nHumidity: " + model.main.humidity + "%";
        infoToday.add(details);
        String temp = setCurrentTemp(model.main.temp);
        infoToday.add(temp);
        infoToday.add(model.weather[0].main);
        String icon = setWeatherIcon(model.weather[0].id, model.sys.sunrise * 1000, model.sys.sunset * 1000);
        infoToday.add(icon);
        return infoToday;
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

package com.mailru.weather_app;

import android.graphics.drawable.Drawable;

public class DataWeather {

    private String day;
    private Drawable img;
    private String grad;

    public DataWeather(String day, Drawable img, String grad) {
        this.day = day;
        this.img = img;
        this.grad = grad;
    }

    String getDay() {
        return day;
    }

    Drawable getImg() {
        return img;
    }

    String getGrad() {
        return grad;
    }

}

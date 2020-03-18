package com.mailru.weather_app;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

class DataWeather implements Serializable {

    private static DataWeather dataWeather;
    private String grad;
    private transient Drawable img;

    private DataWeather(Drawable img, String grad) {
        this.grad = grad;
        this.img = img;
    }

    String getGrad() {
        return grad;
    }

    Drawable getImg() {
        return img;
    }

    static DataWeather getInstance(Drawable img, String grad) {
        if (dataWeather == null)
            dataWeather = new DataWeather(img, grad);
        return dataWeather;
    }
}

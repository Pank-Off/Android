package com.mailru.weather_app;

public class DataWeather {

    private String day;
    private String img;
    private String grad;

    public DataWeather(String day, String img, String grad) {
        this.day = day;
        this.img = img;
        this.grad = grad;
    }

    String getDay() {
        return day;
    }

    String getImg() {
        return img;
    }

    String getGrad() {
        return grad;
    }

}

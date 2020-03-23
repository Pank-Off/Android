package com.mailru.weather_app;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class MainActivity extends BaseActivity {

    private final static String EXTRA = "EXTRA";
    private ImageView settings_img;
    private TextView weatherTomorrowView;
    private TextView weatherTodayView;
    private TextView city;
    private Button searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        String currentCity = getIntent().getStringExtra(CityActivity.CITY_KEY);
        String currentGrad = getIntent().getStringExtra(CityActivity.GRAD_KEY);
        if(currentCity!=null && currentGrad!=null) {
            city.setText(currentCity);
            weatherTodayView.setText(currentGrad);
        }

        setOnImageClickListener();
        setOnSearchBtnClickListener();
    }


    private void initViews() {
        settings_img = findViewById(R.id.settings);
        weatherTomorrowView = findViewById(R.id.weatherTomorrow);
        weatherTodayView = findViewById(R.id.weatherToday);
        city = findViewById(R.id.city);
        searchBtn = findViewById(R.id.searchBtn);
    }

    private void setOnImageClickListener() {
        settings_img.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });
    }

    private void setOnSearchBtnClickListener() {
        searchBtn.setOnClickListener(v->{
            String url = getString(R.string.url_yandex_weather);
            Uri address = Uri.parse(url);
            Intent linkIntent = new Intent(Intent.ACTION_VIEW, address);
            startActivity(linkIntent);
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        super.onSaveInstanceState(outState);
        String weatherTomorrow = weatherTomorrowView.getText().toString();
        Drawable imgWeather = weatherTomorrowView.getResources().getDrawable(R.drawable.night);
        outState.putSerializable(EXTRA, DataWeather.getInstance(imgWeather, weatherTomorrow));

        //Toast.makeText(getApplicationContext(),"onSaveInstanceState",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        DataWeather dataWeather = (DataWeather) savedInstanceState.getSerializable(EXTRA);
        assert dataWeather != null;
        String weatherToday = dataWeather.getGrad();
        Drawable img = dataWeather.getImg();
        int h = img.getIntrinsicWidth();
        int w = img.getIntrinsicHeight();
        img.setBounds(0, 0, w, h);
        weatherTodayView.setText(weatherToday);
        weatherTodayView.setCompoundDrawables(img, null, null, null);
    }
}

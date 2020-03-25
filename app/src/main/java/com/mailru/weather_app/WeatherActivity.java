package com.mailru.weather_app;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.mailru.weather_app.fragments.WeatherFragment;

public class WeatherActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }
        if (savedInstanceState == null) {

            WeatherFragment detail = new WeatherFragment();
            detail.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, detail).commit();
        }
    }
}

package com.mailru.weather_app;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class MainActivity extends BaseActivity {

    private final static String EXTRA = "EXTRA";
    private ImageView settings_img;
    private TextView weatherTomorrowView;
    private TextView weatherTodayView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        settings_img.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });
    }

    private void initViews() {
        settings_img = findViewById(R.id.settings);
        weatherTomorrowView = findViewById(R.id.weatherTomorrow);
        weatherTodayView = findViewById(R.id.weatherToday);
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

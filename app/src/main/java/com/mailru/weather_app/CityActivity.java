package com.mailru.weather_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

public class CityActivity extends BaseActivity {

    private TextView currentCity;
    private CheckBox wind;
    private CheckBox airpress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        //Не слишком перемудрил с распределением кода по методам?
        initViews();
        setOnClickListenerForTextView();
        setOnCheckedChangeListener();
    }

    private void initViews() {
        currentCity = findViewById(R.id.moscow);
        wind = findViewById(R.id.wind);
        airpress = findViewById(R.id.airpress);
    }

    private void setOnCheckedChangeListener() {
        wind.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                String Speed_Wind = "Moscow\n5 m/s";
                currentCity.setText(Speed_Wind);
            } else
                currentCity.setText(R.string.moscow);
        });

        airpress.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                String pressure = "Moscow" + "\n" + "750 mm of mercury";
                currentCity.setText(pressure);
            } else
                currentCity.setText(R.string.moscow);
        });
    }

    private void setOnClickListenerForTextView() {
        currentCity.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }
}
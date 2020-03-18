package com.mailru.weather_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

public class CityActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        TextView view = findViewById(R.id.moscow);
        view.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        CheckBox wind = findViewById(R.id.wind);
        CheckBox airpress = findViewById(R.id.airpress);
        wind.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                String Speed_Wind = "Moscow\n5 m/s";
                view.setText(Speed_Wind);
            } else
                view.setText(R.string.moscow);
        });

        airpress.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                String pressure = "Moscow" + "\n" + "750 mm of mercury";
                view.setText(pressure);
            } else
                view.setText(R.string.moscow);
        });
    }
}
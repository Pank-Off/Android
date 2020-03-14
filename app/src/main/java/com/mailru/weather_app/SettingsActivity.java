package com.mailru.weather_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        TextView city = findViewById(R.id.city);
        city.setOnClickListener(v -> {
            Intent intent = new Intent(this, CityActivity.class);
            startActivity(intent);
        });
    }
}
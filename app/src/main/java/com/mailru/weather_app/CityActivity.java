package com.mailru.weather_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        TextView view = findViewById(R.id.moscow);
        view.setOnClickListener(v->{
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        });

        CheckBox wind = findViewById(R.id.wind);
        CheckBox airpress = findViewById(R.id.airpress);
        wind.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked)
                view.setText("Moscow" +"\n" + "5 m/s");
            else
                view.setText(R.string.moscow);
        });

        airpress.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked)
                view.setText("Moscow" +"\n" + "750 mm of mercury");
            else
                view.setText(R.string.moscow);
        });
    }
}
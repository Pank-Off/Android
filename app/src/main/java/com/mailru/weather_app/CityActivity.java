package com.mailru.weather_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class CityActivity extends BaseActivity {

    static final String CITY_KEY = "CITY_KEY";
    static final String GRAD_KEY = "GRAD_KEY";
    private TextView currentCity;
    private CheckBox wind;
    private CheckBox airpress;
    private EditText inputCity;
    private Button selectBtn;
    private HashMap<String, String> cityMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        //Не слишком перемудрил с распределением кода по методам?
        initViews();
        initMap();
        setOnClickListenerForTextView();
        setOnCheckedChangeListener();
        setOnSelectClickListener();
    }

    private void initMap() {
        cityMap.put(getResources().getString(R.string.moscow), getResources().getString(R.string._8_u00b0));
        cityMap.put(getResources().getString(R.string.tokio), getResources().getString(R.string._4_u00b0));
        cityMap.put(getResources().getString(R.string.paris), getResources().getString(R.string._7_u00b0));
        cityMap.put(getResources().getString(R.string.ny), getResources().getString(R.string._4_u00b0));
        cityMap.put(getResources().getString(R.string.kiev), getResources().getString(R.string._8_u00b0));
        cityMap.put(getResources().getString(R.string.berlin), getResources().getString(R.string._13_u00b0));
        cityMap.put(getResources().getString(R.string.madrid), getResources().getString(R.string._8_u00b0));
        cityMap.put(getResources().getString(R.string.istambul), getResources().getString(R.string._13_u00b0));
        cityMap.put(getResources().getString(R.string.london), getResources().getString(R.string._4_u00b0));
        cityMap.put(getResources().getString(R.string.tallinn), getResources().getString(R.string._7_u00b0));
    }


    private void initViews() {
        currentCity = findViewById(R.id.moscow);
        wind = findViewById(R.id.wind);
        airpress = findViewById(R.id.airpress);
        inputCity = findViewById(R.id.inputCity);
        selectBtn = findViewById(R.id.selectBtn);
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

    private void setOnSelectClickListener() {
        selectBtn.setOnClickListener(v -> {
            if (inputCity.getText() != null) {
                String selected_city = inputCity.getText().toString();
                if (cityMap.containsKey(selected_city)) {
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra(CITY_KEY, selected_city);
                    intent.putExtra(GRAD_KEY, cityMap.get(selected_city));
                    startActivity(intent);
                }
                else{
                    Toast.makeText(this,getString(R.string.incorrect),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
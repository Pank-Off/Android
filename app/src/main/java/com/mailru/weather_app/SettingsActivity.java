package com.mailru.weather_app;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SettingsActivity extends BaseActivity {

    private RecyclerView settings_list;
    private ArrayList<String> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initViews();
        initData();
        initList();
    }


    private void initViews() {
        settings_list = findViewById(R.id.settings_list_view);
    }

    private void initData() {
        data.add(getResources().getString(R.string.unit));
        data.add(getResources().getString(R.string.auto_refresh));
        data.add(getResources().getString(R.string.use_current_location));
        data.add(getResources().getString(R.string.notification));
        data.add(getResources().getString(R.string.show_on_widget));
        data.add(getResources().getString(R.string.refresh_when_app_opens));
        data.add(getResources().getString(R.string.weather_alerts));
        data.add(getResources().getString(R.string.add_weather_icon));
        data.add(getResources().getString(R.string.customization_service));
        data.add(getResources().getString(R.string.about_weather));

    }

    private void initList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        settings_list.setLayoutManager(linearLayoutManager);
        RecyclerCityAdapter adapter = new RecyclerCityAdapter(data, (int position) -> {

        });
        settings_list.setAdapter(adapter);
    }

}
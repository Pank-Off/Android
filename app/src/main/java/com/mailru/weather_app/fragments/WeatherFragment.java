package com.mailru.weather_app.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mailru.weather_app.R;
import com.mailru.weather_app.SettingsActivity;

import java.util.Objects;

public class WeatherFragment extends Fragment {
    private ImageView settings_img;
    private TextView textView;
    private CheckBox details_checkbox;
    private TextView detailsText;
    private LinearLayout weatherLay;

    static WeatherFragment create(int index) {
        WeatherFragment f = new WeatherFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);
        return f;
    }

    int getIndex() {
        return Objects.requireNonNull(getArguments()).getInt("index", 0);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.weather_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setTextCity();
        setOnImageClickListener();
        setOnCheckedChangeListener();
        setOnSearchLayClickListener();
    }

    private void setTextCity() {
        String[] cities = getResources().getStringArray(R.array.cities);
        textView.setText(cities[getIndex()]);
    }

    private void initViews(View view) {
        textView = view.findViewById(R.id.city);
        settings_img = view.findViewById(R.id.settings);
        details_checkbox = view.findViewById(R.id.details_checkbox);
        detailsText = view.findViewById(R.id.detailsText);
        weatherLay = view.findViewById(R.id.firstLay);
    }

    private void setOnImageClickListener() {
        settings_img.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(Objects.requireNonNull(getActivity()), SettingsActivity.class);
            startActivity(intent);
        });
    }

    private void setOnCheckedChangeListener() {
        details_checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                String details_string = "Wind: 5 m/s\nPressure: 750 mm of mercury";
                detailsText.setVisibility(View.VISIBLE);
                detailsText.setText(details_string);
            } else {
                detailsText.setVisibility(View.GONE);
            }
        });
    }
    private void setOnSearchLayClickListener(){
        weatherLay.setOnClickListener(v->{
            String url = getString(R.string.url_yandex_weather);
            Uri uri = Uri.parse(url);
            Intent linkIntent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(linkIntent);
        });

    }
}


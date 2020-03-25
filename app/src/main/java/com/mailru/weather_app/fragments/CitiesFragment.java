package com.mailru.weather_app.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.mailru.weather_app.R;
import com.mailru.weather_app.WeatherActivity;

import java.util.Objects;

public class CitiesFragment extends Fragment {
    private boolean isExistWeather;
    private int currentPosition;
    private Button selectBtn;
    private EditText inputCity;
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initList();
        setOnSelectClickListener();
    }

    private void initViews(View view) {
        selectBtn = view.findViewById(R.id.selectBtn);
        inputCity = view.findViewById(R.id.inputCity);
        listView = view.findViewById(R.id.cities_list_view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isExistWeather = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt("CurrentCity", 0);
        }
        if (isExistWeather) {
            showWeather();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("CurrentCity", currentPosition);
        super.onSaveInstanceState(outState);
    }

    private void initList() {
        ArrayAdapter adapter =
                ArrayAdapter.createFromResource(Objects.requireNonNull(getActivity()), R.array.cities,
                        android.R.layout.simple_list_item_activated_1);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            currentPosition = position;
            showWeather();
        });
    }

    private void setOnSelectClickListener() {
        final String[] cities = getResources().getStringArray(R.array.cities);
        selectBtn.setOnClickListener(v -> {
            if (inputCity.getText() != null) {
                String selected_city = inputCity.getText().toString();
                for (int i = 0; i < cities.length; i++) {
                    if (cities[i].equals(selected_city)) {
                        currentPosition = i;
                        showWeather();
                        return;
                    }
                }
                Toast.makeText(getContext(), "Not exist in city list below", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showWeather() {
        if (isExistWeather) {
            WeatherFragment detail = (WeatherFragment) Objects.requireNonNull(getFragmentManager()).findFragmentById(R.id.fragment);
            if (detail == null || detail.getIndex() != currentPosition) {
                detail = WeatherFragment.create(currentPosition);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment, detail);
                fragmentTransaction.commit();
            }
        } else {
            Intent intent = new Intent();
            intent.setClass(Objects.requireNonNull(getActivity()), WeatherActivity.class);
            intent.putExtra("index", currentPosition);
            startActivity(intent);
        }
    }
}

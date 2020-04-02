package com.mailru.weather_app.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.mailru.weather_app.R;
import com.mailru.weather_app.RecyclerCityAdapter;
import com.mailru.weather_app.WeatherActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;

public class CitiesFragment extends Fragment {
    private boolean isExistWeather;
    private int currentPosition;
    private MaterialButton selectBtn;
    private TextInputEditText inputCity;
    private RecyclerView listView;
    private RecyclerCityAdapter adapter;


    static ArrayList<String> city = new ArrayList<>(Arrays.asList("Moscow", "Tokio", "NY"));

    private Pattern correctCity = Pattern.compile("^[A-Z][a-z]{2,}$");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.city_layout, container, false);
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        listView.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerCityAdapter(city, (int position) -> {
            currentPosition = position;
            showWeather();
        });
        listView.setAdapter(adapter);
    }

    private void setOnSelectClickListener() {
        selectBtn.setOnClickListener(v -> {


            boolean correctInput = validate(inputCity, correctCity);
            boolean emptyString = Objects.requireNonNull(inputCity.getText()).toString().equals("");
            if (!emptyString && correctInput) {

                Snackbar.make(v, "Are you sure?", Snackbar.LENGTH_LONG).setAction("yes", v1 -> {
                    String selected_city = inputCity.getText().toString();
                    currentPosition = adapter.selectBtn(selected_city);
                    inputCity.setText("");
                    showWeather();
                }).show();

            }
        });
    }


    private boolean validate(TextView tv, Pattern check) {
        String value = tv.getText().toString();
        if (check.matcher(value).matches()) {    // Проверим на основе регулярных выражений
            hideError(tv);
            return true;
        } else {
            showError(tv);
            return false;
        }
    }

    // Показать ошибку
    private void showError(TextView view) {
        view.setError("Неверный формат города!");
    }

    // спрятать ошибку
    private void hideError(TextView view) {
        view.setError(null);
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

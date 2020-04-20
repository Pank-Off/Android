package com.mailru.weather_app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mailru.weather_app.R;
import com.mailru.weather_app.RecyclerCityAdapter;

import java.util.ArrayList;

public class SettingsFragment extends Fragment {

    private RecyclerView settings_list;
    private ArrayList<String> data = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initData();
        initList();
    }

    private void initViews(View view) {
        settings_list = view.findViewById(R.id.settings_list_view);
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        settings_list.setLayoutManager(linearLayoutManager);
        RecyclerCityAdapter adapter = new RecyclerCityAdapter(data, (int position) -> {
        });
        settings_list.setAdapter(adapter);
    }

}

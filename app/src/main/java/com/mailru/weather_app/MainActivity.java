package com.mailru.weather_app;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.mailru.weather_app.fragments.CitiesFragment;
import com.mailru.weather_app.fragments.SettingsFragment;

import java.util.Objects;

public class MainActivity extends BaseActivity {

    public static AppBarConfiguration mAppBarConfiguration;
    Context context;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_settings, R.id.nav_city,R.id.nav_weather)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(item -> {
            ActionBar toolbar = getSupportActionBar();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.nav_city:
                    if (toolbar != null) {
                        toolbar.setTitle("City");
                    }
                    CitiesFragment citiesFragment = new CitiesFragment();
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        fragmentManager.popBackStack();
                    }
                    fragmentTransaction.replace(R.id.nav_host_fragment, citiesFragment);
                    fragmentTransaction.detach(Objects.requireNonNull(fragmentManager.findFragmentById(R.id.nav_host_fragment)));

                    fragmentTransaction.commit();
                    break;
                case R.id.nav_settings:
                    if (toolbar != null) {
                        toolbar.setTitle("Settings");
                    }
                    SettingsFragment settingsFragment = new SettingsFragment();
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        fragmentTransaction.replace(R.id.fragment_land, settingsFragment);
                        fragmentTransaction.addToBackStack("backStack");
                    } else {
                        fragmentTransaction.replace(R.id.nav_host_fragment, settingsFragment);
                        fragmentTransaction.detach(Objects.requireNonNull(fragmentManager.findFragmentById(R.id.nav_host_fragment)));
                    }
                    fragmentTransaction.commit();
                    break;
                default:
                   return false;
            }
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.developers:
                setAlert();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.developers)
                .setMessage("WeatherApp Develeped by Kirill Pankov with the help of Geekbrains")
                .setIcon(R.mipmap.ic_launcher_round)
                .setCancelable(false)
                .setPositiveButton(R.string.ok,
                        (dialog, id) -> {
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (fragment != null) {
            fragmentTransaction.attach(fragment);
        }
        fragmentTransaction.commit();
    }

}

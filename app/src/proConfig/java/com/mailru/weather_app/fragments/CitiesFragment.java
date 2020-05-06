package com.mailru.weather_app.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.mailru.weather_app.App;
import com.mailru.weather_app.R;
import com.mailru.weather_app.RecyclerCityDBAdapter;
import com.mailru.weather_app.WeatherDataWeekendLoader;
import com.mailru.weather_app.room.CityDao;
import com.mailru.weather_app.room.CitySource;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import static android.content.Context.LOCATION_SERVICE;

public class CitiesFragment extends Fragment implements OnMapReadyCallback {
    private boolean isExistWeather;
    private int currentPosition;
    private String selectedCity;
    private MaterialButton selectBtn;
    private TextInputEditText inputCity;
    private RecyclerView listView;
    private RecyclerCityDBAdapter adapter;

    private boolean isChecked;
    private Context context;

    @SuppressLint("StaticFieldLeak")
    private static Activity activity;

    private SharedPreferences defalutPrefs;
    private String savePrefKey = "savePref";

    private MaterialButton weatherInYourCityBtn;

    private static final int PERMISSION_REQUEST_CODE = 10;
    private double lat;
    private double lng;
    // private MaterialButton picassoBtn;
    //  private ImageView picassoImg;

    private static CityDao cityDao = App.getInstance().getCityDao();


    private Pattern correctCity = Pattern.compile("^[A-Z][a-z]{2,}$");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(getClass().getSimpleName() + " - LifeCycle", "onCreateView");

        return inflater.inflate(R.layout.city_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();
        activity = getActivity();
        defalutPrefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        try {
            selectedCity = requireArguments().getString("index", null);
        } catch (IllegalStateException e) {
            selectedCity = readFromPreference(defalutPrefs);
        }
        initViews(view);
        initList();
        setOnSelectClickListener();
        setOnWeatherInYourCityClickListener();
        // loadImageWithPicasso();
    }

    private void setOnWeatherInYourCityClickListener() {
        weatherInYourCityBtn.setOnClickListener(v -> getCoordinates());
    }

    private void getCoordinates() {
//        if (ActivityCompat.checkSelfPermission(context,
//                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
//                ActivityCompat.checkSelfPermission(context,
//                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // Запрашиваем эти два Permission’а у пользователя
//            ActivityCompat.requestPermissions(requireActivity(),
//                    new String[]{
//                            Manifest.permission.ACCESS_COARSE_LOCATION,
//                            Manifest.permission.ACCESS_FINE_LOCATION
//                    },
//                    PERMISSION_REQUEST_CODE);
//        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context.checkSelfPermission(Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);

        } else {
            requestLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted

                //  Toast.makeText(context,"RequestLocation",Toast.LENGTH_LONG).show();
                requestLocation();

            }
        }
    }

    @SuppressLint("MissingPermission")
    private void requestLocation() {
        Toast.makeText(context, "RequestLocation", Toast.LENGTH_LONG).show();
        Geocoder geocoder = new Geocoder(context);
        String[] fullCityInfo;
        Location location;
        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        location = Objects.requireNonNull(locationManager).getLastKnownLocation(LocationManager.GPS_PROVIDER);

        lat = location.getLatitude();
        lng = location.getLongitude();
        String latitude = Double.toString(lat);

        String longitude = Double.toString(lng);
        Log.d("Current latitude in G", latitude);
        Log.d("Current longitude in G", longitude);

        try {
            List<Address> addressList = geocoder.getFromLocation(lat, lng, 1);
            String fullAddress = addressList.get(0).getAddressLine(0);
            Log.d("CurrentCity", fullAddress);
            fullCityInfo = fullAddress.split(", ");
            selectedCity = fullCityInfo[2];
        } catch (IOException e) {
            e.printStackTrace();
        }
        showWeather();
    }


//
//        requestPemissions();
//
////        Geocoder geocoder = new Geocoder(context);
////
////        String[] cityMas = {"null", "null", "null"};
////        try {
//            String latitude = Double.toString(lat);
//
//            String longitude = Double.toString(lng);
//            Log.d("Current latitude in G", latitude);
//            Log.d("Current longitude in G", longitude);
////
////
////            List<Address> addressList = geocoder.getFromLocation(lat, lng, 1);
////
////            String fullAddress = addressList.get(0).getAddressLine(0);
////            Log.d("CurrentCity", fullAddress);
////            cityMas = fullAddress.split(", ");
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
//
//
//      //  return cityMas[2];
//return "";
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//
//    }
//
//    private Handler handler = new Handler();
//
//    private void requestPemissions() {
//        // Проверим, есть ли Permission’ы, и если их нет, запрашиваем их у
//        // пользователя
//        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
//                || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            // Запрашиваем координаты
//
//            requestLocation();
//
//        } else {
//            // Permission’ов нет, запрашиваем их у пользователя
//            requestLocationPermissions();
//        }
//    }
//
//    // Запрашиваем координаты
//    private String requestLocation() {
//        // Если Permission’а всё- таки нет, просто выходим: приложение не имеет
//        // смысла
//        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
//            return "";
//        // Получаем менеджер геолокаций
//        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
//        Criteria criteria = new Criteria();
//        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
//
//        // Получаем наиболее подходящий провайдер геолокации по критериям.
//        // Но определить, какой провайдер использовать, можно и самостоятельно.
//        // В основном используются LocationManager.GPS_PROVIDER или
//        // LocationManager.NETWORK_PROVIDER, но можно использовать и
//        // LocationManager.PASSIVE_PROVIDER - для получения координат в
//        // пассивном режиме
//        assert locationManager != null;
//        String provider = locationManager.getBestProvider(criteria, true);
//
//        Geocoder geocoder = new Geocoder(context);
//
//        String[] cityMas = {"null", "null", "null"};
//
//            String latitude = Double.toString(lat);
//
//            String longitude = Double.toString(lng);
//            Log.d("Current latitude in G", latitude);
//            Log.d("Current longitude in G", longitude);
//
//
//            //  return cityMas[2];
//            if (provider != null) {
//                // Будем получать геоположение через каждые 10 секунд или каждые
//                // 10 метров
//                Location loc = locationManager.getLastKnownLocation(provider);
//                List<Address> addressList = null;
//                try {
//                    addressList = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                String fullAddress = addressList.get(0).getAddressLine(0);
//                Log.d("CurrentCity", fullAddress);
//                cityMas = fullAddress.split(", ");
//
//
//
//            locationManager.requestLocationUpdates(provider, 10000, 10, new LocationListener() {
//                @Override
//                public void onLocationChanged(Location location) {
//                    lat = location.getLatitude(); // Широта
//
//                    lng = location.getLongitude(); // Долгота
//                    String latitude = Double.toString(lat);
//
//                    String longitude = Double.toString(lng);
//
//                    Log.d("Current latitude", latitude);
//                    Log.d("Current longitude", longitude);
//                }
//
//                @Override
//                public void onStatusChanged(String provider, int status, Bundle extras) {
//                }
//
//                @Override
//                public void onProviderEnabled(String provider) {
//                }
//
//                @Override
//                public void onProviderDisabled(String provider) {
//                }
//            }, Looper.getMainLooper());
//        }
//        return cityMas[2];
//    }
//
//
//    // Запрашиваем Permission’ы для геолокации
//    private void requestLocationPermissions() {
//        if (ActivityCompat.checkSelfPermission(context,
//                Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED ||
//                ActivityCompat.checkSelfPermission(context,
//                        Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED) {
//            // Запрашиваем эти два Permission’а у пользователя
//            ActivityCompat.requestPermissions(requireActivity(),
//                    new String[]{
//                            Manifest.permission.ACCESS_COARSE_LOCATION,
//                            Manifest.permission.ACCESS_FINE_LOCATION
//                    },
//                    PERMISSION_REQUEST_CODE);
//        }
//        else{
//            requestLocation();
//        }
//    }
//
//
//    // Результат запроса Permission’а у пользователя:
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == PERMISSION_REQUEST_CODE) {   // Запрошенный нами
//            // Permission
//            if (grantResults.length == 2 &&
//                    (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
//                // Все препоны пройдены и пермиссия дана
//                // Запросим координаты
//
//                requestLocation();
//            }
//            else{
//
//            }
//        }
//    }


    private void saveToPreference(SharedPreferences preferences) {

        SharedPreferences.Editor editor = preferences.edit();
        String text = selectedCity;
        editor.putString(savePrefKey, text);
        editor.apply();
    }

    private String readFromPreference(SharedPreferences preferences) {
        return preferences.getString(savePrefKey, "Moscow");

    }

    @Override
    public void onStop() {
        super.onStop();
        saveToPreference(defalutPrefs);
    }

    private void initViews(View view) {
        selectBtn = view.findViewById(R.id.selectBtn);
        inputCity = view.findViewById(R.id.inputCity);
        listView = view.findViewById(R.id.cities_list_view);
        weatherInYourCityBtn = view.findViewById(R.id.weatherInYourCity);
        // picassoBtn = view.findViewById(R.id.picassoBtn);
        //  picassoImg = view.findViewById(R.id.picassoImg);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isExistWeather = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        Log.d(getClass().getSimpleName() + " - LifeCycle", "onActivityCreated");
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt("CurrentPosition", 0);
            selectedCity = savedInstanceState.getString("CurrentCity", null);
        }
        if (isExistWeather) {
            showWeather();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("CurrentPosition", currentPosition);
        outState.putString("CurrentCity", selectedCity);
    }

    private void initList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        listView.setLayoutManager(linearLayoutManager);

        CitySource citySource = new CitySource(cityDao);

        adapter = new RecyclerCityDBAdapter(citySource, (int position) -> {
            currentPosition = position;
            selectedCity = citySource.getCities().get(currentPosition).city;
            boolean hasConnection = hasConnection();
            if (!hasConnection) {
                setAlert();
                return;
            }
            if (checkJson(selectedCity)) {
                showWeather();
            }
        });

        listView.setAdapter(adapter);
    }

    private void setOnSelectClickListener() {
        selectBtn.setOnClickListener(v -> {
            hideKeyboard();
            boolean hasConnection = hasConnection();
            if (!hasConnection) {
                setAlert();
                return;
            }
            boolean correctInput = validate(inputCity, correctCity);
            boolean emptyString = Objects.requireNonNull(inputCity.getText()).toString().equals("");
            if (!emptyString && correctInput) {
                Snackbar.make(v, "Are you sure?", Snackbar.LENGTH_LONG).setAction("yes", v1 -> {
                    selectedCity = inputCity.getText().toString();
                    currentPosition = adapter.selectBtn(selectedCity);
                    inputCity.setText("");
                    showWeather();
                }).show();
            }
        });
    }

    private static void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void setAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.unavailable_network)
                .setMessage("Please, turn on the Internet")
                .setIcon(R.mipmap.ic_launcher_round)
                .setCancelable(false)
                .setPositiveButton(R.string.ok,
                        (dialog, id) -> {
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private boolean hasConnection() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network[] networks = Objects.requireNonNull(cm).getAllNetworks();
        return networks.length != 0;
    }

    private boolean validate(TextView tv, Pattern check) {
        String value = tv.getText().toString();
        if (check.matcher(value).matches() && checkJson(value)) {
            hideError(tv);
            return true;
        } else {
            showError(tv);
            return false;
        }
    }

    private boolean checkJson(String value) {
        isChecked = false;
        Thread t = new Thread() {
            @Override
            public void run() {
                final JSONObject jsonObject = WeatherDataWeekendLoader.getJSONData(value);
                isChecked = jsonObject != null;
            }
        };
        t.start();
        try {
            t.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isChecked;
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

            WeatherFragment detail = (WeatherFragment) Objects.requireNonNull(getParentFragmentManager()).findFragmentById(R.id.fragmentWeather);

            if (detail == null || detail.getIndex() != currentPosition || detail.getIndex() == 0) {

                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                detail = WeatherFragment.create(currentPosition, selectedCity);

                fragmentTransaction.replace(R.id.fragmentWeather, detail);

                fragmentTransaction.commit();
            }
        } else {
            WeatherFragment detail = WeatherFragment.create(currentPosition, selectedCity);
            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, detail);
            fragmentTransaction.commit();
            fragmentTransaction.addToBackStack(null);

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }


//    private void loadImageWithPicasso() {
//        picassoBtn.setOnClickListener(view -> {
//            Toast.makeText(getContext(), "Loading", Toast.LENGTH_SHORT).show();
//            Picasso.get()
//                    .load("https://icon-icons.com/icons2/38/PNG/128/clouds_weather_cloud_4496.png")
//                    .into(picassoImg);
//        });
//    }

}

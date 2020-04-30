package com.mailru.weather_app;

import android.app.Application;

import androidx.room.Room;

import com.mailru.weather_app.room.CityDao;
import com.mailru.weather_app.room.CityDataBase;

// паттерн синглтон, наследуем класс Application
// создаем базу данных в методе onCreate
public class App extends Application {

    private static App instance;

    // база данных
    private CityDataBase db;

    // Так получаем объект приложения
    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Это для синглтона, сохраняем объект приложения
        instance = this;
        // строим базу
        db = Room.databaseBuilder(
                getApplicationContext(),
                CityDataBase.class,
                "cities_database")
                .allowMainThreadQueries() //Только для примеров и тестирования.
//                .addMigrations(new Migration_1_2())
                .build();
    }

    // Получаем EducationDao для составления запросов
    public CityDao getCityDao() {
        return db.getCityDao();
    }
}

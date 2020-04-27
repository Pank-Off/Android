package com.mailru.weather_app.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {CityEntity.class}, version = 1)

public abstract class CityDataBase extends RoomDatabase {
    public abstract CityDao getCityDao();
}

package com.mailru.weather_app.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"city",})})
public class CityEntity {

    // @PrimaryKey - указывает на ключевую запись,
    // autoGenerate = true - автоматическая генерация ключа
    @PrimaryKey(autoGenerate = true)
    public long id;

    // Имя студента
    // @ColumnInfo позволяет задавать параметры колонки в БД
    // name = "first_name" - имя колонки
    @ColumnInfo(name = "city")
    public String city;

}

package com.mailru.weather_app.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CityDao {
    // Метод для добавления студента в базу данных
    // @Insert - признак добавления
    // onConflict - что делать, если такая запись уже есть
    // В данном случае просто заменим её
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCity(CityEntity city);

    // Метод для замены данных студента
    @Update
    void updateCity(CityEntity city);

    // Удаляем данные студента
    @Delete
    void deleteCity(CityEntity city);

    // Удаляем данные студента, зная ключ
    @Query("DELETE FROM CityEntity WHERE id = :id")
    void deteleCityById(long id);

    // Забираем данные по всем студентам
    @Query("SELECT * FROM CityEntity")
    List<CityEntity> getAllCities();

    // Получаем данные одного студента по id
    @Query("SELECT * FROM CityEntity WHERE id = :id")
    CityEntity getCityById(long id);

    //Получаем количество записей в таблице
    @Query("SELECT COUNT() FROM CityEntity")
    long getCountCities();
}

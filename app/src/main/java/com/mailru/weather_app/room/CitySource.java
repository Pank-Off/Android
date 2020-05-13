package com.mailru.weather_app.room;

import java.util.List;


// Вспомогательный класс, развязывающий
// зависимость между Room и RecyclerView
public class CitySource {

    private final CityDao cityDao;

    // Буфер с данными, сюда будем подкачивать данные из БД
    private List<CityEntity> cities;

    public CitySource(CityDao educationDao) {
        this.cityDao = educationDao;
    }

    // Получить всех студентов
    public List<CityEntity> getCities() {
        // Если объекты еще не загружены, то загружаем их.
        // Сделано для того, чтобы не делать запросы в БД каждый раз
        if (cities == null) {
            loadCities();
        }
        return cities;
    }


    // Загрузить студентов в буфер
    private void loadCities() {
        cities = cityDao.getAllCities();
    }

    // Получить количество записей
    public long getCountStudents() {
        return cityDao.getCountCities();
    }

    // Добавляем студента
    public void addCity(CityEntity city) {
        cityDao.insertCity(city);
        // После изменения БД надо повторно прочесть данные из буфера
        loadCities();
    }

    // Заменяем студента
    public void updateCity(CityEntity city) {
        cityDao.updateCity(city);
        loadCities();
    }

    // Удаляем студента из базы
    public void removeCity(long id) {
        cityDao.deteleCityById(id);
        loadCities();
    }


}

package com.baronzhang.android.weather.new_data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import com.baronzhang.android.weather.new_data.entity.Weather;

import java.util.List;

@Dao
public interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdateWeather(Weather weather);

    @Query(value = "DELETE FROM weather WHERE cityId = :cityId")
    void deleteWeatherById(String cityId);

    @Query(value = "DELETE FROM weather")
    void deleteAllWeathers();

    @Query(value = "SELECT * FROM Weather WHERE cityId = :cityId")
    Weather queryWeatherById(String cityId);

    @Query(value = "SELECT * FROM Weather")
    List<Weather> queryAllSavedWeathers();

}

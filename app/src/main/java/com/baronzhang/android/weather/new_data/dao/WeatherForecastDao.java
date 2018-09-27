package com.baronzhang.android.weather.new_data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.baronzhang.android.weather.new_data.entity.Weather;
import com.baronzhang.android.weather.new_data.entity.WeatherForecast;

import java.util.List;

@Dao
public interface WeatherForecastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdateWeatherForecast(List<WeatherForecast> weatherForecasts);

    @Delete
    void deleteWeatherForecast(WeatherForecast... weatherForecasts);

    @Update
    void updateWeatherForecast(WeatherForecast weatherForecast);

    @Query(value = "SELECT * FROM weatherforecast WHERE cityId = :cityId")
    List<WeatherForecast> queryWeatherForecast(String cityId);
}

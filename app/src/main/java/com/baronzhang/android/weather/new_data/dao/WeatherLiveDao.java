package com.baronzhang.android.weather.new_data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.baronzhang.android.weather.new_data.entity.WeatherForecast;
import com.baronzhang.android.weather.new_data.entity.WeatherLive;

import java.util.List;

@Dao
public interface WeatherLiveDao {

    @Query(value = "SELECT * FROM WeatherLive WHERE cityId=:cityId")
    WeatherLive queryWeatherLiveByCityId(String cityId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdateWeatherLive(WeatherLive weatherLive);

}

package com.baronzhang.android.weather.new_data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.baronzhang.android.weather.new_data.entity.AirQualityLive;

@Dao
public interface AirQualityLiveDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAirQualityLive(AirQualityLive airQualityLive);

    @Delete
    void deleteAirQualityLive(AirQualityLive airQualityLive);

    @Update
    void updateAirQualityLive(AirQualityLive airQualityLive);

    @Query(value = "SELECT * FROM airquality WHERE cityId = :cityId")
    AirQualityLive queryAirQualityLiveById(String cityId);
}

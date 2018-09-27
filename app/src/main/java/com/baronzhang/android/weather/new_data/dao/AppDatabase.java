package com.baronzhang.android.weather.new_data.dao;

import android.arch.persistence.room.RoomDatabase;

import com.baronzhang.android.weather.new_data.entity.WeatherForecast;

public abstract class AppDatabase extends RoomDatabase {
    public abstract WeatherDao weatherDao();
    public abstract WeatherForecastDao weatherForecastDao();
    public abstract LifeIndexDao lifeIndexDao();

}

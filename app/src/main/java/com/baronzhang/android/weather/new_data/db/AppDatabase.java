package com.baronzhang.android.weather.new_data.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.baronzhang.android.weather.new_data.dao.AirQualityLiveDao;
import com.baronzhang.android.weather.new_data.dao.LifeIndexDao;
import com.baronzhang.android.weather.new_data.dao.WeatherDao;
import com.baronzhang.android.weather.new_data.dao.WeatherForecastDao;
import com.baronzhang.android.weather.new_data.dao.WeatherLiveDao;
import com.baronzhang.android.weather.new_data.entity.AirQualityLive;
import com.baronzhang.android.weather.new_data.entity.LifeIndex;
import com.baronzhang.android.weather.new_data.entity.Weather;
import com.baronzhang.android.weather.new_data.entity.WeatherForecast;
import com.baronzhang.android.weather.new_data.entity.WeatherLive;

@Database(entities = {Weather.class, AirQualityLive.class, LifeIndex.class,
        WeatherForecast.class, WeatherLive.class}, version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "weather.db";
    private static final Object sLock = new Object();

    private static volatile AppDatabase INSTANCE = null;

    public abstract AirQualityLiveDao airQualityLiveDao();

    public abstract LifeIndexDao lifeIndexDao();

    public abstract WeatherDao weatherDao();

    public abstract WeatherForecastDao weatherForecastDao();

    public abstract WeatherLiveDao weatherLiveDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null)
            synchronized (sLock) {
                if (INSTANCE == null)
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DB_NAME)
                            .build();
            }
        return INSTANCE;
    }
}

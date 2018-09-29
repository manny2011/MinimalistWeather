package com.baronzhang.android.weather.new_data.util;

import android.content.Context;

import com.baronzhang.android.weather.new_data.db.AppDatabase;
import com.baronzhang.android.weather.new_data.repo.WeatherDataRepository;

/**
 * Static methods used to inject classes needed for various Activities and Fragments.
 */

public class InjectorUtils {

    public static WeatherDataRepository getWeatherDataRepository(Context context){
        return WeatherDataRepository.getInstance(AppDatabase.getInstance(context));
    }

    
}

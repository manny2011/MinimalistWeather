package com.baronzhang.android.weather.feature.home.drawer;

import com.baronzhang.android.weather.new_data.entity.Weather;

public interface IOnDeleteCity {
    void onDelete(Weather weather);
    void onSelect(Weather weather);
}

package com.baronzhang.android.weather.data.db.entities.adapter;

import com.baronzhang.android.weather.new_data.entity.AirQualityLive;
import com.baronzhang.android.weather.new_data.entity.LifeIndex;
import com.baronzhang.android.weather.new_data.entity.Weather;
import com.baronzhang.android.weather.new_data.entity.WeatherForecast;
import com.baronzhang.android.weather.new_data.entity.WeatherLive;

import java.util.List;

/**
 * @author baronzhang (baron[dot]zhanglei[at]gmail[dot]com)
 *         16/2/25
 */
public abstract class WeatherAdapter {

    public abstract String getCityId();

    public abstract String getCityName();

    public abstract String getCityNameEn();

    public abstract WeatherLive getWeatherLive();

    public abstract List<WeatherForecast> getWeatherForecasts();

    public abstract List<LifeIndex> getLifeIndexes();

    public abstract AirQualityLive getAirQualityLive();

    public Weather getWeather() {

        Weather weather = new Weather();
        weather.setCityId(getCityId());
        weather.setCityName(getCityName());
        weather.setCityNameEn(getCityNameEn());
        weather.setAirQualityLive(getAirQualityLive());
        weather.setWeatherForecasts(getWeatherForecasts());
        weather.setLifeIndexes(getLifeIndexes());
        weather.setWeatherLive(getWeatherLive());
        return weather;
    }
}

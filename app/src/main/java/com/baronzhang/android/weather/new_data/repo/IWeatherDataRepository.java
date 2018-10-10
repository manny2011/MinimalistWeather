package com.baronzhang.android.weather.new_data.repo;

import com.baronzhang.android.weather.new_data.entity.Weather;

import java.util.List;

import io.reactivex.Observable;


public interface IWeatherDataRepository {

    /**
     * get specific weather from local database first,if necessary,get latest weather from remote API then.
     *
     * @param cityId
     * @param refreshNow
     * @return
     */
    Observable<Weather> getWeather(String cityId, boolean refreshNow);

    /**
     * get all saved cities from local database,together with corresponding weather info.
     * @return
     */
    Observable<List<Weather>> getSavedCityInfo();

    /**
     * delete selected city and return current cityId(which may be changed to a default cityId )
     * @param weather city to be deleted
     * @return current cityId
     */
    Observable<String> deleteCity(Weather weather);
}

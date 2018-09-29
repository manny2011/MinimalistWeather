package com.baronzhang.android.weather.new_data.repo;

import com.baronzhang.android.weather.new_data.entity.Weather;

import java.util.List;

/**
 * Access point for accessing user data.
 */
public interface ILocalWeatherDataSource {

    /**
     * Gets all the users from the data source.
     *
     * @return all the users from the data source.
     */
    Weather queryWeather(String cityId);

    /**
     * gets all weathers of saved cities
     * @return all weathers of saved cities
     */
    List<Weather> queryAllSaveCity();

    /**
     * Inserts the user in the data source, or,
     * if this is an existing user, it updates it.
     *
     * @param weather the user to be inserted or updated.
     */
    void insertOrUpdateWeather(Weather weather);

    /**
     * delete wea
     * @param cityId
     */
    void deleteWeatherByCityId(String cityId);

    /**
     * Deletes all users from the data source.
     */
    void deleteAllWeather();
}
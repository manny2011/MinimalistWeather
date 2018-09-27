package com.baronzhang.android.weather.data.repository;

import android.content.Context;
import android.text.TextUtils;

import com.baronzhang.android.library.util.NetworkUtils;
import com.baronzhang.android.weather.WeatherApplication;
import com.baronzhang.android.weather.data.db.dao.WeatherDao;
import com.baronzhang.android.weather.data.db.entities.adapter.CloudWeatherAdapter;
import com.baronzhang.android.weather.data.db.entities.adapter.KnowWeatherAdapter;
import com.baronzhang.android.weather.data.db.entities.adapter.MiWeatherAdapter;
import com.baronzhang.android.weather.data.db.entities.minimalist.Weather;
import com.baronzhang.android.weather.data.http.ApiClient;
import com.baronzhang.android.weather.data.http.ApiConstants;
import com.baronzhang.android.weather.data.http.entity.envicloud.EnvironmentCloudCityAirLive;
import com.baronzhang.android.weather.data.http.entity.envicloud.EnvironmentCloudForecast;
import com.baronzhang.android.weather.data.http.entity.envicloud.EnvironmentCloudWeatherLive;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.schedulers.Schedulers;

/**
 * @author baronzhang (baron[dot]zhanglei[at]gmail[dot]com)
 * 2016/12/10
 */
public class WeatherDataRepository {
    //2个数据来源:
//一个从persist model中获取
    //一个从server中获取
    public static Observable<Weather> getWeather(String cityId, WeatherDao weatherDao, boolean refreshNow) {

        //从数据库获取天气数据
        Observable<Weather> observableForGetWeatherFromDB = Observable.create(subscriber -> {
            try {
                Weather weather = weatherDao.queryWeather(cityId);
                subscriber.onNext(weather);
                subscriber.onCompleted();
            } catch (SQLException e) {
                throw Exceptions.propagate(e);
            }
        });

        if (!NetworkUtils.isNetworkConnected(WeatherApplication.getInstance()))
            return observableForGetWeatherFromDB;

        //从服务端获取天气数据
        Observable<Weather> observableForGetWeatherFromNetWork = null;
        switch (ApiClient.configuration.getDataSourceType()) {
            case ApiConstants.WEATHER_DATA_SOURCE_TYPE_KNOW:
                observableForGetWeatherFromNetWork = ApiClient.weatherService.getKnowWeather(cityId)
                        .map(knowWeather -> new KnowWeatherAdapter(knowWeather).getWeather());
                break;
            case ApiConstants.WEATHER_DATA_SOURCE_TYPE_MI:
                observableForGetWeatherFromNetWork = ApiClient.weatherService.getMiWeather(cityId)
                        .map(miWeather -> new MiWeatherAdapter(miWeather).getWeather());
                break;
            case ApiConstants.WEATHER_DATA_SOURCE_TYPE_ENVIRONMENT_CLOUD:

                Observable<EnvironmentCloudWeatherLive> weatherLiveObservable = ApiClient.environmentCloudWeatherService.getWeatherLive(cityId);
                Observable<EnvironmentCloudForecast> forecastObservable = ApiClient.environmentCloudWeatherService.getWeatherForecast(cityId);
                Observable<EnvironmentCloudCityAirLive> airLiveObservable = ApiClient.environmentCloudWeatherService.getAirLive(cityId);

                observableForGetWeatherFromNetWork = Observable.combineLatest(weatherLiveObservable, forecastObservable, airLiveObservable,
                        (weatherLive, forecast, airLive) -> new CloudWeatherAdapter(weatherLive, forecast, airLive).getWeather());

                break;
        }
        assert observableForGetWeatherFromNetWork != null;
        observableForGetWeatherFromNetWork = observableForGetWeatherFromNetWork.doOnNext(weather -> Schedulers.io().createWorker().schedule(() -> {
            try {
                weatherDao.insertOrUpdateWeather(weather);
            } catch (SQLException e) {
                throw Exceptions.propagate(e);
            }
        }));

        return Observable.concat(observableForGetWeatherFromDB, observableForGetWeatherFromNetWork)
                .filter(weather -> weather != null && !TextUtils.isEmpty(weather.getCityId()))
                .distinct(weather -> weather.getWeatherLive().getTime())
                .takeUntil(weather -> !refreshNow && System.currentTimeMillis() - weather.getWeatherLive().getTime() <= 15 * 60 * 1000);
    }

    public static Observable<List<Weather>> getSavedCityInfo(WeatherDao weatherDao) {
        try {
            return Observable.just(weatherDao.queryAllSaveCity());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

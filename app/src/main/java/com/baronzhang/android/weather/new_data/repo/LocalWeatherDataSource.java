package com.baronzhang.android.weather.new_data.repo;

import com.baronzhang.android.weather.new_data.dao.AirQualityLiveDao;
import com.baronzhang.android.weather.new_data.dao.LifeIndexDao;
import com.baronzhang.android.weather.new_data.dao.WeatherDao;
import com.baronzhang.android.weather.new_data.dao.WeatherForecastDao;
import com.baronzhang.android.weather.new_data.dao.WeatherLiveDao;
import com.baronzhang.android.weather.new_data.db.AppDatabase;
import com.baronzhang.android.weather.new_data.entity.Weather;
import com.j256.ormlite.stmt.query.In;

import java.util.List;

/**
 * provide only one instance globally.
 */
public class LocalWeatherDataSource implements ILocalWeatherDataSource {

    private static volatile LocalWeatherDataSource INSTANCE=null;

    private WeatherDao mWeatherDao;
    private WeatherForecastDao mWeatherForecastDao;
    private LifeIndexDao mLifeIndexDao;
    private AirQualityLiveDao mAirQualityLiveDao;
    private WeatherLiveDao mWeatherLiveDao;

    private LocalWeatherDataSource(AppDatabase db) {
        this.mWeatherDao = db.weatherDao();
        this.mWeatherForecastDao = db.weatherForecastDao();
        this.mLifeIndexDao = db.lifeIndexDao();
        this.mAirQualityLiveDao = db.airQualityLiveDao();
        this.mWeatherLiveDao=db.weatherLiveDao();
    }

    public static LocalWeatherDataSource getInstance(AppDatabase db){
        if(INSTANCE==null)
            synchronized (LocalWeatherDataSource.class){
                if(INSTANCE==null)
                    INSTANCE=new LocalWeatherDataSource(db);
            }
        return INSTANCE;
    }

    @Override
    public Weather queryWeather(String cityId) {
        Weather weather = mWeatherDao.queryWeatherById(cityId);
        if(weather!=null){
            weather.setAirQualityLive(mAirQualityLiveDao.queryAirQualityLiveById(cityId));
            weather.setLifeIndexes(mLifeIndexDao.queryLifeIndexes(cityId));
            weather.setWeatherForecasts(mWeatherForecastDao.queryWeatherForecast(cityId));
            weather.setWeatherLive(mWeatherLiveDao.queryWeatherLiveByCityId(cityId));
        }
        return weather;
    }

    @Override
    public List<Weather> queryAllSaveCity() {
        List<Weather> weathers = mWeatherDao.queryAllSavedWeathers();
        for (Weather next : weathers) {
            next.setWeatherLive(mWeatherLiveDao.queryWeatherLiveByCityId(next.getCityId()));
            next.setWeatherForecasts(mWeatherForecastDao.queryWeatherForecast(next.getCityId()));
            next.setLifeIndexes(mLifeIndexDao.queryLifeIndexes(next.getCityId()));
            next.setAirQualityLive(mAirQualityLiveDao.queryAirQualityLiveById(next.getCityId()));
        }
        return weathers;
    }

    @Override
    public void insertOrUpdateWeather(Weather weather) {
        mWeatherDao.insertOrUpdateWeather(weather);
        mWeatherForecastDao.insertOrUpdateWeatherForecast(weather.getWeatherForecasts());
        mWeatherLiveDao.insertOrUpdateWeatherLive(weather.getWeatherLive());
        mAirQualityLiveDao.insertAirQualityLive(weather.getAirQualityLive());
        mLifeIndexDao.insertOrUpdateLifeIndexes(weather.getLifeIndexes());
    }

    @Override
    public void deleteWeatherByCityId(String cityId) {
        mWeatherDao.deleteWeatherById(cityId);
    }

    @Override
    public void deleteAllWeather() {
        mWeatherDao.deleteAllWeathers();
    }
}

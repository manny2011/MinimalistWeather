package com.baronzhang.android.weather.feature.home.drawer;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.baronzhang.android.weather.WeatherApplication;
import com.baronzhang.android.weather.data.db.dao.WeatherDao;
import com.baronzhang.android.weather.data.db.entities.City;
import com.baronzhang.android.weather.data.db.entities.minimalist.Weather;
import com.baronzhang.android.weather.data.preference.PreferenceHelper;
import com.baronzhang.android.weather.data.preference.WeatherSettings;
import com.baronzhang.android.weather.data.repository.WeatherDataRepository;
import com.baronzhang.android.weather.feature.selectcity.SelectCityActivity;

import java.io.InvalidClassException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DrawerMenuViewModel extends AndroidViewModel implements RecyclerItemMultiLister {

    public MutableLiveData<List<Weather>> weathers = new MutableLiveData<>();
    public MutableLiveData<String> currentCity=new MutableLiveData<>();

    public DrawerMenuViewModel(@NonNull Application application) {
        super(application);
        weathers.setValue(new ArrayList<>());
        String cityId = PreferenceHelper.getSharedPreferences().getString(WeatherSettings.SETTINGS_CURRENT_CITY_ID.getId(), "");
        currentCity.setValue(cityId);
    }

    public void loadSavedCities() {

        WeatherDataRepository.getSavedCityInfo(new WeatherDao(WeatherApplication.getInstance()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    weathers.setValue(result);
                }, throwable -> {
                });

    }

    public void loadData() {
        try {
            WeatherDao weatherDao=new WeatherDao(getApplication());
            Subscription subscription = Observable.just(weatherDao.queryAllSaveCity())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(weathers -> this.weathers.setValue(weathers));
//            subscriptions.add(subscription);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteCity(String cityId) {

        Observable.just(deleteCityFromDBAndReturnCurrentCityId(cityId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(currentCityId -> {
                    if (currentCityId == null)
                        return;
                    try {
                        PreferenceHelper.savePreference(WeatherSettings.SETTINGS_CURRENT_CITY_ID, currentCityId);
                    } catch (InvalidClassException e) {
                        e.printStackTrace();
                    }
                });
    }

    private String deleteCityFromDBAndReturnCurrentCityId(String cityId) {
        WeatherDao weatherDao = new WeatherDao(WeatherApplication.getInstance());
        String currentCityId = PreferenceHelper.getSharedPreferences().getString(WeatherSettings.SETTINGS_CURRENT_CITY_ID.getId(), "");
        try {
            weatherDao.deleteById(cityId);
            if (cityId.equals(currentCityId)) {//说明删除的是当前选择的城市，所以需要重新设置默认城市
                List<Weather> weatherList = weatherDao.queryAllSaveCity();
                if (weatherList != null && weatherList.size() > 0) {
                    currentCityId = weatherList.get(0).getCityId();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return currentCityId;
    }

    public void saveCurrentCityToPreference(String cityId) throws InvalidClassException{
        PreferenceHelper.savePreference(WeatherSettings.SETTINGS_CURRENT_CITY_ID, cityId);
    }

    @Override
    public void add() {
        //
        getApplication().startActivity(new Intent(getApplication(),SelectCityActivity.class));
    }

    @Override
    public void delete(City city) {
        //
    }

    @Override
    public void navigateTo(City city) {

    }


}

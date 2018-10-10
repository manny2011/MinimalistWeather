package com.baronzhang.android.weather.feature.selectcity;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.baronzhang.android.weather.data.db.CityDatabaseHelper;
import com.baronzhang.android.weather.data.db.entities.City;
import com.baronzhang.android.weather.data.preference.PreferenceHelper;
import com.baronzhang.android.weather.data.preference.WeatherSettings;
import com.baronzhang.android.weather.feature.home.drawer.RecyclerItemMultiLister;
import com.j256.ormlite.dao.Dao;

import java.io.InvalidClassException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class SelectCityViewModel extends AndroidViewModel implements RecyclerItemMultiLister {

    private static final String TAG="SelectCityVM";
    public MutableLiveData<List<City>> items=new MutableLiveData<>();

    public SelectCityViewModel(@NonNull Application application) {
        super(application);
        items.setValue(new ArrayList<>());
    }

    public void loadCities(Context ctx) {
        Dao<City, Integer> cityDao = CityDatabaseHelper.getInstance(ctx).getCityDao(City.class);
        assert cityDao != null;
        try {
            Observable.just(cityDao.queryForAll())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<City>>() {
                        @Override
                        public void onComplete() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(List<City> cities) {
                            items.setValue(cities);
                        }

                        @Override
                        public void onSubscribe(Disposable d) {

                        }
                    });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveCurrentCityToPreference(String cityId) throws InvalidClassException {
        PreferenceHelper.savePreference(WeatherSettings.SETTINGS_CURRENT_CITY_ID, cityId);
        Log.d(TAG, "saveCurrentCityToPreference: 执行了点击事件 ");
    }

    @Override
    public void add() {

    }

    @Override
    public void delete(City city) {

    }

    @Override
    public void navigateTo(City city) {
    }


}

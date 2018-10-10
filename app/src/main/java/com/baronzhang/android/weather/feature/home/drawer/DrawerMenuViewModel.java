package com.baronzhang.android.weather.feature.home.drawer;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.baronzhang.android.weather.data.db.entities.City;
import com.baronzhang.android.weather.data.preference.PreferenceHelper;
import com.baronzhang.android.weather.data.preference.WeatherSettings;
import com.baronzhang.android.weather.feature.selectcity.SelectCityActivity;
import com.baronzhang.android.weather.new_data.entity.Weather;
import com.baronzhang.android.weather.new_data.util.InjectorUtils;
import com.baronzhang.android.weather.new_data.util.RxSchedulerUtils;

import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class DrawerMenuViewModel extends AndroidViewModel implements RecyclerItemMultiLister {

    private CompositeDisposable disposables=new CompositeDisposable();
    public MutableLiveData<List<Weather>> weathers = new MutableLiveData<>();
    public MutableLiveData<String> currentCity=new MutableLiveData<>();

    public DrawerMenuViewModel(@NonNull Application application) {
        super(application);
        weathers.setValue(new ArrayList<>());
        String cityId = PreferenceHelper.getSharedPreferences().getString(WeatherSettings.SETTINGS_CURRENT_CITY_ID.getId(), "");
        currentCity.setValue(cityId);
    }

    @SuppressLint("CheckResult")
    public void loadSavedCities() {

        Disposable disposable = InjectorUtils.getWeatherDataRepository(getApplication())
                .getSavedCityInfo()
                .compose(RxSchedulerUtils.normalSchedulersTransformer())
                .subscribe(e -> weathers.setValue(e));
        disposables.add(disposable);
    }

    @SuppressLint("CheckResult")
    public void deleteCity(Weather weather) {

        Disposable disposable = InjectorUtils.getWeatherDataRepository(getApplication())
                .deleteCity(weather)
                .compose(RxSchedulerUtils.normalSchedulersTransformer())
                .subscribe(this::saveCurrentCityToPreference);
        disposables.add(disposable);
    }

//    private String deleteCityFromDBAndReturnCurrentCityId(String cityId) {
//        WeatherDao weatherDao = new WeatherDao(WeatherApplication.getInstance());
//        String currentCityId = PreferenceHelper.getSharedPreferences().getString(WeatherSettings.SETTINGS_CURRENT_CITY_ID.getId(), "");
//        try {
//            weatherDao.deleteById(cityId);
//            if (cityId.equals(currentCityId)) {//说明删除的是当前选择的城市，所以需要重新设置默认城市
//                List<Weather> weatherList = weatherDao.queryAllSaveCity();
//                if (weatherList != null && weatherList.size() > 0) {
//                    currentCityId = weatherList.get(0).getCityId();
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return currentCityId;
//    }

    public void saveCurrentCityToPreference(String currentCityId) throws InvalidClassException{
        PreferenceHelper.savePreference(WeatherSettings.SETTINGS_CURRENT_CITY_ID, currentCityId);
        this.currentCity.setValue(currentCityId);
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

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}

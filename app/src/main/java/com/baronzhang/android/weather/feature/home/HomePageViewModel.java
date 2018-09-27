package com.baronzhang.android.weather.feature.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.widget.Toast;

import com.baronzhang.android.library.util.RxSchedulerUtils;
import com.baronzhang.android.weather.R;
import com.baronzhang.android.weather.WeatherApplication;
import com.baronzhang.android.weather.data.WeatherDetail;
import com.baronzhang.android.weather.data.db.WeatherDatabaseHelper;
import com.baronzhang.android.weather.data.db.dao.WeatherDao;
import com.baronzhang.android.weather.data.db.entities.minimalist.LifeIndex;
import com.baronzhang.android.weather.data.db.entities.minimalist.Weather;
import com.baronzhang.android.weather.data.db.entities.minimalist.WeatherForecast;
import com.baronzhang.android.weather.data.preference.PreferenceHelper;
import com.baronzhang.android.weather.data.preference.WeatherSettings;
import com.baronzhang.android.weather.data.repository.WeatherDataRepository;
import com.j256.ormlite.dao.Dao;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class HomePageViewModel extends ViewModel {

    private CompositeSubscription subscriptions=new CompositeSubscription();

    public MutableLiveData<String> currentCity=new MutableLiveData<>();
    public MutableLiveData<Weather> weather=new MutableLiveData<>();

    public MutableLiveData<List<WeatherDetail>> weatherDetails = new MutableLiveData<>();
    public MutableLiveData<List<WeatherForecast>> weatherForecasts = new MutableLiveData<>();
    public MutableLiveData<List<LifeIndex>> lifeIndices = new MutableLiveData<>();

    public HomePageViewModel() {
        String cityId = PreferenceHelper.getSharedPreferences().getString(WeatherSettings.SETTINGS_CURRENT_CITY_ID.getId(), "");
        currentCity.setValue(cityId);
        weatherDetails.setValue(new ArrayList<>());
        weatherForecasts.setValue(new ArrayList<>());
        lifeIndices.setValue(new ArrayList<>());
    }

    private List<WeatherDetail> createDetails(Weather weather) {

        List<WeatherDetail> details = new ArrayList<>();
        details.add(new WeatherDetail(R.drawable.ic_index_sunscreen, "体感温度", weather.getWeatherLive().getFeelsTemperature() + "°C"));
        details.add(new WeatherDetail(R.drawable.ic_index_sunscreen, "湿度", weather.getWeatherLive().getHumidity() + "%"));
//        details.add(new WeatherDetail(R.drawable.ic_index_sunscreen, "气压", (int) Double.parseDouble(weather.getWeatherLive().getAirPressure()) + "hPa"));
        details.add(new WeatherDetail(R.drawable.ic_index_sunscreen, "紫外线指数", weather.getWeatherForecasts().get(0).getUv()));
        details.add(new WeatherDetail(R.drawable.ic_index_sunscreen, "降水量", weather.getWeatherLive().getRain() + "mm"));
        details.add(new WeatherDetail(R.drawable.ic_index_sunscreen, "降水概率", weather.getWeatherForecasts().get(0).getPop() + "%"));
        details.add(new WeatherDetail(R.drawable.ic_index_sunscreen, "能见度", weather.getWeatherForecasts().get(0).getVisibility() + "km"));
        return details;
    }

    public void loadWeather(String cityId, boolean refreshNow) {
//        Dao<Weather, ?> weatherDao = WeatherDatabaseHelper.getInstance(WeatherApplication.getInstance()).getWeatherDao(Weather.class);
//        WeatherDatabaseHelper.getInstance(WeatherApplication.getInstance()).getWeatherDao(Weather.class);
        WeatherDao weatherDao = new WeatherDao(WeatherApplication.getInstance());
        Subscription subscription = WeatherDataRepository.getWeather( cityId, weatherDao, refreshNow)
                .compose(RxSchedulerUtils.normalSchedulersTransformer())
                .subscribe(it->{
                    weather.setValue(it);
                    weatherDetails.setValue(createDetails(it));
                    weatherForecasts.setValue(it.getWeatherForecasts());
                    lifeIndices.setValue(it.getLifeIndexes());
                }, throwable -> {
                    Toast.makeText(WeatherApplication.getInstance(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                });
        subscriptions.add(subscription);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        subscriptions.clear();
    }
}

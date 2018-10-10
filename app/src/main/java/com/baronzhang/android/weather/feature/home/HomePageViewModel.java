package com.baronzhang.android.weather.feature.home;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;
import android.widget.Toast;

import com.baronzhang.android.weather.R;
import com.baronzhang.android.weather.WeatherApplication;
import com.baronzhang.android.weather.data.WeatherDetail;
import com.baronzhang.android.weather.data.preference.PreferenceHelper;
import com.baronzhang.android.weather.data.preference.WeatherSettings;
import com.baronzhang.android.weather.new_data.entity.LifeIndex;
import com.baronzhang.android.weather.new_data.entity.Weather;
import com.baronzhang.android.weather.new_data.entity.WeatherForecast;
import com.baronzhang.android.weather.new_data.util.InjectorUtils;
import com.baronzhang.android.weather.new_data.util.RxSchedulerUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HomePageViewModel extends AndroidViewModel {

//    private CompositeSubscription subscriptions = new CompositeSubscription();
    public MutableLiveData<Weather> weather = new MutableLiveData<>();

    public HomePageViewModel(Application application) {
        super(application);
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

    @SuppressLint("CheckResult")
    public void loadWeather(String cityId, boolean refreshNow) {
        long start=System.currentTimeMillis();
        InjectorUtils.getWeatherDataRepository(getApplication())
                .getWeather(cityId, refreshNow)
//                .compose(RxSchedulerUtils.normalSchedulersTransformer())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(it -> {
                    it.setWeatherDetails(createDetails(it));
                    weather.setValue(it);
                }, throwable -> {
                    Toast.makeText(WeatherApplication.getInstance(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                });
        Log.e("interval", "onCreate: load Weather request Interval: "+(System.currentTimeMillis()-start));

//        subscriptions.add(subscription);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
//        subscriptions.clear();
    }
}

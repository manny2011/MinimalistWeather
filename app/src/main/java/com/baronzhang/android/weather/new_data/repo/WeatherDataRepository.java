package com.baronzhang.android.weather.new_data.repo;

import android.text.TextUtils;

import com.baronzhang.android.library.util.NetworkUtils;
import com.baronzhang.android.weather.WeatherApplication;
import com.baronzhang.android.weather.data.db.entities.adapter.CloudWeatherAdapter;
import com.baronzhang.android.weather.data.db.entities.adapter.KnowWeatherAdapter;
import com.baronzhang.android.weather.data.db.entities.adapter.MiWeatherAdapter;
import com.baronzhang.android.weather.data.http.ApiClient;
import com.baronzhang.android.weather.data.http.ApiConstants;
import com.baronzhang.android.weather.data.http.entity.envicloud.EnvironmentCloudCityAirLive;
import com.baronzhang.android.weather.data.http.entity.envicloud.EnvironmentCloudForecast;
import com.baronzhang.android.weather.data.http.entity.envicloud.EnvironmentCloudWeatherLive;
import com.baronzhang.android.weather.data.preference.PreferenceHelper;
import com.baronzhang.android.weather.data.preference.WeatherSettings;
import com.baronzhang.android.weather.new_data.db.AppDatabase;
import com.baronzhang.android.weather.new_data.entity.Weather;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * The significant meaning of introducing RX here is to avoid the annoying thread-switching operations
 * which use callbacks to send results from non-UI thread to UI-thread.
 */
public class WeatherDataRepository implements IWeatherDataRepository {

    private static volatile WeatherDataRepository INSTANCE = null;

    private ILocalWeatherDataSource mLocalWeatherDataSource;//can be injected by Dagger2
    private ApiClient mApiClient;

    public WeatherDataRepository(ILocalWeatherDataSource mLocalWeatherDataSource, ApiClient mApiClient) {
        this.mLocalWeatherDataSource = mLocalWeatherDataSource;
        this.mApiClient = mApiClient;
    }

    public static WeatherDataRepository getInstance(AppDatabase db) {
        if (INSTANCE == null)
            synchronized (WeatherDataRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new WeatherDataRepository(LocalWeatherDataSource.getInstance(db), null);
                }
            }
        return INSTANCE;
    }

    @Override
    public Observable<Weather> getWeather(String cityId, boolean refreshNow) {

        //从数据库获取天气数据
        Observable<Weather> observableForGetWeatherFromDB = Observable.create(new ObservableOnSubscribe<Weather>() {
            @Override
            public void subscribe(ObservableEmitter<Weather> e) {//todo ???需要捕捉异常时,再使用这种原始的创建方式
                Weather weather = mLocalWeatherDataSource.queryWeather(cityId);
                if (weather != null) {
                    e.onNext(weather);
                }
                System.err.println("查询数据库某个城市天气所在线程: " + Thread.currentThread().getName());
                e.onComplete();
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
                        (environmentCloudWeatherLive, environmentCloudForecast, environmentCloudCityAirLive) ->
                                new CloudWeatherAdapter(environmentCloudWeatherLive, environmentCloudForecast, environmentCloudCityAirLive).getWeather())
                        .doOnNext(new Consumer<Weather>() {
                            @Override
                            public void accept(Weather weather) throws Exception {
//                                Schedulers.io().createWorker()//非阻塞式: 通过Worker可以让数据流直接向下传递给OnNext()消耗,而不用等到accept()方法执行完成.
//                                        .schedule(() -> {
                                mLocalWeatherDataSource.insertOrUpdateWeather(weather);//这里需要用这种阻塞式
                                System.err.println("doOnNext更新数据库所在线程: " + Thread.currentThread().getName());
//                                        });
                            }
                        });

                break;
        }

        return Observable.concat(observableForGetWeatherFromDB, observableForGetWeatherFromNetWork)
                .takeUntil(weather -> !refreshNow && System.currentTimeMillis() - weather.getWeatherLive().getTime() < 15 * 60 * 1000);
    }

    @Override
    public Observable<List<Weather>> getSavedCityInfo() {
        return Observable.create(emitter -> {
            System.err.println("查询数据库城市列表所在线程: " + Thread.currentThread().getName());
            List<Weather> weathers = mLocalWeatherDataSource.queryAllSaveCity();
            emitter.onNext(weathers);
            emitter.onComplete();
        });
    }

    @Override
    public Observable<String> deleteCity(Weather weather) {
        return Observable.create(emitter -> {
            String cityId = weather.getCityId();
            String currentCityId = PreferenceHelper.getSharedPreferences().getString(WeatherSettings.SETTINGS_CURRENT_CITY_ID.getId(), "");
            mLocalWeatherDataSource.deleteWeatherByCityId(cityId);
            if (TextUtils.equals(currentCityId, cityId)) {
                List<Weather> weathers = mLocalWeatherDataSource.queryAllSaveCity();
                if (weathers.size() > 0) {
                    emitter.onNext(weathers.get(0).getCityId());
                } else {
                    emitter.onNext(WeatherApplication.DEFAULT_CITY_ID);
                }
            } else {
                emitter.onNext(currentCityId);
            }
            System.err.println("删除数据库中某个城市的天气所在线程: " + Thread.currentThread().getName());
            emitter.onComplete();
        });
    }


}

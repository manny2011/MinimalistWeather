package com.baronzhang.android.weather.feature.home;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.baronzhang.android.weather.base.BaseRecyclerViewAdapter;
import com.baronzhang.android.weather.databinding.ItemForecastBinding;
import com.baronzhang.android.weather.new_data.entity.Weather;
import com.baronzhang.android.weather.new_data.entity.WeatherForecast;

import java.util.ArrayList;
import java.util.List;

/**
 * @author baronzhang (baron[dot]zhanglei[at]gmail[dot]com)
 *         16/6/23
 */
public class ForecastAdapter extends BaseRecyclerViewAdapter<ForecastAdapter.ViewHolder> {

    private HomePageViewModel homePageViewModel;
    private List<WeatherForecast> items=new ArrayList<>(0);

    public ForecastAdapter(HomePageViewModel homePageViewModel) {
        this.homePageViewModel=homePageViewModel;
    }

    public void replaceData(List<WeatherForecast> items){
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemForecastBinding binding = ItemForecastBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ForecastAdapter.ViewHolder holder, int position) {
        WeatherForecast weatherForecast=items.get(position);
        holder.bindData(weatherForecast);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemForecastBinding itemForecastBinding;
        public ViewHolder(ItemForecastBinding binding) {
            super(binding.getRoot());
            this.itemForecastBinding=binding;
        }

        public void bindData(WeatherForecast weatherForecast){
            itemForecastBinding.setForcast(weatherForecast);
            itemForecastBinding.weatherTextView.setText(TextUtils.isEmpty(weatherForecast.getWeather()) ?
                    (weatherForecast.getWeatherDay().equals(weatherForecast.getWeatherNight()) ?
                            weatherForecast.getWeatherDay() : weatherForecast.getWeatherDay() + "转" + weatherForecast.getWeatherNight())
                    : weatherForecast.getWeather());
            itemForecastBinding.executePendingBindings();
        }
    }
}

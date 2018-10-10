package com.baronzhang.android.weather.feature.home.drawer;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.baronzhang.android.weather.base.BaseRecyclerViewAdapter;
import com.baronzhang.android.library.util.DateConvertUtils;
import com.baronzhang.android.weather.R;
import com.baronzhang.android.weather.databinding.ItemCityManagerBinding;
import com.baronzhang.android.weather.new_data.entity.Weather;

import java.util.ArrayList;
import java.util.List;

/**
 * 城市管理页面Adapter
 *
 * @author baronzhang (baron[dot]zhanglei[at]gmail[dot]com)
 * 16/3/16
 */
public class CityManagerAdapter extends BaseRecyclerViewAdapter<CityManagerAdapter.ViewHolder> {
    private static final String CITY_ID = "cityId";

    private DrawerMenuViewModel viewModel;
    private IOnDeleteCity onDeleteCity;
    private List<Weather> items=new ArrayList<>(0);

    public CityManagerAdapter(DrawerMenuViewModel viewModel, IOnDeleteCity onDeleteCity) {
        this.viewModel = viewModel;
        this.onDeleteCity=onDeleteCity;
    }

    public void replaceData(List<Weather> items){
        this.items=items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemCityManagerBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_city_manager, parent, false);
        return new ViewHolder(binding, onDeleteCity);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Weather weather = items.get(position);
        holder.bindData(weather);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ItemCityManagerBinding binding;
        private IOnDeleteCity onDeleteCity;

        public ViewHolder(ItemCityManagerBinding binding, IOnDeleteCity onDeleteCity) {
            super(binding.getRoot());
            this.binding = binding;
            this.onDeleteCity=onDeleteCity;
        }

        public void bindData(Weather weather) {
            binding.setWeather(weather);
            binding.getRoot().setOnClickListener(v -> onDeleteCity.onSelect(weather));
            binding.itemDelete.setOnClickListener(v -> onDeleteCity.onDelete(weather));
            binding.itemTvCity.setText(weather.getCityName());
            binding.itemTvPublishTime.setText("发布于 " + DateConvertUtils.timeStampToDate(weather.getWeatherLive().getTime(), DateConvertUtils.DATA_FORMAT_PATTEN_YYYY_MM_DD_HH_MM));
            binding.itemTvTemp.setText(new StringBuilder().append(weather.getWeatherForecasts().get(0).getTempMin()).append("~").append(weather.getWeatherForecasts().get(0).getTempMax()).append("℃").toString());
            binding.executePendingBindings();
        }
    }

//    public interface OnCityManagerItemClickListener extends AdapterView.OnItemClickListener {
//
//        void onDeleteClick(String cityId);
//    }

}

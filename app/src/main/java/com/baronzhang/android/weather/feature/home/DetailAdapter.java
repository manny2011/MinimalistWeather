package com.baronzhang.android.weather.feature.home;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baronzhang.android.weather.base.BaseRecyclerViewAdapter;
import com.baronzhang.android.weather.R;
import com.baronzhang.android.weather.data.WeatherDetail;
import com.baronzhang.android.weather.databinding.ItemDetailBinding;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author baronzhang (baron[dot]zhanglei[at]gmail[dot]com)
 *         2016/07/06
 */
public class DetailAdapter extends BaseRecyclerViewAdapter<DetailAdapter.ViewHolder> {

    private HomePageViewModel homePageViewModel;

    public DetailAdapter(HomePageViewModel homePageViewModel) {
        this.homePageViewModel=homePageViewModel;
    }

    @Override
    public DetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemDetailBinding binding = ItemDetailBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(DetailAdapter.ViewHolder holder, int position) {
        WeatherDetail detail = homePageViewModel.weatherDetails.getValue().get(position);
        holder.itemDetailBinding.detailIconImageView.setImageResource(detail.getIconResourceId());
        holder.bindData(detail);
//        android:src="@{weatherDetail.iconResourceId}"

    }

    @Override
    public int getItemCount() {
        return homePageViewModel.weatherDetails.getValue().size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemDetailBinding itemDetailBinding;
        ViewHolder(ItemDetailBinding itemDetailBinding) {
            super(itemDetailBinding.getRoot());
            this.itemDetailBinding=itemDetailBinding;
        }

        public void bindData(WeatherDetail weatherDetail){
            itemDetailBinding.setWeatherDetail(weatherDetail);
            itemDetailBinding.executePendingBindings();
        }
    }
}

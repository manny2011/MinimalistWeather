package com.baronzhang.android.weather.feature.home;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baronzhang.android.weather.base.BaseRecyclerViewAdapter;
import com.baronzhang.android.weather.R;
import com.baronzhang.android.weather.data.db.entities.minimalist.LifeIndex;
import com.baronzhang.android.weather.databinding.ItemLifeIndexBinding;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.baronzhang.android.weather.R.drawable.ic_index_sunscreen;

/**
 * @author baronzhang (baron[dot]zhanglei[at]gmail[dot]com)
 *         2016/12/13
 */
public class LifeIndexAdapter extends BaseRecyclerViewAdapter<LifeIndexAdapter.ViewHolder> {

    private Context context;
    private HomePageViewModel homePageViewModel;

    public LifeIndexAdapter(Context context, HomePageViewModel homePageViewModel) {
        this.context = context;
        this.homePageViewModel=homePageViewModel;
    }

    @Override
    public LifeIndexAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemLifeIndexBinding binding = ItemLifeIndexBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(LifeIndexAdapter.ViewHolder holder, int position) {
        LifeIndex lifeIndex = homePageViewModel.lifeIndices.getValue().get(position);
        holder.bindData(lifeIndex);
        holder.lifeIndexBinding.indexIconImageView.setImageDrawable(getIndexDrawable(context, lifeIndex.getName()));
    }

    @Override
    public int getItemCount() {
        return homePageViewModel.lifeIndices.getValue().size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ItemLifeIndexBinding lifeIndexBinding;
        public ViewHolder(ItemLifeIndexBinding binding) {
            super(binding.getRoot());
            this.lifeIndexBinding=binding;
        }
        public void bindData(LifeIndex lifeIndex){
            lifeIndexBinding.setLifeIndex(lifeIndex);
        }
    }

    private Drawable getIndexDrawable(Context context, String indexName) {


        int colorResourceId = ic_index_sunscreen;
        if (indexName.contains("防晒")) {
            colorResourceId = ic_index_sunscreen;
        } else if (indexName.contains("穿衣")) {
            colorResourceId = R.drawable.ic_index_dress;
        } else if (indexName.contains("运动")) {
            colorResourceId = R.drawable.ic_index_sport;
        } else if (indexName.contains("逛街")) {
            colorResourceId = R.drawable.ic_index_shopping;
        } else if (indexName.contains("晾晒")) {
            colorResourceId = R.drawable.ic_index_sun_cure;
        } else if (indexName.contains("洗车")) {
            colorResourceId = R.drawable.ic_index_car_wash;
        } else if (indexName.contains("感冒")) {
            colorResourceId = R.drawable.ic_index_clod;
        } else if (indexName.contains("广场舞")) {
            colorResourceId = R.drawable.ic_index_dance;
        }
        return context.getResources().getDrawable(colorResourceId);
    }
}

package com.baronzhang.android.weather.feature.selectcity;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.annimon.stream.Stream;
import com.baronzhang.android.weather.base.BaseRecyclerViewAdapter;
import com.baronzhang.android.weather.R;
import com.baronzhang.android.weather.data.db.entities.City;
import com.baronzhang.android.weather.databinding.ItemCityBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * @author baronzhang (baron[dot]zhanglei[at]gmail[dot]com)
 * 16/3/16
 */
public class CityListAdapter extends BaseRecyclerViewAdapter<CityListAdapter.ViewHolder> implements Filterable{

    private final SelectCityViewModel viewModel;

    private IOnSelectCity onSelectCity;

    private Filter mFilter;

    private List<City> mFilteredData;

    public void updateFilterdData(List<City> latest){
        this.mFilteredData=latest;
        notifyDataSetChanged();
    }

    public CityListAdapter(SelectCityViewModel viewModel,IOnSelectCity onSelectCity) {
        this.viewModel = viewModel;
        this.onSelectCity=onSelectCity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCityBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_city, parent, false);
        return new ViewHolder(binding, viewModel,onSelectCity);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        City city = mFilteredData.get(position);
        holder.bind(city);
    }

    @Override
    public int getItemCount() {
        return mFilteredData.size();
    }

    @Override
    public Filter getFilter() {
        if(mFilter==null)
            mFilter=new RecyclerViewFilter(viewModel);
        return mFilter;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ItemCityBinding binding;
        private SelectCityViewModel viewModel;
        private IOnSelectCity onSelectCity;

        ViewHolder(ItemCityBinding binding, SelectCityViewModel viewModel, IOnSelectCity onSelectCity) {
            super(binding.getRoot());
            this.binding = binding;
            this.viewModel = viewModel;
            this.onSelectCity=onSelectCity;
        }

        public void bind(City city) {
            binding.setCity(city);
            binding.getRoot().setOnClickListener(v-> {
                if(onSelectCity!=null)
                    onSelectCity.onSelectCity(String.valueOf(city.getCityId()));
            });
            binding.executePendingBindings();
        }

    }

    private class RecyclerViewFilter extends Filter {
        private SelectCityViewModel viewModel;

        public RecyclerViewFilter(SelectCityViewModel viewModel) {
            this.viewModel = viewModel;
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            //返回的results即为过滤后的ArrayList<City>
            FilterResults results = new FilterResults();

            //无约束字符串则返回null
            if (charSequence == null || charSequence.length() == 0) {
                results.values = null;
                results.count = 0;
            } else {
                String prefixString = charSequence.toString().toLowerCase();
                //新建Values存放过滤后的数据
                ArrayList<City> newValues = new ArrayList<>();
                Stream.of(viewModel.items.getValue())
                        .filter(city -> (city.getCityName().contains(prefixString)
                                || city.getCityNameEn().contains(prefixString) || city.getParent().contains(prefixString)
                                || city.getRoot().contains(prefixString)))
                        .forEach(newValues::add);
                results.values = newValues;
                results.count = newValues.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mFilteredData = (List<City>) filterResults.values;
            if (filterResults.count > 0) {
                notifyDataSetChanged();//重绘当前可见区域
            } else {
                mFilteredData = viewModel.items.getValue();
                notifyDataSetChanged();//会重绘控件（还原到初始状态）
            }
        }
    }
}

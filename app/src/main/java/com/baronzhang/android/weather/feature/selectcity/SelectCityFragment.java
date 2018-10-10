package com.baronzhang.android.weather.feature.selectcity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baronzhang.android.weather.base.BaseFragment;
import com.baronzhang.android.weather.R;
import com.baronzhang.android.weather.data.db.entities.City;
import com.baronzhang.android.library.view.DividerItemDecoration;
import com.baronzhang.android.weather.databinding.FragmentSelectCityBinding;

import java.io.InvalidClassException;
import java.util.Objects;

/**
 * @author baronzhang (baron[dot]zhanglei[at]gmail[dot]com)
 */
public class SelectCityFragment extends BaseFragment implements IOnSelectCity {

    public CityListAdapter cityListAdapter;
    private SelectCityViewModel selectCityViewModel;
    FragmentSelectCityBinding binding;

    public SelectCityFragment() {
    }

    public static SelectCityFragment newInstance() {
        return new SelectCityFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_select_city, container, false);

        cityListAdapter = new CityListAdapter(selectCityViewModel,this);
        selectCityViewModel.items.observe(this, cities -> {
            cityListAdapter.updateFilterdData(cities);
            cityListAdapter.notifyDataSetChanged();
        });

        binding.setViewModel(selectCityViewModel);
        binding.rvCityList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvCityList.setAdapter(cityListAdapter);
        binding.rvCityList.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getActivity()), DividerItemDecoration.VERTICAL_LIST));
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        selectCityViewModel.loadCities(getContext());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void setViewModel(SelectCityViewModel selectCityViewModel) {
        this.selectCityViewModel = selectCityViewModel;
    }

    @Override
    public void onSelectCity(City city) {
        try {
            selectCityViewModel.saveCurrentCityToPreference(String.valueOf(city.getCityId()));
            Toast.makeText(this.getActivity(), city.getCityName(), Toast.LENGTH_LONG).show();
        } catch (InvalidClassException e) {
            e.printStackTrace();
        } finally {
            Objects.requireNonNull(getActivity()).finish();
        }
    }
}

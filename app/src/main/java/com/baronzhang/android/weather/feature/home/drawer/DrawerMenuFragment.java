package com.baronzhang.android.weather.feature.home.drawer;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baronzhang.android.weather.BR;
import com.baronzhang.android.weather.base.BaseFragment;
import com.baronzhang.android.weather.databinding.FragmentDrawerMenuBinding;
import com.baronzhang.android.weather.feature.selectcity.SelectCityActivity;
import com.baronzhang.android.weather.new_data.entity.Weather;

import java.io.InvalidClassException;

public class DrawerMenuFragment extends BaseFragment implements IOnDeleteCity {

    private static final String ARG_COLUMN_COUNT = "column-count";

    private int columnCount = 3;
    private CityManagerAdapter cityManagerAdapter;

    private FragmentDrawerMenuBinding binding;
    private DrawerMenuViewModel viewModel;

    public DrawerMenuFragment() {
    }

    public static DrawerMenuFragment newInstance(int columnCount) {
        DrawerMenuFragment fragment = new DrawerMenuFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnSelectCity) {
//            onSelectCity = (OnSelectCity) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            columnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDrawerMenuBinding.inflate(LayoutInflater.from(getContext()));

        binding.head.addCityBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SelectCityActivity.class);
            startActivity(intent);
        });

        if (columnCount <= 1) {
            binding.rvCityManager.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            binding.rvCityManager.setLayoutManager(new GridLayoutManager(getContext(), columnCount));
        }
        binding.rvCityManager.setItemAnimator(new DefaultItemAnimator());
        cityManagerAdapter = new CityManagerAdapter(viewModel, this);
//        cityManagerAdapter.setOnItemClickListener(new CityManagerAdapter.OnCityManagerItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                try {
//                    presenter.saveCurrentCityToPreference(weatherList.get(position).getCityId());
//                    onSelectCity.onSelect(weatherList.get(position).getCityId());
//                } catch (InvalidClassException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onDeleteClick(String cityId) {
//                presenter.deleteCity(cityId);
//            }
//        });
        binding.rvCityManager.setAdapter(cityManagerAdapter);
        binding.setVariable(BR.viewModel, viewModel);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel.weathers.observe(this, it -> {
            cityManagerAdapter.replaceData(it);
        });
        viewModel.loadSavedCities();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onDelete(Weather weather) {
        viewModel.deleteCity(weather);
    }

    @Override
    public void onSelect(Weather weather) {
        try {
            String cityId=weather.getCityId();
            if(!TextUtils.equals(cityId,viewModel.currentCity.getValue())){
                viewModel.saveCurrentCityToPreference(cityId);//event to viewModel
                viewModel.currentCity.setValue(cityId);
            }
        } catch (InvalidClassException e) {
            e.printStackTrace();
        }
    }

    public void setViewModel(DrawerMenuViewModel drawerMenuViewModel) {
        this.viewModel = drawerMenuViewModel;
    }
}

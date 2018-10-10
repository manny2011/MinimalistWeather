package com.baronzhang.android.weather.feature.home;

import android.content.Context;
import android.os.Bundle;
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
import com.baronzhang.android.weather.data.preference.PreferenceHelper;
import com.baronzhang.android.weather.data.preference.WeatherSettings;
import com.baronzhang.android.weather.databinding.FragmentHomePageBinding;
import com.baronzhang.android.weather.new_data.entity.AirQualityLive;

public class HomePageFragment extends BaseFragment {

    private DetailAdapter detailAdapter;
    private ForecastAdapter forecastAdapter;
    private LifeIndexAdapter lifeIndexAdapter;
    private FragmentHomePageBinding binding;

    public void setViewModel(HomePageViewModel homePageViewModel) {
        this.homePageViewModel = homePageViewModel;
    }

    HomePageViewModel homePageViewModel;

    public HomePageFragment() {

    }

    public static HomePageFragment newInstance() {
        return new HomePageFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            onFragmentInteractionListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomePageBinding.inflate(inflater, container, false);

        //天气详情
        binding.detailRecyclerView.setNestedScrollingEnabled(false);
        binding.detailRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        detailAdapter = new DetailAdapter(homePageViewModel);
        detailAdapter.setOnItemClickListener((adapterView, view, i, l) -> {
        });
        binding.forecastRecyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.detailRecyclerView.setAdapter(detailAdapter);

        //天气预报
        binding.forecastRecyclerView.setNestedScrollingEnabled(false);
        binding.forecastRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        forecastAdapter = new ForecastAdapter(homePageViewModel);
        forecastAdapter.setOnItemClickListener((adapterView, view, i, l) -> {
        });
        binding.forecastRecyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.forecastRecyclerView.setAdapter(forecastAdapter);

        //生活指数
        binding.lifeIndexRecyclerView.setNestedScrollingEnabled(false);
        binding.lifeIndexRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        lifeIndexAdapter = new LifeIndexAdapter(getActivity(), homePageViewModel);
//        lifeIndexAdapter.setOnItemClickListener((adapterView, view, i, l) -> Toast.makeText(HomePageFragment.this.getContext(), lifeIndices.get(i).getDetails(), Toast.LENGTH_LONG).show());
        binding.lifeIndexRecyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.lifeIndexRecyclerView.setAdapter(lifeIndexAdapter);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        homePageViewModel.weather.observe(this, it -> {
            AirQualityLive airQualityLive = it.getAirQualityLive();
            binding.indicatorViewAqi.setIndicatorValue(airQualityLive.getAqi());
            binding.tvAdvice.setText(airQualityLive.getAdvice());
            binding.tvAqi.setText(String.valueOf(airQualityLive.getAqi()));
            binding.tvQuality.setText(airQualityLive.getQuality());
            String rank = airQualityLive.getCityRank();
            binding.tvCityRank.setText(TextUtils.isEmpty(rank) ? "首要污染物: " + airQualityLive.getPrimary() : rank);
//            //通知Activity去更新界面?
            detailAdapter.replaceData(it.getWeatherDetails());
            forecastAdapter.replaceData(it.getWeatherForecasts());
            lifeIndexAdapter.replaceData(it.getLifeIndexes());

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        String cityId = PreferenceHelper.getSharedPreferences().getString(WeatherSettings.SETTINGS_CURRENT_CITY_ID.getId(), "");
        homePageViewModel.loadWeather(cityId, false);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}

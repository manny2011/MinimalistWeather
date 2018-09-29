package com.baronzhang.android.weather.feature.selectcity;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.baronzhang.android.library.util.ActivityUtils;
import com.baronzhang.android.weather.R;
import com.baronzhang.android.weather.base.BaseActivity;
import com.baronzhang.android.weather.databinding.ActivitySelectCityBinding;
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;


/**
 * @author baronzhang (baron[dot]zhanglei[at]gmail[dot]com ==>> baronzhang.com)
 */
public class SelectCityActivity extends BaseActivity {

    SelectCityFragment selectCityFragment;
    private SelectCityViewModel selectCityViewModel;
    private ActivitySelectCityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_select_city);
        setupToolBar();

        selectCityFragment = (SelectCityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (selectCityFragment == null) {
            selectCityFragment = SelectCityFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), selectCityFragment, R.id.fragment_container);
        }

        selectCityViewModel = ViewModelProviders.of(this).get(SelectCityViewModel.class);
        selectCityFragment.setViewModel(selectCityViewModel);

    }

    private void setupToolBar() {
        setSupportActionBar(binding.head.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_city, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("CheckResult")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            SearchView searchView = (SearchView) item.getActionView();
            RxSearchView.queryTextChanges(searchView)
                    .map(charSequence -> charSequence == null ? null : charSequence.toString().trim())
                    .throttleLast(100, TimeUnit.MILLISECONDS)
                    .debounce(100, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(searchText -> selectCityFragment.cityListAdapter.getFilter().filter(searchText));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
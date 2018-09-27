package com.baronzhang.android.weather.feature.home;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.baronzhang.android.weather.base.BaseActivity;
import com.baronzhang.android.library.util.ActivityUtils;
import com.baronzhang.android.library.util.DateConvertUtils;
import com.baronzhang.android.weather.R;
import com.baronzhang.android.weather.data.db.entities.minimalist.Weather;
import com.baronzhang.android.weather.databinding.ActivityMainBinding;
import com.baronzhang.android.weather.feature.home.drawer.DrawerMenuFragment;
import com.baronzhang.android.weather.feature.home.drawer.DrawerMenuViewModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.BindView;

/**
 * 首页
 *
 * @author baronzhang (baron[dot]zhanglei[at]gmail[dot]com)
 */
public class MainActivity extends BaseActivity {

    private HomePageViewModel homePageViewModel;
    ActivityMainBinding mainBinding;
    private DrawerMenuViewModel drawerMenuViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        ActivityMainBinding mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
//         mainBinding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setSupportActionBar(mainBinding.appBar.toolbar);
        //设置 Header 为 Material风格
        ClassicsHeader header = new ClassicsHeader(this);
        header.setPrimaryColors(this.getResources().getColor(R.color.colorPrimary), Color.WHITE);
        mainBinding.appBar.refreshLayout.setRefreshHeader(header);

        mainBinding.appBar.refreshLayout.setOnRefreshListener(refreshlayout -> {
            homePageViewModel.loadWeather(drawerMenuViewModel.currentCity.getValue(),true);
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mainBinding.drawerLayout, mainBinding.appBar.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert mainBinding.drawerLayout != null;
        mainBinding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        HomePageFragment homePageFragment = (HomePageFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (homePageFragment == null) {
            homePageFragment = HomePageFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), homePageFragment, R.id.fragment_container);
        }

        homePageViewModel = new HomePageViewModel();
        homePageViewModel=ViewModelProviders.of(this).get(HomePageViewModel.class);
        homePageFragment.setViewModel(homePageViewModel);
        mainBinding.appBar.refreshLayout.setOnRefreshListener(refreshLayout -> homePageViewModel.loadWeather(drawerMenuViewModel.currentCity.getValue(), true));

        homePageViewModel.weather.observe(this, it -> {
            if(mainBinding.appBar.refreshLayout.isRefreshing()){
                mainBinding.appBar.refreshLayout.finishRefresh(true);
            }
            mainBinding.appBar.toolbar.setTitle(it.getCityName());
            mainBinding.appBar.collapsingToolbar.setTitle(it.getCityName());
            mainBinding.appBar.tempTextView.setText(it.getWeatherLive().getTemp());
            mainBinding.appBar.publishTimeTextView.setText(getString(R.string.string_publish_time) + DateConvertUtils.timeStampToDate(it.getWeatherLive().getTime(), DateConvertUtils.DATA_FORMAT_PATTEN_YYYY_MM_DD_HH_MM));
            drawerMenuViewModel.loadSavedCities();
        });


        DrawerMenuFragment drawerMenuFragment = (DrawerMenuFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_drawer_menu);
        drawerMenuViewModel = ViewModelProviders.of(this).get(DrawerMenuViewModel.class);
        if (drawerMenuFragment == null) {
            drawerMenuFragment = DrawerMenuFragment.newInstance(1);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), drawerMenuFragment, R.id.fragment_container_drawer_menu);
        }
        drawerMenuViewModel.currentCity.observe(this,it->{
            homePageViewModel.loadWeather(it,true);
            mainBinding.drawerLayout.closeDrawer(GravityCompat.START);
        });
        drawerMenuFragment.setViewModel(drawerMenuViewModel);
    }

    @Override
    public void onBackPressed() {

        if (mainBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mainBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_about) {
            return true;
        } else if (id == R.id.action_feedback) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}

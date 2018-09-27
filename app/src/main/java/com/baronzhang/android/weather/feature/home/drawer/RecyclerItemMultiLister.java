package com.baronzhang.android.weather.feature.home.drawer;

import com.baronzhang.android.weather.data.db.entities.City;

public interface RecyclerItemMultiLister {
    void add();

    void delete(City city);

    void navigateTo(City city);
}

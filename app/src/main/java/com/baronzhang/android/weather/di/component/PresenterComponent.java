package com.baronzhang.android.weather.di.component;

import com.baronzhang.android.weather.di.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author 张磊 (baron[dot]zhanglei[at]gmail[dot]com)
 *         2016/12/2
 */
@Singleton
@Component(modules = {ApplicationModule.class})
public interface PresenterComponent {


//    void inject(DrawerMenuPresenter presenter);
}
 
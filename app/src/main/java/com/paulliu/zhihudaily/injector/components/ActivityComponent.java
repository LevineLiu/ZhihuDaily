package com.paulliu.zhihudaily.injector.components;

import android.app.Activity;

import com.paulliu.zhihudaily.injector.ActivityScope;
import com.paulliu.zhihudaily.injector.modules.ActivityModule;
import com.paulliu.zhihudaily.ui.BaseAppCompatActivity;
import com.paulliu.zhihudaily.ui.activities.NewsDetailActivity;
import com.paulliu.zhihudaily.ui.activities.SplashActivity;
import com.paulliu.zhihudaily.ui.fragments.HomeFragment;

import dagger.Component;

/**
 * Created on 2017/1/11
 *
 * @author LLW
 */
@ActivityScope
@Component(modules = {ActivityModule.class},dependencies = {AppComponent.class})
public interface ActivityComponent {
    void inject(BaseAppCompatActivity activity);
    void inject(SplashActivity activity);
    void inject(NewsDetailActivity activity);
    void inject(HomeFragment fragment);
}

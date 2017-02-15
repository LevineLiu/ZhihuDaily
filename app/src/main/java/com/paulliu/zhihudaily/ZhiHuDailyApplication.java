package com.paulliu.zhihudaily;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.paulliu.zhihudaily.constant.SharedPreferenceConstant;
import com.paulliu.zhihudaily.injector.components.AppComponent;
import com.paulliu.zhihudaily.injector.components.DaggerAppComponent;
import com.paulliu.zhihudaily.injector.modules.AppModule;
import com.paulliu.zhihudaily.injector.modules.ZhiHuApiModule;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

/**
 * Created on 2017/1/10
 *
 * @author LLW
 */

public class ZhiHuDailyApplication extends Application{
    private final static int MAX_MEMORY_CACHE = 20 * 1024;
    private static AppComponent mAppComponent;
    @Override
    public void onCreate() {
        super.onCreate();
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .zhiHuApiModule(new ZhiHuApiModule())
                .build();
        Picasso picasso = new Picasso.Builder(this)
                .memoryCache(new LruCache(MAX_MEMORY_CACHE))
                .build();
        Picasso.setSingletonInstance(picasso);
        if(mAppComponent.getSharedPreferences().getBoolean(SharedPreferenceConstant.IS_DAY_MODE, true))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

    }

    public AppComponent getAppComponent(){
        return mAppComponent;
    }
}

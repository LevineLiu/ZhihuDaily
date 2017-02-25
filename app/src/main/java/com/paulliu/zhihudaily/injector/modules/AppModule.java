package com.paulliu.zhihudaily.injector.modules;

import android.content.Context;
import android.content.SharedPreferences;

import com.paulliu.zhihudaily.ZhiHuDailyApplication;
import com.paulliu.zhihudaily.db.ZhihuDailyDbManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 2017/1/10
 *
 * @author LLW
 */

@Module
public class AppModule {
    private ZhiHuDailyApplication mApplication;
    private final static String SHARED_PREFERENCE_NAME = "ZhiHuDailySharedPreference";

    public AppModule(ZhiHuDailyApplication application){
        mApplication = application;
    }

    @Singleton
    @Provides
    public ZhiHuDailyApplication provideApplicationContext(){
        return mApplication;
    }

    @Singleton
    @Provides
    public SharedPreferences provideSharedPreferences(){
        return mApplication.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    @Singleton
    @Provides
    public ZhihuDailyDbManager provideDbManager(){
        return new ZhihuDailyDbManager(mApplication);
    }
}

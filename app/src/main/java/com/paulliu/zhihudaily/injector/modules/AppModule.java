package com.paulliu.zhihudaily.injector.modules;

import com.paulliu.zhihudaily.ZhiHuDailyApplication;

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

    public AppModule(ZhiHuDailyApplication application){
        mApplication = application;
    }

    @Singleton
    @Provides
    public ZhiHuDailyApplication provideApplicationContext(){
        return mApplication;
    }
}

package com.paulliu.zhihudaily.injector.modules;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.paulliu.zhihudaily.BuildConfig;
import com.paulliu.zhihudaily.api.ZhiHuDailyApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created on 2017/1/11
 *
 * @author LLW
 */
@Module
public class ZhiHuApiModule {

    @Singleton
    @Provides
    public ZhiHuDailyApi provideZhiHuDailyApi(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ZhiHuDailyApi.class);
    }
}

package com.paulliu.zhihudaily.injector.components;

import android.content.SharedPreferences;

import com.paulliu.zhihudaily.ZhiHuDailyApplication;
import com.paulliu.zhihudaily.api.ZhiHuDailyApi;
import com.paulliu.zhihudaily.db.ZhihuDailyDbManager;
import com.paulliu.zhihudaily.injector.modules.AppModule;
import com.paulliu.zhihudaily.injector.modules.ZhiHuApiModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created on 2017/1/10
 *
 * @author LLW
 */
@Singleton
@Component(modules = {AppModule.class, ZhiHuApiModule.class})
public interface AppComponent {
    ZhiHuDailyApplication getApplication();
    ZhiHuDailyApi getZhiHuAPi();
    SharedPreferences getSharedPreferences();
    ZhihuDailyDbManager getDbManager();
}

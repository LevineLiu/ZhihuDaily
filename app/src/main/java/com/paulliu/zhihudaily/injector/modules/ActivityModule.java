package com.paulliu.zhihudaily.injector.modules;

import android.app.Activity;
import android.content.Context;

import com.paulliu.zhihudaily.injector.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 2017/1/10
 *
 * @author LLW
 */
@Module
public class ActivityModule {
    private Activity mActivity;

    public ActivityModule(Activity activity){
        mActivity = activity;
    }

    @ActivityScope
    @Provides
    public Context provideActivityContext(){
        return mActivity;
    }
}

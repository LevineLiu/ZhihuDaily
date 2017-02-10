package com.paulliu.zhihudaily.mvp.interactor;

import android.content.Context;

import com.paulliu.zhihudaily.R;
import com.paulliu.zhihudaily.api.ZhiHuDailyApi;
import com.paulliu.zhihudaily.entities.SplashImage;
import com.paulliu.zhihudaily.mvp.ICommonInteractor;

import java.util.Calendar;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created on 2017/1/13
 *
 * @author LLW
 */

public class SplashInteractor implements ICommonInteractor<SplashImage>{
    private ZhiHuDailyApi mApi;

    @Inject
    public SplashInteractor(ZhiHuDailyApi api){
        mApi = api;
    }

    @Override
    public Observable<SplashImage> createObservable() {
        return mApi.getStartImage("1080*1920")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public int getSplashBackgroundResId(){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if(hour >= 6 && hour <= 12)
            return R.drawable.morning;
        else if(hour > 12 && hour <= 18)
            return R.drawable.afternoon;
        else
            return R.drawable.night;
    }
}

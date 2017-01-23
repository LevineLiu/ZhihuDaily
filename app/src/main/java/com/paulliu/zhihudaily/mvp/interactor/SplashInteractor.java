package com.paulliu.zhihudaily.mvp.interactor;

import com.paulliu.zhihudaily.api.ZhiHuDailyApi;
import com.paulliu.zhihudaily.entities.SplashImage;
import com.paulliu.zhihudaily.mvp.ICommonInteractor;

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
}

package com.paulliu.zhihudaily.mvp.interactor;

import com.paulliu.zhihudaily.api.ZhiHuDailyApi;
import com.paulliu.zhihudaily.entity.DailyNews;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created on 2017/1/17
 *
 * @author LLW
 */

public class HomeFragmentInteractor {
    private ZhiHuDailyApi mApi;

    @Inject
    public HomeFragmentInteractor(ZhiHuDailyApi api){
        mApi = api;
    }

    public Observable<DailyNews> createLatestNewsObservable(){
        return mApi.getLatestNews()
                   .subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<DailyNews> createBeforeNewsObservable(String date){
        return mApi.getBeforeNews(date)
                   .subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread());
    }
}

package com.paulliu.zhihudaily.mvp.interactor;

import com.paulliu.zhihudaily.api.ZhiHuDailyApi;
import com.paulliu.zhihudaily.entity.NewsDetailEntity;
import com.paulliu.zhihudaily.entity.NewsExtraEntity;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created on 2017/1/23
 *
 * @author LLW
 */

public class NewsDetailInteractor{
    private ZhiHuDailyApi mApi;

    @Inject
    public NewsDetailInteractor(ZhiHuDailyApi api){
        mApi = api;
    }

    public Observable<NewsDetailEntity> createNewsDetailObservable(int id) {
        return mApi.getNewsDetail(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<NewsExtraEntity> createNewsExtraObservable(int id) {
        return mApi.getNewsExtra(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    }
}

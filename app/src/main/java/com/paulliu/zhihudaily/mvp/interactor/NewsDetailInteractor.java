package com.paulliu.zhihudaily.mvp.interactor;

import com.paulliu.zhihudaily.api.ZhiHuDailyApi;
import com.paulliu.zhihudaily.entities.NewsDetailEntity;
import com.paulliu.zhihudaily.mvp.ICommonInteractor;

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
        return mApi.getNewsDetial(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}

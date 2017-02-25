package com.paulliu.zhihudaily.mvp.interactor;

import com.paulliu.zhihudaily.api.ZhiHuDailyApi;
import com.paulliu.zhihudaily.db.ZhihuDailyDbManager;
import com.paulliu.zhihudaily.entity.NewsEntity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created on 2017/2/25.
 *
 * @author Levine
 */

public class MyCollectionInteractor {
    private ZhihuDailyDbManager mManager;

    @Inject
    public MyCollectionInteractor(ZhihuDailyDbManager zhihuDailyDbManager){
        this.mManager = zhihuDailyDbManager;
    }

    public Observable<List<NewsEntity>> createCollectionObservable(final int startIndex, final int endIndex){
        return Observable.create(new ObservableOnSubscribe<List<NewsEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<List<NewsEntity>> e) throws Exception {
                e.onNext(mManager.getFavoriteNewsList(startIndex, endIndex));
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}

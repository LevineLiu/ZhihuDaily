package com.paulliu.zhihudaily.mvp.interactor;

import com.paulliu.zhihudaily.api.ZhiHuDailyApi;
import com.paulliu.zhihudaily.entity.CommentListEntity;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created on 2017/2/10
 *
 * @author LLW
 */

public class CommentListInteractor {
    private ZhiHuDailyApi mApi;

    @Inject
    public CommentListInteractor(ZhiHuDailyApi api){
        mApi = api;
    }

    public Observable<CommentListEntity> createLongCommentObservable(int id){
        return mApi.getLongComments(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<CommentListEntity> createShortCommentObservable(int id){
        return mApi.getShortComments(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    }
}

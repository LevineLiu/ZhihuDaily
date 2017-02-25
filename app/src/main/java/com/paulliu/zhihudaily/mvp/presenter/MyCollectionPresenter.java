package com.paulliu.zhihudaily.mvp.presenter;

import com.paulliu.zhihudaily.entity.NewsEntity;
import com.paulliu.zhihudaily.mvp.ICommonListView;
import com.paulliu.zhihudaily.mvp.interactor.MyCollectionInteractor;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created on 2017/2/25.
 *
 * @author Levine
 */

public class MyCollectionPresenter extends CommonPresenter<List<NewsEntity>>{
    private MyCollectionInteractor mInteractor;

    @Inject
    public MyCollectionPresenter(MyCollectionInteractor interactor){
        mInteractor = interactor;
    }

    @Override
    public void initialize() {

    }

    public void getFaivoiteNewsList(final int startIndex, int endIndex){
        mInteractor.createCollectionObservable(startIndex, endIndex).subscribe(new Observer<List<NewsEntity>>() {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(List<NewsEntity> newsEntities) {
                if(mView != null){
                    if(startIndex == 0)
                        ((ICommonListView<List<NewsEntity>>) mView).onRefreshSuccess(newsEntities);
                    else
                        ((ICommonListView<List<NewsEntity>>) mView).onLoadMoreSuccess(newsEntities);
                }
            }

            @Override
            public void onError(Throwable e) {
                if(mView != null){
                    if(startIndex == 0)
                        ((ICommonListView<List<NewsEntity>>) mView).onRefreshFailure(null);
                    else
                        ((ICommonListView<List<NewsEntity>>) mView).onLoadMoreFailure(null);
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }
}

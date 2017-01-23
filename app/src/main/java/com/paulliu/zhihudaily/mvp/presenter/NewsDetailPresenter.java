package com.paulliu.zhihudaily.mvp.presenter;

import com.paulliu.zhihudaily.entities.NewsDetailEntity;
import com.paulliu.zhihudaily.mvp.CommonPresenter;
import com.paulliu.zhihudaily.mvp.ICommonView;
import com.paulliu.zhihudaily.mvp.interactor.NewsDetailInteractor;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created on 2017/1/23
 *
 * @author LLW
 */

public class NewsDetailPresenter extends CommonPresenter<NewsDetailEntity>{
    private NewsDetailInteractor mInteractor;

    @Inject
    public NewsDetailPresenter(NewsDetailInteractor interactor){
        mInteractor = interactor;
    }

    @Override
    public void initialize() {

    }

    public void getNewsDetail(int id){
        mInteractor.createNewsDetailObservable(id)
                .subscribe(new Observer<NewsDetailEntity>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(NewsDetailEntity newsDetailEntity) {
                        if(mView != null)
                            ((ICommonView<NewsDetailEntity>) mView).onSuccess(newsDetailEntity);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(mView != null)
                            ((ICommonView<NewsDetailEntity>) mView).onFailure(null);

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}

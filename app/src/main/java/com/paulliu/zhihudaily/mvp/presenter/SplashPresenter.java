package com.paulliu.zhihudaily.mvp.presenter;

import com.paulliu.zhihudaily.entities.SplashImage;
import com.paulliu.zhihudaily.mvp.CommonPresenter;
import com.paulliu.zhihudaily.mvp.ICommonView;
import com.paulliu.zhihudaily.mvp.interactor.SplashInteractor;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created on 2017/1/11
 *
 * @author LLW
 */

public class SplashPresenter extends CommonPresenter<SplashImage> {
    private SplashInteractor mInteractor;

    @Inject
    public SplashPresenter(SplashInteractor interactor){
        mInteractor = interactor;
    }

    @Override
    public void initialize() {
        mInteractor.createObservable()
                .subscribe(new Observer<SplashImage>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(SplashImage splashImage) {
                        if(mView != null)
                            ((ICommonView<SplashImage>) mView).onSuccess(splashImage);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}

package com.paulliu.zhihudaily.mvp.presenter;

import android.content.Context;

import com.paulliu.zhihudaily.entity.SplashImage;
import com.paulliu.zhihudaily.mvp.ICommonView;
import com.paulliu.zhihudaily.mvp.interactor.SplashInteractor;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created on 2017/1/11
 *
 * @author LLW
 */

public class SplashPresenter extends CommonPresenter<SplashImage> {
    private SplashInteractor mInteractor;
    private Context mContext;

    @Inject
    public SplashPresenter(Context context, SplashInteractor interactor){
        mInteractor = interactor;
        mContext = context;
    }

    @Override
    public void initialize() {
        //该api不稳定，经常获取失败
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
                        if(mView != null)
                            ((ICommonView<SplashImage>) mView).onFailure();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public int getBackgroundResId(){
        return mInteractor.getSplashBackgroundResId();
    }
}

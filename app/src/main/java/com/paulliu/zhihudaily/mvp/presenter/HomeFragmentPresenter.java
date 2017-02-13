package com.paulliu.zhihudaily.mvp.presenter;

import com.paulliu.zhihudaily.entity.DailyNews;
import com.paulliu.zhihudaily.mvp.ICommonListView;
import com.paulliu.zhihudaily.mvp.interactor.HomeFragmentInteractor;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created on 2017/1/17
 *
 * @author LLW
 */

public class HomeFragmentPresenter extends CommonPresenter<DailyNews>{
    private HomeFragmentInteractor mInteractor;

    @Inject
    public HomeFragmentPresenter(HomeFragmentInteractor interactor) {
        mInteractor = interactor;
    }

    @Override
    public void initialize() {

    }

    public void getLatestNews(){
        mInteractor.createLatestNewsObservable()
                   .subscribe(new Observer<DailyNews>() {
                       @Override
                       public void onSubscribe(Disposable d) {
                           mDisposable = d;
                       }

                       @Override
                       public void onNext(DailyNews dailyNews) {
                           if(mView != null)
                               ((ICommonListView<DailyNews>) mView).onRefreshSuccess(dailyNews);
                       }

                       @Override
                       public void onError(Throwable e) {
                           if(mView != null)
                               ((ICommonListView<DailyNews>) mView).onRefreshFailure(null);
                       }

                       @Override
                       public void onComplete() {

                       }
                   });
    }

    public void getBeforeNews(String date){
        mInteractor.createBeforeNewsObservable(date)
                   .subscribe(new Observer<DailyNews>() {
                       @Override
                       public void onSubscribe(Disposable d) {
                           mDisposable = d;
                       }

                       @Override
                       public void onNext(DailyNews dailyNews) {
                           ((ICommonListView<DailyNews>) mView).onLoadMoreSuccess(dailyNews);
                       }

                       @Override
                       public void onError(Throwable e) {
                           ((ICommonListView<DailyNews>) mView).onLoadMoreFailure(null);
                       }

                       @Override
                       public void onComplete() {

                       }
                   });
    }
}

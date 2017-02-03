package com.paulliu.zhihudaily.mvp.presenter;

import com.paulliu.zhihudaily.entities.ThemeEntity;
import com.paulliu.zhihudaily.mvp.CommonPresenter;
import com.paulliu.zhihudaily.mvp.ICommonListView;
import com.paulliu.zhihudaily.mvp.interactor.ThemeFragmentInteractor;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created on 2017/2/3
 *
 * @author LLW
 */

public class ThemeFragmentPresenter extends CommonPresenter<ThemeEntity>{
    ThemeFragmentInteractor mInteractor;

    @Inject
    public ThemeFragmentPresenter(ThemeFragmentInteractor interactor){
        mInteractor = interactor;
    }


    public void getTheme(int themeId){
        mInteractor.createThemeObservable(themeId)
                   .subscribe(new Observer<ThemeEntity>() {
                       @Override
                       public void onSubscribe(Disposable d) {
                           mDisposable = d;
                       }

                       @Override
                       public void onNext(ThemeEntity themeEntity) {
                           if(mView != null)
                               ((ICommonListView<ThemeEntity>) mView).onRefreshSuccess(themeEntity);
                       }

                       @Override
                       public void onError(Throwable e) {

                       }

                       @Override
                       public void onComplete() {

                       }
                   });
    }

    public void getBeforeTheme(int themeId, int storyId){
        mInteractor.createBeforeThemeObservable(themeId, storyId)
                   .subscribe(new Observer<ThemeEntity>() {
                       @Override
                       public void onSubscribe(Disposable d) {
                           mDisposable = d;
                       }

                       @Override
                       public void onNext(ThemeEntity themeEntity) {
                           if(mView != null)
                               ((ICommonListView<ThemeEntity>) mView).onLoadMoreSuccess(themeEntity);
                       }

                       @Override
                       public void onError(Throwable e) {

                       }

                       @Override
                       public void onComplete() {

                       }
                   });
    }

    @Override
    public void initialize() {

    }
}

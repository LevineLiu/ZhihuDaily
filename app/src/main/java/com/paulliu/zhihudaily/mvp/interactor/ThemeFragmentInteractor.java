package com.paulliu.zhihudaily.mvp.interactor;

import com.paulliu.zhihudaily.api.ZhiHuDailyApi;
import com.paulliu.zhihudaily.entities.ThemeEntity;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created on 2017/2/3
 *
 * @author LLW
 */

public class ThemeFragmentInteractor {
    private ZhiHuDailyApi mApi;

    @Inject
    public ThemeFragmentInteractor(ZhiHuDailyApi api){
        mApi = api;
    }

    public Observable<ThemeEntity> createThemeObservable(int themeId){
        return mApi.getTheme(themeId)
                   .subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ThemeEntity> createBeforeThemeObservable(int themeId, int storyId){
        return mApi.getBeforeTheme(themeId, storyId)
                   .subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread());
    }

}

package com.paulliu.zhihudaily.api;

import com.paulliu.zhihudaily.entities.DailyNews;
import com.paulliu.zhihudaily.entities.NewVersion;
import com.paulliu.zhihudaily.entities.NewsDetailEntity;
import com.paulliu.zhihudaily.entities.SplashImage;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created on 2017/1/9
 *
 * @author LLW
 */

public interface ZhiHuDailyApi {

    @GET("start-image/{resolution}")
    Observable<SplashImage> getStartImage(@Path("resolution") String resolution);

    @GET("version/android/2.3.0")
    Observable<NewVersion> getNewVersion();

    @GET("news/latest")
    Observable<DailyNews> getLatestNews();

    @GET("news/before/{date}")
    Observable<DailyNews> getBeforeNews(@Path("date") String date);

    @GET("news/{id}")
    Observable<NewsDetailEntity> getNewsDetial(@Path("id") int id);

}

package com.paulliu.zhihudaily.api;

import com.paulliu.zhihudaily.entity.CommentListEntity;
import com.paulliu.zhihudaily.entity.DailyNews;
import com.paulliu.zhihudaily.entity.NewVersion;
import com.paulliu.zhihudaily.entity.NewsDetailEntity;
import com.paulliu.zhihudaily.entity.NewsExtraEntity;
import com.paulliu.zhihudaily.entity.SplashImage;
import com.paulliu.zhihudaily.entity.ThemeEntity;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created on 2017/1/9
 *
 * @author LLW
 */

public interface ZhiHuDailyApi {

    //启动页
    @GET("start-image/{resolution}")
    Observable<SplashImage> getStartImage(@Path("resolution") String resolution);

    //版本查询
    @GET("version/android/2.3.0")
    Observable<NewVersion> getNewVersion();

    //最新新闻
    @GET("news/latest")
    Observable<DailyNews> getLatestNews();

    //过往新闻
    @GET("news/before/{date}")
    Observable<DailyNews> getBeforeNews(@Path("date") String date);

    //新闻内容
    @GET("news/{id}")
    Observable<NewsDetailEntity> getNewsDetail(@Path("id") int id);

    //主题日报内容列表
    @GET("theme/{themeId}")
    Observable<ThemeEntity> getTheme(@Path("themeId") int themeId);

    //主题日报内容加载更多
    @GET("theme/{themeId}/before/{storyId}")
    Observable<ThemeEntity> getBeforeTheme(@Path("themeId") int themeId, @Path("storyId") int storyId);

    //新闻额外信息（评论数、点赞数等）
    @GET("story-extra/{id}")
    Observable<NewsExtraEntity> getNewsExtra(@Path("id") int id);

    //新闻长评论
    @GET("story/{id}/long-comments")
    Observable<CommentListEntity> getLongComments(@Path("id") int id);

    //新闻短评论
    @GET("story/{id}/short-comments")
    Observable<CommentListEntity> getShortComments(@Path("id") int id);

    //新闻短评论加载更多
    @GET("story/{storyId}/short-comments/before/{commentId}")
    Observable<CommentListEntity> getBeforeShortComments(@Path("storyId") int storyId, @Path("commentId") int commentId);


    //新闻长评论加载更多
    @GET("story/{storyId}/long-comments/before/{commentId}")
    Observable<CommentListEntity> getBeforeLongComments(@Path("storyId") int storyId, @Path("commentId") int commentId);

}

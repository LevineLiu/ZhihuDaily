package com.paulliu.zhihudaily.mvp.view;

import com.paulliu.zhihudaily.entity.NewsDetailEntity;
import com.paulliu.zhihudaily.entity.NewsExtraEntity;
import com.paulliu.zhihudaily.mvp.IView;

/**
 * Created on 2017/2/8
 *
 * @author LLW
 */

public interface INewsDetailView extends IView{
    void getNewsDetailSuccess(NewsDetailEntity result);
    void getNewsDetailFailure();
    void getNewsExtraSuccess(NewsExtraEntity result);
    void getNewsExtraFailure();
}

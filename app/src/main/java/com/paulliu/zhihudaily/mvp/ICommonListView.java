package com.paulliu.zhihudaily.mvp;

/**
 * Created on 16/4/29
 *
 * @author LLW
 */
public interface ICommonListView<T> extends IView<T>{
    void onRefreshSuccess(T result);
    void onRefreshFailure(T result);
    void onLoadMoreSuccess(T result);
    void onLoadMoreFailure(T result);
}

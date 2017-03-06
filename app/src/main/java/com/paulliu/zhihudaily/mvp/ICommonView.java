package com.paulliu.zhihudaily.mvp;


/**
 * Created on 2017/1/13
 *
 * @author LLW
 */

public interface ICommonView<T> extends IView<T>{
    void onSuccess(T result);
    void onFailure();
}

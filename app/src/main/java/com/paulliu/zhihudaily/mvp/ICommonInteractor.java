package com.paulliu.zhihudaily.mvp;

import io.reactivex.Observable;

/**
 * Created on 2017/1/13
 *
 * @author LLW
 */

public interface ICommonInteractor <T>{
    Observable<T> createObservable();
}

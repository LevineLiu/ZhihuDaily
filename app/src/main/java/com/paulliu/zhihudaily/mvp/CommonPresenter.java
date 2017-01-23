package com.paulliu.zhihudaily.mvp;

import io.reactivex.disposables.Disposable;

/**
 * Created on 16/4/26
 *
 * @author LLW
 */
public abstract class CommonPresenter<T> {
    public IView<T> mView;
    public Disposable mDisposable;

    public abstract void initialize();

    public void attachView(IView<T> view){
        mView = view;
    }

    public void onDestroyView(){
        mView = null;
        if(mDisposable != null)
            mDisposable.dispose();
    }
}

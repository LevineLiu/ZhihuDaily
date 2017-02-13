package com.paulliu.zhihudaily.mvp.presenter;

import com.paulliu.zhihudaily.entity.CommentListEntity;
import com.paulliu.zhihudaily.mvp.interactor.CommentListInteractor;
import com.paulliu.zhihudaily.mvp.view.ICommentListView;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created on 2017/2/10
 *
 * @author LLW
 */

public class CommentListPresenter extends CommonPresenter<CommentListEntity>{
    private CommentListInteractor mInteractor;

    @Inject
    public CommentListPresenter(CommentListInteractor interactor){
        mInteractor = interactor;
    }

    @Override
    public void initialize() {

    }

    public void getLongComments(int id){
        mInteractor.createLongCommentObservable(id).subscribe(new Observer<CommentListEntity>() {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(CommentListEntity commentListEntity) {
                if(mView != null)
                    ((ICommentListView) mView).getLongCommentsSuccess(commentListEntity);
            }

            @Override
            public void onError(Throwable e) {
                if(mView != null)
                    ((ICommentListView) mView).getLongCommentsFailure();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void getShortComments(int id){
        mInteractor.createShortCommentObservable(id).subscribe(new Observer<CommentListEntity>() {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(CommentListEntity commentListEntity) {
                if(mView != null)
                    ((ICommentListView) mView).getShortCommentsSuccess(commentListEntity);
            }

            @Override
            public void onError(Throwable e) {
                if(mView != null)
                    ((ICommentListView) mView).getShortCommentsFailure();
            }

            @Override
            public void onComplete() {

            }
        });
    }
}

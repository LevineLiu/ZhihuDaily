package com.paulliu.zhihudaily.mvp.view;

import com.paulliu.zhihudaily.entities.CommentListEntity;
import com.paulliu.zhihudaily.mvp.IView;

/**
 * Created on 2017/2/10
 *
 * @author LLW
 */

public interface ICommentListView extends IView<CommentListEntity>{
    void getLongCommentsSuccess(CommentListEntity result);
    void getLongCommentsFailure();
    void getShortCommentsSuccess(CommentListEntity result);
    void getShortCommentsFailure();
}

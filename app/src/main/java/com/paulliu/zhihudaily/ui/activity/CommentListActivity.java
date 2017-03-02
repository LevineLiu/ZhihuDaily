package com.paulliu.zhihudaily.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.paulliu.zhihudaily.R;
import com.paulliu.zhihudaily.entity.CommentEntity;
import com.paulliu.zhihudaily.entity.CommentListEntity;
import com.paulliu.zhihudaily.entity.NewsExtraEntity;
import com.paulliu.zhihudaily.listener.OnLoadMoreListener;
import com.paulliu.zhihudaily.mvp.presenter.CommentListPresenter;
import com.paulliu.zhihudaily.mvp.view.ICommentListView;
import com.paulliu.zhihudaily.ui.BaseAppCompatActivity;
import com.paulliu.zhihudaily.ui.adapter.CommentListAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created on 2017/2/8
 *
 * @author LLW
 */

public class CommentListActivity extends BaseAppCompatActivity implements OnLoadMoreListener,
        ICommentListView {
    public final static String NEWS_EXTRA = "news_extra";
    public final static String NEWS_ID = "news_id";

    private int mNewsId, mBeforeLongCommentId, mBeforeShortCommentId;
    private boolean mLongCommentLoadMoreEnable, mShortCommentLoadMoreEnable;
    private CommentListAdapter mAdapter;
    private List<CommentEntity> mShortCommentList = new ArrayList<>();
    private NewsExtraEntity mNewsExtraEntity;
    private LinearLayoutManager mLinearLayoutManager;

    @Inject
    CommentListPresenter mPresenter;

    @BindView(R.id.rv_comment_list)
    RecyclerView mRecyclerView;

    @Override
    protected void getBundleExtra(Bundle extra) {
        mNewsId = extra.getInt(NEWS_ID);
        mNewsExtraEntity = extra.getParcelable(NEWS_EXTRA);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_comment_list;
    }

    @Override
    protected void initView() {
        setDisplayHomeAsUp(TextUtils.concat(String.valueOf(mNewsExtraEntity.getComments()), "条点评").toString());
        getActivityComponent().inject(this);
        mPresenter.attachView(this);
        initRecyclerView();
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return false;
    }

    @Override
    public void onLoadMore() {
        if(mLongCommentLoadMoreEnable)
            mPresenter.getBeforeLongComments(mNewsId, mBeforeLongCommentId);
        else
            mPresenter.getBeforeShortComments(mNewsId, mBeforeShortCommentId);
    }

    @Override
    public void getLongCommentsSuccess(CommentListEntity result) {
        List<CommentEntity> list = new ArrayList<>();
        list.add(null);//长评论title
        list.addAll(result.getComments());
        if (result.getComments() == null || result.getComments().size() == 0 || result.getComments().size() == mNewsExtraEntity.getLong_comments()) {
            //长评论加载完成
            mAdapter.setShortCommentHeaderPosition(list.size());
            list.add(null);//短评论title
        } else {
            mBeforeLongCommentId = result.getComments().get(result.getComments().size() - 1).getId();
            mAdapter.setLoadMoreEnable(true);
            mLongCommentLoadMoreEnable = true;
        }
        mAdapter.setData(list);
    }

    @Override
    public void getLongCommentsFailure() {

    }

    @Override
    public void getShortCommentsSuccess(CommentListEntity result) {
        dismissProgressDialog();
        if (result == null)
            return;
        mShortCommentList = result.getComments();
        mAdapter.addData(result.getComments());
        if(mShortCommentList.size() != mNewsExtraEntity.getShort_comments()){
            mBeforeShortCommentId = result.getComments().get(result.getComments().size() - 1).getId();
            mAdapter.setLoadMoreEnable(true);
            mShortCommentLoadMoreEnable = true;
        }else{
            mAdapter.setLoadMoreEnable(false);
            mShortCommentLoadMoreEnable = false;
        }
        //当item已经进入屏幕RecyclerView的smoothScrollToPosition无效，所以这里使用LinearLayoutManager的scrollToPositionWithOffset
        mLinearLayoutManager.scrollToPositionWithOffset(mAdapter.getShortCommentHeaderPosition(), 0);
    }

    @Override
    public void getShortCommentsFailure() {
        dismissProgressDialog();
    }

    @Override
    public void getBeforeLongCommentsSuccess(CommentListEntity result) {
        //长评论加载完成
        if (result.getComments() == null || result.getComments().size() == 0 || mAdapter.getData().size() + result.getComments().size() - 1 == mNewsExtraEntity.getLong_comments()) {
            mAdapter.setLoadMoreEnable(false);
            mLongCommentLoadMoreEnable = false;
            mAdapter.setShortCommentHeaderPosition(mAdapter.getData().size() + result.getComments().size());
            result.getComments().add(null);
        } else{
            mBeforeLongCommentId = result.getComments().get(result.getComments().size()).getId();
        }
        mAdapter.addData(result.getComments());
    }

    @Override
    public void getBeforeLongCommentsFailure() {

    }

    @Override
    public void getBeforeShortCommentsSuccess(CommentListEntity result) {
        mShortCommentList.addAll(result.getComments());
        if (result.getComments() == null || result.getComments().size() == 0 || mShortCommentList.size() == mNewsExtraEntity.getShort_comments()) {
            //短评论加载完成
            mAdapter.setLoadMoreEnable(false);
            mLongCommentLoadMoreEnable = false;
        } else{
            mBeforeShortCommentId = mShortCommentList.get(mShortCommentList.size() - 1).getId();
        }
        mAdapter.addData(result.getComments());
    }

    @Override
    public void getBeforeShortCommentsFailure() {

    }

    private void initRecyclerView() {
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL) {
        });
        mAdapter = new CommentListAdapter(this, mLinearLayoutManager) {
            @Override
            public void foldShortComments() {
                mAdapter.setLoadMoreEnable(false);
                mRecyclerView.smoothScrollToPosition(0);
                mAdapter.removeAll(mShortCommentList);
            }

            @Override
            public void loadShortComments() {
                showProgressDialog();
                mPresenter.getShortComments(mNewsId);
            }
        };
        mAdapter.setHasStableIds(true);
        mAdapter.setOnLoadMoreListener(this);
        mAdapter.setCommentCount(mNewsExtraEntity.getLong_comments(), mNewsExtraEntity.getShort_comments());
        mRecyclerView.addOnScrollListener(mAdapter.getOnScrollListener());
        mRecyclerView.setAdapter(mAdapter);

        mPresenter.getLongComments(mNewsId);
    }

}

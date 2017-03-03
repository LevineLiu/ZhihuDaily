package com.paulliu.zhihudaily.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.paulliu.zhihudaily.R;
import com.paulliu.zhihudaily.constant.Constants;
import com.paulliu.zhihudaily.entity.NewsEntity;
import com.paulliu.zhihudaily.listener.OnListItemClickListener;
import com.paulliu.zhihudaily.listener.OnLoadMoreListener;
import com.paulliu.zhihudaily.mvp.ICommonListView;
import com.paulliu.zhihudaily.mvp.presenter.MyCollectionPresenter;
import com.paulliu.zhihudaily.ui.BaseAppCompatActivity;
import com.paulliu.zhihudaily.ui.adapter.NewsListAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created on 2017/2/25.
 * @author Levine
 */

public class MyCollectionActivity extends BaseAppCompatActivity implements OnListItemClickListener,
        ICommonListView<List<NewsEntity>>, SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener{

    private int startIndex, endIndex = 15;
    private NewsListAdapter mAdapter;

    @BindView(R.id.srl_content_common_list) SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.rv_content_common_list) RecyclerView mRecyclerView;
    @Inject MyCollectionPresenter mPresenter;

    @Override
    protected void getBundleExtra(Bundle extra) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_daily_news;
    }

    @Override
    protected void initView() {
        getActivityComponent().inject(this);
        mPresenter.attachView(this);
        setDisplayHomeAsUp(getString(R.string.my_collection));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mAdapter = new NewsListAdapter(this, layoutManager);
        mAdapter.setHasStableIds(true);
        mAdapter.setOnLoadMoreListener(this);
        mAdapter.setOnListItemClickListener(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addOnScrollListener(mAdapter.getOnScrollListener());
        mRecyclerView.setAdapter(mAdapter);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryNightMode));
        else
            mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
                mPresenter.getFaivoiteNewsList(startIndex, endIndex);
            }
        }, Constants.DELAY_TIME);
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return false;
    }

    @Override
    public void onItemClick(int position) {
        NewsEntity newsEntity = mAdapter.getData().get(position);
        Bundle bundle = new Bundle();
        bundle.putParcelable(NewsDetailActivity.EXTRA_NEWS_ENTITY, newsEntity);
        navigateTo(NewsDetailActivity.class, bundle);
    }

    @Override
    public void onRefresh() {
        startIndex = 0;
        endIndex = Constants.PAGE_SIZE;
        mPresenter.getFaivoiteNewsList(startIndex, endIndex);
    }

    @Override
    public void onLoadMore() {
        mRefreshLayout.setRefreshing(false);
        mPresenter.getFaivoiteNewsList(startIndex, endIndex);
    }

    @Override
    public void onRefreshSuccess(List<NewsEntity> result) {
        mRefreshLayout.setRefreshing(false);
        mAdapter.setData(result);
        if(result != null){
            startIndex = endIndex;
            endIndex = endIndex + result.size();
        }
        if(result == null || result.size() < 15)
            mAdapter.setLoadMoreEnable(false);
        else
            mAdapter.setLoadMoreEnable(true);

    }

    @Override
    public void onRefreshFailure(List<NewsEntity> result) {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoadMoreSuccess(List<NewsEntity> result) {
        mAdapter.addData(result);
        if(result != null){
            startIndex = endIndex;
            endIndex = endIndex + result.size();
        }
        if(result == null || result.size() < 15)
            mAdapter.setLoadMoreEnable(false);
        else
            mAdapter.setLoadMoreEnable(true);
    }

    @Override
    public void onLoadMoreFailure(List<NewsEntity> result) {
        mRefreshLayout.setRefreshing(false);
    }
}

package com.paulliu.zhihudaily.ui.fragments;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.paulliu.zhihudaily.R;
import com.paulliu.zhihudaily.constants.Constants;
import com.paulliu.zhihudaily.entities.DailyNews;
import com.paulliu.zhihudaily.entities.ThemeEntity;
import com.paulliu.zhihudaily.listener.OnListItemClickListener;
import com.paulliu.zhihudaily.listener.OnLoadMoreListener;
import com.paulliu.zhihudaily.mvp.ICommonListView;
import com.paulliu.zhihudaily.mvp.presenter.ThemeFragmentPresenter;
import com.paulliu.zhihudaily.ui.BaseAppCompatActivity;
import com.paulliu.zhihudaily.ui.BaseFragment;
import com.paulliu.zhihudaily.ui.adapter.CommonThemeListAdapter;
import com.paulliu.zhihudaily.ui.adapter.HomeNewsListAdapter;
import com.paulliu.zhihudaily.widgets.DotsIndexer;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created on 2017/2/3
 *
 * @author LLW
 */

public class ThemeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        OnLoadMoreListener, OnListItemClickListener, ICommonListView<ThemeEntity> {

    public final static String EXTRA_THEME_ID = "extra_theme_id";
    private int mThemeId;
    private CommonThemeListAdapter mAdapter;

    @Inject ThemeFragmentPresenter mPresenter;

    @BindView(R.id.srl_content_common_list) SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.rv_content_common_list) RecyclerView mRecyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_common_list;
    }

    @Override
    protected void initView() {
        ((BaseAppCompatActivity) mContext).getActivityComponent().inject(this);
        mPresenter.attachView(this);
        mThemeId = getArguments().getInt(EXTRA_THEME_ID);
        initRefreshLayout();
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onItemClick(Object param) {

    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onRefreshSuccess(ThemeEntity result) {

    }

    @Override
    public void onRefreshFailure(ThemeEntity result) {

    }

    @Override
    public void onLoadMoreSuccess(ThemeEntity result) {

    }

    @Override
    public void onLoadMoreFailure(ThemeEntity result) {

    }

    private void initRefreshLayout() {
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);

        mAdapter = new CommonThemeListAdapter(mContext, layoutManager);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addOnScrollListener(mAdapter.getOnScrollListener());
        mAdapter.setOnLoadMoreListener(this);
        mAdapter.setOnListItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mRefreshLayout != null) {
                    mRefreshLayout.setRefreshing(true);
                    mPresenter.getTheme(mThemeId);
                }
            }
        }, Constants.DELAY_TIME);
    }
}

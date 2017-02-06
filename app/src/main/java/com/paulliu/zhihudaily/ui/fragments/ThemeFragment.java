package com.paulliu.zhihudaily.ui.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.paulliu.zhihudaily.R;
import com.paulliu.zhihudaily.constants.Constants;
import com.paulliu.zhihudaily.entities.EditorEntity;
import com.paulliu.zhihudaily.entities.NewsEntity;
import com.paulliu.zhihudaily.entities.ThemeEntity;
import com.paulliu.zhihudaily.listener.OnListItemClickListener;
import com.paulliu.zhihudaily.listener.OnLoadMoreListener;
import com.paulliu.zhihudaily.mvp.ICommonListView;
import com.paulliu.zhihudaily.mvp.presenter.ThemeFragmentPresenter;
import com.paulliu.zhihudaily.ui.BaseAppCompatActivity;
import com.paulliu.zhihudaily.ui.BaseFragment;
import com.paulliu.zhihudaily.ui.activities.EditorListActivity;
import com.paulliu.zhihudaily.ui.activities.NewsDetailActivity;
import com.paulliu.zhihudaily.ui.adapter.CommonThemeListAdapter;
import com.paulliu.zhihudaily.widgets.SpeedyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

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
    private int mStoryId;//story id of the last story
    private CommonThemeListAdapter mAdapter;
    private List<EditorEntity> mEditorList;

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
        mPresenter.getTheme(mThemeId);
    }

    @Override
    public void onItemClick(Object param) {
        NewsEntity entity = (NewsEntity) param;
        Bundle bundle = new Bundle();
        bundle.putInt(NewsDetailActivity.NEWS_DETAIL_ID, entity.getId());
        navigateTo(NewsDetailActivity.class, bundle);
    }

    @Override
    public void onLoadMore() {
        mRefreshLayout.setRefreshing(false);
        mPresenter.getBeforeTheme(mThemeId, mStoryId);
    }

    @Override
    public void onRefreshSuccess(ThemeEntity result) {
        mRefreshLayout.setRefreshing(false);
        if(result == null)
            return;
        mEditorList = result.getEditors();
        List<NewsEntity> stories = result.getStories();
        mAdapter.setHeaderInfo(result);
        mAdapter.setData(stories);
        if(stories != null && stories.size() != 0){
            mAdapter.setLoadMoreEnable(true);
            mStoryId = stories.get(stories.size() - 1).getId();
        }
        else
            mAdapter.setLoadMoreEnable(false);
    }

    @Override
    public void onRefreshFailure(ThemeEntity result) {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoadMoreSuccess(ThemeEntity result) {
        if(result == null)
            return;
        List<NewsEntity> stories = result.getStories();
        mAdapter.addData(stories);
        if(stories != null && stories.size() != 0){
            mAdapter.setLoadMoreEnable(true);
            mStoryId = stories.get(stories.size() - 1).getId();
        }
        else
            mAdapter.setLoadMoreEnable(false);
    }

    @Override
    public void onLoadMoreFailure(ThemeEntity result) {

    }

    private void initRefreshLayout() {
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        SpeedyLinearLayoutManager layoutManager = new SpeedyLinearLayoutManager(mContext);

        mAdapter = new CommonThemeListAdapter(mContext, layoutManager){
            @Override
            public void navigateToEditorList() {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(EditorListActivity.EXTRA_EDITORS, (ArrayList<? extends Parcelable>) mEditorList);
                navigateTo(EditorListActivity.class, bundle);
            }
        };
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

    /**
     * when clicking the toolbar, scroll to top
     */
    public void scrollToTop(){
        mRecyclerView.smoothScrollToPosition(0);
    }
}

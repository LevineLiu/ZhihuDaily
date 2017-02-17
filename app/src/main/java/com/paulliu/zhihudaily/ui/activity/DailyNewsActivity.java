package com.paulliu.zhihudaily.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.paulliu.zhihudaily.R;
import com.paulliu.zhihudaily.constant.Constants;
import com.paulliu.zhihudaily.entity.DailyNews;
import com.paulliu.zhihudaily.entity.NewsEntity;
import com.paulliu.zhihudaily.listener.OnListItemClickListener;
import com.paulliu.zhihudaily.mvp.ICommonListView;
import com.paulliu.zhihudaily.mvp.presenter.HomeFragmentPresenter;
import com.paulliu.zhihudaily.ui.BaseAppCompatActivity;
import com.paulliu.zhihudaily.ui.adapter.NewsListAdapter;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created on 2017/2/7
 *
 * @author LLW
 */

public class DailyNewsActivity extends BaseAppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,
        OnListItemClickListener, ICommonListView<DailyNews> {
    public final static String EXTRA_SELECTED_DATE = "extra_selected_date";

    private long mDate;
    private String mNewsDate;
    private NewsListAdapter mAdapter;

    @Inject
    HomeFragmentPresenter mPresenter;

    @BindView(R.id.srl_content_common_list)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.rv_content_common_list)
    RecyclerView mRecyclerView;

    @Override
    protected void getBundleExtra(Bundle extra) {
        if(extra != null)
            mDate = extra.getLong(EXTRA_SELECTED_DATE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_daily_news;
    }

    @Override
    protected void initView() {
        getActivityComponent().inject(this);
        mPresenter.attachView(this);
        initDate();
        initRefreshLayout();
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return false;
    }

    @Override
    public void onRefresh() {
        mPresenter.getBeforeNews(mNewsDate);
    }

    @Override
    public void onItemClick(int position) {
        NewsEntity entity = mAdapter.getData().get(position);
        Bundle bundle = new Bundle();
        bundle.putInt(NewsDetailActivity.NEWS_DETAIL_ID, entity.getId());
        navigateTo(NewsDetailActivity.class, bundle);
    }

    @Override
    public void onRefreshSuccess(DailyNews result) {

    }

    @Override
    public void onRefreshFailure(DailyNews result) {

    }

    @Override
    public void onLoadMoreSuccess(DailyNews result) {
        mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.setData(result.getStories());
    }

    @Override
    public void onLoadMoreFailure(DailyNews result) {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void initDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(mDate));
        setDisplayHomeAsUp(calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日");
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = calendar.get(Calendar.MONTH) + 1 < 10 ? "0" + (calendar.get(Calendar.MONTH) + 1) : String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String day = calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + calendar.get(Calendar.DAY_OF_MONTH) : String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        mNewsDate = year + month + day;
    }

    private void initRefreshLayout(){
        mSwipeRefreshLayout.setOnRefreshListener(this);
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryNightMode));
        else
            mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mAdapter = new NewsListAdapter(this);
        mAdapter.setOnListItemClickListener(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mSwipeRefreshLayout != null) {
                    mSwipeRefreshLayout.setRefreshing(true);
                    mPresenter.getBeforeNews(mNewsDate);
                }
            }
        }, Constants.DELAY_TIME);
    }
}

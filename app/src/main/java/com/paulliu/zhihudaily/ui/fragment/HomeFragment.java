package com.paulliu.zhihudaily.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.RecyclerView;

import com.paulliu.zhihudaily.R;
import com.paulliu.zhihudaily.constant.Constants;
import com.paulliu.zhihudaily.entity.DailyNews;
import com.paulliu.zhihudaily.entity.NewsEntity;
import com.paulliu.zhihudaily.listener.OnListItemClickListener;
import com.paulliu.zhihudaily.listener.OnLoadMoreListener;
import com.paulliu.zhihudaily.mvp.ICommonListView;
import com.paulliu.zhihudaily.mvp.presenter.HomeFragmentPresenter;
import com.paulliu.zhihudaily.ui.BaseAppCompatActivity;
import com.paulliu.zhihudaily.ui.BaseFragment;
import com.paulliu.zhihudaily.ui.activity.NewsDetailActivity;
import com.paulliu.zhihudaily.ui.adapter.HomeBannerPagerAdapter;
import com.paulliu.zhihudaily.ui.adapter.HomeNewsListAdapter;
import com.paulliu.zhihudaily.widget.DotsIndexer;
import com.paulliu.zhihudaily.widget.SpeedyLinearLayoutManager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created on 2017/1/18
 *
 * @author LLW
 */

public class HomeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        OnLoadMoreListener, OnListItemClickListener, ICommonListView<DailyNews> {
    private final static int SCHEDULED_FIXED_RATE = 5;
    private ScheduledExecutorService mSlideShowService;
    private HomeNewsListAdapter mAdapter;
    private int mCurrentSlidePosition;
    private int mSlidesCount;//banner count
    private String mBeforeDate;


    @Inject
    HomeFragmentPresenter mPresenter;

    @BindView(R.id.srl_content_common_list)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.rv_content_common_list)
    RecyclerView mRecyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.common_list;
    }

    @Override
    protected void initView() {
        ((BaseAppCompatActivity) mContext).getActivityComponent().inject(this);
        mSlideShowService = Executors.newSingleThreadScheduledExecutor();
        mPresenter.attachView(this);
        initRefreshLayout();
    }

    @Override
    public void onDestroyView() {
        if (mSlideShowService != null && !mSlideShowService.isShutdown())
            mSlideShowService.shutdown();
        super.onDestroyView();
    }

    @Override
    public void onRefresh() {
        mPresenter.getLatestNews();
    }

    @Override
    public void onLoadMore() {
        mRefreshLayout.setRefreshing(false);
        mPresenter.getBeforeNews(mBeforeDate);
    }

    @Override
    public void onItemClick(Object param) {
        NewsEntity entity = (NewsEntity) param;
        Bundle bundle = new Bundle();
        bundle.putInt(NewsDetailActivity.NEWS_DETAIL_ID, entity.getId());
        navigateTo(NewsDetailActivity.class, bundle);
    }

    @Override
    public void onRefreshSuccess(DailyNews result) {
        mRefreshLayout.setRefreshing(false);
        mBeforeDate = result.getDate();
        mAdapter.setStories(result.getStories(), result.getTop_stories());
        if (result.getStories() == null || result.getStories().size() == 0)
            mAdapter.setLoadMoreEnable(false);
        else
            mAdapter.setLoadMoreEnable(true);
    }

    @Override
    public void onRefreshFailure(DailyNews result) {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoadMoreSuccess(DailyNews result) {
        mBeforeDate = result.getDate();
        mAdapter.addStories(result.getStories());
        if (result.getStories() == null || result.getStories().size() == 0)
            mAdapter.setLoadMoreEnable(false);
        else
            mAdapter.setLoadMoreEnable(true);
    }

    @Override
    public void onLoadMoreFailure(DailyNews result) {

    }

    private void initRefreshLayout() {
        mRefreshLayout.setOnRefreshListener(this);
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryNightMode));
        else
            mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        SpeedyLinearLayoutManager layoutManager = new SpeedyLinearLayoutManager(mContext);
        mAdapter = new HomeNewsListAdapter(mContext, layoutManager) {
            @Override
            public void initViewPager(ViewPager viewPager, DotsIndexer dotsIndexer) {
                initBanner(viewPager, dotsIndexer);
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
                    mPresenter.getLatestNews();
                }
            }
        }, Constants.DELAY_TIME);
    }

    private void initBanner(final ViewPager viewPager, final DotsIndexer dotsIndexer) {
        mSlidesCount = mAdapter.getTopStoriesCount();
        HomeBannerPagerAdapter adapter = new HomeBannerPagerAdapter(mContext) {
            @Override
            public void onImageClick(int id) {
                Bundle bundle = new Bundle();
                bundle.putInt(NewsDetailActivity.NEWS_DETAIL_ID, id);
                navigateTo(NewsDetailActivity.class, bundle);
            }
        };
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(mSlidesCount);
        int padding = mContext.getResources().getDimensionPixelSize(R.dimen.dot_indexer_height);
        dotsIndexer.initDots(mSlidesCount, padding, padding, padding, padding, R.drawable.bg_dots_indexer);
        adapter.setData(mAdapter.getTopNewsEntityList());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentSlidePosition = position;
                dotsIndexer.updatePageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                viewPager.setCurrentItem(mCurrentSlidePosition);
            }
        };

        viewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!mSlideShowService.isShutdown()) {
                    mSlideShowService.scheduleAtFixedRate(new Runnable() {
                        @Override
                        public void run() {
                            if (mSlidesCount != 0) {
                                mCurrentSlidePosition = (mCurrentSlidePosition + 1) % mSlidesCount;
                                handler.obtainMessage().sendToTarget();
                            }
                        }
                    }, 0, SCHEDULED_FIXED_RATE, TimeUnit.SECONDS);
                }
            }
        }, SCHEDULED_FIXED_RATE * 1000);
    }

    /**
     * when clicking the toolbar, scroll to top
     */
    public void scrollToTop() {
        mRecyclerView.smoothScrollToPosition(0);
    }

}

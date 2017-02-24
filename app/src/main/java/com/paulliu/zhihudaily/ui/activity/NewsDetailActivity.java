package com.paulliu.zhihudaily.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.paulliu.zhihudaily.R;
import com.paulliu.zhihudaily.db.ZhihuDailyDbManager;
import com.paulliu.zhihudaily.entity.NewsDetailEntity;
import com.paulliu.zhihudaily.entity.NewsEntity;
import com.paulliu.zhihudaily.entity.NewsExtraEntity;
import com.paulliu.zhihudaily.mvp.presenter.NewsDetailPresenter;
import com.paulliu.zhihudaily.mvp.view.INewsDetailView;
import com.paulliu.zhihudaily.ui.BaseAppCompatActivity;
import com.paulliu.zhihudaily.widget.WebViewBrowseView;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created on 2017/1/22
 *
 * @author LLW
 */

public class NewsDetailActivity extends BaseAppCompatActivity implements INewsDetailView {
    public final static String EXTRA_NEWS_ENTITY = "news_entity";

    private int mId;
    private boolean mIsNewsCollected;
    private String mShareUrl;
    private NewsEntity mNewsEntity;
    private NewsExtraEntity mNewsExtraEntity;
    private NewsDetailEntity mNewsDetailEntity;

    @BindView(R.id.iv_news_detail) ImageView mNewsDetailIv;
    @BindView(R.id.wv_news_detail) WebViewBrowseView mNewsDetailWv;
    @BindView(R.id.tv_copyright) TextView mCopyrightTv;
    @BindView(R.id.tv_news_detail_title) TextView mNewsDetailTitleTv;
    @BindView(R.id.tv_news_comment) TextView mCommentTv;
    @BindView(R.id.tv_news_like) TextView mLikeTv;
    @BindView(R.id.rl_news_detail_banner) RelativeLayout mBannerRl;
    @BindView(R.id.tv_news_collect) TextView mNewsCollectTv;

    @Inject NewsDetailPresenter mPresenter;
    @Inject ZhihuDailyDbManager mDbManager;

    @OnClick({R.id.tv_news_comment, R.id.tv_news_share, R.id.tv_news_like, R.id.tv_news_collect})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_news_share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, mShareUrl);
                intent.setType("text/plain");
                startActivity(intent);
                break;
            case R.id.tv_news_comment:
                Bundle bundle = new Bundle();
                bundle.putInt(CommentListActivity.NEWS_ID, mId);
                bundle.putParcelable(CommentListActivity.NEWS_EXTRA, mNewsExtraEntity);
                navigateTo(CommentListActivity.class, bundle);
                break;
            case R.id.tv_news_collect:
                if(mNewsDetailEntity != null){
                    if(!mIsNewsCollected){
                        if(mDbManager.insertFavoriteNews(mNewsEntity)){
                            mIsNewsCollected = true;
                            mNewsCollectTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_collected, 0, 0, 0);
                        }
                    }else{
                        if(mDbManager.deleteFavoriteNews(mId)) {
                            mIsNewsCollected = false;
                            mNewsCollectTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_uncollected, 0, 0, 0);
                        }
                    }
                }
                break;
            case R.id.tv_news_like:
                break;
        }
    }

    @Override
    protected void getBundleExtra(Bundle extra) {
        if (extra != null) {
            mNewsEntity = extra.getParcelable(EXTRA_NEWS_ENTITY);
            if(mNewsEntity != null)
                mId = mNewsEntity.getId();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news_detail;
    }

    @Override
    protected void initView() {
        setDisplayHomeAsUp(null);
        initComponent();
    }


    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mNewsDetailWv != null)
            mNewsDetailWv.release();
        if(mPresenter != null)
            mPresenter.onDestroyView();
    }

    @Override
    public void getNewsDetailSuccess(NewsDetailEntity result) {
        if (result != null) {
            mNewsDetailEntity = result;
            mShareUrl = result.getShare_url();
            if (!TextUtils.isEmpty(result.getImage())) {
                Picasso.with(this).load(result.getImage()).into(mNewsDetailIv);
                mCopyrightTv.setText(result.getImage_source());
                mNewsDetailTitleTv.setText(result.getTitle());
            } else
                mBannerRl.setVisibility(View.GONE);
            if (result.getBody() != null) {
                if (result.getCss() != null){
                    //remove headline space
                    String body = result.getBody().replace("<div class=\"headline\">\n" +
                            "\n" + "<div class=\"img-place-holder\"></div>", "");
                    if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
                        mNewsDetailWv.loadNightModeHtml(result.getCss().get(0), body);
                    }
                    else{
                        mNewsDetailWv.loadHtmlWithData(result.getCss().get(0), body);
                    }

                }
                else
                    mNewsDetailWv.loadHtmlWithLocalCss("common.css", result.getBody());
            }else
                mNewsDetailWv.loadUrl(result.getShare_url());
        }
    }

    @Override
    public void getNewsDetailFailure() {

    }

    @Override
    public void getNewsExtraSuccess(NewsExtraEntity result) {
        mNewsExtraEntity = result;
        if(mNewsExtraEntity != null){
            mCommentTv.setText(String.valueOf(mNewsExtraEntity.getComments()));
            mLikeTv.setText(String.valueOf(mNewsExtraEntity.getPopularity()));
        }
    }

    @Override
    public void getNewsExtraFailure() {

    }

    private void initComponent() {
        getActivityComponent().inject(this);
        mPresenter.attachView(this);
        mPresenter.getNewsDetail(mId);
        mPresenter.getNewsExtra(mId);
        mIsNewsCollected = mDbManager.isNewsCollected(mId);
        if(mIsNewsCollected)
            mNewsCollectTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_collected, 0, 0, 0);
        else
            mNewsCollectTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_uncollected, 0, 0, 0);
    }
}

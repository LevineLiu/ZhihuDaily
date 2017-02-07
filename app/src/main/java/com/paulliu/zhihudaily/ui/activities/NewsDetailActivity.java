package com.paulliu.zhihudaily.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.paulliu.zhihudaily.R;
import com.paulliu.zhihudaily.entities.NewsDetailEntity;
import com.paulliu.zhihudaily.mvp.ICommonView;
import com.paulliu.zhihudaily.mvp.presenter.NewsDetailPresenter;
import com.paulliu.zhihudaily.ui.BaseAppCompatActivity;
import com.paulliu.zhihudaily.widgets.WebViewBrowseView;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created on 2017/1/22
 *
 * @author LLW
 */

public class NewsDetailActivity extends BaseAppCompatActivity implements ICommonView<NewsDetailEntity> {
    public final static String NEWS_DETAIL_ID = "news_detail_id";

    private int mId;
    private String mShareUrl;

    @BindView(R.id.iv_news_detail)
    ImageView mNewsDetailIv;
    @BindView(R.id.wv_news_detail)
    WebViewBrowseView mNewsDetailWv;
    @BindView(R.id.tv_copyright)
    TextView mCopyrightTv;
    @BindView(R.id.tv_news_detail_title)
    TextView mNewsDetailTitleTv;
    @BindView(R.id.rl_news_detail_banner)
    RelativeLayout mBannerRl;
    @Inject
    NewsDetailPresenter mPresenter;

    @Override
    protected void getBundleExtra(Bundle extra) {
        if (extra != null) {
            mId = extra.getInt(NEWS_DETAIL_ID);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, mShareUrl);
                intent.setType("text/plain");
                startActivity(intent);
                break;
            case R.id.action_comment:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(NewsDetailEntity result) {
        if (result != null) {
            mShareUrl = result.getShare_url();
            if (!TextUtils.isEmpty(result.getImage())) {
                Picasso.with(this).load(result.getImage()).into(mNewsDetailIv);
                mCopyrightTv.setText(result.getImage_source());
                mNewsDetailTitleTv.setText(result.getTitle());
            } else
                mBannerRl.setVisibility(View.GONE);
            if (result.getBody() != null) {
                if (result.getCss() != null)
                    mNewsDetailWv.loadHtmlWithData(result.getCss().get(0), result.getBody());
                else
                    mNewsDetailWv.loadHtmlWithData(result.getBody());
            }else
                mNewsDetailWv.loadUrl(result.getShare_url());
        }
    }

    @Override
    public void onFailure(NewsDetailEntity result) {

    }

    private void initComponent() {
        getActivityComponent().inject(this);
        mPresenter.attachView(this);
        mPresenter.getNewsDetail(mId);
    }
}

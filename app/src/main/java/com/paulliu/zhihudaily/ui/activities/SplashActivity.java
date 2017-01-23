package com.paulliu.zhihudaily.ui.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.paulliu.zhihudaily.R;
import com.paulliu.zhihudaily.constants.Constants;
import com.paulliu.zhihudaily.entities.SplashImage;
import com.paulliu.zhihudaily.mvp.ICommonView;
import com.paulliu.zhihudaily.mvp.presenter.SplashPresenter;
import com.paulliu.zhihudaily.ui.BaseAppCompatActivity;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created on 2017/1/11
 *
 * @author LLW
 */

public class SplashActivity extends BaseAppCompatActivity implements ICommonView<SplashImage>{
    private final static int HANDLER_WHAT = 0;
    private final static int SPLASH_TIME = 4000;

    @BindView(R.id.iv_splash) ImageView mImageView;
    @BindView(R.id.pgb_splash) ProgressBar mProgressBar;

    @Inject SplashPresenter mPresenter;

    @Override
    protected void getBundleExtra(Bundle extra) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void setContentView(int layoutResID) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.setContentView(layoutResID);
    }

    @Override
    protected void initView() {
        getActivityComponent().inject(this);
        initPresenter();
        ((Handler)getHandlerInstance()).sendEmptyMessageDelayed(HANDLER_WHAT, SPLASH_TIME);
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return false;
    }

    @Override
    protected void onDestroy() {
        if(mPresenter != null)
            mPresenter.onDestroyView();
        super.onDestroy();
    }


    @Override
    public void onSuccess(SplashImage result) {
        Picasso.with(this).load(result.getImg()).into(mImageView);
    }

    @Override
    public void onFailure(SplashImage result) {

    }

    @Override
    public void handleHandlerMessage(Message msg) {
        navigateTo(MainActivity.class);
        finish();
    }

    private void initPresenter(){
        mImageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresenter.attachView(SplashActivity.this);
                mPresenter.initialize();
            }
        }, Constants.DELAY_TIME);
    }
}

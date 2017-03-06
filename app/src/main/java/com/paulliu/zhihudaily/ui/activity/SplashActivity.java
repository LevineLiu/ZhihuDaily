package com.paulliu.zhihudaily.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.paulliu.zhihudaily.R;
import com.paulliu.zhihudaily.constant.Constants;
import com.paulliu.zhihudaily.entity.SplashImage;
import com.paulliu.zhihudaily.mvp.ICommonView;
import com.paulliu.zhihudaily.mvp.presenter.SplashPresenter;
import com.paulliu.zhihudaily.ui.BaseAppCompatActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created on 2017/1/11
 *
 * @author LLW
 */

public class SplashActivity extends BaseAppCompatActivity implements ICommonView<SplashImage>{
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
        Picasso.with(this).load(result.getImg()).into(mImageView, new Callback() {
            @Override
            public void onSuccess() {
                loadAnimation();
            }

            @Override
            public void onError() {
                mImageView.setBackgroundResource(mPresenter.getBackgroundResId());
                loadAnimation();
            }
        });
    }

    @Override
    public void onFailure() {
        mImageView.setBackgroundResource(mPresenter.getBackgroundResId());
        loadAnimation();
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

    private void loadAnimation(){
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(mImageView, "scaleX", 1f, 1.2f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mImageView, "scaleY", 1f, 1.2f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(SPLASH_TIME);
        animatorSet.play(animator1).with(animator2);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                navigateTo(MainActivity.class);
                finish();
            }
        });
        animatorSet.start();
    }
}

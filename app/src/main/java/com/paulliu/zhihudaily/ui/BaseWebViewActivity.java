package com.paulliu.zhihudaily.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebViewClient;

import com.paulliu.zhihudaily.R;
import com.paulliu.zhihudaily.widgets.WebViewBrowseView;


/**
 * Created on 16/6/25
 *
 * @author LLW
 */
public class BaseWebViewActivity extends BaseAppCompatActivity{
    public final static String KEY_WEB_TITLE = "key_web_title";
    public final static String KEY_WEB_URL = "key_web_url";
    public final static String WEB_VIEW_INTERFACE = "YWParkAndroidInterface";

    protected String mTitle;
    protected String mUrl;

    public WebViewBrowseView mBrowseView;


    @Override
    protected void getBundleExtra(Bundle extra) {
        mTitle = getIntent().getStringExtra(KEY_WEB_TITLE);
        mUrl = getIntent().getStringExtra(KEY_WEB_URL);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_base_webview;
    }

    @Override
    protected void initView() {
        mBrowseView = (WebViewBrowseView) findViewById(R.id.wv_content);
        initAction();
        setDisplayHomeAsUp(null);
        mBrowseView.setWebViewClient(new WebViewClient());
        mBrowseView.loadUrl(mUrl);
    }

    protected void initAction(){}

    @Override
    protected void onDestroy() {
        if(mBrowseView != null){
            mBrowseView.removeAllViews();
            mBrowseView.release();
        }
        super.onDestroy();
    }

//    @Override
//    public void finish() {
//        ViewGroup viewGroup = (ViewGroup) getWindow().getDecorView();
//        viewGroup.removeAllViews();
//        super.finish();
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && mBrowseView.canGoBack()){
            mBrowseView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return false;
    }

}

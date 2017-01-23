package com.paulliu.zhihudaily.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;


import com.paulliu.zhihudaily.R;
import com.paulliu.zhihudaily.ZhiHuDailyApplication;
import com.paulliu.zhihudaily.broadcast.NetStateChangeBroadcast;
import com.paulliu.zhihudaily.injector.components.ActivityComponent;
import com.paulliu.zhihudaily.injector.components.DaggerActivityComponent;
import com.paulliu.zhihudaily.injector.modules.ActivityModule;
import com.paulliu.zhihudaily.listener.NetStateListener;
import com.paulliu.zhihudaily.utils.AppManager;
import com.paulliu.zhihudaily.utils.GlobalProgressDialog;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created on 16/4/26
 *
 * @author LLW
 */
public abstract class BaseAppCompatActivity extends AppCompatActivity implements NetStateListener {
    protected String TAG;
    protected int mScreenWidth;
    protected int mScreenHeight;
    private boolean isFirstResume = true;
    private ActivityComponent mComponent;
    private Toast mToast;//防止上一个toast还没结束时弹出新的toast，这里只使用一个toast
    private boolean mIsShortDuration;
    public Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extra = null;
        if(getIntent() != null)
            extra = getIntent().getExtras();
        getBundleExtra(extra);

        if(getLayoutId() != 0)
            setContentView(getLayoutId());
        else
            throw new IllegalArgumentException("you must return a right layout id");

        if(isApplyStatusBarTranslucency()){
            setTranslucentStatus(true);
        }

        //get the width and height of the screen
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;

        NetStateChangeBroadcast.setNetStateChangeListener(this);
        AppManager.getInstance().addActivity(this);

        TAG = this.getClass().getSimpleName();
        mComponent = DaggerActivityComponent.builder()
                .appComponent(((ZhiHuDailyApplication) getApplication()).getAppComponent())
                .activityModule(new ActivityModule(this))
                .build();

        initView();

    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        mToolbar = ButterKnife.findById(this, R.id.toolbar);
        if(mToolbar != null){
            setSupportActionBar(mToolbar);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        NetStateChangeBroadcast.getInstance().registerNetStateChangeReceiver(this);
        if(isFirstResume){
            isFirstResume = false;
            onActivityFirstResume();
        }else{
            onActivityResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        NetStateChangeBroadcast.getInstance().unRegisterNetStateChangeReceiver(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetStateChangeBroadcast.removeNetStateChangeListener(this);
        AppManager.getInstance().removeActivity(this);
    }

    @Override
    public void onNetConnected() {
        onNetworkConnected();
    }

    @Override
    public void onNetDisconnected() {
        onNetworkDisconnected();
    }

    public ActivityComponent getActivityComponent(){
        return mComponent;
    }

    protected void onNetworkConnected(){}

    protected void onNetworkDisconnected(){
        //showToast(R.string.network_error);
    }

    protected void setDisplayHomeAsUp(){
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(mToolbar != null)
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
    }
    /**
     * Retrieves the extended data from the last activity.
     *
     * @param extra
     */
    protected abstract void getBundleExtra(Bundle extra);

    /**
     * @return the layout id of this activity
     */
    protected abstract int getLayoutId();

    /**
     * initialize
     */
    protected abstract void initView();

    /**
     * is applyStatusBarTranslucency
     *
     * @return
     */
    protected abstract boolean isApplyStatusBarTranslucency();

    protected void onActivityFirstResume(){

    }

    protected void onActivityResume(){

    }
    /**
     * set status bar translucency
     *
     * @param on
     */
    protected void setTranslucentStatus(boolean on) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (on) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
            }
            win.setAttributes(winParams);
        }
    }

    /**
     * set status bar color
     *
     * @param tintDrawable
     */
    protected void setSystemBarTintDrawable(Drawable tintDrawable) {

    }

    /*************************** start navigate *********************************/

    protected void navigateTo(Class toClass){
        Intent intent = new Intent(this, toClass);
        startActivity(intent);
    }

    protected void navigateAndFinish(Class toClass){
        Intent intent = new Intent(this, toClass);
        startActivity(intent);
        finish();
    }

    protected void navigateTo(Class toClass, Bundle bundle){
        Intent intent = new Intent(this, toClass);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    protected void navigateForResult(Class toClass, int requestCode){
        Intent intent = new Intent(this, toClass);
        startActivityForResult(intent, requestCode);
    }

    protected void navigateForResult(Class toClass, Bundle bundle, int requestCode){
        Intent intent = new Intent(this, toClass);
        intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    /*************************** end navigate *********************************/


    /*************************** start show toast *********************************/

    public void showToast(String message){
        showToast(message, true);
    }

    public void showToast(String message, boolean isShortDuration){
        if(TextUtils.isEmpty(message))
            return;
        if(mToast == null || mIsShortDuration != isShortDuration) {
            mToast = Toast.makeText(this, message, isShortDuration ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
            mIsShortDuration = isShortDuration;
        }
        else
            mToast.setText(message);
        mToast.show();
    }

    public void showToast(int resId){
        showToast(resId, true);
    }

    public void showToast(int resId, boolean isShortDuration) {
        String message = getResources().getString(resId);
        if (TextUtils.isEmpty(message))
            return;
        if (mToast == null || mIsShortDuration != isShortDuration) {
            mToast = Toast.makeText(this, message, isShortDuration ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
            mIsShortDuration = isShortDuration;
        } else
            mToast.setText(message);
        mToast.show();
    }
    /*************************** end show toast *********************************/



    /*************************** start show progress *********************************/

    private GlobalProgressDialog mProgressDialog = new GlobalProgressDialog() {
        @Override
        public Activity getHostActivity() {
            return BaseAppCompatActivity.this;
        }

        @Override
        public void onProgressDialogCancel(DialogInterface dialogInterface) {
            super.onProgressDialogCancel(dialogInterface);
        }
    };
    public final void showProgressDialog(){
        mProgressDialog.showProgressDialog();
    }

    public final void showProgressDialog(CharSequence message){
        mProgressDialog.showProgressDialog(false, message);
    }

    public final void showProgressDialog(boolean cancelable){
        mProgressDialog.showProgressDialog(cancelable);
    }
    public final void showProgressDialog(boolean cancelable, CharSequence message){
        mProgressDialog.showProgressDialog(cancelable, message);
    }
    public final void dismissProgressDialog(){
        mProgressDialog.dismissProgressDialog();
    }

    public int getCurrentDialogId(){
        return mProgressDialog.getCurrentDialogId();
    }

    public boolean isProgressShowing(){
        return mProgressDialog.isShowing();
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener onCancelListener){
        mProgressDialog.setOnCancelListener(onCancelListener);
    }

    /*************************** end show progress *********************************/

    /*************************** start  handler *********************************/
    private InnerWeakHandler mHandler;
    public InnerWeakHandler getHandlerInstance(){
        if(mHandler == null)
            mHandler = new InnerWeakHandler(BaseAppCompatActivity.this);
        return mHandler;
    }

    private static class InnerWeakHandler extends Handler {
        private WeakReference<BaseAppCompatActivity> context;
        public InnerWeakHandler(BaseAppCompatActivity context){
            this.context = new WeakReference<BaseAppCompatActivity>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BaseAppCompatActivity activity = context.get();
            if(activity != null)
                activity.handleHandlerMessage(msg);
        }
    }

    public void handleHandlerMessage(Message msg){

    }


    /*************************** end handler *********************************/

}

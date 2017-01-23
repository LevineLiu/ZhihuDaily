package com.paulliu.zhihudaily.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.paulliu.zhihudaily.R;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created on 16/1/19
 *
 * @author LLW
 */
public abstract class BaseFragment extends Fragment {

    protected String TAG;
    private boolean isFirstVisible = true;
    private boolean isViewCreated = false;
    protected int mScreenWidth;
    protected int mScreenHeight;
    protected Context mContext;
    private Unbinder mUnbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        mUnbinder = ButterKnife.bind(this, view);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mScreenWidth = displayMetrics.widthPixels;
        mScreenHeight = displayMetrics.heightPixels;
        initView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initIsViewCreated();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isFirstVisible) {
                isFirstVisible = false;
                initIsViewCreated();
            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        if(mUnbinder != null)
            mUnbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    private void initIsViewCreated() {
        if (isViewCreated) {
            onFragmentFirstVisible();
        }else
            isViewCreated = true;
    }

    protected void onFragmentFirstVisible() {

    }

    /**
     * get layout id
     *
     * @return layout id
     */
    protected abstract int getLayoutId();


    protected abstract void initView();

    protected void addFragmentWithAnimations(int containerId, Fragment fragment){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.translate_from_bottom, R.anim.translate_to_bottom);
        transaction.add(containerId, fragment, null);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }
    /*************************** start navigate *********************************/


    protected void navigateTo(Class toClass){
        Intent intent = new Intent(mContext, toClass);
        startActivity(intent);
    }


    protected void navigateTo(Class toClass, Bundle bundle){
        Intent intent = new Intent(mContext, toClass);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    protected void navigateForResult(Class toClass, int requestCode){
        Intent intent = new Intent(mContext, toClass);
        startActivityForResult(intent, requestCode);
    }

    protected void navigateForResult(Class toClass, Bundle bundle, int requestCode){
        Intent intent = new Intent(mContext, toClass);
        intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    /*************************** end navigate *********************************/


    /*************************** start  handler *********************************/
    private InnerWeakHandler mHandler;

    public InnerWeakHandler getHandlerInstance(){
        if(mHandler == null)
            mHandler = new InnerWeakHandler(BaseFragment.this);
        return mHandler;
    }

    private static class InnerWeakHandler extends Handler {
        private WeakReference<BaseFragment> context;
        public InnerWeakHandler(BaseFragment context){
            this.context = new WeakReference<BaseFragment>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BaseFragment fragment = context.get();
            if(fragment != null)
                fragment.handleHandlerMessage(msg);
        }
    }

    public void handleHandlerMessage(Message msg){

    }


    /*************************** end handler *********************************/

}

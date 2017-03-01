package com.paulliu.zhihudaily.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paulliu.zhihudaily.R;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
    private View mConvertView;

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
        mConvertView = inflater.inflate(getLayoutId(), container, false);
        mUnbinder = ButterKnife.bind(this, mConvertView);
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            mConvertView.setBackgroundResource(R.color.colorBackgroundDark);
        }else{
            mConvertView.setBackgroundResource(R.color.colorBackground);
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mScreenWidth = displayMetrics.widthPixels;
        mScreenHeight = displayMetrics.heightPixels;
        initView();
        return mConvertView;
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

    /**
     * 设置日夜间模式
     */
    public void setDayNightMode(){
        if(getView() == null)
            return;
        TypedValue background = new TypedValue();
        TypedValue textColor = new TypedValue();
        TypedValue cardBackground = new TypedValue();
        TypedValue colorPrimary = new TypedValue();
        Resources.Theme theme = mContext.getTheme();
        theme.resolveAttribute(R.attr.backgroundColor, background, true);
        theme.resolveAttribute(R.attr.primaryTextColor, textColor, true);
        theme.resolveAttribute(R.attr.backgroundCardColor, cardBackground, true);
        theme.resolveAttribute(R.attr.colorPrimary, colorPrimary, true);

        mConvertView.setBackgroundResource(background.resourceId);
        SwipeRefreshLayout refreshLayout = ButterKnife.findById(mConvertView, R.id.srl_content_common_list);
        RecyclerView recyclerView = ButterKnife.findById(mConvertView, R.id.rv_content_common_list);
        refreshLayout.setColorScheme(colorPrimary.resourceId);
        updateRecyclerView(recyclerView, background, cardBackground, textColor);

    }

    private void updateRecyclerView(RecyclerView recyclerView, TypedValue background,
                                    TypedValue cardBackground, TypedValue textColor){
        for(int i=0; i<recyclerView.getChildCount(); i++){
            ViewGroup childView = (ViewGroup) recyclerView.getChildAt(i);
            if(childView instanceof CardView){
                childView.setBackgroundResource(cardBackground.resourceId);
                TextView textView = ButterKnife.findById(childView, R.id.tv_item_news_title);
                textView.setTextColor(getResources().getColor(textColor.resourceId));
            }
            else
                childView.setBackgroundResource(background.resourceId);
        }

        //清空RecyclerView 缓存的Item，即清空Recycler里的mAttachedScrap(屏幕里的缓存)、
        // mCachedViews(屏幕外的缓存)以及RecycledViewPool的缓存
        //那么，如果是ListView，要怎么做呢？这里的思路是通过反射拿到 AbsListView 类中的 RecycleBin 对象，然后同样再用反射去调用 clear 方法
        Class<RecyclerView> recyclerViewClass = RecyclerView.class;
        try {
            Field declaredField = recyclerViewClass.getDeclaredField("mRecycler");
            declaredField.setAccessible(true);
            Method declaredMethod = Class.forName(RecyclerView.Recycler.class.getName()).getDeclaredMethod("clear", (Class<?>[]) new Class[0]);
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(declaredField.get(recyclerView), new Object[0]);
            RecyclerView.RecycledViewPool recycledViewPool = recyclerView.getRecycledViewPool();
            recycledViewPool.clear();

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}

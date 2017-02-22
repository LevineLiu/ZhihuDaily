package com.paulliu.zhihudaily.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseIntArray;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created on 16/4/20
 *
 * @author LLW
 */
public class FragmentSwitcher {
    public final static int OPERATION_REPLACE = 0x100;
    public final static int OPERATION_SHOW_HIDE = 0x200;

    private int mFragmentContainerId;
    private int mCurrentPosition;
    private Map<Integer, Fragment> mFragmentMap;
    private SparseIntArray mOperationMap;
    private Context mContext;

    public FragmentSwitcher(Context context, int fragmentContainerId) {
        mFragmentContainerId = fragmentContainerId;
        mContext = context;
        mFragmentMap = new HashMap<>();
        mOperationMap = new SparseIntArray();
    }

    public Map<Integer, Fragment> getFragmentMap(){
        return mFragmentMap;
    }

    public void addFragment(int position, Fragment fragment, int operation) {
        mFragmentMap.put(position, fragment);
        addFragment(fragment, position);
        mOperationMap.put(position, operation);
    }

    public Fragment getFragment(int position){
        return mFragmentMap.get(position);
    }

    public void switchFragment(int position) {
        if (position >= 0 && mFragmentMap.get(position) != null) {
            if(mCurrentPosition == position && mFragmentMap.get(position).isAdded())
                return;
            mCurrentPosition = position;
            switch (mOperationMap.get(position)) {
                case OPERATION_REPLACE:
                    replaceFragment(position);
                    break;
                case OPERATION_SHOW_HIDE:
                    showHideFragment(position);
                    break;
            }
        }
    }

    public void removeFragment(int position){
        if (mFragmentMap != null && mFragmentMap.get(position) != null) {
            removeFragment(mFragmentMap.get(position));
            mFragmentMap.remove(position);
        }
    }

    private void replaceFragment(int position) {
        if (mFragmentMap != null && mFragmentMap.get(position) != null) {
            FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(mFragmentContainerId, mFragmentMap.get(position), position + "");
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    private void showHideFragment(int position) {
        if(mFragmentMap == null)
            return;

        Set<Map.Entry<Integer, Fragment>> set = mFragmentMap.entrySet();
        for (Map.Entry<Integer, Fragment> map : set){
            if(map.getValue() != null){
                if(map.getKey() == position)
                    showFragment(map.getValue());
                else
                    hideFragment(map.getValue());
            }else {
                FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
                Fragment fragment = fragmentManager.findFragmentByTag(position + "");
                if(fragment != null){
                    if (map.getKey() == position)
                        showFragment(fragment);
                    else
                        hideFragment(fragment);
                }
            }
        }
    }

    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.show(fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void hideFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void addFragment(Fragment fragment, int position){
        FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(mFragmentContainerId, fragment, position + "");
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void removeFragment(Fragment fragment){
        FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }
}

package com.paulliu.zhihudaily.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.util.SparseIntArray;

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
    private SparseArray<Fragment> mFragmentArray;
    private SparseIntArray mOperationArray;
    private Context mContext;

    public FragmentSwitcher(Context context, int fragmentContainerId) {
        mFragmentContainerId = fragmentContainerId;
        mContext = context;
        mFragmentArray = new SparseArray<>();
        mOperationArray = new SparseIntArray();
    }

    public SparseArray<Fragment> getFragmentArray(){
        return mFragmentArray;
    }

    public void addFragment(int position, Fragment fragment, int operation) {
        mFragmentArray.put(position, fragment);
        addFragment(fragment, position);
        mOperationArray.put(position, operation);
    }

    public Fragment getFragment(int position){
        return mFragmentArray.get(position);
    }

    public void switchFragment(int position) {
        if (position >= 0 && mFragmentArray.get(position) != null) {
            if(mCurrentPosition == position && mFragmentArray.get(position).isAdded())
                return;
            mCurrentPosition = position;
            switch (mOperationArray.get(position)) {
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
        if (mFragmentArray != null && mFragmentArray.get(position) != null) {
            removeFragment(mFragmentArray.get(position));
            mFragmentArray.remove(position);
        }
    }

    private void replaceFragment(int position) {
        if (mFragmentArray != null && mFragmentArray.get(position) != null) {
            FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(mFragmentContainerId, mFragmentArray.get(position), position + "");
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    private void showHideFragment(int position) {
        if(mFragmentArray == null)
            return;

        for(int i = 0; i< mFragmentArray.size(); i++){
            int key = mFragmentArray.keyAt(i);
            Fragment targetFragment = mFragmentArray.get(key);
            if(targetFragment != null){
                if(key == position)
                    showFragment(targetFragment);
                else
                    hideFragment(targetFragment);
            }else{
                FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
                Fragment fragment = fragmentManager.findFragmentByTag(position + "");
                if(fragment != null){
                    if (key == position)
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

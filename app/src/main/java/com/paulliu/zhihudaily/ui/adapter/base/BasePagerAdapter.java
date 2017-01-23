package com.paulliu.zhihudaily.ui.adapter.base;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * A PagerAdapter that works like BaseAdapter.
 * Note that this PagerAdapter does NOT support removing item operation since we use position as key to get page.
 * 
 * <android.support.v4.view.ViewPager
 * android:id="@+id/viewPager"
 * android:layout_width="fill_parent"
 * android:layout_height="164dp" />
 * 
 * @author AlfredZhong
 * @version 2014-01-06
 */
public abstract class BasePagerAdapter extends PagerAdapter {

    private static final String TAG = BasePagerAdapter.class.getSimpleName();
    private Context mContext;
    // a map to store pages.
    private SparseArray<ViewGroup> mPages;

    // left -> right : destroyItem -> instantiateItem, if page width is 1, then ViewPager max child count is 3
    // right -> left : instantiateItem -> destroyItem, if page width is 1, then ViewPager max child count is 4 and soon
    // destroyItem to 3.
    public BasePagerAdapter(Context context) {
        mContext = context;
        mPages = new SparseArray<ViewGroup>();
    }

    // Called when the item of pos not yet destroyItem() and pos is at (current selected - 1) or (current selected + 1)
    @SuppressWarnings("deprecation")
    @Override
    public final Object instantiateItem(ViewGroup container, int position) {
        // container is ViewPager.
        //Log.v(TAG, TAG + " instantiateItem " + position);
        ViewGroup childLayout = mPages.get(position);
        if (childLayout == null) {
            Log.d(TAG, TAG + " add item " + position);
            childLayout = new FrameLayout(mContext);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.FILL_PARENT);
            childLayout.setLayoutParams(params);
            childLayout.setTag(position);
            mPages.put(position, childLayout);
        }
        View child = instantiateView(position, childLayout.getChildAt(0), childLayout);
        if (childLayout.getChildAt(0) == null) {
            ViewGroup.LayoutParams lp = child.getLayoutParams();
            // set real child layout_gravity="center" to center in childLayout.
            if (lp instanceof FrameLayout.LayoutParams) {
                ((FrameLayout.LayoutParams) lp).gravity = android.view.Gravity.CENTER;
            } else if (lp instanceof LinearLayout.LayoutParams) {
                ((LinearLayout.LayoutParams) lp).gravity = android.view.Gravity.CENTER;
            } else if (lp instanceof RelativeLayout.LayoutParams) {
                ((RelativeLayout.LayoutParams) lp).addRule(RelativeLayout.CENTER_IN_PARENT);
            }
            childLayout.addView(child);
        }
        if (container.findViewWithTag(position) == null) {
            container.addView(childLayout);
        } else {
            Log.w(TAG, TAG + " no need to addView " + position);
        }
        return childLayout;
    }

    public abstract View instantiateView(int position, View convertView, ViewGroup parent);

    // Called when position is not at [current selected - 1, current selected + 1]
    @Override
    public final void destroyItem(ViewGroup container, int position, Object object) {
        //Log.v(TAG, TAG + " destroyItem " + position);
        ViewGroup childLayout = mPages.get(position);
        if (childLayout != null) {
            destroyView(position, childLayout.getChildAt(0));
            container.removeView(childLayout);
        }
    }

    public abstract void destroyView(int position, View convertView);

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }

    @Override
    public float getPageWidth(int position) {
        return super.getPageWidth(position);
    }

}

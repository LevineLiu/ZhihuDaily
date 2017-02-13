package com.paulliu.zhihudaily.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

/**
 * Created on 2017/2/4
 *
 * @author LLW
 */

public class SpeedyLinearLayoutManager extends LinearLayoutManager{
    private static final float MILLISECONDS_PER_INCH = 5f; //bigger is slower

    public SpeedyLinearLayoutManager(Context context) {
        super(context);
    }

    public SpeedyLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public SpeedyLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr,
                                     int defStyleRes) {
       super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        LinearSmoothScroller linearSmoothScroller =
                new LinearSmoothScroller(recyclerView.getContext()){
                    //method 1
                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
                    }

                    //method 2
//                    @Override
//                    protected int calculateTimeForScrolling(int dx) {
//                        if(dx > 3000)
//                            dx = 3000;
//                        return super.calculateTimeForScrolling(dx);
//                    }
                };
        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }
}

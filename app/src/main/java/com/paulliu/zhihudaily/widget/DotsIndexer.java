package com.paulliu.zhihudaily.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 16/3/28
 *
 * @author LLW
 */
public class DotsIndexer extends LinearLayout {

    private Context mContext;
    private int mCurrentPosition;
    private List<ImageView> dots = new ArrayList<>();

    public DotsIndexer(Context context){
        this(context, null, 0);
    }

    public DotsIndexer(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public DotsIndexer(Context context, AttributeSet attrs, int style){
        super(context, attrs, style);
        mContext = context;
    }

    /**
     * @param resId the image resource of dots
     */
    public void initDots(int count, int leftPadding, int topPadding, int rightPadding, int bottomPadding, int resId){
        if(count < 0)
            return;
        for(int i=0; i<count; i++){
            ImageView imageView = new ImageView(mContext);
            imageView.setPadding(leftPadding, topPadding, rightPadding, bottomPadding);
            if(i == 0)
                imageView.setEnabled(true);
            else
                imageView.setEnabled(false);
            imageView.setImageResource(resId);
            dots.add(imageView);
            addView(imageView);
        }
    }

    public void updatePageSelected(int selectedPosition){
        for(int i=0; i<dots.size(); i++){
            ImageView imageView = dots.get(i);
            if(i == selectedPosition)
                imageView.setEnabled(true);
            else
                imageView.setEnabled(false);
        }
    }

    public void removeChildViews(){
        try {
            if(getChildCount() != 0){
                dots.clear();
                removeViews(0, getChildCount());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

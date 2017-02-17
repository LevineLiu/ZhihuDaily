package com.paulliu.zhihudaily.ui.adapter.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.paulliu.zhihudaily.listener.OnListItemClickListener;

/**
 * Created on 2017/2/17
 *
 * @author LLW
 *
 * a base ViewHolder to implement item click
 */

public class BaseViewHolder extends RecyclerView.ViewHolder{

    public BaseViewHolder(View view){
        super(view);
    }

    public BaseViewHolder(View view, final OnListItemClickListener listener){
        super(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null)
                    listener.onItemClick(getLayoutPosition());
            }
        });
    }
}

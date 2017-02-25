package com.paulliu.zhihudaily.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.paulliu.zhihudaily.R;
import com.paulliu.zhihudaily.entity.NewsEntity;
import com.paulliu.zhihudaily.listener.OnListItemClickListener;
import com.paulliu.zhihudaily.ui.adapter.base.BaseViewHolder;
import com.paulliu.zhihudaily.ui.adapter.base.RecyclerViewLoadMoreAdapter;
import com.squareup.picasso.Picasso;


import butterknife.ButterKnife;

/**
 * Created on 2017/2/7
 *
 * @author LLW
 */

public class NewsListAdapter extends RecyclerViewLoadMoreAdapter<NewsEntity>{

    public NewsListAdapter(Context context, RecyclerView.LayoutManager layoutManager) {
        super(context, layoutManager);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent) {
        return new ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_news_list, parent, false), mOnListItemClickListener);
    }

    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final NewsEntity newsEntity = mData.get(position);
        if(newsEntity.getImages() != null && newsEntity.getImages().size() != 0)
            Picasso.with(mContext)
                    .load(newsEntity.getImages().get(0))
                    .placeholder(R.drawable.progress_animation)
                    .resizeDimen(R.dimen.home_news_image_width, R.dimen.home_news_image_height)
                    .config(Bitmap.Config.RGB_565)
                    .into(((ItemViewHolder)viewHolder).imageView);
        ((ItemViewHolder) viewHolder).titleTv.setText(newsEntity.getTitle());

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            ((ItemViewHolder) viewHolder).cardView.setBackgroundResource(R.color.colorCardViewBackgroundDark);
            ((ItemViewHolder) viewHolder).titleTv.setTextColor(mContext.getResources().getColor(R.color.colorTextColorDark));
        }else{
            ((ItemViewHolder) viewHolder).cardView.setBackgroundResource(R.color.colorCardViewBackground);
            ((ItemViewHolder) viewHolder).titleTv.setTextColor(mContext.getResources().getColor(R.color.colorTextColor));
        }
    }

    private static class ItemViewHolder extends BaseViewHolder{
        CardView cardView;
        TextView titleTv;
        ImageView imageView;

        public ItemViewHolder(View v, OnListItemClickListener listener) {
            super(v, listener);
            cardView = ButterKnife.findById(v, R.id.card_view_item_news);
            titleTv = ButterKnife.findById(v, R.id.tv_item_news_title);
            imageView = ButterKnife.findById(v, R.id.iv_item_news);
        }
    }
}

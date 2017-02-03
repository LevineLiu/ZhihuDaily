package com.paulliu.zhihudaily.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.paulliu.zhihudaily.R;
import com.paulliu.zhihudaily.entities.NewsEntity;
import com.paulliu.zhihudaily.ui.adapter.base.RecyclerViewLoadMoreAdapter;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;

/**
 * Created on 2017/2/3
 *
 * @author LLW
 */

public class CommonThemeListAdapter extends RecyclerViewLoadMoreAdapter<NewsEntity>{

    public CommonThemeListAdapter(Context context, RecyclerView.LayoutManager layoutManager) {
        super(context, layoutManager);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return new CommonThemeListAdapter.HeaderViewHolder(mInflater.inflate(R.layout.common_banner, parent, false));
        }
        else
            return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent) {
        return new CommonThemeListAdapter.ItemViewHolder(mInflater.inflate(R.layout.item_news_list, parent, false));
    }

    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:

                break;
            case TYPE_ITEM:
                final NewsEntity newsEntity = mData.get(position - 1);
                if(newsEntity.getImages() != null && newsEntity.getImages().size() != 0)
                    Picasso.with(mContext)
                            .load(newsEntity.getImages().get(0))
                            .placeholder(R.drawable.progress_animation)
                            .resizeDimen(R.dimen.home_news_image_width, R.dimen.home_news_image_height)
                            .config(Bitmap.Config.RGB_565)
                            .into(((CommonThemeListAdapter.ItemViewHolder) viewHolder).imageView);
                ((CommonThemeListAdapter.ItemViewHolder) viewHolder).titleTv.setText(newsEntity.getTitle());
                ((CommonThemeListAdapter.ItemViewHolder) viewHolder).cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mOnListItemClickListener != null)
                            mOnListItemClickListener.onItemClick(newsEntity);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEADER;
        else
            return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        if(mData.size() == 0 && mData.size() == 0)
            return 0;
        else
            return mData.size() + 2;//1 for header, 1 for footer
    }

    @Override
    public int getActualItemCount() {
        return mData.size() != 0 ? mData.size() + 1 : 0;
    }


    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        ImageView bannerIv;
        TextView titleTv;

        public HeaderViewHolder(View v) {
            super(v);
            bannerIv = ButterKnife.findById(v, R.id.iv_banner);
            titleTv = ButterKnife.findById(v, R.id.tv_banner_title);
        }
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView titleTv;
        ImageView imageView;

        public ItemViewHolder(View v) {
            super(v);
            cardView = ButterKnife.findById(v, R.id.card_view_item_news);
            titleTv = ButterKnife.findById(v, R.id.tv_item_news_title);
            imageView = ButterKnife.findById(v, R.id.iv_item_news);
        }
    }

}

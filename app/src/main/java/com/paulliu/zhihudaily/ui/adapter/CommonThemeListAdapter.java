package com.paulliu.zhihudaily.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.paulliu.zhihudaily.R;
import com.paulliu.zhihudaily.entity.EditorEntity;
import com.paulliu.zhihudaily.entity.NewsEntity;
import com.paulliu.zhihudaily.entity.ThemeEntity;
import com.paulliu.zhihudaily.ui.adapter.base.RecyclerViewLoadMoreAdapter;
import com.paulliu.zhihudaily.widget.CircleTransform;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;

/**
 * Created on 2017/2/3
 *
 * @author LLW
 */

public class CommonThemeListAdapter extends RecyclerViewLoadMoreAdapter<NewsEntity> {
    private ThemeEntity mThemeEntity;

    public CommonThemeListAdapter(Context context, RecyclerView.LayoutManager layoutManager) {
        super(context, layoutManager);
    }

    public void setHeaderInfo(ThemeEntity themeEntity) {
        mThemeEntity = themeEntity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return new CommonThemeListAdapter.HeaderViewHolder(mInflater.inflate(R.layout.theme_header, parent, false));
        } else
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
                if (!TextUtils.isEmpty(mThemeEntity.getBackground())) {
                    Picasso.with(mContext).load(mThemeEntity.getBackground()).into(((HeaderViewHolder) viewHolder).bannerIv);
                    ((HeaderViewHolder) viewHolder).titleTv.setText(mThemeEntity.getDescription());
                } else
                    ((HeaderViewHolder) viewHolder).contentLayout.setVisibility(View.GONE);
                ((HeaderViewHolder) viewHolder).recommenderTv.setText("主编");

                if (((HeaderViewHolder) viewHolder).recommenderLl.getChildCount() == 1) {
                    for (EditorEntity entity : mThemeEntity.getEditors()) {
                        ImageView imageView = new ImageView(mContext);
                        int diameter = mContext.getResources().getDimensionPixelSize(R.dimen.small_circle_image_diameter);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(diameter, diameter);
                        layoutParams.leftMargin = mContext.getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
                        imageView.setLayoutParams(layoutParams);
                        Picasso.with(mContext).load(entity.getAvatar()).transform(new CircleTransform()).into(imageView);
                        ((HeaderViewHolder) viewHolder).recommenderLl.addView(imageView);
                    }
                }
                ((HeaderViewHolder) viewHolder).recommenderLl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        navigateToEditorList();
                    }
                });
                break;
            case TYPE_ITEM:
                final NewsEntity newsEntity = mData.get(position - 1);
                if (newsEntity.getImages() != null && newsEntity.getImages().size() != 0)
                    Picasso.with(mContext)
                            .load(newsEntity.getImages().get(0))
                            .placeholder(R.drawable.progress_animation)
                            .resizeDimen(R.dimen.home_news_image_width, R.dimen.home_news_image_height)
                            .config(Bitmap.Config.RGB_565)
                            .into(((ItemViewHolder) viewHolder).imageView);
                ((ItemViewHolder) viewHolder).titleTv.setText(newsEntity.getTitle());
                ((ItemViewHolder) viewHolder).cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnListItemClickListener != null)
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
        if (mData == null || mData.size() == 0)
            return 0;
        else
            return mData.size() + 2;//1 for header, 1 for footer
    }

    @Override
    public int getActualItemCount() {
        return mData.size() != 0 ? mData.size() + 1 : 0;
    }

    public void navigateToEditorList(){}

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        ImageView bannerIv;
        TextView titleTv;
        RelativeLayout contentLayout;
        TextView recommenderTv;
        LinearLayout recommenderLl;

        public HeaderViewHolder(View v) {
            super(v);
            bannerIv = ButterKnife.findById(v, R.id.iv_banner);
            titleTv = ButterKnife.findById(v, R.id.tv_banner_title);
            contentLayout = ButterKnife.findById(v, R.id.rl_common_banner);
            recommenderTv = ButterKnife.findById(v, R.id.tv_recommender);
            recommenderLl = ButterKnife.findById(v, R.id.ll_theme_recommender);
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

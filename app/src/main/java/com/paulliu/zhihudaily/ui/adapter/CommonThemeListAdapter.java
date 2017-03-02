package com.paulliu.zhihudaily.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.paulliu.zhihudaily.R;
import com.paulliu.zhihudaily.entity.EditorEntity;
import com.paulliu.zhihudaily.entity.NewsEntity;
import com.paulliu.zhihudaily.entity.ThemeEntity;
import com.paulliu.zhihudaily.ui.adapter.base.BaseRecyclerViewLoadMoreAdapter;
import com.paulliu.zhihudaily.widget.CircleTransform;
import com.squareup.picasso.Picasso;

/**
 * Created on 2017/2/3
 *
 * @author LLW
 */

public class CommonThemeListAdapter extends BaseRecyclerViewLoadMoreAdapter<NewsEntity> {
    private ThemeEntity mThemeEntity;

    public CommonThemeListAdapter(Context context, RecyclerView.LayoutManager layoutManager) {
        super(context, layoutManager);
    }

    public void setHeaderInfo(ThemeEntity themeEntity) {
        mThemeEntity = themeEntity;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_news_list;
    }

    @Override
    public int getHeaderLayoutId() {
        return R.layout.theme_header;
    }

    @Override
    protected void onBindItemViewHolder(VH viewHolder, int position) {
        CardView cardView = viewHolder.getView(R.id.card_view_item_news);
        TextView titleTv = viewHolder.getView(R.id.tv_item_news_title);
        ImageView imageView = viewHolder.getView(R.id.iv_item_news);
        final NewsEntity newsEntity = mData.get(position - 1);
        String tag = (String) imageView.getTag();
        if (newsEntity.getImages() != null && newsEntity.getImages().size() != 0
                && !newsEntity.getImages().get(0).equals(tag)){
            Picasso.with(mContext)
                    .load(newsEntity.getImages().get(0))
                    .placeholder(R.drawable.progress_animation)
                    .resizeDimen(R.dimen.home_news_image_width, R.dimen.home_news_image_height)
                    .config(Bitmap.Config.RGB_565)
                    .into(imageView);
            imageView.setTag(newsEntity.getImages().get(0));
        }else if(newsEntity.getImages() == null){
            imageView.setImageBitmap(null);
        }

        titleTv.setText(newsEntity.getTitle());

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            cardView.setBackgroundResource(R.color.colorCardViewBackgroundDark);
            titleTv.setTextColor(mContext.getResources().getColor(R.color.colorTextColorDark));
        }else{
            cardView.setBackgroundResource(R.color.colorCardViewBackground);
            titleTv.setTextColor(mContext.getResources().getColor(R.color.colorTextColor));
        }
    }

    @Override
    protected void onBindHeaderViewHolder(VH viewHolder, int position) {
        ImageView bannerIv = viewHolder.getView(R.id.iv_banner);
        TextView titleTv = viewHolder.getView(R.id.tv_banner_title);
        RelativeLayout contentLayout = viewHolder.getView(R.id.rl_common_banner);
        TextView recommenderTv = viewHolder.getView(R.id.tv_recommender);
        LinearLayout recommenderLl = viewHolder.getView(R.id.ll_theme_recommender);
        if (!TextUtils.isEmpty(mThemeEntity.getBackground())) {
            Picasso.with(mContext).load(mThemeEntity.getBackground()).into(bannerIv);
            titleTv.setText(mThemeEntity.getDescription());
        } else
            contentLayout.setVisibility(View.GONE);
        recommenderTv.setText("主编");

        if (recommenderLl.getChildCount() == 1) {
            for (EditorEntity entity : mThemeEntity.getEditors()) {
                ImageView imageView = new ImageView(mContext);
                int diameter = mContext.getResources().getDimensionPixelSize(R.dimen.small_circle_image_diameter);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(diameter, diameter);
                layoutParams.leftMargin = mContext.getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
                imageView.setLayoutParams(layoutParams);
                Picasso.with(mContext).load(entity.getAvatar()).transform(new CircleTransform()).into(imageView);
                recommenderLl.addView(imageView);
            }
        }
        recommenderLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToEditorList();
            }
        });
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    public void navigateToEditorList(){}

}

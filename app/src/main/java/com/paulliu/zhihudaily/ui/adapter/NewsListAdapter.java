package com.paulliu.zhihudaily.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.paulliu.zhihudaily.R;
import com.paulliu.zhihudaily.entity.NewsEntity;
import com.paulliu.zhihudaily.ui.adapter.base.BaseRecyclerViewLoadMoreAdapter;
import com.squareup.picasso.Picasso;

/**
 * Created on 2017/2/7
 *
 * @author LLW
 */

public class NewsListAdapter extends BaseRecyclerViewLoadMoreAdapter<NewsEntity> {

    public NewsListAdapter(Context context, RecyclerView.LayoutManager layoutManager) {
        super(context, layoutManager);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_news_list;
    }

    @Override
    protected void onBindItemViewHolder(VH viewHolder, int position) {
        CardView cardView = viewHolder.getView(R.id.card_view_item_news);
        TextView titleTv = viewHolder.getView(R.id.tv_item_news_title);
        ImageView imageView = viewHolder.getView(R.id.iv_item_news);

        final NewsEntity newsEntity = mData.get(position);
        String tag = (String) imageView.getTag();
        if(newsEntity.getImages() != null && newsEntity.getImages().size() != 0 &&
                !newsEntity.getImages().get(0).equals(tag)){
            Picasso.with(mContext)
                    .load(newsEntity.getImages().get(0))
                    .placeholder(R.drawable.progress_animation)
                    .resizeDimen(R.dimen.home_news_image_width, R.dimen.home_news_image_height)
                    .config(Bitmap.Config.RGB_565)
                    .into(imageView);
            imageView.setTag(tag);
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
    public long getItemId(int position) {
        return position;
    }

}

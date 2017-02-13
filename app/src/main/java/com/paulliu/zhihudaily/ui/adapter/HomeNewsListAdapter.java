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
import com.paulliu.zhihudaily.entity.DailyNews;
import com.paulliu.zhihudaily.entity.NewsEntity;
import com.paulliu.zhihudaily.entity.TopNewsEntity;
import com.paulliu.zhihudaily.ui.adapter.base.RecyclerViewLoadMoreAdapter;
import com.paulliu.zhihudaily.widget.DotsIndexer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created on 2017/1/17
 *
 * @author LLW
 */

public class HomeNewsListAdapter extends RecyclerViewLoadMoreAdapter<DailyNews> {
    private List<NewsEntity> mNewsEntityList = new ArrayList<>();
    private List<TopNewsEntity> mTopNewsEntityList = new ArrayList<>();

    public HomeNewsListAdapter(Context context, RecyclerView.LayoutManager layoutManager) {
        super(context, layoutManager);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            HeaderViewHolder headerViewHolder = new HeaderViewHolder(mInflater.inflate(R.layout.fragment_home_header, parent, false));
            initViewPager(headerViewHolder.viewPager, headerViewHolder.dotsIndexer);
            return headerViewHolder;
        }
        else
            return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent) {
        return new ItemViewHolder(mInflater.inflate(R.layout.item_news_list, parent, false));
    }

    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:

                break;
            case TYPE_ITEM:
                final NewsEntity newsEntity = mNewsEntityList.get(position - 1);
                if(newsEntity.getImages() != null && newsEntity.getImages().size() != 0)
                    Picasso .with(mContext)
                            .load(newsEntity.getImages().get(0))
                            .placeholder(R.drawable.progress_animation)
                            .resizeDimen(R.dimen.home_news_image_width, R.dimen.home_news_image_height)
                            .config(Bitmap.Config.RGB_565)
                            .into(((ItemViewHolder) viewHolder).imageView);
                ((ItemViewHolder) viewHolder).titleTv.setText(newsEntity.getTitle());
                ((ItemViewHolder) viewHolder).cardView.setOnClickListener(new View.OnClickListener() {
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
        if(mNewsEntityList.size() == 0 && mTopNewsEntityList.size() == 0)
            return 0;
        else
            return mNewsEntityList.size() + 2;//1 for header, 1 for footer
    }

    @Override
    public int getActualItemCount() {
        return mNewsEntityList.size() != 0 ? mNewsEntityList.size() + 1 : 0;
    }

    public void setStories(List<NewsEntity> storyList, List<TopNewsEntity> topStoryList) {
        mNewsEntityList = storyList;
        if (topStoryList != null && topStoryList.size() != 0)
            mTopNewsEntityList = topStoryList;
        notifyDataSetChanged();
    }

    public void addStories(List<NewsEntity> storyList) {
        mNewsEntityList.addAll(storyList);
        notifyDataSetChanged();
    }

    public int getTopStoriesCount(){
        return mTopNewsEntityList.size();
    }

    public List<TopNewsEntity> getTopNewsEntityList(){
        return mTopNewsEntityList;
    }

    public void initViewPager(ViewPager viewPager, DotsIndexer dotsIndexer){}

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        ViewPager viewPager;
        DotsIndexer dotsIndexer;

        public HeaderViewHolder(View v) {
            super(v);
            viewPager = ButterKnife.findById(v, R.id.vp_home_header);
            dotsIndexer = ButterKnife.findById(v, R.id.dots_indexer_home_header);
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

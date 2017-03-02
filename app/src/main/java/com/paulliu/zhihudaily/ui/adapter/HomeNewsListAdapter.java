package com.paulliu.zhihudaily.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.paulliu.zhihudaily.R;
import com.paulliu.zhihudaily.entity.DailyNews;
import com.paulliu.zhihudaily.entity.NewsEntity;
import com.paulliu.zhihudaily.ui.adapter.base.BaseRecyclerViewLoadMoreAdapter;
import com.paulliu.zhihudaily.widget.DotsIndexer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/1/17
 *
 * @author LLW
 */

public class HomeNewsListAdapter extends BaseRecyclerViewLoadMoreAdapter<DailyNews> {
    private List<NewsEntity> mNewsEntityList = new ArrayList<>();
    private List<NewsEntity> mTopNewsEntityList = new ArrayList<>();

    public HomeNewsListAdapter(Context context, RecyclerView.LayoutManager layoutManager) {
        super(context, layoutManager);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_news_list;
    }

    @Override
    public int getHeaderLayoutId() {
        return R.layout.fragment_home_header;
    }

    @Override
    protected void onBindItemViewHolder(VH viewHolder, int position) {
        CardView cardView = viewHolder.getView(R.id.card_view_item_news);
        TextView titleTv = viewHolder.getView(R.id.tv_item_news_title);
        ImageView imageView = viewHolder.getView(R.id.iv_item_news);

        final NewsEntity newsEntity = mNewsEntityList.get(position - 1);
        String tag = (String) imageView.getTag();
        if(newsEntity.getImages() != null && newsEntity.getImages().size() != 0 &&
                !newsEntity.getImages().get(0).equals(tag)){
            Picasso .with(mContext)
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
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ItemType.HEADER.ordinal()) {
            VH headerViewHolder = VH.getViewHolder(parent, R.layout.fragment_home_header);
            ViewPager viewPager = headerViewHolder.getView(R.id.vp_home_header);
            DotsIndexer dotsIndexer = headerViewHolder.getView(R.id.dots_indexer_home_header);
            initViewPager(viewPager, dotsIndexer);
            return headerViewHolder;
        }
        else
            return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public long getItemId(int position) {
        return position;
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

    public void setStories(List<NewsEntity> storyList, List<NewsEntity> topStoryList) {
        mNewsEntityList = storyList;
        if (topStoryList != null && topStoryList.size() != 0)
            mTopNewsEntityList = topStoryList;
        notifyDataSetChanged();
    }

    public void addStories(List<NewsEntity> storyList) {
        mNewsEntityList.addAll(storyList);
        notifyDataSetChanged();
    }

    public List<NewsEntity> getNewsList(){
        return mNewsEntityList;
    }

    public int getTopStoriesCount(){
        return mTopNewsEntityList.size();
    }

    public List<NewsEntity> getTopNewsEntityList(){
        return mTopNewsEntityList;
    }

    public void initViewPager(ViewPager viewPager, DotsIndexer dotsIndexer){}

}

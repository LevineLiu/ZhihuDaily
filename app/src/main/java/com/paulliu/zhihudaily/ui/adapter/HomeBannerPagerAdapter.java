package com.paulliu.zhihudaily.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.paulliu.zhihudaily.R;
import com.paulliu.zhihudaily.entity.TopNewsEntity;
import com.paulliu.zhihudaily.ui.adapter.base.BasePagerAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created on 16/8/31
 *
 * @author LLW
 */
public class HomeBannerPagerAdapter extends BasePagerAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<TopNewsEntity> mBannerList;

    public HomeBannerPagerAdapter(Context context){
        super(context);
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setData(List<TopNewsEntity> urls){
        mBannerList = urls;
        notifyDataSetChanged();
    }

    @Override
    public View instantiateView(final int position, View convertView, ViewGroup parent) {
        HomeBannerViewHolder viewHolder;
        final TopNewsEntity topNewsEntity = mBannerList.get(position);
        if(convertView == null){
            viewHolder = new HomeBannerViewHolder();
            convertView = mInflater.inflate(R.layout.common_banner, parent, false);
            viewHolder.bannerIv = ButterKnife.findById(convertView, R.id.iv_banner);
            viewHolder.titleTv = ButterKnife.findById(convertView, R.id.tv_banner_title);
            convertView.setTag(viewHolder);
        }else
            viewHolder = (HomeBannerViewHolder) convertView.getTag();

        if(!TextUtils.isEmpty(topNewsEntity.getImage()))
            Picasso.with(mContext).load(topNewsEntity.getImage()).into(viewHolder.bannerIv);
        viewHolder.bannerIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageClick(topNewsEntity.getId());
            }
        });
        viewHolder.titleTv.setText(topNewsEntity.getTitle());
        return convertView;
    }

    @Override
    public void destroyView(int position, View convertView) {
        if(convertView != null){
            ImageView imageView = (ImageView) convertView;
            imageView.setImageBitmap(null);
        }
    }


    @Override
    public int getCount() {
        return mBannerList != null ? mBannerList.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void onImageClick(int id){}

    private static class HomeBannerViewHolder{
        ImageView bannerIv;
        TextView titleTv;
    }
}

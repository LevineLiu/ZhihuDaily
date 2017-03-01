package com.paulliu.zhihudaily.ui.adapter.base;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ProgressBar;

import com.paulliu.zhihudaily.R;
import com.paulliu.zhihudaily.listener.OnListItemClickListener;
import com.paulliu.zhihudaily.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created on 16/3/2
 *
 * @author LLW
 */
public abstract class RecyclerViewLoadMoreAdapter<T> extends RecyclerView.Adapter {

    protected final static int TYPE_HEADER = 0x100;
    protected final static int TYPE_ITEM = 0x200;
    protected final static int TYPE_FOOTER = 0x300;

    protected Context mContext;
    protected List<T> mData;
    protected LayoutInflater mInflater;
    private boolean mLoadMoreEnable;
    private RecyclerView.LayoutManager mLayoutManager;
    private OnLoadMoreListener mOnLoadMoreListener;
    private onRecyclerViewScrollListener mOnRecyclerViewScrollListener;
    public OnListItemClickListener mOnListItemClickListener;

    public RecyclerViewLoadMoreAdapter(Context context, RecyclerView.LayoutManager layoutManager) {
        mInflater = LayoutInflater.from(context);
        this.mLayoutManager = layoutManager;
        this.mContext = context;
        mOnRecyclerViewScrollListener = new onRecyclerViewScrollListener();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1)
            return TYPE_FOOTER;
        else
            return TYPE_ITEM;
    }

    public int getActualItemCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() + 1 : 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FooterViewHolder) {
            if (mLoadMoreEnable) {
                ((FooterViewHolder) holder).footerLayout.setVisibility(View.VISIBLE);
                if (((FooterViewHolder) holder).footerLayout.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
                    //如果使用StaggeredGridLayoutManager，需要把加载更多的ProgressBar居中
                    StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) ((FooterViewHolder) holder).footerLayout.getLayoutParams();
                    params.setFullSpan(true);
                }
            } else {
                ((FooterViewHolder) holder).footerLayout.setVisibility(View.GONE);
            }
        } else {
            onBindItemViewHolder(holder, position);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER)
            return new FooterViewHolder(mInflater.inflate(R.layout.footer_view, parent, false));
        else
            return onCreateItemViewHolder(parent);
    }

    protected abstract RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent);

    protected abstract void onBindItemViewHolder(RecyclerView.ViewHolder viewHolder, int position);

    public void addData(List<T> data) {
        if(data == null)
            return;
        if (mData == null)
            mData = new ArrayList<>();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void setData(List<T> data) {
        if (mData == null)
            mData = new ArrayList<>();
        mData = data;
        notifyDataSetChanged();
    }

    public void remove(int position){
        if(mData != null && position < mData.size()){
            mData.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void removeAll(List<T> data){
        if(mData != null){
            mData.removeAll(data);
            notifyDataSetChanged();
        }
    }

    public List<T> getData(){
        return mData;
    }

    /**
     * Whether load more is enabled
     */
    public void setLoadMoreEnable(boolean enable) {
        mLoadMoreEnable = enable;
        if(!mLoadMoreEnable){
            notifyDataSetChanged();
        }
    }

    /**
     * set the listener of loading more
     */
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }

    /**
     * get the listener of RecyclerView's scrolling
     */
    public onRecyclerViewScrollListener getOnScrollListener() {
        return mOnRecyclerViewScrollListener;
    }

    /**
     * item click listener
     */
    public void setOnListItemClickListener(OnListItemClickListener onListItemClickListener) {
        mOnListItemClickListener = onListItemClickListener;
    }

    protected static class FooterViewHolder extends RecyclerView.ViewHolder {
//        public ProgressBar progress;
        ViewStub footerLayout;

        public FooterViewHolder(View view) {
            super(view);
            footerLayout = ButterKnife.findById(view, R.id.footer_view_layout);
//            footerLayout.setVisibility(View.VISIBLE);
//            progress = ButterKnife.findById(view, R.id.stub_pgb_load_more);
        }
    }

    /**
     * the listener of recycler's scrolling
     * when scrolling to the last fully visible item, perform loading more
     */
    private class onRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE && mLoadMoreEnable) {
                boolean shouldLoadMore = false;
                if (mLayoutManager instanceof LinearLayoutManager) {
                    //the last visible item
                    int lastPosition = ((LinearLayoutManager) mLayoutManager).findLastCompletelyVisibleItemPosition();
                    if (lastPosition == getActualItemCount())
                        shouldLoadMore = true;
                }
                if (mLayoutManager instanceof StaggeredGridLayoutManager) {
                    int positionArray[] = ((StaggeredGridLayoutManager) mLayoutManager).findLastCompletelyVisibleItemPositions(null);
                    for (int position : positionArray) {
                        if (position == getActualItemCount())
                            shouldLoadMore = true;
                    }
                }
                if (shouldLoadMore) {
                    if (mOnLoadMoreListener == null)
                        throw new IllegalArgumentException("please call setOnLoadMoreListener before using the 'load more' function");
                    else
                        mOnLoadMoreListener.onLoadMore();
                }
            }
        }
    }

}

package com.paulliu.zhihudaily.ui.adapter.base;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.paulliu.zhihudaily.R;
import com.paulliu.zhihudaily.listener.OnListItemClickListener;
import com.paulliu.zhihudaily.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created on 2017/3/2
 *
 * @author Levine
 */

public abstract class BaseRecyclerViewLoadMoreAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewLoadMoreAdapter.VH> {
    private final static int HEADER_NONE = 0;

    protected Context mContext;
    protected List<T> mData;
    private boolean mLoadMoreEnable;
    private RecyclerView.LayoutManager mLayoutManager;
    private OnLoadMoreListener mOnLoadMoreListener;
    private OnRecyclerViewScrollListener mOnScrollListener;
    private OnListItemClickListener mOnListItemClickListener;

    protected enum ItemType {
        HEADER,
        NORMAL,
        FOOTER
    }

    public BaseRecyclerViewLoadMoreAdapter(Context context, RecyclerView.LayoutManager layoutManager) {
        this.mContext = context;
        this.mLayoutManager = layoutManager;
        this.mOnScrollListener = new OnRecyclerViewScrollListener();
    }


    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ItemType.HEADER.ordinal())
            return VH.getViewHolder(parent, getHeaderLayoutId());
        else if (viewType == ItemType.FOOTER.ordinal())
            return VH.getViewHolder(parent, R.layout.footer_view);
        else
            return VH.getViewHolder(parent, getLayoutId());
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        int type = getItemViewType(position);
        if (type == ItemType.HEADER.ordinal()) {
            onBindHeaderViewHolder(holder, position);
        } else if (type == ItemType.FOOTER.ordinal()) {
            ViewStub footerLayout = holder.getView(R.id.footer_view_layout);
            if (mLoadMoreEnable) {
                footerLayout.setVisibility(View.VISIBLE);
                if (footerLayout.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
                    //如果使用StaggeredGridLayoutManager，把加载更多的ProgressBar居中
                    StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams)
                            footerLayout.getLayoutParams();
                    params.setFullSpan(true);
                }
            } else {
                footerLayout.setVisibility(View.GONE);
            }
        } else {
            onBindItemViewHolder(holder, position);
            if (mOnListItemClickListener != null) {
                holder.mConvertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getHeaderLayoutId() != HEADER_NONE)
                            mOnListItemClickListener.onItemClick(position - 1);
                        else
                            mOnListItemClickListener.onItemClick(position);
                    }
                });
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && getHeaderLayoutId() != HEADER_NONE)
            return ItemType.HEADER.ordinal();
        else if (position == getItemCount() - 1)
            return ItemType.FOOTER.ordinal();
        else
            return ItemType.NORMAL.ordinal();
    }

    @Override
    public int getItemCount() {
        if (getHeaderLayoutId() != HEADER_NONE)
            return mData != null ? mData.size() + 2 : 0; //1 for header, 1 for footer
        else
            return mData != null ? mData.size() + 1 : 0; //1 for footer
    }

    public int getActualItemCount() {
        return getItemCount() - 1;
    }

    public abstract int getLayoutId();

    public int getHeaderLayoutId() {
        return HEADER_NONE;
    }

    protected abstract void onBindItemViewHolder(VH viewHolder, int position);

    protected void onBindHeaderViewHolder(VH viewHolder, int position) {
    }

    public void addData(List<T> data) {
        if (data == null)
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

    public void remove(int position) {
        if (mData != null && position < mData.size()) {
            mData.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void removeAll(List<T> data) {
        if (mData != null) {
            mData.removeAll(data);
            notifyDataSetChanged();
        }
    }

    public List<T> getData() {
        return mData;
    }

    /**
     * Whether load more is enabled
     */
    public void setLoadMoreEnable(boolean enable) {
        mLoadMoreEnable = enable;
        if (!mLoadMoreEnable) {
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
    public OnRecyclerViewScrollListener getOnScrollListener() {
        return mOnScrollListener;
    }

    /**
     * item click listener
     */
    public void setOnListItemClickListener(OnListItemClickListener onListItemClickListener) {
        mOnListItemClickListener = onListItemClickListener;
    }

    protected static class VH extends RecyclerView.ViewHolder {
        private SparseArray<View> mViewSparseArray;
        private View mConvertView;

        VH(View view) {
            super(view);
            mViewSparseArray = new SparseArray<>();
            mConvertView = view;
        }

        public static VH getViewHolder(ViewGroup parent, int layoutId) {
            return new VH(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
        }

        @SuppressWarnings("unchecked")
        public <T extends View> T getView(int id) {
            View view = mViewSparseArray.get(id);
            if (view == null) {
                view = ButterKnife.findById(mConvertView, id);
                mViewSparseArray.put(id, view);
            }
            return (T) view;
        }


    }

    /**
     * the listener of recycler's scrolling
     * when scrolling to the last fully visible item, perform loading more
     */
    private class OnRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
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

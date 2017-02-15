package com.paulliu.zhihudaily.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.paulliu.zhihudaily.R;
import com.paulliu.zhihudaily.entity.CommentEntity;
import com.paulliu.zhihudaily.ui.adapter.base.RecyclerViewLoadMoreAdapter;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;

/**
 * Created on 2017/2/10
 *
 * @author LLW
 */

public class CommentListAdapter extends RecyclerViewLoadMoreAdapter<CommentEntity> {
    private static final int HEADER_LONG_COMMENT = 0;
    private static final int HEADER_SHORT_COMMENT = 1;
    private int mLongCommentCount, mShortCommentCount, mShortCommentHeaderPosition;
    private boolean mIsExpanded;//短评是否展开

    public CommentListAdapter(Context context, RecyclerView.LayoutManager layoutManager){
        super(context, layoutManager);
    }

    public void setCommentCount(int longCommentCount, int shortCommentCount){
        mLongCommentCount = longCommentCount;
        mShortCommentCount = shortCommentCount;
    }

    public void setShortCommentHeaderPosition(int position){
        mShortCommentHeaderPosition = position;
    }

    public int getShortCommentHeaderPosition(){
        return mShortCommentHeaderPosition;
    }


    @Override
    public int getItemViewType(int position) {
        if(position == 0)
            return HEADER_LONG_COMMENT;
        else if(position == mShortCommentHeaderPosition)
            return HEADER_SHORT_COMMENT;
        else
            return super.getItemViewType(position);
    }

//    @Override
//    public int getActualItemCount() {
//        return mData != null ? mData.size() + 2 : 0;
//    }
//
//    @Override
//    public int getItemCount() {
//        return mData != null ? mData.size() + 3 :0;
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == HEADER_LONG_COMMENT || viewType == HEADER_SHORT_COMMENT)
            return new HeaderViewHolder(mInflater.inflate(R.layout.item_comment_header, parent, false));
        else
            return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent) {
        return new ItemViewHolder(mInflater.inflate(R.layout.item_comment_list, parent, false));
    }

    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (getItemViewType(position)){
            case HEADER_LONG_COMMENT:
                ((HeaderViewHolder) viewHolder).titleTv.setText(TextUtils.concat(String.valueOf(mLongCommentCount), "条长评"));

                break;
            case HEADER_SHORT_COMMENT:
                ((HeaderViewHolder) viewHolder).titleTv.setText(TextUtils.concat(String.valueOf(mShortCommentCount), "条短评"));
                ((HeaderViewHolder) viewHolder).titleTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mIsExpanded){
                            foldShortComments();
                            mIsExpanded = false;
                        }
                        else{
                            loadShortComments();
                            mIsExpanded = true;
                        }
                    }
                });
                if(mShortCommentCount == 0)
                    ((HeaderViewHolder) viewHolder).titleTv.setEnabled(false);
                else
                    ((HeaderViewHolder) viewHolder).titleTv.setEnabled(true);
                if(getActualItemCount()-1 == position)
                    ((HeaderViewHolder) viewHolder).titleTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_down_arrow, 0);
                else
                    ((HeaderViewHolder) viewHolder).titleTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_up_arrow, 0);
                break;
            case TYPE_ITEM:
                CommentEntity commentEntity = mData.get(position);
                if(!TextUtils.isEmpty(commentEntity.getAvatar()))
                    Picasso.with(mContext)
                           .load(commentEntity.getAvatar())
//                           .transform(new CircleTransform())
                           .into(((ItemViewHolder) viewHolder).userAvatarIv);
                ((ItemViewHolder) viewHolder).userNameTv.setText(commentEntity.getAuthor());
                ((ItemViewHolder) viewHolder).commentContentTv.setText(commentEntity.getContent());
                ((ItemViewHolder) viewHolder).likeCountTv.setText(String.valueOf(commentEntity.getLikes()));
                break;
        }
    }

    public void loadShortComments(){}
    public void foldShortComments(){}

    private static class HeaderViewHolder extends RecyclerView.ViewHolder{
        private TextView titleTv;

        public HeaderViewHolder(View view){
            super(view);
            titleTv = ButterKnife.findById(view, R.id.tv_comment_header);
        }
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder{
        private ImageView userAvatarIv;
        private TextView userNameTv;
        private TextView commentContentTv;
        private TextView likeCountTv;

        public ItemViewHolder(View view){
            super(view);
            userAvatarIv = ButterKnife.findById(view, R.id.iv_user_avatar);
            userNameTv = ButterKnife.findById(view, R.id.tv_user_name);
            commentContentTv = ButterKnife.findById(view, R.id.tv_comment_content);
            likeCountTv = ButterKnife.findById(view, R.id.tv_like_count);
        }
    }
}

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
import com.paulliu.zhihudaily.ui.adapter.base.BaseRecyclerViewLoadMoreAdapter;
import com.paulliu.zhihudaily.widget.CircleTransform;
import com.squareup.picasso.Picasso;

/**
 * Created on 2017/2/10
 *
 * @author LLW
 */

public class CommentListAdapter extends BaseRecyclerViewLoadMoreAdapter<CommentEntity> {
    private static final int HEADER_LONG_COMMENT = 0x100;
    private static final int HEADER_SHORT_COMMENT = 0x200;
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
    public int getLayoutId() {
        return R.layout.item_comment_list;
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


    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == HEADER_LONG_COMMENT || viewType == HEADER_SHORT_COMMENT)
            return VH.getViewHolder(parent, R.layout.item_comment_header);
        else
            return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    protected void onBindItemViewHolder(VH viewHolder, int position) {
        switch (getItemViewType(position)){
            case HEADER_LONG_COMMENT:
                TextView titleTv = viewHolder.getView(R.id.tv_comment_header);
                titleTv.setText(TextUtils.concat(String.valueOf(mLongCommentCount), "条长评"));
                break;
            case HEADER_SHORT_COMMENT:
                TextView shortCommentTitleTv = viewHolder.getView(R.id.tv_comment_header);
                shortCommentTitleTv.setText(TextUtils.concat(String.valueOf(mShortCommentCount), "条短评"));
                shortCommentTitleTv.setOnClickListener(new View.OnClickListener() {
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
                    shortCommentTitleTv.setEnabled(false);
                else
                    shortCommentTitleTv.setEnabled(true);
                if(getActualItemCount()-1 == position)
                    shortCommentTitleTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_down_arrow, 0);
                else
                    shortCommentTitleTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_up_arrow, 0);
                break;
            default:
                ImageView userAvatarIv = viewHolder.getView(R.id.iv_user_avatar);
                TextView userNameTv = viewHolder.getView(R.id.tv_user_name);
                TextView commentContentTv = viewHolder.getView(R.id.tv_comment_content);
                TextView likeCountTv = viewHolder.getView(R.id.tv_like_count);

                CommentEntity commentEntity = mData.get(position);
                String tag = (String) userAvatarIv.getTag();
                if(!TextUtils.isEmpty(commentEntity.getAvatar()) && !commentEntity.getAvatar().equals(tag)){
                    Picasso.with(mContext)
                            .load(commentEntity.getAvatar())
                            .transform(new CircleTransform())
                            .into(userAvatarIv);
                    userAvatarIv.setTag(commentEntity.getAvatar());
                }
                userNameTv.setText(commentEntity.getAuthor());
                commentContentTv.setText(commentEntity.getContent());
                likeCountTv.setText(String.valueOf(commentEntity.getLikes()));
                break;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void loadShortComments(){}
    public void foldShortComments(){}
}

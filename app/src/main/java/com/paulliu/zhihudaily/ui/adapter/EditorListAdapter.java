package com.paulliu.zhihudaily.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.paulliu.zhihudaily.R;
import com.paulliu.zhihudaily.entity.EditorEntity;
import com.paulliu.zhihudaily.widget.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created on 2017/2/6
 *
 * @author LLW
 */

public class EditorListAdapter extends RecyclerView.Adapter<EditorListAdapter.EditorItemViewHolder>{
    private Context mContext;
    private List<EditorEntity> mData;


    public EditorListAdapter(Context context){
        mContext = context;
    }

    @Override
    public EditorItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EditorItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_editor, parent, false));
    }

    @Override
    public void onBindViewHolder(EditorItemViewHolder holder, int position) {
        final EditorEntity editorEntity = mData.get(position);
        if(!TextUtils.isEmpty(editorEntity.getAvatar()))
            Picasso.with(mContext).load(editorEntity.getAvatar())
                   .transform(new CircleTransform()).into(holder.avatarIv);
        holder.nameTv.setText(editorEntity.getName());
        holder.introductionTv.setText(editorEntity.getBio());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToEditorPage(editorEntity.getUrl());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public void setData(List<EditorEntity> editors){
        mData = editors;
        notifyDataSetChanged();
    }

    public void navigateToEditorPage(String url){

    }
    static class EditorItemViewHolder extends RecyclerView.ViewHolder{
        ImageView avatarIv;
        TextView nameTv;
        TextView introductionTv;
        View view;

        public EditorItemViewHolder(View v){
            super(v);
            view = v;
            avatarIv = ButterKnife.findById(v, R.id.iv_editor_avatar);
            nameTv = ButterKnife.findById(v, R.id.tv_editor_name);
            introductionTv = ButterKnife.findById(v, R.id.tv_editor_introduction);
        }
    }
}

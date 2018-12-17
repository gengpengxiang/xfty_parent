package com.bj.hmxxparents.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.entity.CommentInfo;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by zz379 on 2017/1/6.
 * 全部学生页面adapter
 */

public class DoukeCommentAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {

    private List<CommentInfo> mDataList;

    public DoukeCommentAdapter(List<CommentInfo> mDataList) {
        this.mDataList = mDataList;
    }

    @Override
    public int getAdapterItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        RecyclerView.ViewHolder vh;

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_comment_info, parent, false);
        vh = new ViewHolderStudent(v, true);

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder fholder, int position, boolean isItem) {
        CommentInfo itemInfo = mDataList.get(position);

        ViewHolderStudent holder = (ViewHolderStudent) fholder;
        holder.tvCommentName.setText(itemInfo.getCommCreaterName());
        holder.tvCommentTime.setText(itemInfo.getCommCreateTime());
        holder.ivUserPhoto.setImageURI(itemInfo.getCommCreaterPhoto());
        holder.tvCommentContent.setText(itemInfo.getCommContent());
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(View view) {
        return new ViewHolderStudent(view, false);
    }

    public class ViewHolderStudent extends RecyclerView.ViewHolder {

        private SimpleDraweeView ivUserPhoto;
        private TextView tvCommentName, tvCommentTime, tvCommentContent;

        public ViewHolderStudent(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                AutoUtils.auto(itemView);
                ivUserPhoto = (SimpleDraweeView) itemView.findViewById(R.id.iv_userPhoto);
                tvCommentName = (TextView) itemView.findViewById(R.id.tv_commentName);
                tvCommentTime = (TextView) itemView.findViewById(R.id.tv_commentTime);
                tvCommentContent = (TextView) itemView.findViewById(R.id.tv_commentContent);
            }
        }
    }

    public void setData(List<CommentInfo> list) {
        this.mDataList = list;
        notifyDataSetChanged();
    }


    public void insert(CommentInfo person, int position) {
        insert(mDataList, person, position);
    }

    public void remove(int position) {
        remove(mDataList, position);
    }

    public void clear() {
        clear(mDataList);
    }

    public CommentInfo getItem(int position) {
        if (position < mDataList.size())
            return mDataList.get(position);
        else
            return null;
    }

    private OnMyItemClickListener myItemClickListener;

    public void setOnMyItemClickListener(OnMyItemClickListener listener) {
        this.myItemClickListener = listener;
    }

    public interface OnMyItemClickListener {
        void onClick(View view, int position);
    }
}

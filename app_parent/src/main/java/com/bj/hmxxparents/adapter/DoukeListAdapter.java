package com.bj.hmxxparents.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.entity.ArticleInfo;
import com.bj.hmxxparents.utils.StringUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by zz379 on 2017/1/6.
 * 全部学生页面adapter
 */

public class DoukeListAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {

    private List<ArticleInfo> mDataList;

    public DoukeListAdapter(List<ArticleInfo> mDataList) {
        this.mDataList = mDataList;
    }

    @Override
    public int getAdapterItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        RecyclerView.ViewHolder vh;

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_douke, parent, false);
        vh = new ViewHolderStudent(v, true);

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder fholder, int position, boolean isItem) {
        ArticleInfo itemInfo = mDataList.get(position);

        ViewHolderStudent holder = (ViewHolderStudent) fholder;
        holder.tvTitle.setText(itemInfo.getTitle());
        holder.tvAuthorName.setText(itemInfo.getAuthor());
        if (!StringUtils.isEmpty(itemInfo.getAuthImg())) {
            holder.ivAuthorPhoto.setImageURI(itemInfo.getAuthImg());
        }
        if (!StringUtils.isEmpty(itemInfo.getArticlePicture())) {
            holder.ivPicture.setImageURI(itemInfo.getArticlePicture());
        }
        String desc = itemInfo.getAuthDesc();
        holder.tvAuthorDesc.setText(StringUtils.isEmpty(desc) ? "暂无" : desc);
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(View view) {
        return new ViewHolderStudent(view, false);
    }

    public class ViewHolderStudent extends RecyclerView.ViewHolder {

        private SimpleDraweeView ivAuthorPhoto;
        private SimpleDraweeView ivPicture;
        private TextView tvTitle, tvAuthorName, tvAuthorDesc;

        public ViewHolderStudent(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                AutoUtils.auto(itemView);
                tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
                tvAuthorName = (TextView) itemView.findViewById(R.id.tv_authorName);
                tvAuthorDesc = (TextView) itemView.findViewById(R.id.tv_authorDesc);
                ivAuthorPhoto = (SimpleDraweeView) itemView.findViewById(R.id.iv_authorPhoto);
                ivPicture = (SimpleDraweeView) itemView.findViewById(R.id.iv_picture);

                itemView.setOnClickListener(v -> {
                    if (myItemClickListener != null) {
                        myItemClickListener.onClick(v, getAdapterPosition());
                    }
                });
            }
        }
    }

    public void setData(List<ArticleInfo> list) {
        this.mDataList = list;
        notifyDataSetChanged();
    }


    public void insert(ArticleInfo person, int position) {
        insert(mDataList, person, position);
    }

    public void remove(int position) {
        remove(mDataList, position);
    }

    public void clear() {
        clear(mDataList);
    }

    public ArticleInfo getItem(int position) {
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

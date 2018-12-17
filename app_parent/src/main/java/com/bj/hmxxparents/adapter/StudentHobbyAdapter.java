package com.bj.hmxxparents.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.entity.StudentHobbyInfo;
import com.bj.hmxxparents.utils.StringUtils;
import com.bj.hmxxparents.widget.HobbyNoneView;
import com.bj.hmxxparents.widget.SquareLayout;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by zz379 on 2017/5/6.
 */

public class StudentHobbyAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {

    private List<StudentHobbyInfo> mDataList;

    public StudentHobbyAdapter(List<StudentHobbyInfo> mDataList) {
        this.mDataList = mDataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        RecyclerView.ViewHolder vh;
        if (viewType == StudentHobbyInfo.SHOW_TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_hobby, parent, false);
            vh = new ViewHolderHobby(v, true);
        } else if (viewType == StudentHobbyInfo.SHOW_TYPE_CATEGORY) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_hobby_category, parent, false);
            vh = new ViewHolderCategory(v, true);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_hobby_none, parent, false);
            vh = new ViewHolderNone(v, true);
        }
        return vh;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(View view) {
        return new ViewHolderHobby(view, false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder fholder, int position, boolean isItem) {
        StudentHobbyInfo item = mDataList.get(position);
        if (fholder instanceof ViewHolderHobby) {
            ViewHolderHobby holder = (ViewHolderHobby) fholder;
            holder.checkBox.setChecked(item.isHobbyIsChecked());
            holder.tvName.setText(item.getHobbyName());
            if (!StringUtils.isEmpty(item.getHobbyPic())) {
                holder.ivHobbyPic.setImageURI(item.getHobbyPic());
            }
        } else if (fholder instanceof ViewHolderCategory) {
            ViewHolderCategory holder = (ViewHolderCategory) fholder;
            holder.tvCategoryName.setText(item.getHobbyName());
        } else {
            ViewHolderNone holder = (ViewHolderNone) fholder;
            holder.tvHobbyNone.setChecked(item.isHobbyIsChecked());
        }
    }

    @Override
    public int getAdapterItemCount() {
        return mDataList.size();
    }

    @Override
    public int getAdapterItemViewType(int position) {
        StudentHobbyInfo item = mDataList.get(position);
        return item.getHobbyShowType();
    }

    public class ViewHolderCategory extends RecyclerView.ViewHolder {
        private TextView tvCategoryName;

        public ViewHolderCategory(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                AutoUtils.auto(itemView);
                tvCategoryName = (TextView) itemView.findViewById(R.id.tv_latest_news);
            }
        }
    }

    public class ViewHolderNone extends RecyclerView.ViewHolder {
        private HobbyNoneView tvHobbyNone;

        public ViewHolderNone(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                AutoUtils.auto(itemView);
                tvHobbyNone = (HobbyNoneView) itemView.findViewById(R.id.tv_hobbyNone);
                tvHobbyNone.setOnClickListener(v -> {
                    if (myItemClickListener != null) {
                        myItemClickListener.onNoneClick(v, getAdapterPosition());
                    }
                });
            }
        }
    }


    public class ViewHolderHobby extends RecyclerView.ViewHolder {
        private SquareLayout flContent;
        private SimpleDraweeView ivHobbyPic;
        private HobbyNoneView checkBox;
        private TextView tvName;

        public ViewHolderHobby(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                AutoUtils.auto(itemView);
                flContent = (SquareLayout) itemView.findViewById(R.id.fl_content);
                ivHobbyPic = (SimpleDraweeView) itemView.findViewById(R.id.iv_picture);
                checkBox = (HobbyNoneView) itemView.findViewById(R.id.cbView);
                tvName = (TextView) itemView.findViewById(R.id.tv_name);

                flContent.setOnClickListener(v -> {
                    if (myItemClickListener != null) {
                        myItemClickListener.onClick(v, getAdapterPosition());
                    }
                });
            }
        }
    }

    private OnMyItemClickListener myItemClickListener;

    public void setOnMyItemClickListener(OnMyItemClickListener listener) {
        this.myItemClickListener = listener;
    }

    public interface OnMyItemClickListener {
        void onClick(View view, int position);

        void onNoneClick(View view, int position);
    }
}

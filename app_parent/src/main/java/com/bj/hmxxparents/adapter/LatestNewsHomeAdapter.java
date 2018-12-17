package com.bj.hmxxparents.adapter;


import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.entity.ClassNewsInfo;
import com.bj.hmxxparents.utils.StringUtils;
import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import static com.bj.hmxxparents.api.HttpUtilService.BASE_RESOURCE_URL;

/**
 * Created by zz379 on 2017/1/6.
 * 全部学生页面adapter
 */

public class LatestNewsHomeAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {

    private List<ClassNewsInfo> mDataList;
    private String newsEmptyDesc;

    public LatestNewsHomeAdapter(List<ClassNewsInfo> mDataList, String newsEmptyDesc) {
        this.mDataList = mDataList;
        this.newsEmptyDesc = newsEmptyDesc;
    }

    private enum ShowType {ITEM_NORMAL, ITEM_EMPTY}

    @Override
    public int getAdapterItemCount() {
        return mDataList.size();
    }

    @Override
    public int getAdapterItemViewType(int position) {
        ClassNewsInfo newsInfo = mDataList.get(position);
        if (newsInfo.getItemShowType() == ClassNewsInfo.ITEM_SHOW_TYPE_EMPTY) {
            return ShowType.ITEM_EMPTY.ordinal();
        } else {
            return ShowType.ITEM_NORMAL.ordinal();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        RecyclerView.ViewHolder vh;
        if (viewType == ShowType.ITEM_NORMAL.ordinal()) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_news_info, parent, false);
            vh = new ViewHolderStudent(v, true);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_tianyuan_empty, parent, false);
            vh = new ViewHolderEmptyView(v, true);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder fholder, int position, boolean isItem) {
        ClassNewsInfo itemInfo = mDataList.get(position);
        if (fholder instanceof ViewHolderStudent) {
            ViewHolderStudent holder = (ViewHolderStudent) fholder;
            if (!StringUtils.isEmpty(itemInfo.getTeacherPic())) {
                holder.imgUserPhoto.setImageURI(itemInfo.getTeacherPic());
            }
            holder.tvteacherName.setText(StringUtils.isEmpty(itemInfo.getTeacherName()) ? "" : itemInfo.getTeacherName());
            holder.tvNewsTime.setText(itemInfo.getNewsTime());

            if (!StringUtils.isEmpty(itemInfo.getNewsThanksStatus()) && itemInfo.getNewsThanksStatus().equals("1")) {
                holder.tvThanksTeacher.setClickable(true);
                holder.tvThanksTeacher.setBackgroundResource(R.drawable.shape_btn_commend);
                holder.tvThanksTeacher.setText("感谢");
            } else {
//                holder.tvThanksTeacher.setClickable(false);
                holder.tvThanksTeacher.setClickable(false);
                holder.tvThanksTeacher.setBackgroundResource(R.drawable.shape_btn_thanks);
                holder.tvThanksTeacher.setText("已感谢");
            }
            holder.tvBadgeDesc.setText(itemInfo.getNewsDesc());


            holder.svIcon.setImageURI(itemInfo.getNewsPicture());


            String newsType = itemInfo.getNewsType();

            if (!StringUtils.isEmpty(newsType)) {
                if (newsType.equals("z1")) {
                    holder.llCommend.setVisibility(View.VISIBLE);
                    holder.llBadge.setVisibility(View.GONE);
                    holder.tvCommendReason.setText(itemInfo.getNewsTitle());
                } else if (newsType.equals("3")) {
                    holder.llCommend.setVisibility(View.GONE);
                    holder.llBadge.setVisibility(View.VISIBLE);
                    Uri uri = Uri.parse("res:///" + R.drawable.ic_badge_behavior);
                    holder.imgBadgeIcon.setImageURI(uri);
                    holder.tvBadgeName.setText(itemInfo.getNewsTitle());
                } else if (newsType.equals("4")) {
                    holder.llCommend.setVisibility(View.GONE);
                    holder.llBadge.setVisibility(View.VISIBLE);
                    Uri uri = Uri.parse("res:///" + R.drawable.ic_badge_science);
                    holder.imgBadgeIcon.setImageURI(uri);
                    holder.tvBadgeName.setText(itemInfo.getNewsTitle());
                } else if (newsType.equals("5")) {
                    holder.llCommend.setVisibility(View.GONE);
                    holder.llBadge.setVisibility(View.VISIBLE);
                    Uri uri = Uri.parse("res:///" + R.drawable.ic_badge_art);
                    holder.imgBadgeIcon.setImageURI(uri);
                    holder.tvBadgeName.setText(itemInfo.getNewsTitle());
                } else if (newsType.equals("6")) {
                    holder.llCommend.setVisibility(View.GONE);
                    holder.llBadge.setVisibility(View.VISIBLE);
                    Uri uri = Uri.parse("res:///" + R.drawable.ic_badge_sport);
                    holder.imgBadgeIcon.setImageURI(uri);
                    holder.tvBadgeName.setText(itemInfo.getNewsTitle());
                } else if (newsType.equals("ic_pinde")) {
                    holder.llCommend.setVisibility(View.GONE);
                    holder.llBadge.setVisibility(View.VISIBLE);
                    Uri uri = Uri.parse("res:///" + R.drawable.ic_badge_language);
                    holder.imgBadgeIcon.setImageURI(uri);
                    holder.tvBadgeName.setText(itemInfo.getNewsTitle());
                } else {
                    holder.llCommend.setVisibility(View.GONE);
                    holder.llBadge.setVisibility(View.VISIBLE);
                    if (!StringUtils.isEmpty(itemInfo.getNewsPicture())) {
                        holder.imgBadgeIcon.setImageURI(itemInfo.getNewsPicture());
                    }
                    holder.tvBadgeName.setText(itemInfo.getNewsTitle());
                }
            } else {
                holder.llCommend.setVisibility(View.GONE);
                holder.llBadge.setVisibility(View.VISIBLE);
                if (!StringUtils.isEmpty(itemInfo.getNewsPicture())) {
                    holder.imgBadgeIcon.setImageURI(itemInfo.getNewsPicture());
                }
                holder.tvBadgeName.setText(itemInfo.getNewsTitle());
            }

        } else {
//            ViewHolderEmptyView holder = (ViewHolderEmptyView) fholder;
//            holder.tv_newsEmptyDesc.setText(newsEmptyDesc);
        }
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(View view) {
        return new ViewHolderStudent(view, false);
    }

    public class ViewHolderEmptyView extends RecyclerView.ViewHolder {

        private TextView tv_newsEmptyDesc;

        public ViewHolderEmptyView(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                AutoUtils.auto(itemView);
                tv_newsEmptyDesc = (TextView) itemView.findViewById(R.id.tv_newsEmptyDesc);
            }
        }
    }

    public class ViewHolderStudent extends RecyclerView.ViewHolder {

        private SimpleDraweeView imgUserPhoto;
        private TextView tvteacherName;
        private TextView tvNewsTime;
        private TextView tvThanksTeacher;

        private LinearLayout llCommend, llBadge;
        private TextView tvCommendReason, tvBadgeName;
        private SimpleDraweeView imgBadgeIcon;
        private TextView tvBadgeDesc;

        private FrameLayout frameLayout;

        private SimpleDraweeView svIcon;

        public ViewHolderStudent(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                AutoUtils.auto(itemView);
                imgUserPhoto = (SimpleDraweeView) itemView.findViewById(R.id.img_kidPhoto);
                tvteacherName = (TextView) itemView.findViewById(R.id.tv_user_name);
                tvNewsTime = (TextView) itemView.findViewById(R.id.tv_news_time);
                tvThanksTeacher = (TextView) itemView.findViewById(R.id.tv_thanks_teacher);

                llCommend = (LinearLayout) itemView.findViewById(R.id.ll_commend);
                llBadge = (LinearLayout) itemView.findViewById(R.id.ll_badge);
                tvCommendReason = (TextView) itemView.findViewById(R.id.tv_commend_reason);
                tvBadgeName = (TextView) itemView.findViewById(R.id.tv_badge_name);
                imgBadgeIcon = (SimpleDraweeView) itemView.findViewById(R.id.img_badge_icon);
                tvBadgeDesc = (TextView) itemView.findViewById(R.id.tv_badge_detail);

                frameLayout = (FrameLayout) itemView.findViewById(R.id.layout_item);

                svIcon = (SimpleDraweeView) itemView.findViewById(R.id.sv_icon);

                tvThanksTeacher.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (myItemClickListener != null) {
                            myItemClickListener.onClick(v, getAdapterPosition());
                        }
                    }
                });

                //add
                frameLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (myItemClickListener != null) {
                            myItemClickListener.onClick(v, getAdapterPosition());
                        }
                    }
                });
            }
        }
    }

    public void setData(List<ClassNewsInfo> list) {
        this.mDataList = list;
        notifyDataSetChanged();
    }

    public void insert(ClassNewsInfo person, int position) {
        insert(mDataList, person, position);
    }

    public void remove(int position) {
        remove(mDataList, position);
    }

    public void clear() {
        clear(mDataList);
    }

    public ClassNewsInfo getItem(int position) {
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

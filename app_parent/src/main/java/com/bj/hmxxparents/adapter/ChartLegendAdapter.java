package com.bj.hmxxparents.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.entity.ChartItemInfo;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by zz379 on 2017/5/6.
 */

public class ChartLegendAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {

    private List<ChartItemInfo> mDataList;

    public ChartLegendAdapter(List<ChartItemInfo> mDataList) {
        this.mDataList = mDataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_chart_legend, parent, false);
        vh = new ViewHolderLegend(v, true);
        return vh;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(View view) {
        return new ViewHolderLegend(view, false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder fholder, int position, boolean isItem) {
        ChartItemInfo item = mDataList.get(position);
        ViewHolderLegend holder = (ViewHolderLegend) fholder;
        holder.ivLegend.setBackgroundColor(item.getColor());
        holder.tvLegendContent.setText(item.getContent() + " " + item.getNumber());
    }

    @Override
    public int getAdapterItemCount() {
        return mDataList.size();
    }

    public class ViewHolderLegend extends RecyclerView.ViewHolder {

        View ivLegend;
        TextView tvLegendContent;

        public ViewHolderLegend(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                AutoUtils.auto(itemView);
                ivLegend = itemView.findViewById(R.id.iv_legend);
                tvLegendContent = (TextView) itemView.findViewById(R.id.tv_legendContent);
            }
        }
    }

    private OnMyItemClickListener myItemClickListener;

    public void setOnMyItemClickListener(OnMyItemClickListener listener) {
        this.myItemClickListener = listener;
    }

    public interface OnMyItemClickListener {
        void onClick(View view, int position);
    }
}

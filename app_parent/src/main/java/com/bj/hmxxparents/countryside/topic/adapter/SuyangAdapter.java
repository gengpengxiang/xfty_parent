package com.bj.hmxxparents.countryside.topic.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.bj.hmxxparents.R;
import com.bj.hmxxparents.countryside.topic.model.TopicDetail;
import com.bj.hmxxparents.huodong.model.HuodongDetail;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2018/11/19 0019.
 */

public class SuyangAdapter extends BaseQuickAdapter<TopicDetail.DataBean.HuodongSuyangBean, BaseViewHolder> {


    public SuyangAdapter(int layoutResId, @Nullable List<TopicDetail.DataBean.HuodongSuyangBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TopicDetail.DataBean.HuodongSuyangBean item) {

        helper.setText(R.id.tv_title,item.getName());

//        TextView tvTitle = helper.getView(R.id.tv_title);
//
//        View line = helper.getView(R.id.line);
//        if(item.isSelect()){
//            tvTitle.setTextColor(Color.parseColor("#000000"));
//            line.setBackgroundColor(Color.parseColor("#4aa003"));
//        }else {
//            line.setBackgroundColor(Color.parseColor("#ffffff"));
//            tvTitle.setTextColor(Color.parseColor("#666666"));
//        }

    }
}

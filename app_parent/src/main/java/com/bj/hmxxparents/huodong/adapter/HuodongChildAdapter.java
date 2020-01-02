package com.bj.hmxxparents.huodong.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.bj.hmxxparents.R;
import com.bj.hmxxparents.comment.modal.CommentType;
import com.bj.hmxxparents.countryside.honorroll.model.Honor;
import com.bj.hmxxparents.huodong.model.HuodongDetail;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2018/11/19 0019.
 */

public class HuodongChildAdapter extends BaseQuickAdapter<HuodongDetail.DataBean.HuodongSuyangBean, BaseViewHolder> {


    public HuodongChildAdapter(int layoutResId, @Nullable List<HuodongDetail.DataBean.HuodongSuyangBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HuodongDetail.DataBean.HuodongSuyangBean item) {

        helper.setText(R.id.tv_title,item.getName());

        TextView tvTitle = helper.getView(R.id.tv_title);

        if(item.isSelect()){
            tvTitle.setBackgroundResource(R.drawable.shape_subject_tag_checked);
            tvTitle.setTextColor(Color.parseColor("#ffffff"));
        }else {
            tvTitle.setBackgroundResource(R.drawable.shape_subject_tag_normal);
            tvTitle.setTextColor(Color.parseColor("#707070"));
        }

    }
}

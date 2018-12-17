package com.bj.hmxxparents.countryside.honorroll.adapter;

import android.support.annotation.Nullable;

import com.bj.hmxxparents.R;
import com.bj.hmxxparents.countryside.honorroll.model.Honor;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2018/11/19 0019.
 */

public class HonorChildAdapter extends BaseQuickAdapter<Honor.DataBean.StudentPmBean, BaseViewHolder> {


    public HonorChildAdapter(int layoutResId, @Nullable List<Honor.DataBean.StudentPmBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Honor.DataBean.StudentPmBean item) {

        helper.setText(R.id.tv_score,item.getHuizhangnum());
        helper.setText(R.id.tv_studentname,item.getStudent_name());

    }
}

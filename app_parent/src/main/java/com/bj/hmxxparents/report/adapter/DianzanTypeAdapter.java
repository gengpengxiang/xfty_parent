package com.bj.hmxxparents.report.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;

import com.bj.hmxxparents.R;
import com.bj.hmxxparents.report.model.Report2;
import com.bj.hmxxparents.report.model.Report3;
import com.bj.hmxxparents.widget.AutoScaleTextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class DianzanTypeAdapter extends BaseQuickAdapter<Report3.DataBean.DznumBean,BaseViewHolder> {


    public DianzanTypeAdapter(int layoutResId, @Nullable List<Report3.DataBean.DznumBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Report3.DataBean.DznumBean item) {

        helper.setText(R.id.tv_name,item.getLiyou()+" "+item.getDznum());
        AutoScaleTextView tvName = helper.getView(R.id.tv_name);

        View view =helper.getView(R.id.iv);
        view.setBackgroundColor(item.getColor());
        tvName.setTextColor(item.getColor());


    }

}

package com.bj.hmxxparents.report.term.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;

import com.bj.hmxxparents.R;
import com.bj.hmxxparents.report.term.model.Report2;
import com.bj.hmxxparents.widget.AutoScaleTextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class BadgeTypeAdapter extends BaseQuickAdapter<Report2.DataBean.HznumBean,BaseViewHolder> {


    public BadgeTypeAdapter(int layoutResId, @Nullable List<Report2.DataBean.HznumBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Report2.DataBean.HznumBean item) {

        helper.setText(R.id.tv_name,item.getName()+" "+item.getHznum());

        AutoScaleTextView tvName = helper.getView(R.id.tv_name);
        View view =helper.getView(R.id.iv);

        switch (item.getCode()){
            case "3":
                view.setBackgroundColor(Color.rgb(196 ,31 ,31));//品德
                tvName.setTextColor(Color.rgb(196 ,31 ,31));
                break;
            case "4":
                view.setBackgroundColor(Color.rgb(44 ,101 ,168));//科学
                tvName.setTextColor(Color.rgb(44 ,101 ,168));
                break;
            case "5":
                view.setBackgroundColor(Color.rgb(156 ,98 ,165));//艺术
                tvName.setTextColor(Color.rgb(156 ,98 ,165));
                break;
            case "6":
                view.setBackgroundColor(Color.rgb(242 ,227, 37));//健康
                tvName.setTextColor(Color.rgb(242 ,227, 37));
                break;
            case "7":
                view.setBackgroundColor(Color.rgb(223 ,99 ,28));//人文
                tvName.setTextColor(Color.rgb(223 ,99 ,28));
                break;
            case "9":
                view.setBackgroundColor(Color.rgb(108 ,173 ,72));//实践
                tvName.setTextColor(Color.rgb(108 ,173 ,72));
                break;
        }



    }

}

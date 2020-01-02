package com.bj.hmxxparents.report.term.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bj.hmxxparents.R;
import com.bj.hmxxparents.report.term.model.BadgeRecord;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import java.util.List;

import javax.microedition.khronos.opengles.GL;

public class BadgeRecordAdapter extends BaseQuickAdapter<BadgeRecord.DataBean,BaseViewHolder> {


    public BadgeRecordAdapter(int layoutResId, @Nullable List<BadgeRecord.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BadgeRecord.DataBean item) {

        String time = item.getStime().substring(5,7)+"."+item.getStime().substring(8,10)+"-"+item.getEtime().substring(5,7)+"."+item.getEtime().substring(8,10);

        helper.setText(R.id.tv_date,time);
        TextView tvTime = helper.getView(R.id.tv_date);
        RecyclerView childRecyclerView = helper.getView(R.id.childRecyclerView);




        if(item.getTime_status().equals("1")){
            tvTime.setVisibility(View.VISIBLE);

//            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) childRecyclerView.getLayoutParams();
//            lp.addRule(RelativeLayout.CENTER_VERTICAL);
//            childRecyclerView.setLayoutParams(lp);
        }else {
            tvTime.setVisibility(View.INVISIBLE);
        }

        childRecyclerView.setLayoutManager(new GridLayoutManager(mContext,6));
        ChildAdapter adapter = new ChildAdapter(R.layout.recycler_item_report_badge_record_child,item.getHuizhang());
        childRecyclerView.setAdapter(adapter);
    }

    class ChildAdapter extends BaseQuickAdapter<String,BaseViewHolder>{

        public ChildAdapter(int layoutResId, @Nullable List<String> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {

            ImageView ivBadge = helper.getView(R.id.iv_badge);

            switch (item){
                case "3":
                    ivBadge.setImageResource(R.mipmap.ic_badge_sipin);
                    break;
                case "4":
                    ivBadge.setImageResource(R.mipmap.ic_badge_keji);
                    break;
                case "5":
                    ivBadge.setImageResource(R.mipmap.ic_badge_yishu);
                    break;
                case "6":
                    ivBadge.setImageResource(R.mipmap.ic_badge_tiyu);
                    break;
                case "7":
                    ivBadge.setImageResource(R.mipmap.ic_badge_renwen);
                    break;
                case "9":
                    ivBadge.setImageResource(R.mipmap.ic_badge_shijian);
                    break;
            }
        }
    }

}

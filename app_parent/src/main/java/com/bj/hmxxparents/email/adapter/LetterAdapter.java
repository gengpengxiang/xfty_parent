package com.bj.hmxxparents.email.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bj.hmxxparents.R;
import com.bj.hmxxparents.email.model.Letter;
import com.bj.hmxxparents.utils.Base64Util;
import com.bj.hmxxparents.widget.AutoScaleTextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import static com.bj.hmxxparents.api.HttpUtilService.BASE_RESOURCE_URL;

public class LetterAdapter extends BaseQuickAdapter<Letter.DataBean.ListDataBean, BaseViewHolder> {

    public Boolean longclick = false;

    public LetterAdapter(int layoutResId, @Nullable List<Letter.DataBean.ListDataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Letter.DataBean.ListDataBean item) {
        helper.setText(R.id.tv_title, Base64Util.decode(item.getTitle()));
        helper.setText(R.id.tv_date, item.getDate().substring(5, 10));
        helper.setText(R.id.tv_content, Base64Util.decode(item.getContent()));

        SimpleDraweeView sv = helper.getView(R.id.sv_userPhoto);
        sv.setImageURI(BASE_RESOURCE_URL + item.getImg());

        TextView tvStatus = helper.getView(R.id.tv_huifu_new);
        if (item.getJ_newhuifu_status().equals("0")) {
            tvStatus.setVisibility(View.GONE);
        } else {
            tvStatus.setVisibility(View.VISIBLE);
        }


        RelativeLayout layout = helper.getView(R.id.layout);


        if (item.isLongPress()) {
            layout.setBackgroundColor(Color.parseColor("#f1f1f1"));
        } else {
            layout.setBackgroundColor(Color.WHITE);
        }

    }

}

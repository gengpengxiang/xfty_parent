package com.bj.hmxxparents.comment.adapter;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.bj.hmxxparents.R;
import com.bj.hmxxparents.comment.modal.CommentInfo;
import com.bj.hmxxparents.email.model.Reply;
import com.bj.hmxxparents.utils.Base64Util;
import com.bj.hmxxparents.utils.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import static com.bj.hmxxparents.api.HttpUtilService.BASE_RESOURCE_URL;

public class CommentAdapter extends BaseQuickAdapter<CommentInfo.DataBean,BaseViewHolder> {


    public CommentAdapter(int layoutResId, @Nullable List<CommentInfo.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommentInfo.DataBean item) {
//        helper.setText(R.id.tv_content, Base64Util.decode(item.getContent()));
//        SimpleDraweeView sv = helper.getView(R.id.sv_userPhoto);
//        sv.setImageURI(BASE_RESOURCE_URL + item.getImg());

        SimpleDraweeView svUser = helper.getView(R.id.sv_user);
        svUser.setImageURI(BASE_RESOURCE_URL + item.getTeacher_pic().getImg());

        helper.setText(R.id.tv_name, item.getTeacher_name());
        helper.setText(R.id.tv_time, item.getDongtai_time());

        helper.setText(R.id.tv_title, item.getDongtai_title());

        SimpleDraweeView svIcon = helper.getView(R.id.sv_icon);
        svIcon.setImageURI(BASE_RESOURCE_URL + item.getDongtai_pic());


        TextView tvThanks = helper.getView(R.id.tv_thanks);
        if (!StringUtils.isEmpty(item.getDongtai_ganxiestatus()) && item.getDongtai_ganxiestatus().equals("1")) {
            tvThanks.setClickable(true);
            tvThanks.setBackgroundResource(R.drawable.shape_btn_commend);
            tvThanks.setText("感谢");

            helper.addOnClickListener(R.id.tv_thanks);
        } else {
            tvThanks.setClickable(false);
            tvThanks.setBackgroundResource(R.drawable.shape_btn_thanks);
            tvThanks.setText("已感谢");
        }

//        helper.addOnClickListener(R.id.tv_thanks);

    }

}

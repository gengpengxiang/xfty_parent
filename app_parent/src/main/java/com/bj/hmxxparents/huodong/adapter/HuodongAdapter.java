package com.bj.hmxxparents.huodong.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bj.hmxxparents.R;
import com.bj.hmxxparents.comment.modal.CommentInfo;
import com.bj.hmxxparents.huodong.model.HuodongInfo;
import com.bj.hmxxparents.utils.StringUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import static com.bj.hmxxparents.api.HttpUtilService.BASE_RESOURCE_URL;

public class HuodongAdapter extends BaseQuickAdapter<HuodongInfo.DataBean,BaseViewHolder> {


    public HuodongAdapter(int layoutResId, @Nullable List<HuodongInfo.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HuodongInfo.DataBean item) {
//        helper.setText(R.id.tv_content, Base64Util.decode(item.getContent()));
//        SimpleDraweeView sv = helper.getView(R.id.sv_userPhoto);
//        sv.setImageURI(BASE_RESOURCE_URL + item.getImg());

        ImageView iv = helper.getView(R.id.iv_user);

        Glide.with(mContext).load(BASE_RESOURCE_URL +item.getImg()).placeholder(R.mipmap.ic_huodong_default).error(R.mipmap.ic_huodong_default).into(iv);

        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_tongji, item.getTaolun_num()+"讨论"+"    "+item.getUser_num()+"参与");


    }

}

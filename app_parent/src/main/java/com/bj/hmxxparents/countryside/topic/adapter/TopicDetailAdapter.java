package com.bj.hmxxparents.countryside.topic.adapter;

import android.support.annotation.Nullable;

import com.bj.hmxxparents.R;
import com.bj.hmxxparents.countryside.topic.model.Topic;
import com.bj.hmxxparents.countryside.topic.model.TopicDetail;
import com.bj.hmxxparents.utils.Base64Util;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;

import static com.bj.hmxxparents.api.HttpUtilService.BASE_RESOURCE_URL;

public class TopicDetailAdapter extends BaseQuickAdapter<TopicDetail.DataBean.HuifuListBean, BaseViewHolder> {


    public TopicDetailAdapter(int layoutResId, @Nullable List<TopicDetail.DataBean.HuifuListBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, TopicDetail.DataBean.HuifuListBean item) {

        SimpleDraweeView svPhoto = helper.getView(R.id.sv_photo);
        svPhoto.setImageURI(BASE_RESOURCE_URL+item.getImg());

        helper.setText(R.id.tv_name,item.getName());
        helper.setText(R.id.tv_date,item.getTime());
        helper.setText(R.id.tv_content,Base64Util.decode(item.getHuifu_content()));
    }

}

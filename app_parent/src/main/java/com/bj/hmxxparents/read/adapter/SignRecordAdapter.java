package com.bj.hmxxparents.read.adapter;

import android.support.annotation.Nullable;

import com.bj.hmxxparents.R;
import com.bj.hmxxparents.email.model.Reply;
import com.bj.hmxxparents.read.model.SignRecord;
import com.bj.hmxxparents.utils.Base64Util;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import static com.bj.hmxxparents.api.HttpUtilService.BASE_RESOURCE_URL;

public class SignRecordAdapter extends BaseQuickAdapter<SignRecord.DataBean.QiandaoDataBean,BaseViewHolder> {


    public SignRecordAdapter(int layoutResId, @Nullable List<SignRecord.DataBean.QiandaoDataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SignRecord.DataBean.QiandaoDataBean item) {
        helper.setText(R.id.tv_name, item.getStudent_name());

        SimpleDraweeView svPhoto = helper.getView(R.id.sv_photo);
        svPhoto.setImageURI(BASE_RESOURCE_URL+item.getStudent_img());

        helper.setText(R.id.tv_date, item.getCreatetime().substring(0,16));
        helper.setText(R.id.tv_days, "已坚持"+item.getQiandao()+"天");
        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_number, item.getNum());
        helper.setText(R.id.tv_duration, item.getTime());
    }

}

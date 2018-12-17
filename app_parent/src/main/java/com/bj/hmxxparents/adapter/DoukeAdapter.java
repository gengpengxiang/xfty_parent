package com.bj.hmxxparents.adapter;

import android.support.annotation.Nullable;

import com.bj.hmxxparents.R;
import com.bj.hmxxparents.entity.ArticleInfo;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by Administrator on 2018/ic_pinde/4 0004.
 */

public class DoukeAdapter extends BaseQuickAdapter<ArticleInfo,BaseViewHolder> {

    public DoukeAdapter(int layoutResId, @Nullable List<ArticleInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ArticleInfo item) {

        helper.setText(R.id.tv_title,item.getTitle());
        helper.setText(R.id.tv_authorName,item.getAuthor());
        helper.setText(R.id.tv_authorDesc,item.getAuthDesc());

        SimpleDraweeView sv = helper.getView(R.id.iv_authorPhoto);
        sv.setImageURI(item.getAuthImg());
        SimpleDraweeView sv2 = helper.getView(R.id.iv_picture);
        //sv2.setImageURI(item.getArticlePicture());
        Glide.with(mContext).load(item.getArticlePicture()).crossFade().centerCrop().into((SimpleDraweeView) helper.getView(R.id.iv_picture));
    }
}

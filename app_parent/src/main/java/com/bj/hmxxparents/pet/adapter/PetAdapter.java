package com.bj.hmxxparents.pet.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.pet.model.PetInfo;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.List;
import static com.bj.hmxxparents.api.HttpUtilService.BASE_RESOURCE_URL;

public class PetAdapter extends BaseQuickAdapter<PetInfo.DataBean,BaseViewHolder> {

    private int selected = -1;

    public PetAdapter(int layoutResId, @Nullable List<PetInfo.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PetInfo.DataBean item) {

        helper.setText(R.id.tv_name,item.getName());
        FrameLayout layout = helper.getView(R.id.layout_pet);
        LinearLayout layoutRoot = helper.getView(R.id.layout_root);
        //ImageView ivPet = helper.getView(R.id.iv_pet);
        ImageView ivStatus = helper.getView(R.id.iv_status);
        TextView tv = helper.getView(R.id.tv_name);

        ImageView ivLock = helper.getView(R.id.iv_lock);
        ImageView ivGray = helper.getView(R.id.iv_gray);

        ImageView ivPet = helper.getView(R.id.iv_pet);

        Glide.with(mContext).load(BASE_RESOURCE_URL+item.getImg_s()).crossFade().fitCenter().into(ivPet);


        SimpleDraweeView svPet = helper.getView(R.id.sv_pet);
        //svPet.setImageURI(BASE_RESOURCE_URL+item.getImg_s());

        //Glide.with(mContext).load(BASE_RESOURCE_URL+item.getImg_s()).into(svPet);

        if(item.getJiesuo_status()==1){
            tv.setBackgroundResource(R.drawable.shape_tv_corner_orange);
            layout.setBackgroundResource(R.drawable.shape_stroke_orange);

            ivLock.setVisibility(View.INVISIBLE);
            ivGray.setVisibility(View.INVISIBLE);
        }else {
            tv.setBackgroundResource(R.drawable.shape_tv_corner_gray);
            layout.setBackgroundResource(R.drawable.shape_stroke_gray);

            ivLock.setVisibility(View.VISIBLE);
            ivGray.setVisibility(View.VISIBLE);
        }

        if(item.getStatus()==1){
            ivStatus.setVisibility(View.VISIBLE);
        }else {
            ivStatus.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        LinearLayout layout = holder.getView(R.id.layout_root);
        ImageView imageView = holder.getView(R.id.iv_status);

        if(selected == position){
            layout.setBackgroundResource(R.drawable.shape_layout_corner_orange);
            imageView.setVisibility(View.VISIBLE);
        }else {
            layout.setBackgroundResource(R.drawable.shape_layout_corner_no);
            imageView.setVisibility(View.INVISIBLE);
        }
    }

    public void setSelection(int position){
        this.selected = position;
        notifyDataSetChanged();
    }
}

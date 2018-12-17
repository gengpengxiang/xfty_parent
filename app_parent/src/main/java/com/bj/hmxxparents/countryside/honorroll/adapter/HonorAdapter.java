package com.bj.hmxxparents.countryside.honorroll.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.countryside.honorroll.model.Honor;
import com.bj.hmxxparents.widget.SpacesItemDecoration;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.ArrayList;
import java.util.List;
import static com.bj.hmxxparents.api.HttpUtilService.BASE_RESOURCE_URL;

public class HonorAdapter extends BaseQuickAdapter<Honor.DataBean, BaseViewHolder> {

    public HonorAdapter(int layoutResId, @Nullable List<Honor.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Honor.DataBean item) {
        SimpleDraweeView svBadge = helper.getView(R.id.sv_badge);
        svBadge.setImageURI(BASE_RESOURCE_URL+item.getFenlei_img());

        helper.setText(R.id.tv_classname,item.getClass_name());
        helper.setText(R.id.tv_date,item.getTime());

        helper.addOnClickListener(R.id.tv_share);

        if(item.getStudent_pm().size()!=0) {
            //银牌
            helper.setText(R.id.tv_score1, item.getStudent_pm().get(1).getHuizhangnum());

            SimpleDraweeView svMedal1 = helper.getView(R.id.sv_medal1);
            //Glide.with(mContext).load(BASE_RESOURCE_URL+item.getStudent_pm().get(1).getImg()).into(svMedal1);
            svMedal1.setImageURI(BASE_RESOURCE_URL + item.getStudent_pm().get(1).getImg());
            helper.setText(R.id.tv_studentname1, item.getStudent_pm().get(1).getStudent_name());
            //金牌
            helper.setText(R.id.tv_score2, item.getStudent_pm().get(0).getHuizhangnum());
            SimpleDraweeView svMedal2 = helper.getView(R.id.sv_medal2);
            svMedal2.setImageURI(BASE_RESOURCE_URL + item.getStudent_pm().get(0).getImg());
            //Glide.with(mContext).load(BASE_RESOURCE_URL+item.getStudent_pm().get(0).getImg()).into(svMedal2);
            helper.setText(R.id.tv_studentname2, item.getStudent_pm().get(0).getStudent_name());
            //铜牌
            helper.setText(R.id.tv_score3, item.getStudent_pm().get(2).getHuizhangnum());
            SimpleDraweeView svMedal3 = helper.getView(R.id.sv_medal3);
            //Glide.with(mContext).load(BASE_RESOURCE_URL+item.getStudent_pm().get(2).getImg()).into(svMedal3);
            svMedal3.setImageURI(BASE_RESOURCE_URL + item.getStudent_pm().get(2).getImg());
            helper.setText(R.id.tv_studentname3, item.getStudent_pm().get(2).getStudent_name());

            RecyclerView recyclerView = helper.getView(R.id.recyclerView);
//        recyclerView.setLayoutManager(new GridLayoutManager(mContext,5));
            //recyclerView.addItemDecoration(new SpacesItemDecoration(15));


            if (item.getStudent_pm().size() > 3) {
                recyclerView.setVisibility(View.VISIBLE);

                List<Honor.DataBean.StudentPmBean> beanList = item.getStudent_pm();

                List<Honor.DataBean.StudentPmBean> beanList2 = new ArrayList<>();

                for (int i = 3; i < beanList.size(); i++) {
                    beanList2.add(beanList.get(i));
                }

                recyclerView.setLayoutManager(new GridLayoutManager(mContext, beanList2.size()));

                HonorChildAdapter childdapter = new HonorChildAdapter(R.layout.recycler_item_honor_student, beanList2);
                recyclerView.setAdapter(childdapter);
            } else {
                recyclerView.setVisibility(View.GONE);
            }

        }

    }

//    public class HonorChildAdapter extends BaseQuickAdapter<Honor.DataBean.StudentPmBean, BaseViewHolder>{
//
//
//        public HonorChildAdapter(int layoutResId, @Nullable List<Honor.DataBean.StudentPmBean> data) {
//            super(layoutResId, data);
//        }
//
//        @Override
//        protected void convert(BaseViewHolder helper, Honor.DataBean.StudentPmBean item) {
//
//            helper.setText(R.id.tv_score,item.getHuizhangnum());
//            helper.setText(R.id.tv_studentname,item.getStudent_name());
//
//        }
//    }


}

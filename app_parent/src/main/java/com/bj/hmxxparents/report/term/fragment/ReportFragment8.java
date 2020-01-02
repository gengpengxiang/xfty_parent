package com.bj.hmxxparents.report.term.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bj.hmxxparents.BaseFragment;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.api.MLConfig;
import com.bj.hmxxparents.report.term.model.Report6;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.bj.hmxxparents.api.HttpUtilService.BASE_RESOURCE_URL;
import static com.bj.hmxxparents.api.HttpUtilService.BASE_URL;


/**
 * Created by zz379 on 2017/3/29.
 * 荣誉榜
 */

public class ReportFragment8 extends BaseFragment {


    @BindView(R.id.iv_badge_2)
    SimpleDraweeView ivBadge2;
    @BindView(R.id.tv_badge_num_2)
    TextView tvBadgeNum2;
    @BindView(R.id.tv_badge_name_2)
    TextView tvBadgeName2;
    @BindView(R.id.ll_badge_2)
    LinearLayout llBadge2;
    @BindView(R.id.iv_badge_1)
    SimpleDraweeView ivBadge1;
    @BindView(R.id.tv_badge_num_1)
    TextView tvBadgeNum1;
    @BindView(R.id.tv_badge_name_1)
    TextView tvBadgeName1;
    @BindView(R.id.ll_badge_1)
    LinearLayout llBadge1;
    @BindView(R.id.iv_badge_3)
    SimpleDraweeView ivBadge3;
    @BindView(R.id.tv_badge_num_3)
    TextView tvBadgeNum3;
    @BindView(R.id.tv_badge_name_3)
    TextView tvBadgeName3;
    @BindView(R.id.ll_badge_3)
    LinearLayout llBadge3;
    @BindView(R.id.iv_evaluation_2)
    SimpleDraweeView ivEvaluation2;
    @BindView(R.id.tv_evaluation_num_2)
    TextView tvEvaluationNum2;
    @BindView(R.id.tv_evaluation_name_2)
    TextView tvEvaluationName2;
    @BindView(R.id.ll_evaluation_2)
    LinearLayout llEvaluation2;
    @BindView(R.id.iv_evaluation_1)
    SimpleDraweeView ivEvaluation1;
    @BindView(R.id.tv_evaluation_num_1)
    TextView tvEvaluationNum1;
    @BindView(R.id.tv_evaluation_name_1)
    TextView tvEvaluationName1;
    @BindView(R.id.ll_evaluation_1)
    LinearLayout llEvaluation1;
    @BindView(R.id.iv_evaluation_3)
    SimpleDraweeView ivEvaluation3;
    @BindView(R.id.tv_evaluation_num_3)
    TextView tvEvaluationNum3;
    @BindView(R.id.tv_evaluation_name_3)
    TextView tvEvaluationName3;
    @BindView(R.id.ll_evaluation_3)
    LinearLayout llEvaluation3;
    private Unbinder unbinder;
    private String studentId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_honor, container, false);
        unbinder = ButterKnife.bind(this, view);
        studentId = getActivity().getIntent().getStringExtra("id");
        initView();

        getReportInfo6();
        return view;
    }


    private void initView() {

    }


    private void getReportInfo6() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                OkGo.<String>post(BASE_URL + "jzbaogao/getbg5")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("studentcode", studentId)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String str = response.body().toString();
                                Log.e("报告666", str);

                                e.onNext(str);
                                e.onComplete();
                            }
                        });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Report6 report6 = JSON.parseObject(s, new TypeReference<Report6>() {
                        });
                        if (report6.getRet().equals("1")) {
                            setDatas(report6.getData());
                        }

                    }
                });

    }

    private void setDatas(Report6.DataBean bean) {

        try {
            ivBadge1.setImageURI(BASE_RESOURCE_URL + bean.getHuizhang().get(0).getImg());
            ivBadge2.setImageURI(BASE_RESOURCE_URL + bean.getHuizhang().get(1).getImg());
            ivBadge3.setImageURI(BASE_RESOURCE_URL + bean.getHuizhang().get(2).getImg());

            tvBadgeNum1.setText(bean.getHuizhang().get(0).getBadge());
            tvBadgeNum2.setText(bean.getHuizhang().get(1).getBadge());
            tvBadgeNum3.setText(bean.getHuizhang().get(2).getBadge());

            tvBadgeName1.setText(bean.getHuizhang().get(0).getName());
            tvBadgeName2.setText(bean.getHuizhang().get(1).getName());
            tvBadgeName3.setText(bean.getHuizhang().get(2).getName());

            ivEvaluation1.setImageURI(BASE_RESOURCE_URL + bean.getDianzan().get(0).getImg());
            ivEvaluation2.setImageURI(BASE_RESOURCE_URL + bean.getDianzan().get(1).getImg());
            ivEvaluation3.setImageURI(BASE_RESOURCE_URL + bean.getDianzan().get(2).getImg());

            tvEvaluationNum1.setText(bean.getDianzan().get(0).getValue());
            tvEvaluationNum2.setText(bean.getDianzan().get(1).getValue());
            tvEvaluationNum3.setText(bean.getDianzan().get(2).getValue());

            tvEvaluationName1.setText(bean.getDianzan().get(0).getName());
            tvEvaluationName2.setText(bean.getDianzan().get(1).getName());
            tvEvaluationName3.setText(bean.getDianzan().get(2).getName());

        }catch (Exception e){

        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}

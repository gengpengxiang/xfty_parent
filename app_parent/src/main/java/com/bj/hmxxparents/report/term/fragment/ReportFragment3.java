package com.bj.hmxxparents.report.term.fragment;

import android.graphics.Bitmap;
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
import com.bj.hmxxparents.entity.MessageEvent;
import com.bj.hmxxparents.report.term.model.Report1;
import com.bj.hmxxparents.utils.LL;
import com.bj.hmxxparents.wxapi.WXUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.progressbar.BGAProgressBar;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.bj.hmxxparents.api.HttpUtilService.BASE_RESOURCE_URL;
import static com.bj.hmxxparents.api.HttpUtilService.BASE_URL;


/**
 * Created by zz379 on 2017/3/29.
 * 徽章和评价报告页
 */

public class ReportFragment3 extends BaseFragment {

    @BindView(R.id.sv_logo)
    SimpleDraweeView svLogo;
    @BindView(R.id.tv_badge_score)
    TextView tvBadgeScore;
    @BindView(R.id.bar_badge_mine)
    BGAProgressBar barBadgeMine;
    @BindView(R.id.bar_badge_average)
    BGAProgressBar barBadgeAverage;
    @BindView(R.id.bar_badge_max)
    BGAProgressBar barBadgeMax;
    @BindView(R.id.tv_evaluation_score)
    TextView tvEvaluationScore;
    @BindView(R.id.bar_evaluation_mine)
    BGAProgressBar barEvaluationMine;
    @BindView(R.id.bar_evaluation_mine_average)
    BGAProgressBar barEvaluationMineAverage;
    @BindView(R.id.bar_evaluation_max)
    BGAProgressBar barEvaluationMax;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    private Unbinder unbinder;

    private String studentId;
    private boolean isProgressAnimation = true; // 页面第一次初始化的时候必定需要加载动画,把字段置为True;
    private Disposable d;

    private int badgeMineNum,badgeAverageNum,badgeMaxNum,evaluationMineNum,evaluationAverageNum,evaluationMaxNum;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report1, container, false);
        unbinder = ButterKnife.bind(this, view);
        studentId = getActivity().getIntent().getStringExtra("id");


        initView();

        getReportInfo1();

        return view;
    }

    private void getReportInfo1() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                OkGo.<String>post(BASE_URL + "jzbaogao/index")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("studentcode", studentId)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String str = response.body().toString();
                                Log.e("报告111", str);

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

                        try {

                            Report1 report1 = JSON.parseObject(s, new TypeReference<Report1>() {
                            });
                            if (report1.getRet().equals("1")) {
                                svLogo.setImageURI(BASE_RESOURCE_URL + report1.getData().getStudent_hz_dz().getImg());
                                tvBadgeScore.setText(report1.getData().getStudent_hz_dz().getBpm());

                                String badgeMine = report1.getData().getStudent_hz_dz().getBadge();
                                String badgeAverage = report1.getData().getClass_avg().getBavg();
                                String badgeNumMax = report1.getData().getClass_avg().getBmax();
                                //我的徽章
                                barBadgeMine.setMax(Integer.valueOf(badgeNumMax) * 10000);
                                barBadgeMine.setProgress(Integer.valueOf(badgeMine) * 10000);
                                barBadgeMine.setmText(badgeMine);

                                badgeMineNum = Integer.valueOf(badgeMine) * 10000;
                                //徽章平均数量int类型
                                badgeAverageNum = (int) (Double.valueOf(badgeAverage) * 10000);
                                badgeMaxNum = Integer.valueOf(badgeNumMax) * 10000;

                                int progressAverage = (int) (Double.valueOf(badgeAverage) * 10000);
                                //处理小数点
                                java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
                                double d = Double.valueOf(badgeAverage);
                                //班级平均
                                barBadgeAverage.setMax(Integer.valueOf(badgeNumMax) * 10000);
                                barBadgeAverage.setProgress(progressAverage * 10000);
                                barBadgeAverage.setmText(df.format(d));
                                //班级最高
                                barBadgeMax.setMax(Integer.valueOf(badgeNumMax) * 10000);
                                barBadgeMax.setProgress(Integer.valueOf(badgeNumMax) * 10000);
                                barBadgeMax.setmText(badgeNumMax);


                                //评价班级排名
                                tvEvaluationScore.setText(report1.getData().getStudent_hz_dz().getDpm());
                                String evaluationMine = report1.getData().getStudent_hz_dz().getValue();
                                String evaluationAverage = report1.getData().getClass_avg().getDavg();
                                String evaluationMax = report1.getData().getClass_avg().getDmax();

                                evaluationMineNum = Integer.valueOf(evaluationMine) * 10000;
                                //评价平均数量int类型
                                evaluationAverageNum = (int) (Double.valueOf(evaluationAverage) * 10000);
                                evaluationMaxNum = Integer.valueOf(evaluationMax) * 10000;

                                barEvaluationMine.setMax(Integer.valueOf(evaluationMax) * 10000);
//                            barEvaluationMine.setProgress(Integer.valueOf(evaluationMine));
                                barEvaluationMine.setProgress(Integer.valueOf(evaluationMine) * 10000);
                                barEvaluationMine.setmText(evaluationMine);

                                int progressAverage2 = (int) (Double.valueOf(evaluationAverage) * 10000);

                                double d2 = Double.valueOf(evaluationAverage);
                                barEvaluationMineAverage.setMax(Integer.valueOf(evaluationMax) * 10000);
                                barEvaluationMineAverage.setProgress(progressAverage2);
                                barEvaluationMineAverage.setmText(df.format(d2));

                                barEvaluationMax.setMax(Integer.valueOf(evaluationMax) * 10000);
                                barEvaluationMax.setProgress(Integer.valueOf(evaluationMax) * 10000);
                                barEvaluationMax.setmText(evaluationMax);
                            }

                        }catch (Exception e){

                        }
                    }
                });
    }

    private void initView() {

    }

    @Override
    protected void onVisible() {
        super.onVisible();
//        if (!isFirstInit && !isProgressAnimation) {
//            startAnimation();
//        }

        startAnimation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if (d != null && !d.isDisposed()) {
            d.dispose();
            d = null;
        }
    }


    private void startAnimation() {
        Observable.interval(10, TimeUnit.MILLISECONDS)
                .map(aLong -> aLong.intValue())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        d = disposable;
                        isProgressAnimation = true;
                    }

                    @Override
                    public void onNext(@NonNull Integer aInt) {
                        if (aInt > 99) {
                            d.dispose();
                            isProgressAnimation = false;
                        }
                        barBadgeMine.setProgress(badgeMineNum/100*aInt);
                        barBadgeAverage.setProgress(badgeAverageNum / 100 * aInt);
                        barBadgeMax.setProgress(badgeMaxNum / 100 * aInt);

                        barEvaluationMine.setProgress(evaluationMineNum/100*aInt);
//                        barEvaluationMineAverage.setProgress(evaluationAverageNum*100*aInt);
                        barEvaluationMineAverage.setProgress(evaluationAverageNum/100*aInt);
                        barEvaluationMax.setProgress(evaluationMaxNum/100*aInt);
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        LL.i("onError_");
                        isProgressAnimation = false;
                    }

                    @Override
                    public void onComplete() {
                        LL.i("onComplete_");
                        isProgressAnimation = false;
                    }
                });
    }

}

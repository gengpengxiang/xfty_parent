package com.bj.hmxxparents.report.fragment;

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
import com.bj.hmxxparents.report.model.Report1;
import com.bj.hmxxparents.wxapi.WXUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.progressbar.BGAProgressBar;
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
 * 全校活力榜页面
 */

public class ReportFragment1 extends BaseFragment {

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report1, container, false);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
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
                        Report1 report1 = JSON.parseObject(s, new TypeReference<Report1>() {
                        });
                        if (report1.getRet().equals("1")) {
                            svLogo.setImageURI(BASE_RESOURCE_URL + report1.getData().getStudent_hz_dz().getImg());
                            tvBadgeScore.setText(report1.getData().getStudent_hz_dz().getBpm());

                            String badgeMine = report1.getData().getStudent_hz_dz().getBadge();
                            String badgeAverge = report1.getData().getClass_avg().getBavg();
                            String badgeNumMax = report1.getData().getClass_avg().getBmax();


                            barBadgeMine.setMax(Integer.valueOf(badgeNumMax));
                            barBadgeMine.setProgress(Integer.valueOf(badgeMine));

                            barBadgeMine.setmText(badgeMine);

                            int progressAverage = Double.valueOf(badgeAverge).intValue();

                            //处理小数点
                            java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
                            double d = Double.valueOf(badgeAverge);

                            barBadgeAverage.setMax(Integer.valueOf(badgeNumMax));
//                            barBadgeAverage.setProgress(progressAverage);
                            barBadgeAverage.setProgress(progressAverage);
                            barBadgeAverage.setmText(df.format(d));
                            //barBadgeAverage.setmText(BigDecimal.valueOf(Double.valueOf(badgeAverge)).stripTrailingZeros().toPlainString());

                            barBadgeMax.setMax(Integer.valueOf(badgeNumMax));
                            barBadgeMax.setProgress(Integer.valueOf(badgeNumMax));
                            barBadgeMax.setmText(badgeNumMax);

                            //评价班级排名
                            tvEvaluationScore.setText(report1.getData().getStudent_hz_dz().getDpm());
                            String evaluationMine = report1.getData().getStudent_hz_dz().getValue();
                            String evaluationAverage = report1.getData().getClass_avg().getDavg();
                            String evaluationMax = report1.getData().getClass_avg().getDmax();

                            barEvaluationMine.setMax(Integer.valueOf(evaluationMax));
                            barEvaluationMine.setProgress(Integer.valueOf(evaluationMine));
                            barEvaluationMine.setmText(evaluationMine);

                            int progressAverage2 = Double.valueOf(evaluationAverage).intValue();

                            double d2 = Double.valueOf(evaluationAverage);
                            barEvaluationMineAverage.setMax(Integer.valueOf(evaluationMax));
                            barEvaluationMineAverage.setProgress(progressAverage2);
                            barEvaluationMineAverage.setmText(df.format(d2));

                            barEvaluationMax.setMax(Integer.valueOf(evaluationMax));
                            barEvaluationMax.setProgress(Integer.valueOf(evaluationMax));
                            barEvaluationMax.setmText(evaluationMax);
                        }
                    }
                });
    }

    private void initView() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void update(MessageEvent event) {
        if (event.getMessage().equals("reportShare")) {
//            if (event.getPage() == 0) {
            String type = event.getParam1();
            WXUtil.shareImgToSession(getActivity(), getBitmap(), type);
//            }
        }
    }

    private Bitmap getBitmap() {
        llContainer.setDrawingCacheEnabled(true);
        llContainer.buildDrawingCache();  //启用DrawingCache并创建位图
        Bitmap bitmap = Bitmap.createBitmap(llContainer.getDrawingCache()); //创建一个DrawingCache的拷贝，因为DrawingCache得到的位图在禁用后会被回收
        llContainer.setDrawingCacheEnabled(false);  //禁用DrawingCahce否则会影响性能
        return bitmap;
    }

}

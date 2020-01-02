package com.bj.hmxxparents.report.term.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bj.hmxxparents.BaseFragment;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.api.MLConfig;
import com.bj.hmxxparents.api.MLProperties;
import com.bj.hmxxparents.entity.MessageEvent;
import com.bj.hmxxparents.report.term.model.Fengchao;
import com.bj.hmxxparents.utils.PreferencesUtils;
import com.bj.hmxxparents.wxapi.WXUtil;
import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.bj.hmxxparents.api.HttpUtilService.BASE_URL;


/**
 * Created by zz379 on 2017/3/29.
 * 学期报告首页
 */

public class ReportFragment2 extends BaseFragment {


    @BindView(R.id.tv_title3)
    TextView tvTitle;
    @BindViews({R.id.iv0, R.id.iv1, R.id.iv2, R.id.iv3, R.id.iv4, R.id.iv5, R.id.iv6, R.id.iv7, R.id.iv8, R.id.iv9, R.id.iv10,
            R.id.iv11, R.id.iv12, R.id.iv13, R.id.iv14, R.id.iv15, R.id.iv16, R.id.iv17, R.id.iv18, R.id.iv19, R.id.iv20,
            R.id.iv21, R.id.iv22, R.id.iv23, R.id.iv24, R.id.iv25, R.id.iv26, R.id.iv27, R.id.iv28, R.id.iv29, R.id.iv30,
            R.id.iv31, R.id.iv32, R.id.iv33, R.id.iv34, R.id.iv35, R.id.iv36})
    ImageView[] ivs;
    @BindViews({R.id.i2v0, R.id.i2v1, R.id.i2v2, R.id.i2v3, R.id.i2v4, R.id.i2v5, R.id.i2v6, R.id.i2v7, R.id.i2v8, R.id.i2v9, R.id.i2v10,
            R.id.i2v11, R.id.i2v12, R.id.i2v13, R.id.i2v14, R.id.i2v15, R.id.i2v16, R.id.i2v17, R.id.i2v18, R.id.i2v19, R.id.i2v20,
            R.id.i2v21, R.id.i2v22, R.id.i2v23, R.id.i2v24, R.id.i2v25, R.id.i2v26, R.id.i2v27, R.id.i2v28, R.id.i2v29, R.id.i2v30,
            R.id.i2v31, R.id.i2v32, R.id.i2v33, R.id.i2v34, R.id.i2v35, R.id.i2v36, R.id.i2v37, R.id.i2v38, R.id.i2v39, R.id.i2v40,
            R.id.i2v41, R.id.i2v42, R.id.i2v43, R.id.i2v44, R.id.i2v45, R.id.i2v46, R.id.i2v47, R.id.i2v48, R.id.i2v49, R.id.i2v50,
            R.id.i2v51, R.id.i2v52, R.id.i2v53, R.id.i2v54, R.id.i2v55, R.id.i2v56, R.id.i2v57, R.id.i2v58, R.id.i2v59, R.id.i2v60,
            R.id.i2v61, R.id.i2v62, R.id.i2v63, R.id.i2v64, R.id.i2v65, R.id.i2v66, R.id.i2v67, R.id.i2v68, R.id.i2v69, R.id.i2v70,
            R.id.i2v71, R.id.i2v72, R.id.i2v73, R.id.i2v74, R.id.i2v75, R.id.i2v76, R.id.i2v77, R.id.i2v78, R.id.i2v79, R.id.i2v80,
            R.id.i2v81, R.id.i2v82, R.id.i2v83, R.id.i2v84, R.id.i2v85, R.id.i2v86, R.id.i2v87, R.id.i2v88, R.id.i2v89, R.id.i2v90,})
    ImageView[] ivs2;
    @BindView(R.id.layout_fengchao1)
    RelativeLayout layoutFengchao1;
    @BindView(R.id.layout_fengchao2)
    RelativeLayout layoutFengchao2;
    @BindView(R.id.layout_container)
    LinearLayout layoutContainer;
    @BindView(R.id.layout_title3)
    RelativeLayout layoutTitle3;
    @BindView(R.id.tv_title0)
    TextView tvTitle0;
    @BindView(R.id.tv_title1)
    TextView tvTitle1;


    private Unbinder unbinder;


    private String badgeNum = "";
    private String studentId;
    private String studentName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_fengchao, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        studentId = getActivity().getIntent().getStringExtra("id");
        studentName = PreferencesUtils.getString(getActivity(), MLProperties.BUNDLE_KEY_KID_NAME);
        initView();
        getReportInfo2();
        return view;
    }

    private void initView() {

        for (int i = 0; i < ivs2.length; i++) {
            if (i == 1 || i == 7 || i == 8 || i == 19 || i == 20 || i == 21 || i == 37 || i == 38 || i == 39 || i == 40 || i == 61 || i == 62 || i == 63 || i == 64 || i == 65) {
                Glide.with(getActivity()).load(R.mipmap.ic_badge_shijian).crossFade().into(ivs2[i]);
            } else if (i == 2 || i == 9 || i == 10 || i == 22 || i == 23 || i == 24 || i == 41 || i == 42 || i == 43 || i == 44 || i == 66 || i == 67 || i == 68 || i == 69 || i == 70) {
                Glide.with(getActivity()).load(R.mipmap.ic_badge_yishu).crossFade().into(ivs2[i]);
            } else if (i == 3 || i == 11 || i == 12 || i == 25 || i == 26 || i == 27 || i == 45 || i == 46 || i == 47 || i == 48 || i == 71 || i == 72 || i == 73 || i == 74 || i == 75) {
                Glide.with(getActivity()).load(R.mipmap.ic_badge_keji).crossFade().into(ivs2[i]);
            } else if (i == 4 || i == 13 || i == 14 || i == 28 || i == 29 || i == 30 || i == 49 || i == 50 || i == 51 || i == 52 || i == 76 || i == 77 || i == 78 || i == 79 || i == 80) {
                Glide.with(getActivity()).load(R.mipmap.ic_badge_sipin).crossFade().into(ivs2[i]);
            } else if (i == 5 || i == 15 || i == 16 || i == 31 || i == 32 || i == 33 || i == 53 || i == 54 || i == 55 || i == 56 || i == 37 || i == 81 || i == 82 || i == 83 || i == 84 || i == 85) {
                Glide.with(getActivity()).load(R.mipmap.ic_badge_tiyu).crossFade().into(ivs2[i]);
            } else if (i == 6 || i == 17 || i == 18 || i == 34 || i == 35 || i == 36 || i == 57 || i == 58 || i == 59 || i == 60 || i == 86 || i == 87 || i == 88 || i == 89 || i == 90) {
                Glide.with(getActivity()).load(R.mipmap.ic_badge_renwen).crossFade().into(ivs2[i]);
            }
        }

        for (int i = 0; i < ivs.length; i++) {
            if (i == 1 || i == 7 || i == 8 || i == 19 || i == 20 || i == 21) {
                Glide.with(getActivity()).load(R.mipmap.ic_badge_shijian).crossFade().into(ivs[i]);
            } else if (i == 2 || i == 9 || i == 10 || i == 22 || i == 23 || i == 24) {
                Glide.with(getActivity()).load(R.mipmap.ic_badge_yishu).crossFade().into(ivs[i]);
            } else if (i == 3 || i == 11 || i == 12 || i == 25 || i == 26 || i == 27) {
                Glide.with(getActivity()).load(R.mipmap.ic_badge_keji).crossFade().into(ivs[i]);
            } else if (i == 4 || i == 13 || i == 14 || i == 28 || i == 29 || i == 30) {
                Glide.with(getActivity()).load(R.mipmap.ic_badge_sipin).crossFade().into(ivs[i]);
            } else if (i == 5 || i == 15 || i == 16 || i == 31 || i == 32 || i == 33) {
                Glide.with(getActivity()).load(R.mipmap.ic_badge_tiyu).crossFade().into(ivs[i]);
            } else if (i == 6 || i == 17 || i == 18 || i == 34 || i == 35 || i == 36) {
                Glide.with(getActivity()).load(R.mipmap.ic_badge_renwen).crossFade().into(ivs[i]);
            }
        }

    }

    private void getReportInfo2() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                OkGo.<String>post(BASE_URL + "jzbaogao/getbg_hz")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("studentcode", studentId)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String str = response.body().toString();
                                Log.e("报告蜂巢", str);

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
                            Fengchao fengchao = JSON.parseObject(s, new TypeReference<Fengchao>() {
                            });
                            if (fengchao.getRet().equals("1")) {
                                badgeNum = fengchao.getData().getHzsum();

                                if (badgeNum.equals("0")) {
                                    layoutTitle3.setVisibility(View.GONE);
                                    tvTitle0.setVisibility(View.VISIBLE);
                                } else {
                                    layoutTitle3.setVisibility(View.VISIBLE);
                                    tvTitle0.setVisibility(View.GONE);
                                }

                                tvTitle1.setText(studentName+","+"恭喜你！");

                                SpannableStringBuilder builder = new SpannableStringBuilder("获得了" + badgeNum + "枚徽章");
                                builder.setSpan(new AbsoluteSizeSpan(60), 3, 3 + badgeNum.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                tvTitle.setText(builder);

                                SpannableStringBuilder builder2 = new SpannableStringBuilder(studentName+"获得了" + badgeNum + "枚徽章");
                                builder2.setSpan(new AbsoluteSizeSpan(60), studentName.length()+3, studentName.length()+3 + badgeNum.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


                                tvTitle0.setText(builder2);

                                List<Integer> jihao = fengchao.getData().getJihao();
                                String yemian = fengchao.getData().getYemian();
                                if (yemian.equals("1")) {
                                    layoutFengchao1.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < jihao.size(); i++) {
                                        ivs[jihao.get(i)].setAlpha(1.0f);
                                    }
                                } else {
                                    layoutFengchao2.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < jihao.size(); i++) {
                                        ivs2[jihao.get(i)].setAlpha(1.0f);
                                    }
                                }
                            }
                        } catch (Exception e) {

                        }
                    }
                });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void update(MessageEvent event) {
        if (event.getMessage().equals("reportTermShare")) {
//            if (event.getPage() == 0) {
            String type = event.getParam1();
            WXUtil.shareImgToSession(getActivity(), getBitmap(), type);
//            }
        }
    }

    private Bitmap getBitmap() {
        layoutContainer.setDrawingCacheEnabled(true);
        layoutContainer.buildDrawingCache();  //启用DrawingCache并创建位图
        Bitmap bitmap = Bitmap.createBitmap(layoutContainer.getDrawingCache()); //创建一个DrawingCache的拷贝，因为DrawingCache得到的位图在禁用后会被回收
        layoutContainer.setDrawingCacheEnabled(false);  //禁用DrawingCahce否则会影响性能
        return bitmap;
    }

}

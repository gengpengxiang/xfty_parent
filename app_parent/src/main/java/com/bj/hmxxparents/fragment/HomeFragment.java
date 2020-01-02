package com.bj.hmxxparents.fragment;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.bj.hmxxparents.BaseFragment;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.activity.BadgeActivity;
import com.bj.hmxxparents.activity.BadgeDetailActivity;
import com.bj.hmxxparents.activity.ChatActivity;
import com.bj.hmxxparents.activity.GaijinDetailActivity;
import com.bj.hmxxparents.activity.LevelDetailActivity;
import com.bj.hmxxparents.activity.LoadingGameActivity;
import com.bj.hmxxparents.activity.QRCodeScanActivity;
import com.bj.hmxxparents.activity.RankListActivity;
import com.bj.hmxxparents.activity.RelationKidActivity;
import com.bj.hmxxparents.adapter.LatestNewsHomeAdapter;
import com.bj.hmxxparents.api.Constant;
import com.bj.hmxxparents.api.LmsDataService;
import com.bj.hmxxparents.api.MLConfig;
import com.bj.hmxxparents.api.MLProperties;
import com.bj.hmxxparents.comment.CommentActivity;
import com.bj.hmxxparents.entity.AppVersionInfo;
import com.bj.hmxxparents.entity.ClassNewsInfo;
import com.bj.hmxxparents.entity.KidClassInfo;
import com.bj.hmxxparents.entity.KidDataInfo;
import com.bj.hmxxparents.entity.MsgEvent;
import com.bj.hmxxparents.entity.ReportInfo;
import com.bj.hmxxparents.huodong.HuodongActivity;
import com.bj.hmxxparents.manager.UMPushManager;
import com.bj.hmxxparents.pet.model.StudentInfo;
import com.bj.hmxxparents.pet.view.PetALLActivity;
import com.bj.hmxxparents.read.ReadActivity;
import com.bj.hmxxparents.read.ReadActivity2;
import com.bj.hmxxparents.report.study.ReportStudyActivity;
import com.bj.hmxxparents.report.term.ReportTermActivity;
import com.bj.hmxxparents.service.DownloadAppService;
import com.bj.hmxxparents.utils.AppUtils;
import com.bj.hmxxparents.utils.LL;
import com.bj.hmxxparents.utils.NetUtils;
import com.bj.hmxxparents.utils.PreferencesUtils;
import com.bj.hmxxparents.utils.StringUtils;
import com.bj.hmxxparents.utils.T;
import com.bj.hmxxparents.widget.AutoScaleTextView;
import com.douhao.game.entity.ChallengeInfo;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.analytics.MobclickAgent;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.CancelConfirmAlertDialog;
import cn.pedant.SweetAlert.CancelConfirmAlertDialog2;
import cn.pedant.SweetAlert.UpdateAPPAlertDialog;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;
import static com.bj.hmxxparents.api.HttpUtilService.BASE_API_URL;
import static com.bj.hmxxparents.api.HttpUtilService.BASE_RESOURCE_URL;

/**
 * Created by zz379 on 2017/4/ic_pinde.
 * 首页，学生主页
 */

public class HomeFragment extends BaseFragment {

    private static final int QRCODE_REQUEST_CODE = 1;

    @BindView(R.id.mXRefreshView)
    XRefreshView mXRefreshView;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.header_tv_title)
    TextView tvTitle;
    @BindView(R.id.header_img_qrcode)
    ImageView imgScan;
    SimpleDraweeView ivSchoolImg;
    TextView tvSchoolRankTitle;
    TextView tvSchoolRankContent;
    RelativeLayout rlSchoolRank;

    private LatestNewsHomeAdapter mAdapter;
    private int currentPage = 1;
    public static long lastRefreshTime;
    private List<ClassNewsInfo> mDataList = new ArrayList<>();
    private View headerView;

    private SimpleDraweeView imgStudentPhoto;
    private TextView tvStudentDesc, tvJifen, tvHuizhang, tvZhuanxiang, tvDengji;
    private TextView tvAddScore, tvAddBadge, tvAddZhuanxiang;
    private TextView tvChallengeContent;
    private ChallengeInfo mChallengeInfo;

    private String kidId, kidName, kidImg;

    private String kidScore, kidBadge, kidBadgePro, kidGrade, kidPingYu;
    private String userPhoneNumber;
    private String schoolImg, schoolID;
    private String firstContent;
    private String userClassName;
    private String userRelation;
    private int isUserVirtual;
    private TextView tvStudentReport;
    private ReportInfo currReportInfo;
    private PopupWindow popAddBadge;
    private ImageView ivPopAddBadge;
    private TextView tvPopBadgeNumber;
    private View popview;
    private boolean state = true;
    private ScaleAnimation scaleAnimation;
    private RelativeLayout layoutPet;
    private LinearLayout layoutHeader;
    private List<String> strList = new ArrayList<>();

    private Timer timer = new Timer();
    private TextView tvContent;
    private ScaleAnimation sca0, sca1;
    private SimpleDraweeView svPet;
    private String petUrl;
    private ImageView ivClothes;
    private RelativeLayout layoutInfo;
    private TextView tvScore;
    private TextView tvBanji, tvGaijin, tvDianzan;
    //报告相关
    private ImageView reportBeahviorBig, reportStudyBig, reportRead,reportBeahviorSmall, reportStudySmall;
    private LinearLayout layoutReportSmall;

    private AutoScaleTextView autofitTextView;
    private RelativeLayout layoutHuizhang;

    private String yuedudaka_map = "1";
    private int huodong_status = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_student_detail, container, false);
        ButterKnife.bind(this, view);
        // 初始化页面
        EventBus.getDefault().register(this);
        initToolBar();

        initView();

        if (!StringUtils.isEmpty(PreferencesUtils.getString(getActivity(), "URL_PET", ""))) {
            String urlPet = PreferencesUtils.getString(getActivity(), "URL_PET", "");
            PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                    .setUri(urlPet)
                    .setOldController(svPet.getController())
                    .setAutoPlayAnimations(true) //自动播放gif动画
                    .build();
            svPet.setController(controller);
        }

//        getPetPermission(0);
        return view;
    }

    private void getPetPermission(int flag) {
        kidId = PreferencesUtils.getString(getActivity(), MLProperties.BUNDLE_KEY_KID_ID);
        Log.e("xueshengid", kidId);
        Observable.create(new ObservableOnSubscribe<StudentInfo>() {
            @Override
            public void subscribe(ObservableEmitter<StudentInfo> e) throws Exception {
                OkGo.<String>post(BASE_API_URL + "jz/getdata")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("studentid", kidId)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String str = response.body().toString();
                                Log.e("getpermission获取学生数据", str);
                                StudentInfo studentInfo = JSON.parseObject(str, new TypeReference<StudentInfo>() {
                                });
                                e.onNext(studentInfo);
                                e.onComplete();
                            }
                        });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<StudentInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(StudentInfo studentInfo) {

                        if (studentInfo.getData() != null) {
                            StudentInfo.DataBean bean = studentInfo.getData();


                            yuedudaka_map = bean.getYuedudaka_map();

                            PreferencesUtils.putString(getActivity(), "yuedudaka_map", bean.getYuedudaka_map());

                            PreferencesUtils.putString(getActivity(), "xuexibaogao_yingyustatus", bean.getXuexibaogao_yingyustatus());

                            autofitTextView.setText(bean.getScore() + "");
                            layoutHuizhang.setVisibility(View.VISIBLE);

//                            tvScore.setVisibility(View.VISIBLE);
//                            tvScore.setText(bean.getScore() + "");
                            tvHuizhang.setText(bean.getHuizhang());
                            tvBanji.setText(bean.getClass_score());

                            huodong_status = bean.getHuodong_status();


//                            int a = Integer.parseInt(bean.getGaijin());
//                            int b = Integer.parseInt(bean.getDianzan());
//                            int c = a+b;
//
//                            tvGaijin.setText(c+"");
//                            tvDianzan.setText(bean.getDianzan());

                            try {
                                int a = Integer.parseInt(bean.getGaijin());
                                int b = Integer.parseInt(bean.getDianzan());
                                int c = a+b;
                                tvGaijin.setText(c+"");
                                tvDianzan.setText(bean.getHuodong_num());

                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }


                            if (bean.getBaogao_status().equals("1")&&bean.getXuexibaogao_status().equals("1")) {
                                layoutReportSmall.setVisibility(View.VISIBLE);
                            }else {
                                layoutReportSmall.setVisibility(View.GONE);
                            }

                            if (bean.getBaogao_status().equals("1")&&!bean.getXuexibaogao_status().equals("1")) {
                                reportBeahviorBig.setVisibility(View.VISIBLE);
                            }

                            if (bean.getXuexibaogao_status().equals("1")&&!bean.getBaogao_status().equals("1")) {
                                reportStudyBig.setVisibility(View.VISIBLE);
                            }

                            if(bean.getYuedudaka_status().equals("1")){
                                reportRead.setVisibility(View.VISIBLE);
                            }else {
                                reportRead.setVisibility(View.GONE);
                            }


                            int status = studentInfo.getData().getChongwu_status();
                            if (status == 1) {
                                layoutPet.setVisibility(View.VISIBLE);
                                layoutInfo.setVisibility(View.GONE);

                                strList.clear();
                                Random random = new Random();
                                if (studentInfo.getData().getChongwu().getChongwu_guli() != null) {
                                    for (int i = 0; i < studentInfo.getData().getChongwu().getChongwu_guli().size(); i++) {
                                        strList.add(studentInfo.getData().getChongwu().getChongwu_guli().get(i).getContent());
                                    }
                                    int pos = random.nextInt(strList.size());
                                    tvContent.setText(strList.get(pos).replace("\\n", "\n"));
                                } else {
                                    tvContent.setVisibility(View.INVISIBLE);
                                    stopTimer();
                                }


                                petUrl = BASE_RESOURCE_URL + studentInfo.getData().getChongwu().getChongwu_info().getImg();

                                PreferencesUtils.putString(getActivity(), "URL_PET", petUrl);
                                showGif(svPet, petUrl);
                                svPet.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (state) {
                                            tvContent.clearAnimation();
                                            tvContent.startAnimation(sca1);
                                            state = false;
                                            stopTimer();
                                        } else {
                                            stopTimer();
                                            startTimer();
                                            tvContent.clearAnimation();
                                            tvContent.startAnimation(sca0);
                                            int pos0 = random.nextInt(strList.size());
                                            tvContent.setText(strList.get(pos0).replace("\\n", "\n"));
                                            state = true;
                                        }
                                    }
                                });
                            } else {
                                layoutPet.setVisibility(View.GONE);
                                layoutInfo.setVisibility(View.VISIBLE);
                            }
                            if (flag == 1) {
                                initHeaderView();
                            }

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            tvContent.clearAnimation();
            if (state) {
                tvContent.startAnimation(sca1);
                state = false;
                stopTimer();
            }
        }
    };

    private void showGif(SimpleDraweeView sv, String url) {

        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setUri(url)
                .setOldController(sv.getController())
                .setAutoPlayAnimations(true) //自动播放gif动画
                .build();
        sv.setController(controller);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    private void initToolBar() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.app_name);
        imgScan.setVisibility(View.VISIBLE);
    }

    private void initView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new LatestNewsHomeAdapter(mDataList, getString(R.string.text_desc_empty_news_home));
        mAdapter.setOnMyItemClickListener(new LatestNewsHomeAdapter.OnMyItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                // 弹出与老师一对一沟通的提示框
                switch (view.getId()) {
                    case R.id.tv_thanks_teacher:
                        if (mDataList.get(position - 1).getNewsType().equals("z1")) {
                            firstContent = String.format("谢谢老师鼓励的点赞“%s”", mDataList.get(position - 1).getNewsTitle());
                        } else {
                            firstContent = String.format("谢谢老师奖励的徽章“%s”", mDataList.get(position - 1).getNewsTitle());
                        }
                        myThanksTeacherTask(position - 1);
                        break;
                    case R.id.layout_item:
                        if (!mDataList.get(position - 1).getNewsType().equals("z1")) {
                            Intent intent = new Intent(getActivity(), BadgeActivity.class);
                            intent.putExtra("name", mDataList.get(position - 1).getNewsTitle());
                            intent.putExtra("shuoming", mDataList.get(position - 1).getNewsDesc());
                            intent.putExtra("xueke", mDataList.get(position - 1).getXueke());
                            intent.putExtra("huodeNum", mDataList.get(position - 1).getHuode_num());
                            intent.putExtra("xueke_img", mDataList.get(position - 1).getXueke_img());
                            startActivity(intent);
                        }
                        break;
                }
//                if (mDataList.get(position - 1).getNewsType().equals("z1")) {
//                    firstContent = String.format("谢谢老师鼓励的点赞“%s”", mDataList.get(position - 1).getNewsTitle());
//                } else {
//                    firstContent = String.format("谢谢老师奖励的徽章“%s”", mDataList.get(position - 1).getNewsTitle());
//                }
//                myThanksTeacherTask(position - 1);
            }
        });
        headerView = mAdapter.setHeaderView(R.layout.recycler_header_student_detail, mRecyclerView);
        initHeaderView();

        mRecyclerView.setAdapter(mAdapter);
        // set xRefreshView
        mXRefreshView.setPullRefreshEnable(true);
        mXRefreshView.setPullLoadEnable(true);
        mXRefreshView.restoreLastRefreshTime(lastRefreshTime);
        mXRefreshView.setAutoRefresh(false);
        mXRefreshView.setAutoLoadMore(true);

        mXRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPull) {
                currentPage = 1;
                // 刷新时需要调用两个Task
                MyGetKidDataTask myGetKidDataTask = new MyGetKidDataTask();
                myGetKidDataTask.execute(kidId);
                MyGetStudentAllNewsTask myGetStudentAllNewsTask = new MyGetStudentAllNewsTask();
                myGetStudentAllNewsTask.execute(kidId, String.valueOf(currentPage));
                if (!StringUtils.isEmpty(schoolImg)) {
                    getStudentInfoFromAPI();
                }
                // 刷新挑战赛的排名信息
                getStudentChallengeInfo();
                // 是否显示报告入口
                //getReportInfo();

                getPetPermission(0);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                LL.i("加载更多数据");
                currentPage++;
                MyGetStudentAllNewsTask myGetStudentAllNewsTask = new MyGetStudentAllNewsTask();
                myGetStudentAllNewsTask.execute(kidId, String.valueOf(currentPage));
            }
        });
        // 初始化 徽章增加动画
        initPopAddBadgeView();
    }

    private void initHeaderView() {

        autofitTextView = (AutoScaleTextView) headerView.findViewById(R.id.autoFitTextView);
        layoutHuizhang = (RelativeLayout) headerView.findViewById(R.id.layout_huizhang);

        tvScore = (TextView) headerView.findViewById(R.id.tv_score);
        tvBanji = (TextView) headerView.findViewById(R.id.tv_banji);
        tvGaijin = (TextView) headerView.findViewById(R.id.tv_gaijin);
        tvDianzan = (TextView) headerView.findViewById(R.id.tv_dianzan);

        imgStudentPhoto = (SimpleDraweeView) headerView.findViewById(R.id.img_kidPhoto);
        tvStudentDesc = (TextView) headerView.findViewById(R.id.tv_kid_pingyu);
//        tvJifen = (TextView) headerView.findViewById(R.id.tv_jifen);
        tvHuizhang = (TextView) headerView.findViewById(R.id.tv_huizhang);
        tvZhuanxiang = (TextView) headerView.findViewById(R.id.tv_zhuanxiang);
        tvDengji = (TextView) headerView.findViewById(R.id.tv_dengji);
        tvSchoolRankTitle = (TextView) headerView.findViewById(R.id.tv_school_rank_title);
        tvSchoolRankContent = (TextView) headerView.findViewById(R.id.tv_school_rank_content);
        ivSchoolImg = (SimpleDraweeView) headerView.findViewById(R.id.img_classBg);
        tvAddScore = (TextView) headerView.findViewById(R.id.tv_addJifen);
        tvAddBadge = (TextView) headerView.findViewById(R.id.tv_addHuizhang);
        tvAddZhuanxiang = (TextView) headerView.findViewById(R.id.tv_addZhuanxiang);
        tvChallengeContent = (TextView) headerView.findViewById(R.id.tv_game_content);
        //tvStudentReport = (TextView) headerView.findViewById(R.id.tv_student_report);
        //add
        layoutHeader = (LinearLayout) headerView.findViewById(R.id.ll_kid_pingyu);

        layoutPet = (RelativeLayout) headerView.findViewById(R.id.layout_pet);
        layoutInfo = (RelativeLayout) headerView.findViewById(R.id.layout_info);
        tvContent = (TextView) headerView.findViewById(R.id.tv_content);
        svPet = (SimpleDraweeView) headerView.findViewById(R.id.sv_pet);
        ivClothes = (ImageView) headerView.findViewById(R.id.iv_clothes);

        //报告相关
        reportBeahviorBig = (ImageView) headerView.findViewById(R.id.iv_report_beahvior_big);
        reportStudyBig = (ImageView) headerView.findViewById(R.id.iv_report_study_big);
        reportRead = (ImageView)headerView.findViewById(R.id.iv_read);
        layoutReportSmall = (LinearLayout)headerView.findViewById(R.id.layout_report_small);
        reportBeahviorSmall = (ImageView) headerView.findViewById(R.id.iv_report_beahvior_small);
        reportStudySmall = (ImageView) headerView.findViewById(R.id.iv_report_study_small);

        ivClothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PetALLActivity.class);
                startActivity(intent);
            }
        });

        scaleAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setFillAfter(true);
        //放大
        sca0 = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        sca0.setDuration(300);
        sca0.setFillAfter(true);
        //缩小
        sca1 = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        sca1.setDuration(300);
        sca1.setFillAfter(true);


        FrameLayout flKidBadge = (FrameLayout) headerView.findViewById(R.id.fl_kid_badge);
        FrameLayout flKidCommend = (FrameLayout) headerView.findViewById(R.id.fl_kid_commend);
        FrameLayout layoutClass = (FrameLayout) headerView.findViewById(R.id.layout_class);
        FrameLayout layoutGaijin = (FrameLayout) headerView.findViewById(R.id.layout_gaijin);
        rlSchoolRank = (RelativeLayout) headerView.findViewById(R.id.rl_school_rank);

        RelativeLayout rlReadingGameEnter = (RelativeLayout) headerView.findViewById(R.id.rl_reading_game);
        rlReadingGameEnter.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt(MLProperties.BUNDLE_KEY_GAME_LEVEL_NUMBER, mChallengeInfo.getChallengeNumber());
            bundle.putInt(MLProperties.BUNDLE_KEY_GAME_SCORE_COUNT, mChallengeInfo.getChallengeScore());
            bundle.putInt(MLProperties.BUNDLE_KEY_GAME_RANK_COUNT, mChallengeInfo.getChallengeRank());
            LoadingGameActivity.intentToLoadingGame(getActivity(), bundle);
        });

        rlSchoolRank.setOnClickListener(v -> actionSchoolRankClick());

        tvDengji.setOnClickListener(v -> {
            // showTipsDialog(getString(R.string.tips_grade));
            Intent intent = new Intent(getActivity(), LevelDetailActivity.class);
            startActivity(intent);
        });
        flKidBadge.setOnClickListener(v -> {
            // actionAddBadge("1");
            Intent intent = new Intent(getActivity(), BadgeDetailActivity.class);
            intent.putExtra(MLProperties.BUNDLE_KEY_KID_ID, kidId);
            intent.putExtra(MLProperties.BUNDLE_KEY_KID_BADGE, kidBadge);
            startActivity(intent);
        });
        //班级点击
        layoutClass.setOnClickListener(v -> {
            T.showShort(getActivity(), "蜂蜜：" + tvBanji.getText().toString());
        });

        layoutGaijin.setOnClickListener(v -> {
//            T.showShort(getActivity(),"敬请期待");

//            Intent intent = new Intent(getActivity(), GaijinDetailActivity.class);
//            intent.putExtra(MLProperties.BUNDLE_KEY_KID_ID, kidId);
//            intent.putExtra(MLProperties.BUNDLE_KEY_KID_SCORE, kidScore);
//            startActivity(intent);
            Intent intent = new Intent(getActivity(), CommentActivity.class);
            intent.putExtra(MLProperties.BUNDLE_KEY_KID_ID, kidId);
            intent.putExtra(MLProperties.BUNDLE_KEY_KID_SCORE, kidScore);
            startActivity(intent);
        });


        flKidCommend.setOnClickListener(v -> {
//            Intent intent = new Intent(getActivity(), CommendDetailActivity.class);
//            intent.putExtra(MLProperties.BUNDLE_KEY_KID_ID, kidId);
//            intent.putExtra(MLProperties.BUNDLE_KEY_KID_SCORE, kidScore);
//            startActivity(intent);

            if(huodong_status==0){
                T.showShort(getActivity(),"敬请期待");
            } if(huodong_status==1){
                Intent intent = new Intent(getActivity(), HuodongActivity.class);
                intent.putExtra(MLProperties.BUNDLE_KEY_KID_ID, kidId);
                startActivity(intent);
            }

        });

        reportBeahviorBig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReportTermActivity.class);
                intent.putExtra("id", kidId);
                startActivity(intent);
            }
        });
        reportStudyBig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReportStudyActivity.class);
                intent.putExtra("id", kidId);
                startActivity(intent);
            }
        });

        reportBeahviorSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReportTermActivity.class);
                intent.putExtra("id", kidId);
                startActivity(intent);
            }
        });
        reportStudySmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReportStudyActivity.class);
                intent.putExtra("id", kidId);
                startActivity(intent);
            }
        });

        reportRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(yuedudaka_map.equals("1")){
                    Intent intent = new Intent(getActivity(), ReadActivity.class);
                    intent.putExtra("yuedudaka_map","1");
                    startActivity(intent);
                }if(yuedudaka_map.equals("2")){
                    Intent intent = new Intent(getActivity(), ReadActivity2.class);
                    startActivity(intent);
                }

            }
        });
    }

    //打开定时器
    private void startTimer() {
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Message message = handler.obtainMessage();
                    message.what = 1;
                    handler.sendMessage(message);

                }
            }, 5000);
        } else {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Message message = handler.obtainMessage();
                    message.what = 1;
                    handler.sendMessage(message);

                }
            }, 5000);
        }
    }


    // 停止定时器
    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            // 一定设置为null，否则定时器不会被回收
            timer = null;

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void update(MsgEvent event) {
        if (event.getMsg().equals("ReplacePet")) {
            getPetPermission(1);
        }
    }

    private void initData() {
        int loginStatus = PreferencesUtils.getInt(getActivity(), MLProperties.PREFER_KEY_LOGIN_STATUS, 0);
        userPhoneNumber = PreferencesUtils.getString(getActivity(), MLProperties.PREFER_KEY_USER_ID);
        userClassName = PreferencesUtils.getString(getActivity(), MLProperties.BUNDLE_KEY_CLASS_NAME, "");

        // 初始化友盟
        UMPushManager manager = UMPushManager.getInstance();
        manager.setPushAlias(userPhoneNumber);

        kidId = PreferencesUtils.getString(getActivity(), MLProperties.BUNDLE_KEY_KID_ID);
        kidName = PreferencesUtils.getString(getActivity(), MLProperties.BUNDLE_KEY_KID_NAME);
        kidImg = PreferencesUtils.getString(getActivity(), MLProperties.BUNDLE_KEY_KID_IMG);
        userRelation = PreferencesUtils.getString(getActivity(), MLProperties.BUNDLE_KEY_KID_RELATION, "");

        isUserVirtual = PreferencesUtils.getInt(getActivity(), MLProperties.PREFER_KEY_USER_VIRTUAL, 0);

        kidScore = PreferencesUtils.getString(getActivity(), MLProperties.BUNDLE_KEY_KID_SCORE);
        kidGrade = PreferencesUtils.getString(getActivity(), MLProperties.BUNDLE_KEY_KID_GRADE);
        kidBadge = PreferencesUtils.getString(getActivity(), MLProperties.BUNDLE_KEY_KID_BADGE);
        kidBadgePro = PreferencesUtils.getString(getActivity(), MLProperties.BUNDLE_KEY_KID_BADGE_PRO);
        kidPingYu = PreferencesUtils.getString(getActivity(), MLProperties.BUNDLE_KEY_KID_PINGYU);
        schoolImg = PreferencesUtils.getString(getActivity(), MLProperties.BUNDLE_KEY_SCHOOL_IMG, "");
        schoolID = PreferencesUtils.getString(getActivity(), MLProperties.BUNDLE_KEY_SCHOOL_CODE, "");

        Log.e("学生id", kidId);

        if (!StringUtils.isEmpty(schoolImg)) {
            ivSchoolImg.setImageURI(Uri.parse(schoolImg));
        } else {
            getStudentInfoFromAPI();
        }

        // 初始化头部的数据
        initHeaderData();

        //add
        getPetPermission(0);
        //ad

        currentPage = 1;
        mDataList.clear();
        MyGetKidDataTask myGetKidDataTask = new MyGetKidDataTask();
        myGetKidDataTask.execute(kidId);
        MyGetStudentAllNewsTask myGetStudentAllNewsTask = new MyGetStudentAllNewsTask();
        myGetStudentAllNewsTask.execute(kidId, String.valueOf(currentPage));

        // 判断这个家长是否绑定了学生，如果没有跳转到绑定页面
        MyCheckRelationKidTask myCheckRelationKidTask = new MyCheckRelationKidTask();
        myCheckRelationKidTask.execute(userPhoneNumber);

        // 如果是虚拟用户，则弹提示框
        if (isUserVirtual == 1 && loginStatus == 0) {
            showWelcomeTipsDialog("小二送你虚拟角色“" + kidName + "”");
        }
        // 保存这次登录的时间
        PreferencesUtils.putLong(getActivity(), MLProperties.PREFER_KEY_LOGIN_Time, System.currentTimeMillis());
        PreferencesUtils.putInt(getActivity(), MLProperties.PREFER_KEY_LOGIN_STATUS, 1);

        // 检查新版本 如果是Wi-Fi环境下才进行检查
        if (NetUtils.isConnected(getActivity().getApplicationContext()) &&
                NetUtils.isWifi(getActivity().getApplicationContext())) {
            MyCheckNewVersionTask versionTask = new MyCheckNewVersionTask();
            versionTask.execute();
        }
    }

    private void showWelcomeTipsDialog(String content) {
        CancelConfirmAlertDialog2 dialog = new CancelConfirmAlertDialog2(getActivity());
        dialog.setTitleText("欢迎大驾");
        dialog.setContentText(content);
        dialog.setCancelText("知道了");
        dialog.showConfirmButton(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

        stopTimer();
    }

    private void initHeaderData() {
        if (kidImg != null) {
            imgStudentPhoto.setImageURI(Uri.parse(kidImg));
        }
        tvDengji.setText((StringUtils.isEmpty(kidGrade) ? "" : kidGrade) + "等级");
        if (kidPingYu != null && !kidPingYu.equals("")) {
            tvStudentDesc.setText(kidPingYu);
        } else {
            tvStudentDesc.setText("我们还不知道你的最新状态。");
        }
        // tvJifen.setText("点赞" + (StringUtils.isEmpty(kidScore) || kidScore.equals("0") ? "" : " " + kidScore));
        //tvHuizhang.setText("徽章" + (StringUtils.isEmpty(kidBadge) || kidBadge.equals("0") ? "" : " " + kidBadge));
        //tvZhuanxiang.setText("专项" + (StringUtils.isEmpty(kidBadgePro) || kidBadgePro.equals("0") ? "" : " " + kidBadgePro));

        //change
        //tvZhuanxiang.setText("班级");
    }

    private void actionQRCode() {
        requestCameraPermission();
    }

    private void requestCameraPermission() {

        RxPermissions rxPermissions = new RxPermissions(getActivity());
        rxPermissions.request(Manifest.permission.CAMERA)
                .subscribe(success -> {
                    if (success) {
                        Intent intent = new Intent(getActivity(), QRCodeScanActivity.class);
                        startActivityForResult(intent, QRCODE_REQUEST_CODE);
                    } else {
                        // 未授权拨打电话
                        LL.i("未授权拨打电话");
                    }
                });
    }

    private Handler mHandler = new Handler();

    private void actionAddScore(final String value) {
        tvAddScore.setText("+" + value);
        animationTextView(tvAddScore, value);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                kidScore = String.valueOf(Integer.valueOf(StringUtils.isEmpty(kidScore) ? "0" : kidScore) + Integer.valueOf(value));
                // tvJifen.setText("点赞 " + (StringUtils.isEmpty(kidScore) ? "0" : kidScore));
                PreferencesUtils.putString(getActivity(), MLProperties.BUNDLE_KEY_KID_SCORE, kidScore);
            }
        }, 900);

    }

    private void actionAddBadge(final String value) {
        showPopViewAddBadge(1, value, new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // 隐藏弹框
                hidePopViewAddBadge();
                // 修改数字
                kidBadge = String.valueOf(Integer.valueOf(StringUtils.isEmpty(kidBadge) ? "0" : kidBadge) + Integer.valueOf(value));
                // tvHuizhang.setText("徽章" + (StringUtils.isEmpty(kidBadge) || kidBadge.equals("0") ? "" : " " + kidBadge));
                PreferencesUtils.putString(getActivity(), MLProperties.BUNDLE_KEY_KID_BADGE, kidBadge);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    private void actionAddBadgePro(final String value) {
        showPopViewAddBadge(2, value, new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // 隐藏弹框
                hidePopViewAddBadge();
                // 修改数据
                kidBadgePro = String.valueOf(Integer.valueOf(StringUtils.isEmpty(kidBadgePro) ? "0" : kidBadgePro) + Integer.valueOf(value));
                //tvZhuanxiang.setText("专项" + (StringUtils.isEmpty(kidBadgePro) || kidBadgePro.equals("0") ? "" : " " + kidBadgePro));
                PreferencesUtils.putString(getActivity(), MLProperties.BUNDLE_KEY_KID_BADGE_PRO, kidBadge);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == QRCODE_REQUEST_CODE && resultCode == RESULT_OK) {
            String result = data.getExtras().getString("result");
            Log.e("二维码结果：", result);

            MyAddScoreTask myAddScoreTask = new MyAddScoreTask();
            myAddScoreTask.execute(kidId, result);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private class MyAddScoreTask extends AsyncTask<String, Integer, String[]> {

        @Override
        protected void onPreExecute() {
            showLoadingDialog();
        }

        @Override
        protected String[] doInBackground(String... params) {
            String kidId = params[0];
            String qrCode = params[1];
            Log.e("qrCode", qrCode);
            Log.e("qrCode2", qrCode.length() + "");
            LmsDataService mService = new LmsDataService();
            String[] result;
            try {
                if (qrCode.startsWith("http://qr12.cn/")) {
                    String resultCode = mService.getQRCodeFormWeb(qrCode);
                    result = mService.addKidBadge(kidId, resultCode);//扫描徽章
//                    result = mService.addKidScore(kidId, resultCode);
                } else {
//                    if (!StringUtils.checkQRCode(qrCode)) {
//                        result = new String[4];
//                        result[0] = "0";
//                        result[1] = "错误的二维码";
//                    } else {
                    result = mService.addKidBadge(kidId, qrCode);
//                        result = mService.addKidScore(kidId, qrCode);
                    Log.e("识别结果呢", result[0].toString() + result[1].toString());
//                    }
                }
                return result;
            } catch (Exception e) {
                LL.e(e);
                result = new String[4];
                result[0] = "0";
                result[1] = "服务器开小差了，请待会重试";
                return result;
            }
        }

        @Override
        protected void onPostExecute(String[] result) {

            Log.e("识别结果", result.toString());
            hideLoadingDialog();
            if (StringUtils.isEmpty(result[0]) || !result[0].equals("1")) {
                T.showShort(getActivity(), result[1]);
            } else {
                String type = result[2];
                String value = result[3];
                if (type.equals("8")) {

                    Log.e("徽章type1", type);
                    actionAddBadgePro(value);
                } else {
                    Log.e("徽章type2", type);
                    actionAddBadge(value);
                }
                mHandler.postDelayed(() -> {
                    // 获取学生数据，并更新
                    LL.i("刷新数据");
                    currentPage = 1;
                    mXRefreshView.mPullRefreshing = true;
                    // 刷新时需要调用两个Task
                    MyGetKidDataTask myGetKidDataTask = new MyGetKidDataTask();
                    myGetKidDataTask.execute(kidId);
                    MyGetStudentAllNewsTask myGetStudentAllNewsTask = new MyGetStudentAllNewsTask();
                    myGetStudentAllNewsTask.execute(kidId, String.valueOf(currentPage));
                }, 3000);
            }
        }
    }

    private void animationTextView(final TextView tv, String value) {
        tv.setVisibility(View.VISIBLE);
        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0f);
        alphaAnimation.setDuration(1000);
        animationSet.addAnimation(alphaAnimation);
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, -0.3f,
                Animation.RELATIVE_TO_PARENT, -1.0f);
        translateAnimation.setDuration(1000);
        animationSet.addAnimation(translateAnimation);

        tv.startAnimation(animationSet);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv.setVisibility(View.GONE);
            }
        }, 800);
    }

    private boolean ifPageInit = true;

    private class MyGetKidDataTask extends AsyncTask<String, Integer, KidDataInfo> {
        @Override
        protected void onPreExecute() {
            if (ifPageInit) {
                showLoadingDialog();
                ifPageInit = false;
            }
        }

        @Override
        protected KidDataInfo doInBackground(String... params) {
            LmsDataService mService = new LmsDataService();
            KidDataInfo kidDataInfo;
            try {
                kidDataInfo = mService.getStudentDataFromAPI(params[0]);
                // 保存学生的数据
                PreferencesUtils.putString(getActivity(), MLProperties.BUNDLE_KEY_KID_SCORE, kidDataInfo.getScore());
                PreferencesUtils.putString(getActivity(), MLProperties.BUNDLE_KEY_KID_BADGE, kidDataInfo.getBadge());
                PreferencesUtils.putString(getActivity(), MLProperties.BUNDLE_KEY_KID_BADGE_PRO, kidDataInfo.getBadgePro());
                PreferencesUtils.putString(getActivity(), MLProperties.BUNDLE_KEY_KID_GRADE, kidDataInfo.getGrade());
                PreferencesUtils.putString(getActivity(), MLProperties.BUNDLE_KEY_KID_PINGYU, kidDataInfo.getPingyu());
            } catch (Exception e) {
                e.printStackTrace();
                LL.e(e);
                kidDataInfo = new KidDataInfo();
                kidDataInfo.setErrorCode("0");
                kidDataInfo.setMessage("服务器开小差了，请待会重试");
            }
            return kidDataInfo;
        }

        @Override
        protected void onPostExecute(KidDataInfo result) {
            hideLoadingDialog();
            String errorCode = result.getErrorCode();
            String message = result.getMessage();
            if (errorCode.equals("0") || errorCode.equals("2")) {
                T.showShort(getActivity(), message);
            } else {
                kidScore = result.getScore();
                //  tvJifen.setText("点赞" + (StringUtils.isEmpty(kidScore) || kidScore.equals("0") ? "" : " " + kidScore));
                kidBadge = result.getBadge();
                // tvHuizhang.setText("徽章" + (StringUtils.isEmpty(kidBadge) || kidBadge.equals("0") ? "" : " " + kidBadge));
                kidBadgePro = result.getBadgePro();
                // tvZhuanxiang.setText("专项" + (StringUtils.isEmpty(kidBadgePro) || kidBadgePro.equals("0") ? "" : " " + kidBadgePro));
                kidGrade = result.getGrade();
                tvDengji.setText((StringUtils.isEmpty(kidGrade) ? "" : kidGrade) + "等级");
                kidPingYu = (StringUtils.isEmpty(result.getPingyu()) ? "你准备好了吗？" : result.getPingyu());
                tvStudentDesc.setText(kidPingYu);

                tvHuizhang.setText(result.getBadge());


                String newKidImg = PreferencesUtils.getString(getActivity(), MLProperties.BUNDLE_KEY_KID_IMG);
                if (!StringUtils.isEmpty(newKidImg) && !newKidImg.equals(kidImg)) {
                    imgStudentPhoto.setImageURI(Uri.parse(newKidImg));
                }
            }
        }
    }

    private class MyCheckRelationKidTask extends AsyncTask<String, Integer, String[]> {

        @Override
        protected String[] doInBackground(String... params) {
            LmsDataService mService = new LmsDataService();
            String[] result;
            try {
                result = mService.checkRelationKidFromAPI(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
                LL.e(e);
                result = new String[2];
                result[0] = "0";
                result[1] = "服务器开小差了，请待会重试";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (StringUtils.isEmpty(result[0]) || result[0].equals("0")) {
                // T.showShort(HomeActivity.this, StringUtils.isEmpty(result[1]) ? "获取关联学生失败" : result[1]);
            } else if (result[0].equals("1")) {
                if (!result[1].equals(kidId)) {
                    PreferencesUtils.putString(getActivity(), MLProperties.BUNDLE_KEY_KID_ID, result[1]);
                    PreferencesUtils.putString(getActivity(), MLProperties.BUNDLE_KEY_KID_NAME, result[2]);
                }
            } else {
                // 跳转到关联页面
                PreferencesUtils.putInt(getActivity(), MLProperties.PREFER_KEY_LOGIN_STATUS, 0);
                PreferencesUtils.putInt(getActivity(), MLProperties.PREFER_KEY_IS_USER_INFO_COMPLETE, 0);

                Intent intent = new Intent(getActivity(), RelationKidActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        }
    }

    private class MyGetStudentAllNewsTask extends AsyncTask<String, Integer, List<ClassNewsInfo>> {

        @Override
        protected List<ClassNewsInfo> doInBackground(String... params) {
            String studentID = params[0];
            String pageIndex = params[1];
            LmsDataService mService = new LmsDataService();
            List<ClassNewsInfo> dataList;
            try {
                dataList = mService.getStudentAllNewsFromAPI(studentID, pageIndex);
            } catch (Exception e) {
                e.printStackTrace();
                LL.e(e);
                dataList = null;
            }
            return dataList;
        }

        @Override
        protected void onPostExecute(List<ClassNewsInfo> result) {
            if (result == null) {
                cleanXRefreshView();
                T.showShort(getActivity(), "服务器开小差了，请重试");
            } else {
                loadData(result);
            }
        }
    }

    private void loadData(List<ClassNewsInfo> list) {
        lastRefreshTime = mXRefreshView.getLastRefreshTime();
        if (mXRefreshView.mPullRefreshing) {
            mDataList.clear();
            mXRefreshView.stopRefresh();
            mXRefreshView.setAutoLoadMore(true);
            mXRefreshView.setPullLoadEnable(true);
        }
        if (list == null || list.size() < 10) {
            mXRefreshView.setPullLoadEnable(false);
        }
        mXRefreshView.stopLoadMore();
        // 更新数据
        mDataList.addAll(list);
        mAdapter.notifyDataSetChanged();
        if (null == mAdapter.getCustomLoadMoreView()) {
            mAdapter.setCustomLoadMoreView(new XRefreshViewFooter(getActivity()));
        }

        //add
        state = true;
        //add
        if (state) {
            startTimer();
        }
    }

    private void cleanXRefreshView() {
        mXRefreshView.stopRefresh();
        mXRefreshView.stopLoadMore();
    }

    @Override
    public void onResume() {
        super.onResume();
        String newKidImg = PreferencesUtils.getString(getActivity(), MLProperties.BUNDLE_KEY_KID_IMG);
        if (!StringUtils.isEmpty(newKidImg) && !newKidImg.equals(kidImg)) {
            imgStudentPhoto.setImageURI(Uri.parse(newKidImg));
        }
        GetSchoolRankListStatusTask task = new GetSchoolRankListStatusTask();
        task.execute();
        // 刷新挑战赛的排名信息
        getStudentChallengeInfo();
        // 是否显示报告入口
       // getReportInfo();


        if (state) {
            startTimer();
        }


    }

    @Override
    protected void onVisible() {
        super.onVisible();
        MobclickAgent.onPageStart("home");
    }

    @Override
    protected void onInVisible() {
        super.onInVisible();
        MobclickAgent.onPageEnd("home");
    }

    @Override
    public void onPause() {
        super.onPause();

        stopTimer();
        // tvContent.clearAnimation();
//        tvContent.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.header_ll_right)
    void actionHeaderRightClick() {
        actionQRCode();
    }

    private class MyCheckNewVersionTask extends AsyncTask<String, Integer, AppVersionInfo> {

        @Override
        protected AppVersionInfo doInBackground(String... params) {
            LmsDataService mService = new LmsDataService();
            AppVersionInfo info;
            String versionName = AppUtils.getVersionName(getActivity());
            String qudao = AppUtils.getMetaDataFromApplication(getActivity(), MLConfig.KEY_CHANNEL_NAME);
            try {
                info = mService.checkNewVersion(versionName, qudao);
            } catch (Exception e) {
                e.printStackTrace();
                LL.e(e);
                info = new AppVersionInfo();
                info.setErrorCode("0");
            }
            return info;
        }

        @Override
        protected void onPostExecute(AppVersionInfo info) {
            if (StringUtils.isEmpty(info.getErrorCode()) || info.getErrorCode().equals("0")) {
                // LL.i(info.getMessage());
                return;
            }

            Log.e("版本信息", info.toString());

            if (info.getErrorCode().equals("1")) {
                showNewVersionDialog(info.getQiangzhigengxin(), info.getTitle(), info.getContent(), info.getDownloadUrl());
            } else if (info.getErrorCode().equals("2")) {
                // T.showShort(TeacherClassesActivity.getActivity(), info.getMessage());
            }
        }
    }

    private void showNewVersionDialog(String gengxin, String title, String content, final String downloadUrl) {

        UpdateAPPAlertDialog dialog = new UpdateAPPAlertDialog(getActivity());
        dialog.setTitleText(title);
        dialog.setContentText(content);
        dialog.setCanceledOnTouchOutside(false);


        dialog.setConfirmClickListener(new UpdateAPPAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(UpdateAPPAlertDialog sweetAlertDialog) {
                RxPermissions rxPermissions = new RxPermissions(getActivity());
                rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(success -> {
                            if (success) {
                                startDownloadAppService(downloadUrl);
                                sweetAlertDialog.startDownload();
                            } else {
                                sweetAlertDialog.dismiss();
                            }
                        });
            }
        });
        dialog.setCancelClickListener(new UpdateAPPAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(UpdateAPPAlertDialog sweetAlertDialog) {
                if (gengxin.equals("1")) {
                    T.showShort(getActivity(), "请更新版本后重试");
                } else {
                    sweetAlertDialog.dismiss();
                }
            }
        });
        dialog.setCancelDownloadListener(new UpdateAPPAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(UpdateAPPAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
                stopDownloadAppService(downloadUrl);
            }
        });

        dialog.show();

        if (gengxin.equals("1")) {
            dialog.setCancelable(false);
            dialog.hideTvCancelDownLoad();
        } else {
            dialog.setCancelable(true);
        }

    }

    private void startDownloadAppService(String downloadUrl) {
        Intent intent = new Intent(getActivity(), DownloadAppService.class);
        Bundle args = new Bundle();
        args.putString(MLConfig.KEY_BUNDLE_DOWNLOAD_URL, downloadUrl);
        intent.putExtras(args);
        getActivity().startService(intent);
    }

    private void stopDownloadAppService(String downloadUrl) {
        OkHttpUtils.getInstance().cancelTag(DownloadAppService.FILENAME);
    }

    private void actionSchoolRankClick() {
        Intent intent = new Intent(getActivity(), RankListActivity.class);
        intent.putExtra(MLProperties.BUNDLE_KEY_KID_ID, kidId);
        startActivity(intent);
    }

    /**
     * 获取是否显示全校排行榜数据的入口
     */
    private class GetSchoolRankListStatusTask extends AsyncTask<String, Integer, String[]> {

        @Override
        protected String[] doInBackground(String... params) {
            LmsDataService mService = new LmsDataService();
            String[] result;
            try {
                result = mService.getSchoolRankListStatusFromAPI(schoolID);
            } catch (Exception e) {
                e.printStackTrace();
                LL.e(e);
                result = new String[3];
                result[0] = "0";
                result[2] = "0";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            if (!StringUtils.isEmpty(strings[0]) && strings[0].equals("1")
                    && strings[2].equals("1")) {
                rlSchoolRank.setVisibility(View.VISIBLE);
            } else {
                rlSchoolRank.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 获取学生的自身相关属性
     */
    private void getStudentInfoFromAPI() {
        Observable.create((ObservableOnSubscribe<KidClassInfo>) emitter -> {
            LmsDataService mService = new LmsDataService();
            KidClassInfo kidClassInfo = mService.getKidClassInfoFromAPI2(kidId);
            emitter.onNext(kidClassInfo);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<KidClassInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(KidClassInfo kidClassInfo) {
                        // 保存学生数据
                        schoolImg = kidClassInfo.getSchoolImg();

                        PreferencesUtils.putString(getActivity(), MLProperties.BUNDLE_KEY_KID_ID, kidClassInfo.getKidId());
                        PreferencesUtils.putString(getActivity(), MLProperties.BUNDLE_KEY_KID_NAME, kidClassInfo.getKidName());
                        PreferencesUtils.putString(getActivity(), MLProperties.BUNDLE_KEY_KID_IMG, kidClassInfo.getKidImg());
                        PreferencesUtils.putString(getActivity(), MLProperties.BUNDLE_KEY_SCHOOL_CODE, kidClassInfo.getSchoolId());
                        PreferencesUtils.putString(getActivity(), MLProperties.BUNDLE_KEY_SCHOOL_NAME, kidClassInfo.getSchoolName());
                        PreferencesUtils.putString(getActivity(), MLProperties.BUNDLE_KEY_SCHOOL_IMG, kidClassInfo.getSchoolImg());
                        PreferencesUtils.putString(getActivity(), MLProperties.BUNDLE_KEY_CLASS_CODE, kidClassInfo.getClassId());
                        PreferencesUtils.putString(getActivity(), MLProperties.BUNDLE_KEY_CLASS_NAME, kidClassInfo.getClassName());
                        PreferencesUtils.putString(getActivity(), MLProperties.BUNDLE_KEY_KID_GENDER, kidClassInfo.getKidGender());
                        PreferencesUtils.putString(getActivity(), MLProperties.BUNDLE_KEY_KID_BIRTHDAY, kidClassInfo.getKidBirthday());

                        if (!StringUtils.isEmpty(schoolImg)) {
                            ivSchoolImg.setImageURI(Uri.parse(schoolImg));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void myThanksTeacherTask(int position) {
        Observable.create((ObservableOnSubscribe<String[]>) emitter -> {
            LmsDataService mService = new LmsDataService();
            String newsID = mDataList.get(position).getNewsId();
            String[] result = mService.getThanksTeacherResultFromAPI(newsID, String.valueOf(position));
            emitter.onNext(result);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String[]>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String[] result) {
                        if (!StringUtils.isEmpty(result[0]) && result[0].equals("1")) {
                            T.showShort(getActivity(), "感谢成功");
                            if (position < mDataList.size()) {
                                mDataList.get(position).setNewsThanksStatus("2");
                                mAdapter.notifyDataSetChanged();
                            }
                            // 跳转到聊天页面，并保存会话记录
                            //showCommunicationDialog(position);
                        } else {
                            T.showShort(getActivity(), "感谢失败，请重试");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        T.showShort(getActivity(), "服务器开小差了，请重试");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void showCommunicationDialog(int position) {
        CancelConfirmAlertDialog dialog = new CancelConfirmAlertDialog(getActivity())
                .setTitleText("一对一沟通")
                .setContentText("您是否需要和老师一对一沟通？")
                .setCancelText("取消")
                .setConfirmText("去沟通")
                .setCancelClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.dismiss();
                }).setConfirmClickListener(sweetAlertDialog -> {
                    // 保存会话记录
                    saveConversation(mDataList.get(position).getTeacherPhone());
                    // 跳转到沟通页面，并发送一条固定的消息
                    sendMessage(position, firstContent);
                    sweetAlertDialog.dismiss();
                    MobclickAgent.onEvent(getActivity(), "thanks_chat");
                });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /**
     * 获取学生挑战赛的相关信息
     */
    private void getStudentChallengeInfo() {
        Observable.create((ObservableOnSubscribe<ChallengeInfo>) e -> {
            LmsDataService mService = new LmsDataService();
            mChallengeInfo = mService.getStudentChallengeInfoFromAPI(kidId);
            e.onNext(mChallengeInfo);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ChallengeInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ChallengeInfo challengeInfo) {
                        String content = "挑战" + challengeInfo.getChallengeNumber() + "次，"
                                + "得分" + challengeInfo.getChallengeScore() + "，"
                                + "全国排名" + (challengeInfo.getChallengeRank() == 0 ? "千里之外" : "" +
                                challengeInfo.getChallengeRank());
                        tvChallengeContent.setText(content);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getReportInfo() {
        Observable.create((ObservableOnSubscribe<ReportInfo>) emitter -> {
            LmsDataService mService = new LmsDataService();
            currReportInfo = mService.getReportBaseInfoFromAPI(userPhoneNumber, kidId);
            emitter.onNext(currReportInfo);
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ReportInfo>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {

                    }

                    @Override
                    public void onNext(@NonNull ReportInfo reportInfo) {
                        String reportState = reportInfo.schoolReportState;
                        if ("1".equals(reportState)) {
                            // tvStudentReport.setVisibility(View.VISIBLE);
                        } else {
                            // tvStudentReport.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void saveConversation(String teacherPhone) {
        Observable.create((ObservableOnSubscribe<String[]>) e -> {
            LmsDataService mService = new LmsDataService();
            String[] result = mService.saveConversationFromAPI(teacherPhone, userPhoneNumber);
            e.onNext(result);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String[]>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String[] strings) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void sendMessage(int position, String content) {
        String userID = MLConfig.EASE_TEACHER_ID_PREFIX + mDataList.get(position).getTeacherPhone();
        String currUserNick = PreferencesUtils.getString(getActivity(), MLProperties.BUNDLE_KEY_KID_NAME);
        if (StringUtils.isEmpty(userRelation)) {
            currUserNick = currUserNick + "的家长";
        } else if ("baba".equals(userRelation)) {
            currUserNick = currUserNick + "的爸爸";
        } else if ("mama".equals(userRelation)) {
            currUserNick = currUserNick + "的妈妈";
        }
        //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        EMMessage message = EMMessage.createTxtSendMessage(content, userID);
        message.setAttribute(Constant.EXTRA_CURR_USER_NICK, currUserNick);
        message.setAttribute(Constant.EXTRA_CURR_USER_PHOTO, PreferencesUtils.getString(getActivity(), MLProperties.BUNDLE_KEY_KID_IMG));
        message.setAttribute(Constant.EXTRA_TO_USER_NICK, mDataList.get(position).getTeacherName());
        message.setAttribute(Constant.EXTRA_TO_USER_PHOTO, mDataList.get(position).getTeacherPic());
        message.setAttribute(EaseConstant.EXTRA_TO_CLASS_NAME, userClassName);
        //发送消息
        EMClient.getInstance().chatManager().sendMessage(message);

        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra(Constant.EXTRA_USER_ID, userID);
        intent.putExtra(Constant.EXTRA_TO_USER_NICK, mDataList.get(position).getTeacherName());
        intent.putExtra(Constant.EXTRA_TO_USER_PHOTO, mDataList.get(position).getTeacherPic());
        startActivity(intent);
    }

    private void initPopAddBadgeView() {
        popview = LayoutInflater.from(getActivity()).inflate(R.layout.pop_add_badge, null);
        popAddBadge = new PopupWindow(popview, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, false);
        popAddBadge.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddBadge.setFocusable(false);
        popAddBadge.setOutsideTouchable(false);
        popAddBadge.setOnDismissListener(() -> {
            setBackgroundAlpha(getActivity(), 1f);
        });
        ivPopAddBadge = (ImageView) popview.findViewById(R.id.iv_badge);
        tvPopBadgeNumber = (TextView) popview.findViewById(R.id.tv_badgeNumber);
    }

    private void showPopViewAddBadge(int type, String value, Animator.AnimatorListener listener) {
        if (popAddBadge != null && !popAddBadge.isShowing()) {
            setBackgroundAlpha(getActivity(), 0.5f);
            if (type == 1) {
                popAddBadge.setAnimationStyle(R.style.MyPopupWindow_scale_anim_style);
                ivPopAddBadge.setImageResource(R.drawable.ic_homepage_badge_add);
                tvPopBadgeNumber.setTextColor(Color.rgb(247, 220, 191));
                tvPopBadgeNumber.setText("徽章 +" + value);
            } else {
                popAddBadge.setAnimationStyle(R.style.MyPopupWindow_scale_anim_style_2);
                ivPopAddBadge.setImageResource(R.drawable.ic_homepage_pro_add);
                tvPopBadgeNumber.setTextColor(Color.rgb(213, 237, 166));
                tvPopBadgeNumber.setText("专项 +" + value);
            }
            popAddBadge.showAtLocation(tvTitle, Gravity.CENTER, 0, popAddBadge.getHeight());
            // 启动动画
            startAnim(popview, listener);
        }
    }

    private void startAnim(View ivPopAddBadge, Animator.AnimatorListener listener) {
        ObjectAnimator animX1 = ObjectAnimator.ofFloat(ivPopAddBadge, "scaleX", 1f, 1.5f);
        animX1.setDuration(1000);
        animX1.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator animY1 = ObjectAnimator.ofFloat(ivPopAddBadge, "scaleY", 1f, 1.5f);
        animY1.setDuration(1000);
        animY1.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator animX2 = ObjectAnimator.ofFloat(ivPopAddBadge, "scaleX", 1.5f, 1f);
        animX2.setDuration(1000);
        animX2.setInterpolator(new DecelerateInterpolator());

        ObjectAnimator animY2 = ObjectAnimator.ofFloat(ivPopAddBadge, "scaleY", 1.5f, 1f);
        animY2.setDuration(1000);
        animY2.setInterpolator(new DecelerateInterpolator());

        animX2.addListener(listener);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animX1).with(animY1);
        animatorSet.play(animX2).with(animY2).after(animX1);
        animatorSet.start();
    }

    private void hidePopViewAddBadge() {
        if (popAddBadge != null && popAddBadge.isShowing()) {
            popAddBadge.dismiss();
        }
    }

    /**
     * 设置背景透明度
     *
     * @param activity
     * @param bgAlpha
     */
    public void setBackgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        if (bgAlpha == 1) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        activity.getWindow().setAttributes(lp);
    }
}

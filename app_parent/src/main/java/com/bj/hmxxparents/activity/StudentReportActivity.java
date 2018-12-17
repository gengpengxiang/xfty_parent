package com.bj.hmxxparents.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bj.hmxxparents.BaseActivity;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.api.LmsDataService;
import com.bj.hmxxparents.api.MLProperties;
import com.bj.hmxxparents.entity.OrderInfo;
import com.bj.hmxxparents.entity.ReportInfo;
import com.bj.hmxxparents.entity.TradeInfo;
import com.bj.hmxxparents.fragment.StudentReportFragment1;
import com.bj.hmxxparents.fragment.StudentReportFragment2;
import com.bj.hmxxparents.fragment.StudentReportFragment3;
import com.bj.hmxxparents.fragment.StudentReportFragment4;
import com.bj.hmxxparents.fragment.StudentReportFragment5;
import com.bj.hmxxparents.fragment.StudentReportFragment6;
import com.bj.hmxxparents.utils.LL;
import com.bj.hmxxparents.utils.NetUtils;
import com.bj.hmxxparents.utils.PreferencesUtils;
import com.bj.hmxxparents.utils.StringUtils;
import com.bj.hmxxparents.utils.T;
import com.bj.hmxxparents.widget.VerticalViewPager;
import com.douhao.game.Constants;
import com.douhao.game.utils.Util;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.TipsAlertDialog3;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zz379 on 2017/6/29.
 * 学生本学期表现报告页面
 */

public class StudentReportActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.header_img_back)
    ImageView imgBack;
    @BindView(R.id.header_tv_title)
    TextView tvTitle;
    @BindView(R.id.header_tv_share)
    TextView tvShare;
    @BindView(R.id.header_ll_right)
    LinearLayout llHeaderRight;
    @BindView(R.id.header_ll_left)
    LinearLayout llHeaderLeft;
    @BindView(R.id.mViewPager)
    VerticalViewPager mViewPager;
    @BindView(R.id.iv_next)
    ImageView ivNext;
    @BindView(R.id.fl_next)
    FrameLayout flNext;

    int pageIndex = 0;

    private String kidName, kidID;
    private String userPhoneNumber;
    private IWXAPI api;
    private int reportRealPay;    // 当前需要的实际的金钱
    private int reportPrice;        // 原价
    private boolean isUserPaySuccess = false;  // 是否支付成功
    private String currTradeID = "";     // 当前商户订单号

    private FragmentManager fm;
    private ArrayList<Fragment> mTabs = new ArrayList<>();
    private StudentReportFragment1 report1;
    private StudentReportFragment2 report2;
    private StudentReportFragment3 report3;
    private StudentReportFragment4 report4;
    private StudentReportFragment5 report5;
    private StudentReportFragment6 report6;
    private PopupWindow popPayEntry;
    private PopupWindow popPayDetail;
    private PopupWindow popShare;
    private ReportInfo currReportInfo;
    private FragmentPagerAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_report);
        ButterKnife.bind(this);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_DOUHAO_PARENT_ID);
        //
        initToolBar();
        initView();
        initData();
    }

    private void initToolBar() {
        imgBack.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvShare.setVisibility(View.VISIBLE);
    }

    private void initView() {
        reportRealPay = getIntent().getExtras().getInt("reportRealPay", 0);
        reportPrice = getIntent().getExtras().getInt("reportPrice", 0);
        isUserPaySuccess = getIntent().getExtras().getBoolean("isUserPaySuccess", false);

        //initPopViewPayEntry();
        //initPopViewPayDetail();
        initPopViewShare();

        fm = getSupportFragmentManager();

        ivNext.setVisibility(View.VISIBLE);
        flNext.setEnabled(true);
        mViewPager.setPageScrollEnable(true);

//        if (reportRealPay == 0 || isUserPaySuccess) {
//            ivNext.setVisibility(View.VISIBLE);
//            flNext.setEnabled(true);
//            mViewPager.setPageScrollEnable(true);
//        } else {
//            ivNext.setVisibility(View.INVISIBLE);
//            flNext.setEnabled(false);
//            mViewPager.setPageScrollEnable(false);
//            flNext.post(() -> showPopViewPayEntry());
//        }
    }

    private void initData() {
        kidID = PreferencesUtils.getString(this, MLProperties.BUNDLE_KEY_KID_ID);
        kidName = PreferencesUtils.getString(this, MLProperties.BUNDLE_KEY_KID_NAME, "");
        userPhoneNumber = PreferencesUtils.getString(this, MLProperties.PREFER_KEY_USER_ID);

        if (kidName.length() > 6) kidName = kidName.substring(0, 6);
        tvTitle.setText(kidName + "本学期表现报告");

        loadAllDataInfo();
    }

    private void loadAllDataInfo() {
        if (!NetUtils.isConnected(this)) {
            T.showShort(this, "无法连接到网络，请检查您的网络设置");
            hideLoadingDialog();
            return;
        }
        LmsDataService mService = new LmsDataService();
        Observable<ReportInfo> page1 = Observable.create((ObservableOnSubscribe<ReportInfo>) emitter -> {
            ReportInfo info = mService.getReportIndex1FromAPI(kidID);
            emitter.onNext(info);
            emitter.onComplete();
        }).subscribeOn(Schedulers.io()).retry(3);
        Observable<ReportInfo> page2 = Observable.create((ObservableOnSubscribe<ReportInfo>) emitter -> {
            ReportInfo info = mService.getReportIndex2FromAPI(kidID);
            emitter.onNext(info);
            emitter.onComplete();
        }).subscribeOn(Schedulers.io()).retry(3);
        Observable<ReportInfo> page3 = Observable.create((ObservableOnSubscribe<ReportInfo>) emitter -> {
            ReportInfo info = mService.getReportIndex3FromAPI(kidID);
            emitter.onNext(info);
            emitter.onComplete();
        }).subscribeOn(Schedulers.io()).retry(3);
        Observable<ReportInfo> page4 = Observable.create((ObservableOnSubscribe<ReportInfo>) emitter -> {
            ReportInfo info = mService.getReportIndex4FromAPI(kidID);
            emitter.onNext(info);
            emitter.onComplete();
        }).subscribeOn(Schedulers.io()).retry(3);
        Observable<ReportInfo> page5 = Observable.create((ObservableOnSubscribe<ReportInfo>) emitter -> {
            ReportInfo info = mService.getReportIndex5FromAPI(kidID);
            emitter.onNext(info);
            emitter.onComplete();
        }).subscribeOn(Schedulers.io()).retry(3);

        Observable.zip(page1, page2, page3, page4, page5, (reportInfo, reportInfo2, reportInfo3, reportInfo4, reportInfo5) -> {
            reportInfo.userBadgePieMap = reportInfo2.userBadgePieMap;
            reportInfo.userCommendPieMap = reportInfo3.userCommendPieMap;
            reportInfo.userBadgeLineMap = reportInfo4.userBadgeLineMap;
            reportInfo.classBadgeLineMap = reportInfo4.classBadgeLineMap;
            reportInfo.userCommendLineMap = reportInfo4.userCommendLineMap;
            reportInfo.classCommendLineMap = reportInfo4.classCommendLineMap;
            reportInfo.badgeRankList = reportInfo5.badgeRankList;
            reportInfo.commendRankList = reportInfo5.commendRankList;

            return reportInfo;
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ReportInfo>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        showLoadingDialog();
                    }

                    @Override
                    public void onNext(@NonNull ReportInfo reportInfo) {
                        updateData(reportInfo);
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        hideLoadingDialog();
                        T.showShort(StudentReportActivity.this, "服务器开小差了，请稍后重试");
                        LL.e(throwable);
                    }

                    @Override
                    public void onComplete() {
                        hideLoadingDialog();
                    }
                });
    }

    private void updateData(ReportInfo reportInfo) {
        EventBus.getDefault().postSticky(reportInfo);

        currReportInfo = reportInfo;
        report1 = new StudentReportFragment1();
        report2 = new StudentReportFragment2();
        report3 = new StudentReportFragment3();
        report4 = new StudentReportFragment4();
        report5 = new StudentReportFragment5();
        report6 = new StudentReportFragment6();
        mTabs.add(report1);
        mTabs.add(report2);
        mTabs.add(report3);
        mTabs.add(report4);
        mTabs.add(report5);
        mTabs.add(report6);

        mAdapter = new FragmentPagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                return mTabs.get(position);
            }

            @Override
            public int getCount() {
                return mTabs.size();
            }
        };

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setOnPageChangeListener(this);
        // 隐藏加载进度条
        hideLoadingDialog();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @OnClick(R.id.header_ll_left)
    void clickBack() {
        onBackPressed();
    }

    @OnClick(R.id.header_ll_right)
    void clickShareReport() {
        llHeaderRight.setEnabled(false);
        llHeaderLeft.setEnabled(false);
        showPopViewShareReport();
        MobclickAgent.onEvent(this, "report_share");
    }

    @OnClick(R.id.fl_next)
    void clickNext() {
        if (pageIndex < mTabs.size() - 1) {
            mViewPager.setCurrentItem(pageIndex + 1);
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        pageIndex = position;
        if (pageIndex == (mTabs.size() - 1)) {
            ivNext.setVisibility(View.INVISIBLE);
        } else {
            ivNext.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onBackPressed() {
        if (popPayDetail != null && popPayDetail.isShowing()) {
            popPayDetail.dismiss();
            return;
        }
        if (popShare != null && popShare.isShowing()) {
            popShare.dismiss();
            return;
        }
        super.onBackPressed();
    }

    private void initPopViewPayEntry() {
        View popView = LayoutInflater.from(this).inflate(R.layout.pop_pay_report_entry, null);
        popPayEntry = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, false);
        popPayEntry.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popPayEntry.setFocusable(false);
        popPayEntry.setOutsideTouchable(false);
        TextView tvPayReport = (TextView) popView.findViewById(R.id.tv_payReport);
        tvPayReport.setOnClickListener(view -> {
            // 显示支付详情按钮
            showPopViewPayDetail();


        });
        FrameLayout flExample = (FrameLayout) popView.findViewById(R.id.fl_example);
        flExample.setOnClickListener(view -> {
            Intent intent = new Intent(StudentReportActivity.this, ReportExampleActivity.class);
            startActivity(intent);
        });
    }

    private void showPopViewPayEntry() {
        if (popPayEntry != null && !popPayEntry.isShowing()) {
            popPayEntry.showAtLocation(flNext, Gravity.BOTTOM, 0, popPayEntry.getHeight());
        }
    }

    private void hidePopViewPayEntry() {
        if (popPayEntry != null && popPayEntry.isShowing()) {
            popPayEntry.dismiss();
        }
    }

    private void initPopViewPayDetail() {
        View popView = LayoutInflater.from(this).inflate(R.layout.pop_pay_report_detail, null);
        popPayDetail = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, false);
        popPayDetail.setAnimationStyle(R.style.MyPopupWindow_anim_style);
        popPayDetail.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popPayDetail.setFocusable(false);
        popPayDetail.setOutsideTouchable(true);
        popPayDetail.setOnDismissListener(() -> setBackgroundAlpha(this, 1f));
        TextView tvRealPay = (TextView) popView.findViewById(R.id.tv_realPay);
        tvRealPay.setText("¥" + ((double) reportRealPay) / 100);
        TextView tvPrice = (TextView) popView.findViewById(R.id.tv_price);
        tvPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tvPrice.setText("¥" + ((double) reportPrice) / 100);
        TextView tvReportProtocol = (TextView) popView.findViewById(R.id.tv_report_protocol);
        tvReportProtocol.setOnClickListener(view -> {
            Intent intent = new Intent(this, ReportProtocolActivity.class);
            startActivity(intent);
        });
        TextView tvPay = (TextView) popView.findViewById(R.id.tv_payReport);
        tvPay.setOnClickListener(view -> {
            // 开始支付,隐藏支付窗口
            hidePopViewPayDetail();
            // 获取订单号
            getTheOrderFromAPI(String.valueOf(reportRealPay));
            MobclickAgent.onEvent(StudentReportActivity.this, "report_pay");
        });
    }

    private void showPopViewPayDetail() {
        if (popPayDetail != null && !popPayDetail.isShowing()) {
            setBackgroundAlpha(this, 0.5f);
            popPayDetail.showAtLocation(flNext, Gravity.BOTTOM, 0, popPayDetail.getHeight());
        }
    }

    private void hidePopViewPayDetail() {
        if (popPayDetail != null && popPayDetail.isShowing()) {
            popPayDetail.dismiss();
        }
    }

    private void initPopViewShare() {
        View popView = LayoutInflater.from(this).inflate(R.layout.pop_share_report, null);
        popShare = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, false);
        popShare.setAnimationStyle(R.style.MyPopupWindow_anim_style);
        popShare.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popShare.setFocusable(false);
        popShare.setOutsideTouchable(true);
        popShare.setOnDismissListener(() -> {
            setBackgroundAlpha(this, 1f);
            new Handler().postDelayed(() -> {
                llHeaderLeft.setEnabled(true);
                llHeaderRight.setEnabled(true);
            }, 100);
        });
        ImageView ivShareSession = (ImageView) popView.findViewById(R.id.iv_shareSession);
        ivShareSession.setOnClickListener(view -> {
            if (report1 != null) {
                shareToSession(report1.getSharePicture());
            } else {
                hidePopViewShareReport();
            }
        });
        ImageView ivShareTimeline = (ImageView) popView.findViewById(R.id.iv_shareTimeline);
        ivShareTimeline.setOnClickListener(view -> {
            if (report1 != null) {
                shareToTimeline(report1.getSharePicture());
            } else {
                hidePopViewShareReport();
            }
        });
    }

    private void showPopViewShareReport() {
        if (popShare != null && !popShare.isShowing()) {
            setBackgroundAlpha(this, 0.5f);
            popShare.showAtLocation(flNext, Gravity.BOTTOM, 0, popShare.getHeight());
        }
    }

    private void hidePopViewShareReport() {
        if (popShare != null && popShare.isShowing()) {
            popShare.dismiss();
        }
    }

    /**
     * 分享到微信
     */
    public void shareToSession(Bitmap shareBmp) {
        if (!isWeixinAvilible(this)) {
            Toast.makeText(this, "抱歉！您还没有安装微信", Toast.LENGTH_SHORT).show();
            return;
        }
        if (popShare.isShowing() && shareBmp != null) {
            popShare.dismiss();

            WXImageObject imgObject = new WXImageObject(shareBmp);
            WXMediaMessage msg = new WXMediaMessage();
            msg.mediaObject = imgObject;
            // 设置缩略图
            if (shareBmp != null && !shareBmp.isRecycled()) {
                Bitmap thumbBmp = Bitmap.createScaledBitmap(shareBmp, shareBmp.getWidth() / 10,
                        shareBmp.getHeight() / 10, true);
                // msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
                msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
                shareBmp.recycle();
            }
            // 构造一个Req
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            // transaction 字段用于唯一标示一个请求
            req.transaction = buildTransaction("img");
            req.message = msg;
            req.scene = SendMessageToWX.Req.WXSceneSession;

            // 调用api接口发送数据到微信
            api.sendReq(req);
        }
    }

    /**
     * 分享到朋友圈
     */
    public void shareToTimeline(Bitmap shareBmp) {
        if (!isWeixinAvilible(this)) {
            Toast.makeText(this, "抱歉！您还没有安装微信", Toast.LENGTH_SHORT).show();
            return;
        }
        if (popShare.isShowing() && shareBmp != null) {
            popShare.dismiss();

            WXImageObject imgObject = new WXImageObject(shareBmp);
            WXMediaMessage msg = new WXMediaMessage();
            msg.mediaObject = imgObject;
            // 设置缩略图
            if (shareBmp != null && !shareBmp.isRecycled()) {
                Bitmap thumbBmp = Bitmap.createScaledBitmap(shareBmp, 150,
                        shareBmp.getHeight() * 150 / shareBmp.getWidth(), true);
                msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
                shareBmp.recycle();
            }
            // 构造一个Req
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            // transaction 字段用于唯一标示一个请求
            req.transaction = buildTransaction("img");
            req.message = msg;
            req.scene = SendMessageToWX.Req.WXSceneTimeline;

            // 调用api接口发送数据到微信
            api.sendReq(req);
        }
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    /***
     * 检查是否安装了微信
     * @param context
     * @return
     */
    private boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 生成订单
     */
    private void getTheOrderFromAPI(String price) {
        Observable.create((ObservableOnSubscribe<OrderInfo>) emitter -> {
            LmsDataService mService = new LmsDataService();
            OrderInfo info = mService.getTheOrderInfoFromAPI(price, userPhoneNumber, "baogao");
            emitter.onNext(info);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OrderInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        showLoadingDialog();
                    }

                    @Override
                    public void onNext(OrderInfo orderInfo) {
                        // 获取订单成功后 发起微信支付
                        if ("SUCCESS".equals(orderInfo.getResult_code())
                                && "SUCCESS".equals(orderInfo.getReturn_code())
                                && "OK".equals(orderInfo.getReturn_msg())) {
                            startPay(orderInfo);
                        } else {
                            // 获取订单失败
                            T.showShort(StudentReportActivity.this, "订单生成失败");
                        }
                        hideLoadingDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideLoadingDialog();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 开始支付
     *
     * @param orderInfo
     */
    private void startPay(OrderInfo orderInfo) {
        if (api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT) {
            currTradeID = orderInfo.getOut_trade_no();  // 获取当前商户订单号
            PayReq request = new PayReq();
            request.appId = orderInfo.getAppid();
            request.partnerId = orderInfo.getMch_id();   // 商户号
            request.prepayId = orderInfo.getPrepay_id();
            request.packageValue = "Sign=WXPay";
            request.nonceStr = orderInfo.getNonce_str();
            request.timeStamp = orderInfo.getTimeStamp();
            request.extData = "app data";
            request.sign = orderInfo.getSign();
            api.sendReq(request);
        } else {
            T.showShort(this, "当前手机暂不支持该功能");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if (!StringUtils.isEmpty(currTradeID)) {
            queryTheTradeStateFromAPI(currTradeID);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /**
     * 查询订单支付状态
     *
     * @param tradeID
     */
    private void queryTheTradeStateFromAPI(String tradeID) {
        Observable.create((ObservableOnSubscribe<TradeInfo>) emitter -> {
            LmsDataService mService = new LmsDataService();
            TradeInfo info = mService.getTheTradeInfoFromAPI(tradeID);
            emitter.onNext(info);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TradeInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(TradeInfo tradeInfo) {
                        currTradeID = "";
                        if ("SUCCESS".equals(tradeInfo.getResult_code())) {
                            if ("SUCCESS".equals(tradeInfo.getTrade_state())) {
                                showAlertDialog("支付成功", "您现在就可以去查看完整报告啦");
                                showNextPage();
                            } else if ("NOTPAY".equals(tradeInfo.getTrade_state())) {
                                showAlertDialog("支付失败", "支付遇到问题，请重试");
                            }
                        } else {
                            showAlertDialog("支付失败", "支付遇到问题，请重试");
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

    // 现在以后的几页内容
    private void showNextPage() {
        hidePopViewPayEntry();
        ivNext.setVisibility(View.VISIBLE);
        flNext.setEnabled(true);
        mViewPager.setPageScrollEnable(true);   // 可以滑动
        mAdapter.notifyDataSetChanged();
    }

    private void showAlertDialog(String title, String content) {
        TipsAlertDialog3 dialog = new TipsAlertDialog3(this);
        dialog.setTitleText(title);
        dialog.setContentText(content);
        dialog.setConfirmText("好的");
        dialog.setConfirmClickListener(sweetAlertDialog -> {
            sweetAlertDialog.dismiss();
        });
        dialog.show();
    }
}

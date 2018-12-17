package com.bj.hmxxparents.fragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bj.hmxxparents.BaseFragment;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.api.MLProperties;
import com.bj.hmxxparents.entity.ReportInfo;
import com.bj.hmxxparents.utils.LL;
import com.bj.hmxxparents.utils.PreferencesUtils;
import com.bj.hmxxparents.utils.StringUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.progressbar.BGAProgressBar;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zz379 on 2017/ic_pinde/3.
 * 学生本学期表现报告页面--第一页
 */

public class StudentReportFragment1 extends BaseFragment {
    @BindView(R.id.iv_kidPhoto)
    SimpleDraweeView ivKidPhoto;
    @BindView(R.id.tv_badgeRank)
    TextView tvBadgeRank;
    @BindView(R.id.pb_myBadgeNumber)
    BGAProgressBar pbMyBadgeNum;
    @BindView(R.id.pb_classBadgeNumber)
    BGAProgressBar pbClassBadgeNum;
    @BindView(R.id.tv_commendRank)
    TextView tvCommendRank;
    @BindView(R.id.pb_myCommendNumber)
    BGAProgressBar pbMyCommendNum;
    @BindView(R.id.pb_classCommendNumber)
    BGAProgressBar pbClassCommendNum;
    @BindView(R.id.ll_shareView)
    LinearLayout llShareView;
    @BindView(R.id.pb_example)
    BGAProgressBar pbExample;

    private String mBadgeRank, mCommendRank;
    private int mBadgeNum, mCommendNum;
    private double classBadgeNum, classCommendNum;
    private String kidPhotoPath;

    private boolean isProgressAnimation = true; // 页面第一次初始化的时候必定需要加载动画,把字段置为True;
    private Disposable d;

    public StudentReportFragment1() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_1, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);

        initView();
        initData();
        return view;
    }

    private void initView() {
    }

    private void initData() {
        kidPhotoPath = PreferencesUtils.getString(getActivity(), MLProperties.BUNDLE_KEY_KID_IMG, "");
        ivKidPhoto.setImageURI(Uri.parse(kidPhotoPath));
    }

    @Override
    protected void onVisible() {
        super.onVisible();
        MobclickAgent.onPageStart("report1");
        if (!isFirstInit && !isProgressAnimation) {
            startAnimation();
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        if (d != null && !d.isDisposed()) {
            d.dispose();
            d = null;
        }
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(ReportInfo event) {
        LL.i("StudentReportFragment1 - 收到消息 : 学生徽章数" + event.userBadgeCount);
        mBadgeRank = event.userBadgeRank;
        mCommendRank = event.userCommendRank;

        mBadgeNum = Integer.parseInt((StringUtils.isEmpty(event.userBadgeCount)) ?
                "0" : event.userBadgeCount);
        mCommendNum = Integer.parseInt((StringUtils.isEmpty(event.userCommendCount)) ?
                "0" : event.userCommendCount);

        classBadgeNum = new BigDecimal((StringUtils.isEmpty(event.classBadgeAvg)) ?
                "0" : event.classBadgeAvg)
                .setScale(2, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
        classCommendNum = new BigDecimal((StringUtils.isEmpty(event.classCommendAvg)) ?
                "0" : event.classCommendAvg)
                .setScale(2, BigDecimal.ROUND_HALF_UP)
                .doubleValue();

        updateView();
    }

    private void updateView() {
        tvBadgeRank.setText(mBadgeRank);
        tvCommendRank.setText(mCommendRank);

        pbExample.setMax(100);

        int maxBadgeProgress = (int) ((Math.max(mBadgeNum * 10000, classBadgeNum * 10000)) * 1.2f);
        if (maxBadgeProgress == 0) maxBadgeProgress = 1;
        pbMyBadgeNum.setmText(String.valueOf(mBadgeNum));
        pbMyBadgeNum.setMax(maxBadgeProgress);
        // pbMyBadgeNum.setProgress(mBadgeNum * 10000);

        if (classBadgeNum == 0) {
            pbClassBadgeNum.setmText("0");
        } else {
            pbClassBadgeNum.setmText(BigDecimal.valueOf(classBadgeNum).stripTrailingZeros().toPlainString());
        }
        pbClassBadgeNum.setMax(maxBadgeProgress);
        // pbClassBadgeNum.setProgress((int) (classBadgeNum * 10000));

        int maxCommendProgress = (int) ((Math.max(mCommendNum * 10000, classCommendNum * 10000)) * 1.2f);
        if (maxCommendProgress == 0) maxCommendProgress = 1;
        pbMyCommendNum.setmText(String.valueOf(mCommendNum));
        pbMyCommendNum.setMax(maxCommendProgress);
        // pbMyCommendNum.setProgress(mCommendNum * 10000);

        if (classCommendNum == 0) {
            pbClassCommendNum.setmText("0");
        } else {
            pbClassCommendNum.setmText(BigDecimal.valueOf(classCommendNum).stripTrailingZeros().toPlainString());
        }
        pbClassCommendNum.setMax(maxCommendProgress);
        // pbClassCommendNum.setProgress((int) (classCommendNum * 10000));
        pbClassCommendNum.post(() -> startAnimation());
    }

    /**
     * 页面动画
     * BGAProgressBar pb, int progress, long timemillis
     */
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
                        LL.i("Animation: " + aInt);
                        pbExample.setProgress(aInt);
                        pbMyBadgeNum.setProgress(mBadgeNum * 100 * aInt);
                        pbClassBadgeNum.setProgress((int) (classBadgeNum * 100 * aInt));
                        pbMyCommendNum.setProgress(mCommendNum * 100 * aInt);
                        pbClassCommendNum.setProgress((int) (classCommendNum * 100 * aInt));
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

    public Bitmap getSharePicture() {
        Bitmap bitmap1 = getBitmapByView(llShareView);
        Bitmap bitmap2 = getFooterView();
        Bitmap bitmap3 = mergeBitmap(bitmap1, bitmap2);
        bitmap1.recycle();
        bitmap2.recycle();
        return bitmap3;
    }

    private Bitmap getFooterView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.share_footer_view, null);
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;   // 屏幕高度（像素）

        layoutView(view, width, height);//去到指定view大小的函数
        return getBitmapByView(view);
    }

    private void layoutView(View v, int width, int height) {
        // 指定整个View的大小 参数是左上角 和右下角的坐标
        v.layout(0, 0, width, height);
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST);
        /** 当然，measure完后，并不会实际改变View的尺寸，需要调用View.layout方法去进行布局。
         * 按示例调用layout函数后，View的大小将会变成你想要设置成的大小。
         */
        v.measure(measuredWidth, measuredHeight);
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
    }

    private Bitmap getBitmapByView(View view) {
        view.setBackgroundColor(Color.parseColor("#4aa003"));
        // 创建对应大小的bitmap
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private Bitmap mergeBitmap(Bitmap firstBitmap, Bitmap secondBitmap) {
        Bitmap bitmap = Bitmap.createBitmap(firstBitmap.getWidth(), firstBitmap.getHeight() + secondBitmap.getHeight(),
                firstBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(firstBitmap, 0, 0, null);
        canvas.drawBitmap(secondBitmap, 0, firstBitmap.getHeight(), null);
        return bitmap;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onInVisible() {
        super.onInVisible();
        MobclickAgent.onPageEnd("report1");
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}

package com.bj.hmxxparents.report.study;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bj.hmxxparents.BaseActivity;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.api.MLProperties;
import com.bj.hmxxparents.entity.MessageEvent;
import com.bj.hmxxparents.report.study.fragment.ReportFragment1;
import com.bj.hmxxparents.report.study.fragment.ReportFragment2;
import com.bj.hmxxparents.report.study.fragment.ReportFragment3;
import com.bj.hmxxparents.report.study.fragment.ReportFragment4;
import com.bj.hmxxparents.utils.PreferencesUtils;
import com.bj.hmxxparents.utils.ScreenUtils;
import com.bj.hmxxparents.widget.VerticalViewPager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ReportStudyActivity extends BaseActivity {

    @BindView(R.id.mViewPager)
    VerticalViewPager mViewPager;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_next)
    ImageView ivNext;
    @BindView(R.id.fl_next)
    FrameLayout flNext;
    @BindView(R.id.rl_root)
    RelativeLayout rlRoot;
    @BindView(R.id.bt_share)
    ImageView btShare;
    private Unbinder unbinder;

    private FragmentManager fragmentManager;
    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private ReportFragment1 reportFragment1;
    private ReportFragment2 reportFragment2;
    private ReportFragment3 reportFragment3;
    private ReportFragment4 reportFragment4;

    private FragmentPagerAdapter adapter;

    int pageIndex = 0;
    private PopupWindow popShare;
    private String studentName;
    private String yingyustatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenUtils.setFullScreen(this);
        setContentView(R.layout.activity_report);
        unbinder = ButterKnife.bind(this);
        studentName = PreferencesUtils.getString(this, MLProperties.BUNDLE_KEY_KID_NAME);
        yingyustatus = PreferencesUtils.getString(this, "xuexibaogao_yingyustatus");
        initTitle();
        initViews();
        initPopViewShare();
    }

    private void initTitle() {
        tvTitle.setText(studentName + "学习报告");
        btShare.setVisibility(View.GONE);
    }

    private void initViews() {
        reportFragment1 = new ReportFragment1();
        reportFragment2 = new ReportFragment2();
        reportFragment3 = new ReportFragment3();
        reportFragment4 = new ReportFragment4();

        fragmentList.add(reportFragment1);
        fragmentList.add(reportFragment2);
        fragmentList.add(reportFragment3);

        if (yingyustatus.equals("1")) {
            fragmentList.add(reportFragment4);
        } else {

        }


        fragmentManager = getSupportFragmentManager();
        ivNext.setVisibility(View.VISIBLE);
        flNext.setEnabled(true);
        mViewPager.setPageScrollEnable(true);

        adapter = new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        };

        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pageIndex = position;
                if (pageIndex == (fragmentList.size() - 1)) {
                    ivNext.setVisibility(View.INVISIBLE);
                } else {
                    ivNext.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick({R.id.bt_share, R.id.bt_back, R.id.fl_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                finish();
                break;
            case R.id.bt_share:
                showPopViewShareReport();
                break;
            case R.id.fl_next:
                if (pageIndex < fragmentList.size() - 1) {
                    mViewPager.setCurrentItem(pageIndex + 1, false);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (popShare != null && popShare.isShowing()) {
            popShare.dismiss();
            return;
        }
        super.onBackPressed();
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
                //llHeaderLeft.setEnabled(true);
                //llHeaderRight.setEnabled(true);
            }, 100);
        });
        ImageView ivShareSession = (ImageView) popView.findViewById(R.id.iv_shareSession);
        ivShareSession.setOnClickListener(view -> {

            EventBus.getDefault().post(new MessageEvent("reportStudyShare", "weixin", pageIndex));
            if (popShare != null && popShare.isShowing()) {
                popShare.dismiss();
                return;
            }

        });
        ImageView ivShareTimeline = (ImageView) popView.findViewById(R.id.iv_shareTimeline);
        ivShareTimeline.setOnClickListener(view -> {
            EventBus.getDefault().post(new MessageEvent("reportStudyShare", "pengyouquan", pageIndex));
            if (popShare != null && popShare.isShowing()) {
                popShare.dismiss();
                return;
            }
        });
    }

    private void showPopViewShareReport() {
        if (popShare != null && !popShare.isShowing()) {
            setBackgroundAlpha(this, 0.5f);
            popShare.showAtLocation(rlRoot, Gravity.BOTTOM, 0, popShare.getHeight());
        }
    }

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

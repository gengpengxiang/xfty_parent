package com.bj.hmxxparents.report.term;

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
import com.bj.hmxxparents.report.term.fragment.ReportFragment2;
import com.bj.hmxxparents.report.term.fragment.ReportFragment22;
import com.bj.hmxxparents.report.term.fragment.ReportFragment3;
import com.bj.hmxxparents.report.term.fragment.ReportFragment4;
import com.bj.hmxxparents.report.term.fragment.ReportFragment5;
import com.bj.hmxxparents.report.term.fragment.ReportFragment6;
import com.bj.hmxxparents.report.term.fragment.ReportFragment7;
import com.bj.hmxxparents.report.term.fragment.ReportFragment8;
import com.bj.hmxxparents.report.term.fragment.ReportFragment1;
import com.bj.hmxxparents.utils.PreferencesUtils;
import com.bj.hmxxparents.utils.ScreenUtils;
import com.bj.hmxxparents.widget.VerticalViewPager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ReportTermActivity extends BaseActivity {


    @BindView(R.id.mViewPager)
    VerticalViewPager mViewPager;
    @BindView(R.id.iv_next)
    ImageView ivNext;
    @BindView(R.id.fl_next)
    FrameLayout flNext;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_root)
    RelativeLayout rlRoot;

    private Unbinder unbinder;

    private FragmentManager fragmentManager;
    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private ReportFragment2 reportFragment2;
    private ReportFragment22 reportFragment22;
    private ReportFragment3 reportFragment3;
    private ReportFragment4 reportFragment4;
    private ReportFragment5 reportFragment5;
    private ReportFragment6 reportFragment6;
    private ReportFragment7 reportFragment7;
    private ReportFragment8 reportFragment8;
    private ReportFragment1 reportFragment1;
    private FragmentPagerAdapter adapter;

    int pageIndex = 0;
    private PopupWindow popShare;
    private String studentName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenUtils.setFullScreen(this);
        setContentView(R.layout.activity_report);
        unbinder = ButterKnife.bind(this);
        studentName = PreferencesUtils.getString(this, MLProperties.BUNDLE_KEY_KID_NAME);
        initTitle();
        initViews();
        initPopViewShare();
    }

    private void initViews() {
        reportFragment1 = new ReportFragment1();
        reportFragment2 = new ReportFragment2();
        reportFragment3 = new ReportFragment3();
        reportFragment4 = new ReportFragment4();
        reportFragment5 = new ReportFragment5();
        reportFragment6 = new ReportFragment6();
        reportFragment7 = new ReportFragment7();
        reportFragment8 = new ReportFragment8();

        reportFragment22 = new ReportFragment22();


        fragmentList.add(reportFragment2);

        fragmentList.add(reportFragment22);

        fragmentList.add(reportFragment3);
        fragmentList.add(reportFragment4);
        fragmentList.add(reportFragment5);
        fragmentList.add(reportFragment6);
        fragmentList.add(reportFragment7);
        fragmentList.add(reportFragment8);

        fragmentList.add(reportFragment1);

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
        mViewPager.setOffscreenPageLimit(7);
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

    private void initTitle() {
        tvTitle.setText(studentName+"本学期表现报告");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick({R.id.fl_next, R.id.bt_back, R.id.bt_share})
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
                    mViewPager.setCurrentItem(pageIndex + 1,false);
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

            EventBus.getDefault().post(new MessageEvent("reportTermShare", "weixin",pageIndex));
            if (popShare != null && popShare.isShowing()) {
                popShare.dismiss();
                return;
            }
        });
        ImageView ivShareTimeline = (ImageView) popView.findViewById(R.id.iv_shareTimeline);
        ivShareTimeline.setOnClickListener(view -> {
            EventBus.getDefault().post(new MessageEvent("reportTermShare", "pengyouquan",pageIndex));
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

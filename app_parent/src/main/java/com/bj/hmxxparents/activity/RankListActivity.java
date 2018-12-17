package com.bj.hmxxparents.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bj.hmxxparents.BaseActivity;
import com.bj.hmxxparents.fragment.SchoolRankFragment;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.adapter.SimpleFragmentPagerAdapter2;
import com.bj.hmxxparents.api.LmsDataService;
import com.bj.hmxxparents.api.MLProperties;
import com.bj.hmxxparents.entity.BadgeType;
import com.bj.hmxxparents.utils.LL;
import com.bj.hmxxparents.utils.PreferencesUtils;
import com.bj.hmxxparents.utils.T;
import com.bj.hmxxparents.widget.CustomViewPager;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zz379 on 2017/2/17.
 * 全校活力榜页面
 */

public class RankListActivity extends BaseActivity {

    @BindView(R.id.header_img_back)
    ImageView imgBack;
    @BindView(R.id.header_tv_title)
    TextView tvTitle;
    @BindView(R.id.header_img_question)
    ImageView imgQuestion;
    @BindView(R.id.mTabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.mViewPager)
    CustomViewPager mViewPager;

    private List<BadgeType> mTabDataList = new ArrayList<>();
    private List<Fragment> mTabs = new ArrayList<>();

    private String schoolID;
    private int minTabSize;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank_list);
        ButterKnife.bind(this);

        DisplayMetrics display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);
        minTabSize = (display.widthPixels / (display.densityDpi / 160)) / 70;
        LL.i("页面Tab开始滑动的阀值：" + minTabSize);

        // 初始化页面
        initToolBar();
        initView();
        initDatas();
    }

    private void initToolBar() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("全校活力榜");
        imgBack.setVisibility(View.VISIBLE);
        imgQuestion.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.header_ll_left)
    void actionBackClick() {
        if (MainActivity.getInstance() == null) {
            Intent intent = new Intent(RankListActivity.this, MainActivity.class);
            intent.putExtra(MLProperties.BUNDLE_KEY_MAIN_PAGEINDEX, 0);
            startActivity(intent);
            this.finish();
            overridePendingTransition(R.anim.left_right_in, R.anim.left_right_out);
        } else {
            this.finish();
            overridePendingTransition(R.anim.left_right_in, R.anim.left_right_out);
        }
    }

    @Override
    public void onBackPressed() {
        actionBackClick();
    }

    @OnClick(R.id.header_ll_right)
    void actionTipsClick() {
        showTipsDialog(getString(R.string.tips_rank_list));
    }


    private void initView() {
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                ((TextView) tab.getCustomView().findViewById(R.id.tv_name)).setTextColor(ContextCompat.getColor(RankListActivity.this, R.color.text_tab_selected));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ((TextView) tab.getCustomView().findViewById(R.id.tv_name)).setTextColor(ContextCompat.getColor(RankListActivity.this, R.color.text_tab_unselected_2));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // 设置分割线
//        LinearLayout linearLayout = (LinearLayout) mTabLayout.getChildAt(0);
//        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
//        linearLayout.setDividerDrawable(ContextCompat.getDrawable(this, R.drawable.divider_tablayout_badge));
//        linearLayout.setDividerPadding(DensityUtils.dp2px(this, 10));
    }

    private void initDatas() {
        schoolID = PreferencesUtils.getString(RankListActivity.this, MLProperties.BUNDLE_KEY_SCHOOL_CODE);

        MyClassCommendTypeAsyncTask task = new MyClassCommendTypeAsyncTask();
        task.execute();
    }

    private class MyClassCommendTypeAsyncTask extends AsyncTask<String, Integer, List<BadgeType>> {

        @Override
        protected List<BadgeType> doInBackground(String... params) {
            LmsDataService mService = new LmsDataService();
            List<BadgeType> newList;
            try {
                newList = mService.getSchoolRankListTypesFromAPI(schoolID);
            } catch (Exception e) {
                e.printStackTrace();
                newList = new ArrayList<>();
            }
            return newList;
        }

        @Override
        protected void onPostExecute(List<BadgeType> badgeTypeList) {
            if (badgeTypeList == null) {
                T.showShort(RankListActivity.this, "服务器开小差了，请待会重试");
            } else if (badgeTypeList.size() == 0) {
                T.showShort(RankListActivity.this, "数据异常");
            } else {
                updateCommendType(badgeTypeList);
            }
        }
    }

    private void updateCommendType(List<BadgeType> subjectInfos) {
        mTabDataList.clear();
        mTabs.clear();
        mTabDataList.addAll(subjectInfos);

        for (BadgeType item : mTabDataList) {
            View mTabView = LayoutInflater.from(this).inflate(R.layout.custom_tab_badge_2, null);
            TextView tvName = (TextView) mTabView.findViewById(R.id.tv_name);
            tvName.setText(item.getName());
            mTabLayout.addTab(mTabLayout.newTab().setCustomView(mTabView));
            // 创建视图
            SchoolRankFragment fragment = SchoolRankFragment.newInstance(item.getBadgeTypeID(), schoolID);
            mTabs.add(fragment);
        }

        if (mTabDataList.size() <= minTabSize) {
            mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        } else {
            mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }

        SimpleFragmentPagerAdapter2 fragmentAdapter = new SimpleFragmentPagerAdapter2(getSupportFragmentManager(),
                mTabs);
        mViewPager.setAdapter(fragmentAdapter);
        mViewPager.setOffscreenPageLimit(mTabDataList.size() - 1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}

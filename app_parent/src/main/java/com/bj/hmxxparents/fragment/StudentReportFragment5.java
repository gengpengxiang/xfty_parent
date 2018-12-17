package com.bj.hmxxparents.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bj.hmxxparents.BaseFragment;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.entity.ReportInfo;
import com.bj.hmxxparents.utils.LL;
import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * Created by zz379 on 2017/ic_pinde/3.
 * 学生本学期表现报告页面--第五页
 */

public class StudentReportFragment5 extends BaseFragment {

    private List<ReportInfo.User> commendRankList;
    private List<ReportInfo.User> badgeRankList;
    @BindViews({R.id.ll_badge_1, R.id.ll_badge_2, R.id.ll_badge_3})
    LinearLayout[] llBadges;
    @BindViews({R.id.ll_commend_1, R.id.ll_commend_2, R.id.ll_commend_3})
    LinearLayout[] llCommends;
    @BindViews({R.id.iv_badge_1, R.id.iv_badge_2, R.id.iv_badge_3})
    SimpleDraweeView[] ivBadgePhotos;
    @BindViews({R.id.iv_commend_1, R.id.iv_commend_2, R.id.iv_commend_3})
    SimpleDraweeView[] ivCommendPhotos;
    @BindViews({R.id.tv_badge_name_1, R.id.tv_badge_name_2, R.id.tv_badge_name_3})
    TextView[] tvBadgeNames;
    @BindViews({R.id.tv_badge_num_1, R.id.tv_badge_num_2, R.id.tv_badge_num_3})
    TextView[] tvBadgeNums;
    @BindViews({R.id.tv_commend_name_1, R.id.tv_commend_name_2, R.id.tv_commend_name_3})
    TextView[] tvCommendNames;
    @BindViews({R.id.tv_commend_num_1, R.id.tv_commend_num_2, R.id.tv_commend_num_3})
    TextView[] tvCommendNums;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_5, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        initView();
        initData();
        return view;
    }

    private void initView() {

    }

    private void initData() {

    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(ReportInfo event) {
        LL.i("StudentReportFragment5 - 收到消息 : 学生徽章数" + event.userBadgeCount);
        badgeRankList = event.badgeRankList;
        commendRankList = event.commendRankList;

        updateBadgeData(this.badgeRankList);
        updateCommendData(this.commendRankList);
    }

    private void updateBadgeData(List<ReportInfo.User> list) {
        if (list == null) return;
        for (int i = 0; i < list.size(); i++) {
            llBadges[i].setVisibility(View.VISIBLE);
            ivBadgePhotos[i].setImageURI(Uri.parse(list.get(i).photoPath));
            tvBadgeNames[i].setText(list.get(i).username);
            tvBadgeNums[i].setText(list.get(i).count);
        }
    }

    private void updateCommendData(List<ReportInfo.User> list) {
        if (list == null) return;
        for (int i = 0; i < list.size(); i++) {
            llCommends[i].setVisibility(View.VISIBLE);
            ivCommendPhotos[i].setImageURI(Uri.parse(list.get(i).photoPath));
            tvCommendNames[i].setText(list.get(i).username);
            tvCommendNums[i].setText(list.get(i).count);
        }
    }

    @Override
    protected void onVisible() {
        super.onVisible();
        MobclickAgent.onPageStart("report5");
    }

    @Override
    protected void onInVisible() {
        super.onInVisible();
        MobclickAgent.onPageEnd("report5");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}

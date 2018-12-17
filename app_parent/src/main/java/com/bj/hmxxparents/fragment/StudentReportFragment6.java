package com.bj.hmxxparents.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bj.hmxxparents.BaseFragment;
import com.bj.hmxxparents.R;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;

/**
 * Created by zz379 on 2017/ic_pinde/3.
 * 学生本学期表现报告页面--第六页
 */

public class StudentReportFragment6 extends BaseFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_6, container, false);
        ButterKnife.bind(this, view);

        initView();
        initData();
        return view;
    }

    private void initView() {

    }

    private void initData() {

    }

    @Override
    protected void onVisible() {
        super.onVisible();
        MobclickAgent.onPageStart("report6");
    }

    @Override
    protected void onInVisible() {
        super.onInVisible();
        MobclickAgent.onPageEnd("report6");
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

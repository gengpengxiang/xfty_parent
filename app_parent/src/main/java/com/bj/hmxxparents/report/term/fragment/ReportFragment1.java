package com.bj.hmxxparents.report.term.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bj.hmxxparents.BaseFragment;
import com.bj.hmxxparents.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by zz379 on 2017/3/29.
 * 学期报告首页
 */

public class ReportFragment1 extends BaseFragment {


    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_last, container, false);
        unbinder = ButterKnife.bind(this, view);

        initView();

        return view;
    }

    private void initView() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}

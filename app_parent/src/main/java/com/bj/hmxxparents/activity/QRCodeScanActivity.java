package com.bj.hmxxparents.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bj.hmxxparents.BaseActivity;
import com.bj.hmxxparents.fragment.QRCodeScanFragment1;
import com.bj.hmxxparents.fragment.QRCodeScanFragment2;
import com.bj.hmxxparents.R;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by zz379 on 2017/4/17.
 * 二维码扫描页面，共有两个二维码识别fragment
 * 第一个Fragment使用zbar库进行识别，当识别错误的时候，切换到第二个Fragment
 * 第二个Fragment使用zxing库进行识别
 * 这样可以提高二维码的识别正确率
 */

public class QRCodeScanActivity extends BaseActivity {

    private FragmentManager fm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scan);

        initToolBar();
        initView();
    }

    private void initToolBar() {
        LinearLayout llLeft = (LinearLayout) this.findViewById(R.id.header_ll_left);
        llLeft.setOnClickListener(v -> QRCodeScanActivity.this.finish());
        TextView tvTitle = (TextView) this.findViewById(R.id.header_tv_title);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.title_qrcode_scan);
        ImageView imgBack = (ImageView) this.findViewById(R.id.header_img_back);
        imgBack.setVisibility(View.VISIBLE);
    }

    private void initView() {
        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = new QRCodeScanFragment1();
        ft.add(R.id.contentView, fragment, "qrcode_1");
        ft.commit();
    }

    /**
     * 切换二维码识别库
     */
    public void switchLib() {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = new QRCodeScanFragment2();
        ft.replace(R.id.contentView, fragment, "qrcode_2");
        ft.commit();
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

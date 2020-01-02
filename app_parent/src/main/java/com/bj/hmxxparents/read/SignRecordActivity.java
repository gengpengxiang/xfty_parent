package com.bj.hmxxparents.read;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bj.hmxxparents.BaseActivity;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.api.MLProperties;
import com.bj.hmxxparents.read.adapter.SignRecordAdapter;
import com.bj.hmxxparents.read.calendar.ZWCalendarView;
import com.bj.hmxxparents.read.model.SignRecord;
import com.bj.hmxxparents.read.presenter.SignRecordPresenter;
import com.bj.hmxxparents.read.view.IViewSignRecord;
import com.bj.hmxxparents.utils.PreferencesUtils;
import com.bj.hmxxparents.utils.ScreenUtils;
import com.bj.hmxxparents.widget.SpacesItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.jessyan.autosize.internal.CustomAdapt;

public class SignRecordActivity extends BaseActivity implements IViewSignRecord, CustomAdapt {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mSmartRefreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.calendarView)
    ZWCalendarView calendarView;
    @BindView(R.id.tv_calendar_title)
    TextView tvCalendarTitle;
    @BindView(R.id.blackView)
    View blackView;
    private Unbinder unbinder;

    private SignRecordAdapter adapter;

    private List<SignRecord.DataBean.QiandaoDataBean> recordList = new ArrayList<>();
    private List<String> daysList = new ArrayList<>();
    private List<String> daysList2 = new ArrayList<>();
    HashMap<String, Boolean> sign = new HashMap<>();
    private View headerView;
//    private TextView tvMonth;
    private String student_code;
    private SignRecordPresenter presenter;
    private String titleMonth, paramMonth;
    private SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ScreenUtils.setFullScreen(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏
        setContentView(R.layout.activity_sign_record);
        unbinder = ButterKnife.bind(this);
        student_code = PreferencesUtils.getString(this, MLProperties.BUNDLE_KEY_KID_ID);

        presenter = new SignRecordPresenter(this, this);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        paramMonth = simpleDateFormat.format(date);


        initViews();

        //tvMonth.setText(myMonth);


        //presenter.getSignRecord(student_code, month);
    }

    private void initViews() {

        headerView = getLayoutInflater().inflate(R.layout.recycler_header_sign_record, null);
//        tvMonth = (TextView) headerView.findViewById(R.id.tv_month);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(1));


        adapter = new SignRecordAdapter(R.layout.recycler_item_sign_record, recordList);
//        adapter.addHeaderView(headerView);
//        adapter.setHeaderAndEmpty(true);
//        adapter.setEmptyView(R.layout.recycler_item_tianyuan_empty,mRecyclerView);
        mRecyclerView.setAdapter(adapter);

        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                presenter.getSignRecord(student_code, paramMonth);
            }
        });
        mSmartRefreshLayout.setEnableLoadmore(false);

        calendarView.setSelectListener(new ZWCalendarView.SelectListener() {
            @Override
            public void change(int year, int month) {
                tvCalendarTitle.setText(String.format("%s 年 %s 月", year, month));

                paramMonth = year + "-" + String.format("%02d", month);
                Log.e("月份", paramMonth);

                titleMonth = String.format("%s 年 %s 月", year, month);
                //tvMonth.setText(titleMonth);

                presenter.getSignRecord(student_code, paramMonth);

            }

            @Override
            public void select(int year, int month, int day, int week) {
//                Toast.makeText(getApplicationContext(),
//                        String.format("%s 年 %s 月 %s日，周%s", year, month, day, week),
//                        Toast.LENGTH_SHORT).show();
//
//                Log.e("点击的月份", year + "-" + month);
//                Log.e("点击的日期", day + "");


                for (int i = 0; i < recordList.size(); i++) {
                    if (daysList2.get(i).equals(String.valueOf(day))) {
                        Log.e("i===", i + "");

                        mRecyclerView.smoothScrollToPosition(i);
                    }
                }


            }
        });

//        calendarView.setSignRecords(sign);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // 当新设置中，屏幕布局模式为横排时
        if (newConfig.orientation == 1) {
            blackView.setVisibility(View.VISIBLE);
        }
        if (newConfig.orientation == 2) {
            blackView.setVisibility(View.GONE);
        }
        super.onConfigurationChanged(newConfig);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void getSignRecord(String result) {
        SignRecord signRecord = JSON.parseObject(result, new TypeReference<SignRecord>() {
        });
        adapter.setEmptyView(R.layout.recycler_item_tianyuan_empty, mRecyclerView);
        if (signRecord.getRet().equals("1")) {
            if (mSmartRefreshLayout.isRefreshing()) {
                mSmartRefreshLayout.finishRefresh();
            }
            recordList.clear();
            recordList.addAll(signRecord.getData().getQiandao_data());
            adapter.notifyDataSetChanged();

            daysList.clear();
            daysList.addAll(signRecord.getData().getQiandao_date2());

            daysList2.clear();
            daysList2.addAll(signRecord.getData().getQiandao_date());

            int num = signRecord.getData().getQiandao_data().size();
            for (int i = 0; i < num; i++) {
                sign.put(daysList.get(i), true);
            }
            calendarView.setSignRecords(sign);
        }
    }

    @OnClick({R.id.bt_calendar_last, R.id.bt_calendar_next, R.id.bt_close})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_calendar_last:
                calendarView.showPreviousMonth();
                break;
            case R.id.bt_calendar_next:
                calendarView.showNextMonth();
                break;
            case R.id.bt_close:
                finish();
                break;
        }
    }

//    class TopSmoothScroller extends LinearSmoothScroller {
//        TopSmoothScroller(Context context) {
//            super(context);
//        }
//
//        @Override
//        protected int getHorizontalSnapPreference() {
//            return SNAP_TO_START;//具体见源码注释
//        }
//
//        @Override
//        protected int getVerticalSnapPreference() {
//            return SNAP_TO_START;//具体见源码注释
//        }
//    }

    @Override
    public boolean isBaseOnWidth() {
        return true;
    }

    @Override
    public float getSizeInDp() {
        return 640;
    }
}

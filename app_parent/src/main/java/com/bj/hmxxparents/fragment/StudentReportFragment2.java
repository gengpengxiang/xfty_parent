package com.bj.hmxxparents.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bj.hmxxparents.BaseFragment;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.adapter.ChartLegendAdapter;
import com.bj.hmxxparents.entity.ChartItemInfo;
import com.bj.hmxxparents.entity.ReportInfo;
import com.bj.hmxxparents.utils.DensityUtils;
import com.bj.hmxxparents.utils.LL;
import com.bj.hmxxparents.widget.SpacesItemDecoration;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zz379 on 2017/ic_pinde/3.
 * 学生本学期表现报告页面--第二页
 */

public class StudentReportFragment2 extends BaseFragment {

    @BindView(R.id.tv_badgeNumber)
    TextView tvBadgeNumber;
    @BindView(R.id.pie_chart_badge)
    PieChart mChart;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    private List<Integer> colors = new ArrayList<>();
    private List<ChartItemInfo> mDataList = new ArrayList<>();
    private ChartLegendAdapter mAdapter;

    public static final int[] LIBRARY_COLORS = {
            Color.rgb(74, 144, 226), Color.rgb(189, 16, 224), Color.rgb(245, 166, 35),
            Color.rgb(76, 175, 80), Color.rgb(248, 231, 28), Color.rgb(126, 211, 33),
            Color.rgb(244, 118, 138), Color.rgb(249, 185, 79), Color.rgb(135, 176, 223),
            Color.rgb(251, 241, 124), Color.rgb(146, 226, 150), Color.rgb(213, 68, 91),
            Color.rgb(168, 130, 213), Color.rgb(132, 156, 133), Color.rgb(220, 186, 64),
            Color.rgb(116, 122, 180), Color.rgb(215, 195, 195), Color.rgb(244, 140, 120),
            Color.rgb(207, 158, 115), Color.rgb(97, 156, 99)
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_2, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        initView();
        initData();
        return view;
    }

    private void initView() {
        initReportChart();

        int padding = DensityUtils.dp2px(getActivity(), 30);
        int itemPadding = DensityUtils.dp2px(getActivity(), 10);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setPadding(padding, 0, padding, 0);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        mRecyclerView.setLayoutManager(layoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration(itemPadding);
        mRecyclerView.addItemDecoration(decoration);

        mAdapter = new ChartLegendAdapter(mDataList);
        mRecyclerView.setAdapter(mAdapter);
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
        LL.i("StudentReportFragment2 - 收到消息 : 学生徽章数" + event.userBadgeCount);
        tvBadgeNumber.setText(event.userBadgeCount);
        updateData(event.userBadgePieMap);
    }

    private void updateData(HashMap<String, Integer> userBadgePieMap) {
        if (userBadgePieMap == null) return;
        mDataList.clear();
        for (Map.Entry<String, Integer> entry : userBadgePieMap.entrySet()) {
            mDataList.add(new ChartItemInfo(entry.getKey(), entry.getValue()));
        }
        randomColor(mDataList);
        mAdapter.notifyDataSetChanged();
        // 设置图表
        mChart.animateY(1800, Easing.EasingOption.EaseInOutQuad);
        setData(mDataList);
    }

    private void randomColor(List<ChartItemInfo> mDataList) {
        colors.clear();
        for (int i = 0; i < mDataList.size(); i++) {
            //添加颜色
            int itemColor = LIBRARY_COLORS[i % LIBRARY_COLORS.length];
            colors.add(itemColor);
            mDataList.get(i).setColor(itemColor);
        }
    }

    @Override
    protected void onVisible() {
        super.onVisible();
        MobclickAgent.onPageStart("report2");
        if (!isFirstInit) {
            mChart.animateY(1800, Easing.EasingOption.EaseInOutQuad);
            setData(mDataList);
        }
    }

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
    }

    private void initReportChart() {
        mChart.getDescription().setEnabled(false);
        mChart.setDrawHoleEnabled(true);
        mChart.setTransparentCircleRadius(55f); //  设置中心透明圈半径占整个饼状图半径的百分比，默认是 55％ 的半径
        mChart.setTransparentCircleAlpha(110);
        mChart.setTransparentCircleColor(Color.parseColor("#FFFFFF"));
        mChart.setHoleRadius(50f);  // 设置中心圆孔半径占整个饼状图半径的百分比,默认的50％（即50f）
        mChart.setHoleColor(Color.parseColor("#4aa003"));
        mChart.setRotation(0);
        mChart.setRotationAngle(0);
        mChart.setUsePercentValues(true);   // 设置是否使用百分比
        mChart.setNoDataText(null);
        mChart.setNoDataTextColor(Color.WHITE);
        mChart.setDrawCenterText(false);
        mChart.setTouchEnabled(false);  //启用/禁用与图表的所有可能的触摸交互。
        mChart.setDrawEntryLabels(false);
        // mChart.animateY(1800, Easing.EasingOption.EaseInOutQuad);

        Legend l = mChart.getLegend();
        l.setEnabled(false);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setTextSize(7f);
        l.setTextColor(Color.WHITE);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        l.setMaxSizePercent(0.75f);
        l.setWordWrapEnabled(true);
    }

    private void setData(List<ChartItemInfo> datalist) {
        int count = datalist.size();

        ArrayList<PieEntry> entries = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            entries.add(new PieEntry((float) (datalist.get(i).getNumber()), datalist.get(i).getContent()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        if (count <= 5) {
            data.setValueTextSize(15f);
        } else if (count >= 13) {
            data.setValueTextSize(7f);
        } else {
            data.setValueTextSize((float) (20 - count));
        }
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);
        // undo all highlights
        mChart.highlightValues(null);
        mChart.invalidate();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onInVisible() {
        super.onInVisible();
        MobclickAgent.onPageEnd("report2");
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}

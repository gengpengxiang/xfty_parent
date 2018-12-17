package com.bj.hmxxparents.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bj.hmxxparents.BaseFragment;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.entity.ReportInfo;
import com.bj.hmxxparents.utils.LL;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zz379 on 2017/ic_pinde/3.
 * 学生本学期表现报告页面--第四页
 */

public class StudentReportFragment4 extends BaseFragment {
    @BindView(R.id.line_chart_badge)
    LineChart chartBadge;
    @BindView(R.id.line_chart_commend)
    LineChart chartCommend;

    public HashMap<Integer, Float> userBadgeLineMap;   // 徽章折线分布
    public HashMap<Integer, Float> classBadgeLineMap;   // 班级徽章折线分布
    public HashMap<Integer, Float> userCommendLineMap; // 点赞折线分布
    public HashMap<Integer, Float> classCommendLineMap; // 班级点赞折线分布

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_4, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        initView();
        initData();
        return view;
    }

    private void initView() {
        initReportChart();
    }

    private void initReportChart() {
        initChartBadge();
        initChartCommend();
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
        LL.i("StudentReportFragment4 - 收到消息 : 学生徽章数" + event.userBadgeCount);

        this.userBadgeLineMap = event.userBadgeLineMap;
        this.classBadgeLineMap = event.classBadgeLineMap;
        this.userCommendLineMap = event.userCommendLineMap;
        this.classCommendLineMap = event.classCommendLineMap;

        chartBadge.animateY(2000, Easing.EasingOption.Linear);
        setBadgeData(event.userBadgeLineMap, event.classBadgeLineMap);
        chartCommend.animateY(2000, Easing.EasingOption.Linear);
        setCommendData(event.userCommendLineMap, event.classCommendLineMap);
    }

    @Override
    protected void onVisible() {
        super.onVisible();
        MobclickAgent.onPageStart("report4");
        if (!isFirstInit) {
            chartBadge.animateY(2000, Easing.EasingOption.Linear);
            setBadgeData(this.userBadgeLineMap, this.classBadgeLineMap);

            chartCommend.animateY(2000, Easing.EasingOption.Linear);
            setCommendData(this.userCommendLineMap, this.classCommendLineMap);
        }
    }

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
    }

    private void initChartBadge() {
        chartBadge.getDescription().setEnabled(false);
        chartBadge.setNoDataText("");
        chartBadge.setNoDataTextColor(Color.WHITE);
        chartBadge.setTouchEnabled(false);  //启用/禁用与图表的所有可能的触摸交互。

        XAxis xAxis = chartBadge.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);  // 设置X轴的位置
        xAxis.setEnabled(true);
        xAxis.setAxisLineColor(Color.rgb(255, 203, 193));
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(false);
        xAxis.setAxisLineWidth(3f);
        xAxis.setAxisMinimum(0.5f);

        YAxis leftAxis = chartBadge.getAxisLeft();
        leftAxis.setEnabled(true);
        leftAxis.setAxisLineWidth(3f);
        leftAxis.setAxisLineColor(Color.rgb(255, 203, 193));
        leftAxis.setDrawLabels(false);
        leftAxis.setDrawGridLines(false);

        YAxis rightAxis = chartBadge.getAxisRight();
        rightAxis.setEnabled(false);

        Legend l = chartBadge.getLegend();
        l.setEnabled(false);
    }

    private void initChartCommend() {
        chartCommend.getDescription().setEnabled(false);
        chartCommend.setNoDataText("");
        chartCommend.setNoDataTextColor(Color.WHITE);
        chartCommend.setTouchEnabled(false);  //启用/禁用与图表的所有可能的触摸交互。

        XAxis xAxis = chartCommend.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);  // 设置X轴的位置
        xAxis.setEnabled(true);
        xAxis.setAxisLineColor(Color.rgb(255, 203, 193));
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(false);
        xAxis.setAxisLineWidth(3f);
        xAxis.setAxisMinimum(0.5f);

        YAxis leftAxis = chartCommend.getAxisLeft();
        leftAxis.setEnabled(true);
        leftAxis.setAxisLineWidth(3f);
        leftAxis.setAxisLineColor(Color.rgb(255, 203, 193));
        leftAxis.setDrawLabels(false);
        leftAxis.setDrawGridLines(false);

        YAxis rightAxis = chartCommend.getAxisRight();
        rightAxis.setEnabled(false);

        Legend l = chartCommend.getLegend();
        l.setEnabled(false);
    }

    private void setBadgeData(HashMap<Integer, Float> userMap, HashMap<Integer, Float> classMap) {
        ArrayList<Entry> yVals1 = new ArrayList<>();
        ArrayList<Entry> yVals2 = new ArrayList<>();
        int count = userMap.size();

        XAxis xAxis = chartBadge.getXAxis();
        xAxis.setAxisMaximum(count + 0.5f);

        YAxis leftAxis = chartBadge.getAxisLeft();
        leftAxis.resetAxisMaximum();
        leftAxis.resetAxisMinimum();
        float maxYValue = 0f;

        for (int i = 0; i < count; i++) {
            float val1 = userMap.get(i + 1);
            float val2 = classMap.get(i + 1);
            maxYValue = Math.max(maxYValue, Math.max(val1, val2));
            yVals1.add(new Entry(i + 1, val1));
            yVals2.add(new Entry(i + 1, val2));
        }
        // 设置Y轴最大值
        if (maxYValue > 0f && maxYValue < 1f) {
            maxYValue = maxYValue * 2;
            leftAxis.setAxisMaximum(maxYValue);
            leftAxis.setAxisMinimum(-maxYValue);
        } else if (maxYValue == 0f) {
            maxYValue = 1f;
            leftAxis.setAxisMaximum(maxYValue);
            leftAxis.setAxisMinimum(-1f);
        } else {
            maxYValue = maxYValue * 1.2f;
            leftAxis.setAxisMaximum(maxYValue);
            leftAxis.setAxisMinimum(-1f);
        }

        LineDataSet set1, set2;
        if (chartBadge.getData() != null && chartBadge.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chartBadge.getData().getDataSetByIndex(0);
            set2 = (LineDataSet) chartBadge.getData().getDataSetByIndex(1);
            set1.setValues(yVals1);
            set2.setValues(yVals2);
            chartBadge.getData().notifyDataChanged();
            chartBadge.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(yVals1, "我的徽章数");
            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(Color.rgb(74, 144, 226));
            set1.setCircleColor(Color.rgb(74, 144, 226));
            set1.setLineWidth(2f);
            set1.setCircleRadius(4f);
            set1.setFillAlpha(65);
            set1.setDrawCircleHole(false);
            set1.setDrawValues(true);

            set2 = new LineDataSet(yVals2, "班级平均数");
            set2.setAxisDependency(YAxis.AxisDependency.LEFT);
            set2.setColor(Color.rgb(126, 211, 33));
            set2.setCircleColor(Color.rgb(126, 211, 33));
            set2.setLineWidth(2f);
            set2.setCircleRadius(4f);
            set2.setFillAlpha(65);
            set2.setDrawCircleHole(false);
            set2.setDrawValues(true);

            LineData data = new LineData(set1, set2);
            data.setValueTextColor(Color.WHITE);
            data.setValueTextSize(9f);

            chartBadge.setData(data);
        }
        // redraw
        chartBadge.invalidate();
    }

    private void setCommendData(HashMap<Integer, Float> userMap, HashMap<Integer, Float> classMap) {
        ArrayList<Entry> yVals1 = new ArrayList<>();
        ArrayList<Entry> yVals2 = new ArrayList<>();
        int count = userMap.size();

        XAxis xAxis = chartCommend.getXAxis();
        xAxis.setAxisMaximum(count + 0.5f);

        YAxis leftAxis = chartCommend.getAxisLeft();
        leftAxis.resetAxisMaximum();
        leftAxis.resetAxisMinimum();
        float maxYValue = 0f;

        for (int i = 0; i < count; i++) {
            float val1 = userMap.get(i + 1);
            float val2 = classMap.get(i + 1);
            maxYValue = Math.max(maxYValue, Math.max(val1, val2));
            yVals1.add(new Entry(i + 1, val1));
            yVals2.add(new Entry(i + 1, val2));
        }

        // 设置Y轴最大值
        // 设置Y轴最大值
        if (maxYValue > 0f && maxYValue < 1f) {
            maxYValue = maxYValue * 2;
            leftAxis.setAxisMaximum(maxYValue);
            leftAxis.setAxisMinimum(-maxYValue);
        } else if (maxYValue == 0f) {
            maxYValue = 1f;
            leftAxis.setAxisMaximum(maxYValue);
            leftAxis.setAxisMinimum(-1f);
        } else {
            maxYValue = maxYValue * 1.2f;
            leftAxis.setAxisMaximum(maxYValue);
            leftAxis.setAxisMinimum(-1f);
        }

        LineDataSet set1, set2;

        if (chartCommend.getData() != null && chartCommend.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chartCommend.getData().getDataSetByIndex(0);
            set2 = (LineDataSet) chartCommend.getData().getDataSetByIndex(1);
            set1.setValues(yVals1);
            set2.setValues(yVals2);
            chartCommend.getData().notifyDataChanged();
            chartCommend.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(yVals1, "我的点赞数");
            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(Color.rgb(144, 19, 254));
            set1.setCircleColor(Color.rgb(144, 19, 254));
            set1.setLineWidth(2f);
            set1.setCircleRadius(4f);
            set1.setFillAlpha(65);
            set1.setDrawCircleHole(false);
            set1.setDrawValues(true);

            set2 = new LineDataSet(yVals2, "班级平均数");
            set2.setAxisDependency(YAxis.AxisDependency.LEFT);
            set2.setColor(Color.rgb(126, 211, 33));
            set2.setCircleColor(Color.rgb(126, 211, 33));
            set2.setLineWidth(2f);
            set2.setCircleRadius(4f);
            set2.setFillAlpha(65);
            set2.setDrawCircleHole(false);
            set2.setDrawValues(true);

            LineData data = new LineData(set1, set2);
            data.setValueTextColor(Color.WHITE);
            data.setValueTextSize(9f);

            chartCommend.setData(data);
        }
        // redraw
        chartCommend.invalidate();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onInVisible() {
        super.onInVisible();
        MobclickAgent.onPageEnd("report4");
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}

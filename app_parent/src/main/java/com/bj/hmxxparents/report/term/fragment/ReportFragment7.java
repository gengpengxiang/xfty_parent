package com.bj.hmxxparents.report.term.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bj.hmxxparents.BaseFragment;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.api.MLConfig;
import com.bj.hmxxparents.report.term.model.Report5;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.bj.hmxxparents.api.HttpUtilService.BASE_URL;


/**
 * Created by zz379 on 2017/3/29.
 * 学期表现曲线报告页
 */

public class ReportFragment7 extends BaseFragment {


    @BindView(R.id.tv_report_name)
    TextView tvReportName;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    @BindView(R.id.chart_badge)
    LineChart chartBadge;
    @BindView(R.id.chart_evaluation)
    LineChart chartEvaluation;
    private Unbinder unbinder;
    private String studentId;

    private List<Float> badgeNumList = new ArrayList<>();
    private List<Float> badgeNumList2 = new ArrayList<>();


    private List<String> dateList = new ArrayList<>();
    String[] str = new String[4];

    private List<Float> evaluationNumList = new ArrayList<>();
    private List<Float> evaluationNumList2 = new ArrayList<>();

    private List<Report5.DataBean> dataList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_zhexian_term, container, false);
        unbinder = ButterKnife.bind(this, view);
        studentId = getActivity().getIntent().getStringExtra("id");
        initView();
        initChartBadge();
        initChartEvaluation();
        getReportInfo5();
        return view;
    }

    private void initChartBadge() {
        chartBadge.getDescription().setEnabled(false);// 不显示数据描述
        chartBadge.setNoDataText("");// 没有数据的时候，显示“暂无数据”
        chartBadge.setNoDataTextColor(Color.WHITE);
        chartBadge.setTouchEnabled(false);  //启用/禁用与图表的所有可能的触摸交互。

        //chartBadge.setExtraLeftOffset(50);

        XAxis xAxis = chartBadge.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);  // //设置X轴的位置（默认在上方)
        xAxis.setEnabled(false);
        xAxis.setAxisLineColor(R.color.text_blue_light);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);//不显示网格线
        xAxis.setDrawLabels(true);
        xAxis.setAxisLineWidth(3f);
        xAxis.setAxisMinimum(0.5f);
        xAxis.setAxisMaximum((float)dateList.size());
        xAxis.setLabelCount(dateList.size(), false);


        LabelFormatter labelFormatter = new LabelFormatter(str);
        xAxis.setValueFormatter(labelFormatter);

        YAxis leftAxis = chartBadge.getAxisLeft();
        leftAxis.setEnabled(false);
        leftAxis.setAxisLineWidth(3f);
        leftAxis.setAxisLineColor(R.color.text_blue_light);
        leftAxis.setDrawLabels(false);
        leftAxis.setDrawGridLines(false);


        YAxis rightAxis = chartBadge.getAxisRight();
        rightAxis.setEnabled(false);// 不显示y轴右边的值

        Legend l = chartBadge.getLegend();
        l.setEnabled(false);// 不显示图例
    }

    private void initChartEvaluation() {
        chartEvaluation.getDescription().setEnabled(false);
        chartEvaluation.setNoDataText("");
        chartEvaluation.setNoDataTextColor(Color.WHITE);
        chartEvaluation.setTouchEnabled(false);  //启用/禁用与图表的所有可能的触摸交互。


        XAxis xAxis = chartEvaluation.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);  // 设置X轴的位置
        xAxis.setEnabled(false);
        xAxis.setAxisLineColor(Color.rgb(255, 203, 193));
        xAxis.setDrawAxisLine(true);// 显示x轴
        xAxis.setDrawGridLines(true);
        xAxis.setDrawLabels(true);
        xAxis.setAxisLineWidth(3f);
        xAxis.setAxisMinimum(0.5f);
        xAxis.setAxisMaximum((float)dateList.size());
        xAxis.setLabelCount(dateList.size(), false);


        YAxis leftAxis = chartEvaluation.getAxisLeft();
        leftAxis.setEnabled(false);
        leftAxis.setAxisLineWidth(3f);
        leftAxis.setAxisLineColor(Color.rgb(255, 203, 193));
        leftAxis.setDrawLabels(false);
        leftAxis.setDrawGridLines(false);

        YAxis rightAxis = chartEvaluation.getAxisRight();
        rightAxis.setEnabled(false);

        Legend l = chartEvaluation.getLegend();
        l.setEnabled(false);
    }

    private void initView() {
        tvReportName.setText("学期表现曲线");
    }


    private void getReportInfo5() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                OkGo.<String>post(BASE_URL + "jzbaogao/getbg4_2")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("studentcode", studentId)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String str = response.body().toString();
                                Log.e("报告555", str);

                                e.onNext(str);
                                e.onComplete();
                            }
                        });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {

                        try {
                            Report5 report5 = JSON.parseObject(s, new TypeReference<Report5>() {
                            });
                            if (report5.getRet().equals("1")) {

                                dataList = report5.getData();

//                                setChartBadgeData(report5.getData());
//
//                                setChartEvaluationData(report5.getData());
                            }
                        }catch (Exception e){

                        }

                    }
                });
    }

    private void setChartBadgeData(List<Report5.DataBean> dataList) {

        for (int i = 0; i < dataList.size(); i++) {
            badgeNumList.add(Float.valueOf(dataList.get(i).getWeekdata_hz()));
            badgeNumList2.add(Float.valueOf(dataList.get(i).getClass_avg_hz()));

            }

        ArrayList<Entry> yVals1 = new ArrayList<>();
        ArrayList<Entry> yVals2 = new ArrayList<>();
        int count = dataList.size();

        XAxis xAxis = chartBadge.getXAxis();
//        xAxis.setAxisMaximum(count + 0.5f);

        xAxis.setAxisMaximum(count+0.2f);//修改此处可改变两个数据之间的距离
        xAxis.setAxisMinimum(0.7f);//修改此处可控制第一条数据的偏移量

        YAxis leftAxis = chartBadge.getAxisLeft();
        leftAxis.resetAxisMaximum();
        leftAxis.resetAxisMinimum();
        float maxYValue = 0f;

        for (int i = 0; i < count; i++) {
            float val1 = badgeNumList.get(i);
            float val2 = badgeNumList2.get(i);
            maxYValue = Math.max(maxYValue, Math.max(val1, val2));
            yVals1.add(new Entry(i+1 , val1));
            yVals2.add(new Entry(i+1 , val2));

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
//            set1.setColor(R.color.color_line_blue);
            set1.setColor(getResources().getColor(R.color.color_line_blue));
            set1.setCircleColor(getResources().getColor(R.color.color_line_blue));

            set1.setLineWidth(2f);//线条宽度
            set1.setCircleRadius(4f);
            set1.setFillAlpha(65);
            set1.setDrawCircleHole(false);
            set1.setDrawValues(true);//不显示坐标点的数据

            set1.setValueTextColor(getResources().getColor(R.color.color_line_blue));

            set2 = new LineDataSet(yVals2, "班级平均数");
            set2.setAxisDependency(YAxis.AxisDependency.LEFT);
//            set2.setColor(R.color.color_line_green);// 设置曲线颜色
            set2.setColor(getResources().getColor(R.color.color_line_green));// 设置曲线颜色
            set2.setCircleColor(getResources().getColor(R.color.color_line_green));
            set2.setLineWidth(2f);
            set2.setCircleRadius(4f);
            set2.setFillAlpha(65);
            set2.setDrawCircleHole(false);
            set2.setDrawValues(true);
            set2.setValueTextColor(getResources().getColor(R.color.color_line_green));

            LineData data = new LineData(set1, set2);
            //data.setValueTextColor(getResources().getColor(R.color.color_line_blue));
            data.setValueTextSize(12f);

            chartBadge.setData(data);
        }
        // redraw
        chartBadge.invalidate();
    }

    private void setChartEvaluationData(List<Report5.DataBean> dataList) {
        for (int i = 0; i < dataList.size(); i++) {
            evaluationNumList.add(Float.valueOf(dataList.get(i).getWeekdata_dz()));
            evaluationNumList2.add(Float.valueOf(dataList.get(i).getClass_avg_dz()));
        }

        ArrayList<Entry> yVals1 = new ArrayList<>();
        ArrayList<Entry> yVals2 = new ArrayList<>();
        int count = dataList.size();

        XAxis xAxis = chartEvaluation.getXAxis();
        xAxis.setAxisMaximum(count+0.2f);//修改此处可改变两个数据之间的距离
        xAxis.setAxisMinimum(0.7f);//修改此处可控制第一条数据的偏移量

        YAxis leftAxis = chartEvaluation.getAxisLeft();
        leftAxis.resetAxisMaximum();
        leftAxis.resetAxisMinimum();
        float maxYValue = 0f;

        for (int i = 0; i < count; i++) {
//            float val1 = userMap.get(i + 1);
//            float val2 = classMap.get(i + 1);
//            maxYValue = Math.max(maxYValue, Math.max(val1, val2));
//            yVals1.add(new Entry(i + 1, val1));
//            yVals2.add(new Entry(i + 1, val2));
            float val1 = evaluationNumList.get(i);
            float val2 = evaluationNumList2.get(i);
            maxYValue = Math.max(maxYValue, Math.max(val1, val2));
            yVals1.add(new Entry(i+1 , val1));
            yVals2.add(new Entry(i+1 , val2));
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
        if (chartEvaluation.getData() != null && chartEvaluation.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chartEvaluation.getData().getDataSetByIndex(0);
            set2 = (LineDataSet) chartEvaluation.getData().getDataSetByIndex(1);
            set1.setValues(yVals1);
            set2.setValues(yVals2);
            chartEvaluation.getData().notifyDataChanged();
            chartEvaluation.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(yVals1, "我的评价数");
            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(getResources().getColor(R.color.color_bar_purple));// 设置曲线颜色
            set1.setCircleColor(getResources().getColor(R.color.color_bar_purple));
            set1.setLineWidth(2f);
            set1.setCircleRadius(4f);
            set1.setFillAlpha(65);
            set1.setDrawCircleHole(false);
            set1.setDrawValues(true);//不显示坐标点的数据

            set1.setValueTextColor(getResources().getColor(R.color.color_bar_purple));

            set2 = new LineDataSet(yVals2, "班级平均数");
            set2.setAxisDependency(YAxis.AxisDependency.LEFT);
            set2.setColor(getResources().getColor(R.color.color_line_green));
            set2.setCircleColor(getResources().getColor(R.color.color_line_green));
            set2.setLineWidth(2f);
            set2.setCircleRadius(4f);
            set2.setFillAlpha(65);
            set2.setDrawCircleHole(false);
            set2.setDrawValues(true);

            set2.setValueTextColor(getResources().getColor(R.color.color_line_green));

            LineData data = new LineData(set1, set2);
          //  data.setValueTextColor(Color.WHITE);

            data.setValueTextSize(12f);

            chartEvaluation.setData(data);
        }
        // redraw
        chartEvaluation.invalidate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    protected void onVisible() {
        super.onVisible();
        if (!isFirstInit) {
            chartBadge.animateY(1000, Easing.EasingOption.Linear);

            chartEvaluation.animateY(1000, Easing.EasingOption.Linear);

            setChartBadgeData(dataList);

            setChartEvaluationData(dataList);
        }
    }

    class LabelFormatter implements IAxisValueFormatter {
        private final String[] mLabels;
        public LabelFormatter(String[] labels) {
            mLabels = labels;
        }
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            try {
                return mLabels[(int) value];
            } catch (Exception e) {
                e.printStackTrace();
                return mLabels[0];
            }
        }
    }

}

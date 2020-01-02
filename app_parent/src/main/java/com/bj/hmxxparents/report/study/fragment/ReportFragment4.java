package com.bj.hmxxparents.report.study.fragment;

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
import com.bj.hmxxparents.api.MLProperties;
import com.bj.hmxxparents.report.study.model.Report1;
import com.bj.hmxxparents.report.study.model.Report3;
import com.bj.hmxxparents.report.study.model.Report4;
import com.bj.hmxxparents.utils.PreferencesUtils;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
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
 * 英语页面
 */

public class ReportFragment4 extends BaseFragment {


    @BindView(R.id.tv_report_name)
    TextView tvReportName;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    @BindView(R.id.chart_score)
    LineChart chartScore;


    private Unbinder unbinder;
    private String studentId, classcode, yingyustatus;

    private List<Float> scoreMineList = new ArrayList<>();
    private List<Float> scoreAverageList = new ArrayList<>();
    private List<Float> scoreMaxList = new ArrayList<>();


    private List<Report4.DataBean.FenxiBean> dataList = new ArrayList<>();
    private List<Report4.DataBean.DengjiBean> dengjiList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_zhexian_study2, container, false);
        unbinder = ButterKnife.bind(this, view);
        studentId = getActivity().getIntent().getStringExtra("id");
        classcode = PreferencesUtils.getString(getActivity(), MLProperties.BUNDLE_KEY_CLASS_CODE);
        yingyustatus = PreferencesUtils.getString(getActivity(), "xuexibaogao_yingyustatus");
        initView();
        initChartScore();
        getReportInfo1();
        return view;
    }

    private void initChartScore() {
        chartScore.getDescription().setEnabled(false);// 不显示数据描述
        chartScore.setNoDataText("");// 没有数据的时候，显示“暂无数据”
        chartScore.setNoDataTextColor(Color.WHITE);
        chartScore.setTouchEnabled(false);  //启用/禁用与图表的所有可能的触摸交互。

        XAxis xAxis = chartScore.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);  // //设置X轴的位置（默认在上方)
        xAxis.setEnabled(true);
        xAxis.setAxisLineColor(R.color.text_blue_light);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);//不显示网格线
        xAxis.setDrawLabels(false);
        xAxis.setAxisLineWidth(3f);
        xAxis.setAxisMinimum(0.5f);
        xAxis.setAxisMaximum((float) 4);
        xAxis.setLabelCount(4, false);


//        LabelFormatter labelFormatter = new LabelFormatter(str);
//        xAxis.setValueFormatter(labelFormatter);

        YAxis leftAxis = chartScore.getAxisLeft();
        leftAxis.setEnabled(true);
        leftAxis.setAxisLineWidth(3f);
        leftAxis.setAxisLineColor(R.color.text_blue_light);
        leftAxis.setDrawLabels(false);
        leftAxis.setDrawAxisLine(false);//是否绘制轴线
        leftAxis.setDrawGridLines(false);



        YAxis rightAxis = chartScore.getAxisRight();
        rightAxis.setEnabled(false);// 不显示y轴右边的值

        Legend l = chartScore.getLegend();
        l.setEnabled(false);// 不显示图例
    }

    private void initView() {
        tvReportName.setText("英语");
    }

    private void getReportInfo1() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                OkGo.<String>post(BASE_URL + "jzxuexibaogao/getbg_yingyu")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("studentcode", studentId)
                        .params("classcode", classcode)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String str = response.body().toString();
                                Log.e("报告英语", str);

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
                            Report4 report4 = JSON.parseObject(s, new TypeReference<Report4>() {
                            });
                            if (report4.getRet().equals("1")) {

                                dataList = report4.getData().getFenxi();
                                dengjiList = report4.getData().getDengji();
//                                setChartBadgeData(report5.getData());
//
//                                setChartEvaluationData(report5.getData());
                            }
                        } catch (Exception e) {

                        }

                    }
                });
    }

    private void setChartData(List<Report4.DataBean.FenxiBean> dataList) {

        for (int i = 0; i < dataList.size(); i++) {
            scoreMineList.add(Float.valueOf(dataList.get(i).getYingyu()));
            scoreAverageList.add(Float.valueOf(dataList.get(i).getAvg_yingyu()));
            scoreMaxList.add(Float.valueOf(dataList.get(i).getMax_yingyu()));
        }

        ArrayList<Entry> yVals1 = new ArrayList<>();
        ArrayList<Entry> yVals2 = new ArrayList<>();
        ArrayList<Entry> yVals3 = new ArrayList<>();
        int count = dataList.size();

        XAxis xAxis = chartScore.getXAxis();
//        xAxis.setAxisMaximum(count + 0.5f);

        xAxis.setAxisMaximum(4 + 0.25f);//修改此处可改变两个数据之间的距离
        xAxis.setAxisMinimum(0.7f);//修改此处可控制第一条数据的偏移量

        YAxis leftAxis = chartScore.getAxisLeft();
        leftAxis.resetAxisMaximum();
        leftAxis.resetAxisMinimum();
        float maxYValue = 100f;
        float minYValue = 0f;

        for (int i = 0; i < count; i++) {
            float val1 = scoreMineList.get(i);
            float val2 = scoreAverageList.get(i);
            float val3 = scoreMaxList.get(i);
            maxYValue = val3;
            yVals1.add(new Entry(i + 1, val1));
            yVals2.add(new Entry(i + 1, val2));
            yVals3.add(new Entry(i + 1, val3));
            maxYValue = Math.max(maxYValue, Math.max(val1, val3));
            minYValue = Math.min(minYValue, Math.min(val1, val2));
        }

        leftAxis.setAxisMaximum(100f+10f);
        leftAxis.setAxisMinimum(0f);

        //添加标志线，这里将其颜色设置为透明，左边显示A,B,C,D

        for (int i = 0; i < dengjiList.size(); i++) {
            LimitLine upLimit = null;
            if(i==0){
                upLimit = new LimitLine(Float.valueOf(dengjiList.get(i).getFanwei().getMin()), "A");
            }else if(i==1){
                upLimit = new LimitLine(Float.valueOf(dengjiList.get(i).getFanwei().getMin()), "B");
            }else if(i==2){
                upLimit = new LimitLine(Float.valueOf(dengjiList.get(i).getFanwei().getMin()), "C");
            }else if(i==3){
                upLimit = new LimitLine(Float.valueOf(dengjiList.get(i).getFanwei().getMin()), "D");
            }

            upLimit.setLineColor(Color.GRAY);
            upLimit.setTextColor(getResources().getColor(R.color.color_line_blue));
            upLimit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
            leftAxis.addLimitLine(upLimit);
        }


        LineDataSet set1, set2, set3;
        if (chartScore.getData() != null && chartScore.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chartScore.getData().getDataSetByIndex(0);
            set2 = (LineDataSet) chartScore.getData().getDataSetByIndex(1);
            set3 = (LineDataSet) chartScore.getData().getDataSetByIndex(2);
            set1.setValues(yVals1);
            set2.setValues(yVals2);
            set3.setValues(yVals3);
            chartScore.getData().notifyDataChanged();
            chartScore.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(yVals1, "我的总分");
            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
//            set1.setColor(R.color.color_line_blue);
            set1.setColor(getResources().getColor(R.color.color_line_blue));
            set1.setCircleColor(getResources().getColor(R.color.color_line_blue));

            set1.setLineWidth(2f);//线条宽度
            set1.setCircleRadius(4f);
            set1.setFillAlpha(65);
            set1.setDrawCircleHole(false);
            set1.setDrawValues(false);//不显示坐标点的数据

            set1.setValueTextColor(getResources().getColor(R.color.color_line_blue));

            set2 = new LineDataSet(yVals2, "班级平均");
            set2.setAxisDependency(YAxis.AxisDependency.LEFT);
//            set2.setColor(R.color.color_line_green);// 设置曲线颜色
            set2.setColor(getResources().getColor(R.color.color_line_green));// 设置曲线颜色
            set2.setCircleColor(getResources().getColor(R.color.color_line_green));
            set2.setLineWidth(2f);
            set2.setCircleRadius(4f);
            set2.setFillAlpha(65);
            set2.setDrawCircleHole(false);
            set2.setDrawValues(false);
            set2.setValueTextColor(getResources().getColor(R.color.color_line_green));

            set3 = new LineDataSet(yVals3, "班级最高");
            set3.setAxisDependency(YAxis.AxisDependency.LEFT);
//            set2.setColor(R.color.color_line_green);// 设置曲线颜色
            set3.setColor(getResources().getColor(R.color.text_orange));// 设置曲线颜色
            set3.setCircleColor(getResources().getColor(R.color.text_orange));
            set3.setLineWidth(2f);
            set3.setCircleRadius(4f);
            set3.setFillAlpha(65);
            set3.setDrawCircleHole(false);
            set3.setDrawValues(false);
            set3.setValueTextColor(getResources().getColor(R.color.text_orange));


            LineData data = new LineData(set1, set2, set3);
            //data.setValueTextColor(getResources().getColor(R.color.color_line_blue));
            data.setValueTextSize(12f);

            chartScore.setData(data);
        }
        // redraw
        chartScore.invalidate();
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
            chartScore.animateY(1000, Easing.EasingOption.Linear);
            setChartData(dataList);
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

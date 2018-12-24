package com.bj.hmxxparents.report.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.bj.hmxxparents.report.adapter.BadgeTypeAdapter;
import com.bj.hmxxparents.report.adapter.DianzanTypeAdapter;
import com.bj.hmxxparents.report.model.Report2;
import com.bj.hmxxparents.report.model.Report3;
import com.bj.hmxxparents.widget.SpacesItemDecoration;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
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
 * 全校活力榜页面
 */

public class ReportFragment3 extends BaseFragment {


    @BindView(R.id.tv_report_name)
    TextView tvReportName;
    @BindView(R.id.tv_score)
    TextView tvScore;
    @BindView(R.id.chart)
    PieChart mChart;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    private Unbinder unbinder;
    private String studentId;

    private DianzanTypeAdapter adapter;
    private List<Report3.DataBean.DznumBean> dataList = new ArrayList<>();

    ArrayList<Integer> colors = new ArrayList<Integer>();
    public static final int[] LIBRARY_COLORS = {
            Color.rgb(74, 144, 226), Color.rgb(189, 16, 224), Color.rgb(245, 166, 35),
            Color.rgb(76, 175, 80), Color.rgb(248, 231, 28), Color.rgb(126, 211, 33),
            Color.rgb(244, 118, 138), Color.rgb(249, 185, 79), Color.rgb(135, 176, 223),
            Color.rgb(251, 241, 124), Color.rgb(146, 226, 150), Color.rgb(213, 68, 91),
            Color.rgb(168, 130, 213), Color.rgb(132, 156, 133), Color.rgb(220, 186, 64),
            Color.rgb(116, 122, 180), Color.rgb(215, 195, 195), Color.rgb(244, 140, 120),
            Color.rgb(207, 158, 115), Color.rgb(97, 156, 99)
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report2, container, false);
        unbinder = ButterKnife.bind(this, view);
        studentId = getActivity().getIntent().getStringExtra("id");
        initView();
        getReportInfo2();
        initChart();
        return view;
    }

    private void initView() {

        tvReportName.setText("点赞");

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(10));
        adapter = new DianzanTypeAdapter(R.layout.recycler_item_report_chart_type,dataList);
        mRecyclerView.setAdapter(adapter);
    }

    private void initChart() {
        mChart.getDescription().setEnabled(false);
        mChart.setDrawHoleEnabled(true);
        mChart.setTransparentCircleRadius(55f); //  设置中心透明圈半径占整个饼状图半径的百分比，默认是 55％ 的半径
        mChart.setTransparentCircleAlpha(110);
        mChart.setTransparentCircleColor(Color.parseColor("#FFFFFF"));
        mChart.setHoleRadius(50f);  // 设置中心圆孔半径占整个饼状图半径的百分比,默认的50％（即50f）
       // mChart.setHoleColor(Color.parseColor("#4aa003"));
        mChart.setRotation(0);
        mChart.setRotationAngle(-90);
        mChart.setUsePercentValues(true);   // 设置是否使用百分比
        mChart.setNoDataText("暂无数据");
        mChart.setNoDataTextColor(getResources().getColor(R.color.color_line_green));
        mChart.setDrawCenterText(false);
        mChart.setTouchEnabled(false);  //启用/禁用与图表的所有可能的触摸交互。
        mChart.setDrawEntryLabels(false);

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

    private void getReportInfo2() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                OkGo.<String>post(BASE_URL + "jzbaogao/getbg3")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("studentcode", studentId)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String str = response.body().toString();
                                Log.e("报告333", str);

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
                       Report3 report3 = JSON.parseObject(s,new TypeReference<Report3>(){});
                       if(report3.getRet().equals("1")){
                           dataList.clear();
                           if(report3.getData().getDznum()!=null) {
                               dataList.addAll(report3.getData().getDznum());
                               adapter.notifyDataSetChanged();
                               randomColor(dataList);
                               tvScore.setText(report3.getData().getDzsum().get(0).getDzsum());
                               setData();
                           }else {
                               tvScore.setText(report3.getData().getDzsum().get(0).getDzsum());
                           }
                       }

                    }
                });

    }

    private void randomColor(List<Report3.DataBean.DznumBean> mDataList) {
        colors.clear();
        for (int i = 0; i < mDataList.size(); i++) {
            //添加颜色
            int itemColor = LIBRARY_COLORS[i % LIBRARY_COLORS.length];
            colors.add(itemColor);
            mDataList.get(i).setColor(itemColor);
        }
    }

    private void setData() {
        int count = dataList.size();

        ArrayList<PieEntry> entries = new ArrayList<>();

        for (int i = 0; i < count; i++) {
//            entries.add(new PieEntry((float) (dataList.get(i).getHznum()), dataList.get(i).getName()));
            entries.add(new PieEntry(Float.valueOf(dataList.get(i).getDznum()), dataList.get(i).getLiyou()));
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
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}

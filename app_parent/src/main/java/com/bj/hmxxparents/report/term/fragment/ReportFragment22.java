package com.bj.hmxxparents.report.term.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bj.hmxxparents.BaseFragment;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.api.MLConfig;
import com.bj.hmxxparents.report.term.adapter.BadgeRecordAdapter;
import com.bj.hmxxparents.report.term.model.BadgeRecord;
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
 * 徽章获得时间表
 */

public class ReportFragment22 extends BaseFragment {


    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    private Unbinder unbinder;

    private String studentId;
    private BadgeRecordAdapter adapter;
    private List<BadgeRecord.DataBean> dataList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_badge_record, container, false);
        unbinder = ButterKnife.bind(this, view);
        studentId = getActivity().getIntent().getStringExtra("id");
        initView();
        getReportInfo2();
        return view;
    }

    private void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new BadgeRecordAdapter(R.layout.recycler_item_report_badge_record,dataList);
        mRecyclerView.setAdapter(adapter);

    }

    private void getReportInfo2() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                OkGo.<String>post(BASE_URL + "jzbaogao/getbg_hztime")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("studentcode", studentId)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String str = response.body().toString();
                                Log.e("报告徽章获得时间表", str);

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
                            BadgeRecord badgeRecord = JSON.parseObject(s, new TypeReference<BadgeRecord>() {
                            });
                            if (badgeRecord.getRet().equals("1")) {
                                dataList.clear();

                                dataList.addAll(badgeRecord.getData());
                                adapter.notifyDataSetChanged();
                            }
                        }catch (Exception e){

                        }

                    }
                });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}

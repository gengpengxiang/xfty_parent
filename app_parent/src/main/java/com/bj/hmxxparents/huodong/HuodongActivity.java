package com.bj.hmxxparents.huodong;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bj.hmxxparents.BaseActivity;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.api.MLProperties;
import com.bj.hmxxparents.comment.adapter.CommentAdapter;
import com.bj.hmxxparents.comment.modal.CommentInfo;
import com.bj.hmxxparents.entity.MessageEvent;
import com.bj.hmxxparents.huodong.adapter.HuodongAdapter;
import com.bj.hmxxparents.huodong.model.HuodongInfo;
import com.bj.hmxxparents.huodong.presenter.HuodongPresenter;
import com.bj.hmxxparents.huodong.view.IViewHuodong;
import com.bj.hmxxparents.widget.SpacesItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HuodongActivity extends BaseActivity implements IViewHuodong{

    @BindView(R.id.header_img_back)
    ImageView headerImgBack;
    @BindView(R.id.header_tv_title)
    TextView headerTvTitle;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mSmartRefreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    private Unbinder unbinder;

    private HuodongAdapter huodongAdapter;
    private List<HuodongInfo.DataBean> huodongList = new ArrayList<>();
    private HuodongPresenter presenter;
    private String kidId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huodong);
        unbinder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        presenter = new HuodongPresenter(this,this);



        initToolBar();
        initViews();

        presenter.getHuodongList();
    }

    private void initToolBar() {
        headerTvTitle.setText("活动");
        headerTvTitle.setVisibility(View.VISIBLE);
        headerImgBack.setVisibility(View.VISIBLE);

        kidId = getIntent().getStringExtra(MLProperties.BUNDLE_KEY_KID_ID);
    }

    private void initViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(1));

        huodongAdapter = new HuodongAdapter(R.layout.recycler_item_huodong, huodongList);
        mRecyclerView.setAdapter(huodongAdapter);
        huodongAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(HuodongActivity.this,HuodongDetailActivity.class);
                intent.putExtra("huodong_code",huodongList.get(position).getCode());
                startActivity(intent);
            }
        });

        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                presenter.getHuodongList();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.header_ll_left)
    public void onClick() {
        finish();
    }

    @Override
    public void getHuodongList(String result) {
        if(mSmartRefreshLayout.isRefreshing()){
            mSmartRefreshLayout.finishRefresh();
        }if(mSmartRefreshLayout.isLoading()){
            mSmartRefreshLayout.finishLoadmore();
        }

        huodongAdapter.setEmptyView(R.layout.recycler_item_tianyuan_empty,mRecyclerView);

        HuodongInfo huodongInfo = JSON.parseObject(result, new TypeReference<HuodongInfo>() {
        });
        if(huodongInfo.getRet().equals("1")){
            huodongList.clear();
            huodongList.addAll(huodongInfo.getData());
            huodongAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void uodate(MessageEvent event) {
        if (event.getMessage().equals("publishTopicSuccess")) {
            presenter.getHuodongList();
        }
    }
}

package com.bj.hmxxparents.email;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bj.hmxxparents.BaseActivity;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.api.MLProperties;
import com.bj.hmxxparents.email.adapter.ReplyAdapter;
import com.bj.hmxxparents.email.model.Reply;
import com.bj.hmxxparents.email.presenter.ReplyPresenter;
import com.bj.hmxxparents.email.view.IViewReply;
import com.bj.hmxxparents.entity.BaseDataInfo;
import com.bj.hmxxparents.utils.Base64Util;
import com.bj.hmxxparents.utils.KeyBoardUtils;
import com.bj.hmxxparents.utils.PreferencesUtils;
import com.bj.hmxxparents.utils.ScreenUtils;
import com.bj.hmxxparents.utils.StringUtils;
import com.bj.hmxxparents.utils.T;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class EmailDetailActivity extends BaseActivity implements IViewReply {

    @BindView(R.id.header_img_back)
    ImageView headerImgBack;
    @BindView(R.id.header_tv_title)
    TextView headerTvTitle;
    @BindView(R.id.header_ll_left)
    LinearLayout headerLlLeft;
    @BindView(R.id.layout_sanjiao)
    LinearLayout layoutSanjiao;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mSmartRefreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.edt_content)
    EditText edtContent;
    @BindView(R.id.tv_send)
    TextView tvSend;
    private Unbinder unbinder;

    private ReplyPresenter presenter;
    private ReplyAdapter adapter;
    private List<Reply.DataBean.HuifuListBean> dataList = new ArrayList<>();
    private String userPhoneNumber;
    private String xinjianid;
    private int currentPage = 0;
    private String authorPhone;//动态发布者的手机号
    private View headerView;
    TextView tvTitle;
    TextView tvDate;
    TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 如果存在虚拟按键，则设置虚拟按键的背景色
        if (ScreenUtils.checkDeviceHasNavigationBar(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setNavigationBarColor(ContextCompat.getColor(this, android.R.color.black));
            }
        }
        setContentView(R.layout.activity_email_detail);
        unbinder = ButterKnife.bind(this);
        userPhoneNumber = PreferencesUtils.getString(this, MLProperties.PREFER_KEY_USER_ID, "");
        xinjianid = getIntent().getStringExtra("xinjianid");
        initTitleBar();


        initRecyclerHeaderView();
        presenter = new ReplyPresenter(this, this);
        presenter.getReply(xinjianid, currentPage);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReplyAdapter(R.layout.recycler_item_letter_reply, dataList);
        adapter.addHeaderView(headerView);
        adapter.setHeaderAndEmpty(true);



        mRecyclerView.setAdapter(adapter);

        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                currentPage = 0;
                presenter.getReply(xinjianid, currentPage);
            }
        });
        mSmartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                currentPage++;
                presenter.getReply(xinjianid, currentPage);
            }
        });
    }

    private void initRecyclerHeaderView() {
        headerView = getLayoutInflater().inflate(R.layout.recycler_header_email_detail, null);
        tvTitle = (TextView) headerView.findViewById(R.id.tv_title);
        tvDate = (TextView) headerView.findViewById(R.id.tv_date);
        tvContent = (TextView) headerView.findViewById(R.id.tv_content);
    }

    private void initTitleBar() {
        headerImgBack.setVisibility(View.VISIBLE);
        headerTvTitle.setVisibility(View.VISIBLE);
        headerLlLeft.setVisibility(View.VISIBLE);
        //layoutSanjiao.setVisibility(View.VISIBLE);

        headerTvTitle.setText("校长信箱");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


    @OnClick(R.id.header_ll_left)
    public void onClick() {
        finish();
    }

    @OnClick({R.id.header_ll_left, R.id.bt_up, R.id.bt_down, R.id.tv_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_ll_left:
                finish();
                break;
            case R.id.bt_up:
                T.showShort(EmailDetailActivity.this, "上一封");
                break;
            case R.id.bt_down:
                T.showShort(EmailDetailActivity.this, "下一封");
                break;
            case R.id.tv_send:
                if (StringUtils.isEmpty(edtContent.getText().toString())) {
                    T.showLong(EmailDetailActivity.this, "回复内容不能为空");
                } else {
                    String content = Base64Util.encode(edtContent.getText().toString());
                    presenter.sendReply(userPhoneNumber, content, xinjianid,authorPhone);

                    KeyBoardUtils.closeKeybord(this.getCurrentFocus().getWindowToken(), this);
                    edtContent.setText("");
                }
                break;
        }
    }

    @Override
    public void getEmailReply(String result) {

        mSmartRefreshLayout.finishRefresh();
        mSmartRefreshLayout.finishLoadmore();
        adapter.setEmptyView(R.layout.recycler_item_tianyuan_empty2,mRecyclerView);
        Reply reply = JSON.parseObject(result, new TypeReference<Reply>() {
        });

        if (reply.getRet().equals("1")) {

            authorPhone = reply.getData().getOtherphone();

            tvTitle.setText(Base64Util.decode(reply.getData().getTitle()));
            tvDate.setText(reply.getData().getDate());
            tvContent.setText(Base64Util.decode(reply.getData().getContent()));

            if (currentPage == 0) {
                dataList.clear();
            }

            if (reply.getData().getHuifu_list() != null) {

                dataList.clear();
                dataList.addAll(reply.getData().getHuifu_list());
                adapter.notifyDataSetChanged();

            }



        }
    }

    @Override
    public void sendReply(String result) {
        BaseDataInfo dataInfo = JSON.parseObject(result, new TypeReference<BaseDataInfo>() {
        });

        if (dataInfo.getRet().equals("1")) {
            currentPage = 0;
            presenter.getReply(xinjianid, currentPage);
            T.showLong(EmailDetailActivity.this, "回复成功");
        }
    }

}

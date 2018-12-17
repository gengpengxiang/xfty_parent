package com.bj.hmxxparents.email;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bj.hmxxparents.BaseActivity;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.api.MLProperties;
import com.bj.hmxxparents.email.adapter.LetterAdapter;
import com.bj.hmxxparents.email.model.Letter;
import com.bj.hmxxparents.email.presenter.EmailPresenter;
import com.bj.hmxxparents.email.view.IViewEmail;
import com.bj.hmxxparents.entity.BaseDataInfo;
import com.bj.hmxxparents.entity.MessageEvent;
import com.bj.hmxxparents.utils.Base64Util;
import com.bj.hmxxparents.utils.PreferencesUtils;
import com.bj.hmxxparents.utils.ScreenUtils;
import com.bj.hmxxparents.utils.T;
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

public class EmailActivity extends BaseActivity implements IViewEmail {

    @BindView(R.id.header_img_back)
    ImageView headerImgBack;
    @BindView(R.id.header_tv_title)
    TextView headerTvTitle;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mSmartRefreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.bt_delete)
    Button btDelete;
    @BindView(R.id.header_iv_edit)
    ImageView headerIvEdit;
    @BindView(R.id.header_ll_right)
    LinearLayout headerLlRight;
    @BindView(R.id.header_ll_left)
    LinearLayout headerLlLeft;
    private Unbinder unbinder;

    private LetterAdapter adapter;
    private EmailPresenter presenter;
    private List<Letter.DataBean.ListDataBean> dataList = new ArrayList<>();

    private boolean isLongClick = false;
    private String userPhoneNumber;
    private int pos = -1;
    private TabLayout.Tab tab1, tab2, tab3;
    private int currentPage = 0;
    private int currentTab = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 如果存在虚拟按键，则设置虚拟按键的背景色
        if (ScreenUtils.checkDeviceHasNavigationBar(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setNavigationBarColor(ContextCompat.getColor(this, android.R.color.black));
            }
        }
        setContentView(R.layout.activity_email);
        unbinder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        headerImgBack.setVisibility(View.VISIBLE);
        headerLlRight.setVisibility(View.VISIBLE);
        headerIvEdit.setVisibility(View.VISIBLE);
        headerTvTitle.setVisibility(View.VISIBLE);
        headerTvTitle.setText("校长信箱");

        userPhoneNumber = PreferencesUtils.getString(EmailActivity.this, MLProperties.PREFER_KEY_USER_ID);

        presenter = new EmailPresenter(this, this);
        presenter.getEmailList(currentPage, userPhoneNumber, "quanbu");

        LinearLayout linearLayout = (LinearLayout) tabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout.setDividerDrawable(ContextCompat.getDrawable(this, R.drawable.layout_divider_vertical));
        //linearLayout.setDividerPadding(8);


        tab1 = tabLayout.getTabAt(0);
        tab2 = tabLayout.getTabAt(1);
        tab3 = tabLayout.getTabAt(2);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (isLongClick) {

                } else {
                    mSmartRefreshLayout.autoRefresh();
                    if (tab.getPosition() == 0) {
                        currentPage = 0;
                        presenter.getEmailList(currentPage, userPhoneNumber, "quanbu");
                    }
                    if (tab.getPosition() == 1) {
                        currentPage = 0;
                        presenter.getEmailList(currentPage, userPhoneNumber, "huifu");
                    }
                    if (tab.getPosition() == 2) {
                        currentPage = 0;
                        presenter.getEmailList(currentPage, userPhoneNumber, "caogao");
                    }
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(2));

        adapter = new LetterAdapter(R.layout.recycler_item_letter, dataList);
        mRecyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                if (isLongClick) {
                    return;
                }

                if (tabLayout.getSelectedTabPosition() == 2) {
                    Intent intent = new Intent(EmailActivity.this, EditActivity.class);
                    intent.putExtra("title", Base64Util.decode(dataList.get(position).getTitle()));
                    intent.putExtra("content", Base64Util.decode(dataList.get(position).getContent()));
                    intent.putExtra("xinjianid",dataList.get(position).getXinjianid());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(EmailActivity.this, EmailDetailActivity.class);
                    intent.putExtra("xinjianid", dataList.get(position).getXinjianid());
                    startActivity(intent);
                }
            }
        });
        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter2, View view, int position) {

                if(!isLongClick) {

                    btDelete.setVisibility(View.VISIBLE);
                    isLongClick = true;

                    pos = position;

                    dataList.get(position).setLongPress(true);

                    adapter.notifyDataSetChanged();

                    setTabLayoutClickable(false);
                }
                return true;
            }
        });

        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                currentPage = 0;
                refresh(currentPage);


            }
        });
        mSmartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                currentPage++;
                refresh(currentPage);

            }
        });

    }

    private void setTabLayoutClickable(boolean b) {
        LinearLayout tabStrip = (LinearLayout) tabLayout.getChildAt(0);
        for (int i = 0; i < tabStrip.getChildCount(); i++) {
            View tabView = tabStrip.getChildAt(i);
            if (tabView != null) {
                tabView.setClickable(b);
            }
        }
    }

    private void refresh(int page) {
        if (tabLayout.getSelectedTabPosition() == 0) {
            presenter.getEmailList(page, userPhoneNumber, "quanbu");
        }
        if (tabLayout.getSelectedTabPosition() == 1) {
            presenter.getEmailList(page, userPhoneNumber, "huifu");
        }
        if (tabLayout.getSelectedTabPosition() == 2) {
            presenter.getEmailList(page, userPhoneNumber, "caogao");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isLongClick) {
                isLongClick = false;
                btDelete.setVisibility(View.GONE);
                dataList.get(pos).setLongPress(false);
                adapter.notifyDataSetChanged();

                setTabLayoutClickable(true);
            } else {
                setResult(222);
                finish();

            }
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        presenter.onDestory();
        EventBus.getDefault().unregister(this);


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void update(MessageEvent event){
        if(event.getMessage().equals("writelettersuccess")){
            refresh(0);
        }
    }

    @OnClick({R.id.header_ll_left, R.id.header_ll_right, R.id.bt_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_ll_left:
                //finish();

                if (isLongClick) {
                    isLongClick = false;
                    btDelete.setVisibility(View.GONE);
                    dataList.get(pos).setLongPress(false);
                    adapter.notifyDataSetChanged();

                    setTabLayoutClickable(true);
                } else {
                    setResult(222);
                    finish();
                }

                break;
            case R.id.header_ll_right:

                if (isLongClick) {
//                    isLongClick = false;
//                    btDelete.setVisibility(View.GONE);
//                    dataList.get(pos).setLongPress(false);
//                    adapter.notifyDataSetChanged();
                    //T.showShort(EmailActivity.this, "当前状态不能编辑");
                } else {
                    Intent intent = new Intent(EmailActivity.this, EditActivity.class);
                    intent.putExtra("title", "");
                    intent.putExtra("content", "");
                    intent.putExtra("xinjianid","no");
                    startActivity(intent);
                }

                break;
            case R.id.bt_delete:
                String id = dataList.get(pos).getXinjianid();
                presenter.delete(id);
                break;
        }
    }

    @Override
    public void getEmailList(String emailInfo) {

        adapter.setEmptyView(R.layout.recycler_item_tianyuan_empty,mRecyclerView);

        if (mSmartRefreshLayout.isRefreshing()) {
            mSmartRefreshLayout.finishRefresh();
        }
        if (mSmartRefreshLayout.isLoading()) {
            mSmartRefreshLayout.finishLoadmore();
        }

        Letter letter = JSON.parseObject(emailInfo, new TypeReference<Letter>() {
        });

        if (letter.getRet().equals("1")) {//全部查询成功
//            tab1.setText("全部(" + letter.getData().getQuanbu_num() + ")");
//            tab2.setText("已回复(" + letter.getData().getHuifu_num() + ")");
//            tab3.setText("草稿箱(");

            if (currentPage == 0) {
                dataList.clear();
            }
            dataList.addAll(letter.getData().getList_data());
            adapter.notifyDataSetChanged();
        }

        if (letter.getData() == null) {
            if (currentPage == 0) {
                dataList.clear();
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void delete(String result) {

        BaseDataInfo dataInfo = JSON.parseObject(result, new TypeReference<BaseDataInfo>() {
        });

        if (dataInfo.getRet().equals("1")) {
            setTabLayoutClickable(true);
            if (isLongClick) {
                isLongClick = false;
                btDelete.setVisibility(View.GONE);
                dataList.get(pos).setLongPress(false);
                adapter.notifyDataSetChanged();

                setTabLayoutClickable(true);
            }

            currentPage = 0;
            refresh(currentPage);

        }
    }

}

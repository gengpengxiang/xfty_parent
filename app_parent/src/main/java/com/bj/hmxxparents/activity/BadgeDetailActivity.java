package com.bj.hmxxparents.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.bj.hmxxparents.BaseActivity;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.adapter.LatestNewsHomeAdapter;
import com.bj.hmxxparents.api.Constant;
import com.bj.hmxxparents.api.LmsDataService;
import com.bj.hmxxparents.api.MLConfig;
import com.bj.hmxxparents.api.MLProperties;
import com.bj.hmxxparents.entity.BadgeType;
import com.bj.hmxxparents.entity.ClassNewsInfo;
import com.bj.hmxxparents.entity.SubjectInfo;
import com.bj.hmxxparents.utils.LL;
import com.bj.hmxxparents.utils.PreferencesUtils;
import com.bj.hmxxparents.utils.StringUtils;
import com.bj.hmxxparents.utils.T;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.umeng.analytics.MobclickAgent;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.CancelConfirmAlertDialog;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zz379 on 2017/2/17.
 * 徽章二级页面，即在首页点击徽章图标进入的页面
 */

public class BadgeDetailActivity extends BaseActivity {

    @BindView(R.id.header_img_back)
    ImageView imgBack;
    @BindView(R.id.header_tv_title)
    TextView tvTitle;
    @BindView(R.id.header_img_question)
    ImageView imgQuestion;
    @BindView(R.id.mTabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.mTagFlowLayout)
    TagFlowLayout mTagFlowLayout;
    @BindView(R.id.mXRefreshView)
    XRefreshView mXRefreshView;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    private LatestNewsHomeAdapter mAdapter;
    private int currentPage = 1;
    public static long lastRefreshTime;
    private List<ClassNewsInfo> mDataList = new ArrayList<>();
    private View headerView;
    private String kidId;
    //    private String badgeTypeID = "0";
    private int badgeTypeID = 0;
    private String xuekeTypeID = "00";

    private List<SubjectInfo> mSubjectList = new ArrayList<>();
    private LayoutInflater mInflater;
    private TagAdapter<SubjectInfo> mTagAdapter;

    private List<BadgeType> mTabDataList = new ArrayList<>();
    private int minTabSize;

    private String firstContent;
    private String userPhone;
    private String userClassName;
    private String userRelation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badge_detail);
        ButterKnife.bind(this);

        DisplayMetrics display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);
        minTabSize = (display.widthPixels / (display.densityDpi / 160)) / 70;
        LL.i("页面Tab开始滑动的阀值：" + minTabSize);

        // 初始化页面
        initToolBar();
        initView();
        initDatas();
    }

    private void initToolBar() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("徽章");
        imgBack.setVisibility(View.VISIBLE);
        imgQuestion.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.header_ll_left)
    void actionBackClick() {
        this.finish();
    }

    @OnClick(R.id.header_ll_right)
    void actionTipsClick() {
        showTipsDialog(getString(R.string.tips_badge));
    }


    private void initView() {
        mInflater = LayoutInflater.from(this);
        kidId = getIntent().getStringExtra(MLProperties.BUNDLE_KEY_KID_ID);
        userPhone = PreferencesUtils.getString(this, MLProperties.PREFER_KEY_USER_ID);
        userClassName = PreferencesUtils.getString(this, MLProperties.BUNDLE_KEY_CLASS_NAME, "");
        userRelation = PreferencesUtils.getString(this, MLProperties.BUNDLE_KEY_KID_RELATION, "");

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ((TextView) tab.getCustomView().findViewById(R.id.tv_name)).setTextColor(ContextCompat.getColor(BadgeDetailActivity.this, R.color.text_tab_selected));
                ((TextView) tab.getCustomView().findViewById(R.id.tv_number)).setTextColor(ContextCompat.getColor(BadgeDetailActivity.this, R.color.text_tab_selected));

                badgeTypeID = mTabDataList.get(tab.getPosition()).getType_id();
                xuekeTypeID = "00";
                //initXuekeInfo(mTabDataList.get(tab.getPosition()).getXuekeList());

                mXRefreshView.startRefresh();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ((TextView) tab.getCustomView().findViewById(R.id.tv_name)).setTextColor(ContextCompat.getColor(BadgeDetailActivity.this, R.color.text_tab_unselected_2));
                ((TextView) tab.getCustomView().findViewById(R.id.tv_number)).setTextColor(ContextCompat.getColor(BadgeDetailActivity.this, R.color.text_tab_unselected_2));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        // 设置分割线
//        LinearLayout linearLayout = (LinearLayout) mTabLayout.getChildAt(0);
//        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
//        linearLayout.setDividerDrawable(ContextCompat.getDrawable(this, R.drawable.divider_tablayout_badge));
//        linearLayout.setDividerPadding(DensityUtils.dp2px(this, 10));

        // 初始化流式布局
        mTagAdapter = new TagAdapter<SubjectInfo>(mSubjectList) {
            @Override
            public View getView(FlowLayout parent, int position, SubjectInfo subjectInfo) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv_subject_tag, mTagFlowLayout, false);
                tv.setText(subjectInfo.getSubName() + " " + subjectInfo.getSubBadgeCount());
                return tv;
            }
        };
        mTagFlowLayout.setAdapter(mTagAdapter);
        mTagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                mTagAdapter.setSelectedList(position);
                xuekeTypeID = mSubjectList.get(position).getSubID();

                mXRefreshView.startRefresh();
                return true;
            }
        });

        mRecyclerView.setHasFixedSize(true);
        // look as listview
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // set Adatper
        mAdapter = new LatestNewsHomeAdapter(mDataList, getString(R.string.text_desc_empty_news_badge));
        mAdapter.setOnMyItemClickListener(new LatestNewsHomeAdapter.OnMyItemClickListener() {
            @Override
            public void onClick(View view, int position) {

                switch (view.getId()) {
                    case R.id.tv_thanks_teacher:
                        if (mDataList.get(position - 1).getNewsType().equals("z1")) {
                            firstContent = String.format("谢谢老师鼓励的点赞“%s”", mDataList.get(position - 1).getNewsTitle());
                        } else {
                            firstContent = String.format("谢谢老师奖励的徽章“%s”", mDataList.get(position - 1).getNewsTitle());
                        }
                        myThanksTeacherTask(position - 1);
                        break;
                    case R.id.layout_item:
                        if (!mDataList.get(position - 1).getNewsType().equals("z1")) {
                            Intent intent = new Intent(BadgeDetailActivity.this, BadgeActivity.class);
                            intent.putExtra("name", mDataList.get(position - 1).getNewsTitle());
                            intent.putExtra("shuoming", mDataList.get(position - 1).getNewsDesc());
                            intent.putExtra("xueke", mDataList.get(position - 1).getXueke());
                            intent.putExtra("huodeNum", mDataList.get(position - 1).getHuode_num());
                            intent.putExtra("xueke_img", mDataList.get(position - 1).getXueke_img());
                            startActivity(intent);
                        }
                        break;
                }


//                if (mDataList.get(position - 1).getNewsType().equals("z1")) {
//                    T.showLong(BadgeDetailActivity.this,"鼓励");
//                    firstContent = String.format("谢谢老师鼓励的点赞“%s”", mDataList.get(position - 1).getNewsTitle());
//                } else {
//                    firstContent = String.format("谢谢老师奖励的徽章“%s”", mDataList.get(position - 1).getNewsTitle());
//                    T.showLong(BadgeDetailActivity.this,"奖励");
//                }
//                myThanksTeacherTask(position - 1);
            }
        });
        headerView = mAdapter.setHeaderView(R.layout.recycler_header_line_textview_line, mRecyclerView);
        initHeaderView();

        mRecyclerView.setAdapter(mAdapter);

        // set xRefreshView
        mXRefreshView.setPullRefreshEnable(true);
        mXRefreshView.setPullLoadEnable(true);
        mXRefreshView.restoreLastRefreshTime(lastRefreshTime);
        mXRefreshView.setAutoRefresh(false);
        mXRefreshView.setAutoLoadMore(true);

        mXRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isRefresh) {
                LL.i("刷新数据");
                currentPage = 1;
//                mXRefreshView.setAutoLoadMore(true);
//                mXRefreshView.setPullLoadEnable(true);

                MyGetStudentAllNewsTask myGetStudentAllNewsTask = new MyGetStudentAllNewsTask();
                myGetStudentAllNewsTask.execute(kidId, String.valueOf(currentPage));
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                LL.i("加载更多数据");
                currentPage++;
                MyGetStudentAllNewsTask myGetStudentAllNewsTask = new MyGetStudentAllNewsTask();
                myGetStudentAllNewsTask.execute(kidId, String.valueOf(currentPage));
            }
        });
    }

//    private void initXuekeInfo(List<SubjectInfo> xuekeList) {
//        mSubjectList.clear();
//        mSubjectList.addAll(xuekeList);
//
//        if (mSubjectList.size() > 0) {
//            mTagAdapter.setSelectedList(0);
//        }
//        mTagAdapter.notifyDataChanged();
//    }

    private void initHeaderView() {
        TextView tv = ButterKnife.findById(headerView, R.id.tv_latest_news);
        tv.setText("徽章记录");
    }

    private void initDatas() {
        mDataList.clear();

        MyClassCommendTypeAsyncTask task = new MyClassCommendTypeAsyncTask();
        task.execute();
    }

    private class MyClassCommendTypeAsyncTask extends AsyncTask<String, Integer, List<BadgeType>> {

        @Override
        protected List<BadgeType> doInBackground(String... params) {
            LmsDataService mService = new LmsDataService();
            List<BadgeType> newList;
            try {
                newList = mService.getClassBadgeTypeListFromAPI2(kidId);
            } catch (Exception e) {
                e.printStackTrace();
                newList = new ArrayList<>();
            }
            return newList;
        }

        @Override
        protected void onPostExecute(List<BadgeType> badgeTypeList) {
            if (badgeTypeList == null) {
                Log.e("开小差listnull","true");
                T.showShort(BadgeDetailActivity.this, "服务器开小差了，请待会重试");
            } else if (badgeTypeList.size() == 0) {
                T.showShort(BadgeDetailActivity.this, "数据异常");
            } else {
                updateCommendType(badgeTypeList);
            }
        }
    }

    private void updateCommendType(List<BadgeType> subjectInfos) {
        mTabDataList.clear();
        mTabDataList.addAll(subjectInfos);

        for (BadgeType item : mTabDataList) {
            View mTabView = LayoutInflater.from(this).inflate(R.layout.custom_tab_badge, null);
            TextView tvName = (TextView) mTabView.findViewById(R.id.tv_name);
            TextView tvNumber = (TextView) mTabView.findViewById(R.id.tv_number);
//            tvName.setText(item.getName());
//            tvNumber.setText(String.valueOf(item.getNumber()));
            tvName.setText(item.getType_name());
            //tvNumber.setText(String.valueOf(item.getNumber()));
            mTabLayout.addTab(mTabLayout.newTab().setCustomView(mTabView));
        }

        if (mTabDataList.size() <= minTabSize) {
            //mTabLayout.setTabMode(TabLayout.MODE_FIXED);
            mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        } else {
            mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }

        //initXuekeInfo(mTabDataList.get(0).getXuekeList());
    }

    private class MyGetStudentAllNewsTask extends AsyncTask<String, Integer, List<ClassNewsInfo>> {

        @Override
        protected List<ClassNewsInfo> doInBackground(String... params) {
            String studentID = params[0];
            String pageIndex = params[1];
            LmsDataService mService = new LmsDataService();
            List<ClassNewsInfo> dataList;
            try {
                dataList = mService.getClassBadgeNewsFromAPI(kidId, String.valueOf(badgeTypeID), xuekeTypeID, pageIndex);
            } catch (Exception e) {
                e.printStackTrace();
                LL.e(e);
                dataList = null;
            }
            return dataList;
        }

        @Override
        protected void onPostExecute(List<ClassNewsInfo> result) {
            if (result == null) {
                cleanXRefreshView();
                T.showShort(BadgeDetailActivity.this, "服务器开小差了，请重试");
            } else {
                loadData(result);
            }
        }
    }

    private void loadData(List<ClassNewsInfo> list) {
        lastRefreshTime = mXRefreshView.getLastRefreshTime();
        if (mXRefreshView.mPullRefreshing) {
            mDataList.clear();
            mXRefreshView.stopRefresh();
            mXRefreshView.setAutoLoadMore(true);
            mXRefreshView.setPullLoadEnable(true);
        }
        if (list == null || list.size() < 10) {
            mXRefreshView.setPullLoadEnable(false);
        }
        mXRefreshView.stopLoadMore();
        // 更新数据
        mDataList.addAll(list);
        mAdapter.notifyDataSetChanged();
        if (null == mAdapter.getCustomLoadMoreView()) {
            mAdapter.setCustomLoadMoreView(new XRefreshViewFooter(this));
        }
    }

    private void cleanXRefreshView() {
        mXRefreshView.stopRefresh();
        mXRefreshView.stopLoadMore();
    }

    private void myThanksTeacherTask(int position) {
        Observable.create((ObservableOnSubscribe<String[]>) emitter -> {
            LmsDataService mService = new LmsDataService();
            String newsID = mDataList.get(position).getNewsId();
            String[] result = mService.getThanksTeacherResultFromAPI(newsID, String.valueOf(position));
            emitter.onNext(result);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String[]>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String[] result) {
                        if (!StringUtils.isEmpty(result[0]) && result[0].equals("1")) {
                            T.showShort(BadgeDetailActivity.this, "感谢成功");
                            if (position < mDataList.size()) {
                                mDataList.get(position).setNewsThanksStatus("2");
                                mAdapter.notifyDataSetChanged();
                            }
                            // 跳转到聊天页面，并保存会话记录
                            //showCommunicationDialog(position);
                        } else {
                            T.showShort(BadgeDetailActivity.this, "感谢失败，请重试");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        T.showShort(BadgeDetailActivity.this, "服务器开小差了，请重试");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void showCommunicationDialog(int position) {
        CancelConfirmAlertDialog dialog = new CancelConfirmAlertDialog(this)
                .setTitleText("一对一沟通")
                .setContentText("您是否需要和老师一对一沟通？")
                .setCancelText("取消")
                .setConfirmText("去沟通")
                .setCancelClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.dismiss();
                }).setConfirmClickListener(sweetAlertDialog -> {
                    // 保存会话记录
                    saveConversation(mDataList.get(position).getTeacherPhone());
                    // 跳转到沟通页面，并发送一条固定的消息
                    sendMessage(position, firstContent);
                    sweetAlertDialog.dismiss();
                });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void sendMessage(int position, String content) {
        String userID = MLConfig.EASE_TEACHER_ID_PREFIX + mDataList.get(position).getTeacherPhone();
        String currUserNick = PreferencesUtils.getString(this, MLProperties.BUNDLE_KEY_KID_NAME);
        if (StringUtils.isEmpty(userRelation)) {
            currUserNick = currUserNick + "的家长";
        } else if ("baba".equals(userRelation)) {
            currUserNick = currUserNick + "的爸爸";
        } else if ("mama".equals(userRelation)) {
            currUserNick = currUserNick + "的妈妈";
        }
        //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        EMMessage message = EMMessage.createTxtSendMessage(content, userID);
        message.setAttribute(Constant.EXTRA_CURR_USER_NICK, currUserNick);
        message.setAttribute(Constant.EXTRA_CURR_USER_PHOTO, PreferencesUtils.getString(this, MLProperties.BUNDLE_KEY_KID_IMG));
        message.setAttribute(Constant.EXTRA_TO_USER_NICK, mDataList.get(position).getTeacherName());
        message.setAttribute(Constant.EXTRA_TO_USER_PHOTO, mDataList.get(position).getTeacherPic());
        message.setAttribute(EaseConstant.EXTRA_TO_CLASS_NAME, userClassName);
        //发送消息
        EMClient.getInstance().chatManager().sendMessage(message);

        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(Constant.EXTRA_USER_ID, userID);
        intent.putExtra(Constant.EXTRA_TO_USER_NICK, mDataList.get(position).getTeacherName());
        intent.putExtra(Constant.EXTRA_TO_USER_PHOTO, mDataList.get(position).getTeacherPic());
        startActivity(intent);
    }

    private void saveConversation(String teacherPhone) {
        Observable.create((ObservableOnSubscribe<String[]>) e -> {
            LmsDataService mService = new LmsDataService();
            String[] result = mService.saveConversationFromAPI(teacherPhone, userPhone);
            e.onNext(result);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String[]>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String[] strings) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("badge");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("badge");
        MobclickAgent.onPause(this);
    }
}

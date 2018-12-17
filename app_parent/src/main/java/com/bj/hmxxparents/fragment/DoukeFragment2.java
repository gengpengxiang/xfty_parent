package com.bj.hmxxparents.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.bj.hmxxparents.BaseFragment;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.activity.DoukeDetailActivity;
import com.bj.hmxxparents.activity.LoadingGameActivity;
import com.bj.hmxxparents.adapter.DoukeAdapter;
import com.bj.hmxxparents.api.LmsDataService;
import com.bj.hmxxparents.api.MLProperties;
import com.bj.hmxxparents.entity.ArticleInfo;
import com.bj.hmxxparents.utils.LL;
import com.bj.hmxxparents.utils.PreferencesUtils;
import com.bj.hmxxparents.utils.StringUtils;
import com.bj.hmxxparents.utils.T;
import com.bj.hmxxparents.widget.DecorationForDouke;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.douhao.game.entity.ChallengeInfo;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zz379 on 2017/4/ic_pinde.
 * 逗课文章列表页面
 */

public class DoukeFragment2 extends BaseFragment {

    @BindView(R.id.mXRefreshView)
    XRefreshView mXRefreshView;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.header_tv_title)
    TextView tvTitle;

    private View headerView;
    private TextView tvChallengeContent;
    private ChallengeInfo mChallengeInfo;

    private DoukeAdapter mAdapter;
    private int currentPage = 1;
    public static long lastRefreshTime;
    private List<ArticleInfo> mDataList = new ArrayList<>();
    private String kidId;

    private String jianjie;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_douke, container, false);
        ButterKnife.bind(this, view);

        initToolbar();
        initView();

        initData();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        initData();
    }

    private void initToolbar() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.bottom_tab_1);
    }



    private void initView() {
        // 下拉刷新控件
        mRecyclerView.setHasFixedSize(true);
        // look as listview
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // set Adatper
        mAdapter = new DoukeAdapter(R.layout.recycler_item_douke,mDataList);

       mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
           @Override
           public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
               String path = mDataList.get(position ).getArticlePath();
               String id = mDataList.get(position ).getArticleID();

               if (!StringUtils.isEmpty(path)) {
                   Intent intent = new Intent(getActivity(), DoukeDetailActivity.class);
                   intent.putExtra(MLProperties.BUNDLE_KEY_DOUKE_ID, id);
                   intent.putExtra(MLProperties.BUNDLE_KEY_DOUKE_URL, path);
                   startActivity(intent);
               } else {
                   T.showShort(getActivity(), "页面不存在");
               }
           }
       });
        // 添加header
//        headerView = mAdapter.setHeaderView(R.layout.recycler_header_reading_game_enter, mRecyclerView);
        headerView = getActivity().getLayoutInflater().inflate(R.layout.recycler_header_reading_game_enter, null);
        //mAdapter.addHeaderView(headerView);
//        initHeaderView();
        mRecyclerView.addItemDecoration(new DecorationForDouke(getActivity(), LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

        initHeaderView();

        // set xRefreshView
        mXRefreshView.setMoveForHorizontal(true);   // 在手指横向移动的时候，让XRefreshView不拦截事件
        mXRefreshView.setPullRefreshEnable(true);
        mXRefreshView.setPullLoadEnable(true);
        mXRefreshView.restoreLastRefreshTime(lastRefreshTime);
        mXRefreshView.setAutoRefresh(false);
        mXRefreshView.setAutoLoadMore(false);
//        mXRefreshView.setEmptyView(R.layout.recycler_item_douke_empty);
        mXRefreshView.setEmptyView(R.layout.recycler_item_tianyuan_empty);
        mXRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                LL.i("刷新数据");
                currentPage = 1;
                lastRefreshTime = mXRefreshView.getLastRefreshTime();
                getStudentChallengeInfo();

                getDoukeList();
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                currentPage++;
                getDoukeList();

            }
        });
    }

    private void getDoukeList() {

        Observable.create(new ObservableOnSubscribe<List<ArticleInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<List<ArticleInfo>> e) throws Exception {
                LmsDataService mService = new LmsDataService();

                List<ArticleInfo> dataList = mService.getDouKeListFromAPI(currentPage);
                e.onNext(dataList);
                e.onComplete();

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ArticleInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<ArticleInfo> articleInfos) {
                        cleanXRefreshView();

                        if(currentPage==1){
                            mDataList.clear();
                            mDataList.addAll(articleInfos);
                            mAdapter.notifyDataSetChanged();
                            //mXRefreshView.setPullLoadEnable(true);
                        }else {
                            mDataList.addAll(articleInfos);
                            mAdapter.notifyDataSetChanged();
                            //mXRefreshView.setPullLoadEnable(true);
                        }

                        //loadData(articleInfos);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void initHeaderView() {
        tvChallengeContent = (TextView) headerView.findViewById(R.id.tv_game_content);
        RelativeLayout rlReadingGameEnter = (RelativeLayout) headerView.findViewById(R.id.rl_reading_game);
        rlReadingGameEnter.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt(MLProperties.BUNDLE_KEY_GAME_LEVEL_NUMBER, mChallengeInfo.getChallengeNumber());
            bundle.putInt(MLProperties.BUNDLE_KEY_GAME_SCORE_COUNT, mChallengeInfo.getChallengeScore());
            bundle.putInt(MLProperties.BUNDLE_KEY_GAME_RANK_COUNT, mChallengeInfo.getChallengeRank());
            LoadingGameActivity.intentToLoadingGame(getActivity(), bundle);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // 更新挑战赛相关信息
        getStudentChallengeInfo();
    }

    @Override
    protected void onVisible() {
        super.onVisible();
        MobclickAgent.onPageStart("douke");
    }

    @Override
    protected void onInVisible() {
        super.onInVisible();
        MobclickAgent.onPageEnd("douke");
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void initData() {
        kidId = PreferencesUtils.getString(getActivity(), MLProperties.BUNDLE_KEY_KID_ID);
        currentPage = 1;
        mDataList.clear();

        getDoukeList();

//        MyGetStudentAllNewsTask myGetStudentAllNewsTask = new MyGetStudentAllNewsTask();
//        myGetStudentAllNewsTask.execute();
    }

//    private void loadData(List<ArticleInfo> list) {
//        lastRefreshTime = mXRefreshView.getLastRefreshTime();
//        if (mXRefreshView.mPullRefreshing) {
//            mDataList.clear();
//            mXRefreshView.stopRefresh();
//            mXRefreshView.setAutoLoadMore(true);
//            mXRefreshView.setPullLoadEnable(true);
//        }
//        if (list == null || list.size() < 10) {
//            mXRefreshView.setPullLoadEnable(false);
//        }
//        mXRefreshView.stopLoadMore();
//        // 更新数据
//        mDataList.addAll(list);
//        mAdapter.notifyDataSetChanged();
//        if (null == mAdapter.getCustomLoadMoreView()) {
//            mAdapter.setCustomLoadMoreView(new XRefreshViewFooter(getActivity()));
//        }
//    }

    private void cleanXRefreshView() {
        mXRefreshView.stopRefresh();
        mXRefreshView.stopLoadMore();
    }

    private void getStudentChallengeInfo() {
        Observable.create((ObservableOnSubscribe<ChallengeInfo>) e -> {
            LmsDataService mService = new LmsDataService();
            mChallengeInfo = mService.getStudentChallengeInfoFromAPI(kidId);
            e.onNext(mChallengeInfo);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ChallengeInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ChallengeInfo challengeInfo) {
                        String content = "挑战" + challengeInfo.getChallengeNumber() + "次，"
                                + "得分" + challengeInfo.getChallengeScore() + "，"
                                + "全国排名" + (challengeInfo.getChallengeRank() == 0 ? "千里之外" : "" +
                                challengeInfo.getChallengeRank());
                        tvChallengeContent.setText(content);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}

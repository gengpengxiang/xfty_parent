package com.bj.hmxxparents.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.bj.hmxxparents.api.LmsDataService;
import com.bj.hmxxparents.api.MLProperties;
import com.bj.hmxxparents.entity.EventLevelInfos;
import com.bj.hmxxparents.utils.PreferencesUtils;
import com.bj.hmxxparents.utils.ScreenUtils;
import com.bj.hmxxparents.utils.T;
import com.douhao.game.activity.DouhaoLoadingGameActivity;
import com.douhao.game.entity.LevelInfo;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zz379 on 2017/6/5.
 * 字词挑战赛预加载页面，即点击首页字词挑战赛后首先进入的一个页面
 */

public class LoadingGameActivity extends DouhaoLoadingGameActivity {

    public static void intentToLoadingGame(Context context, Bundle extras) {
        Intent intent = new Intent(context, LoadingGameActivity.class);
        intent.putExtras(extras);
        context.startActivity(intent);
    }

    private CompositeDisposable disposables;
    public boolean isCacheData = false;
    public Bundle extraDatas;
    public int lvlNumber;
    public int scoreCount;
    public int rankCount;
    private String kidID;
    private List<LevelInfo> cacheData = new ArrayList<>();

    private int pbProgress = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // 全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        // 如果存在虚拟按键，则设置虚拟按键的背景色
        if (ScreenUtils.checkDeviceHasNavigationBar(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setNavigationBarColor(ContextCompat.getColor(this, android.R.color.black));
            }
        }
        disposables = new CompositeDisposable();
    }

    @Override
    protected void initView() {
        super.initView();
        tvStartGame.setOnClickListener(view -> {
            disposables.clear();
            // 发送数据
            EventBus.getDefault().postSticky(new EventLevelInfos(cacheData));
            ReadingGameActivity.intentToReadingGame(this, extraDatas);
            LoadingGameActivity.this.finish();
        });
    }

    @Override
    protected void initData() {
        kidID = PreferencesUtils.getString(this, MLProperties.BUNDLE_KEY_KID_ID);
        super.initData();
        extraDatas = getIntent().getExtras();
        lvlNumber = extraDatas.getInt(MLProperties.BUNDLE_KEY_GAME_LEVEL_NUMBER, 0);
        scoreCount = extraDatas.getInt(MLProperties.BUNDLE_KEY_GAME_SCORE_COUNT, 0);
        rankCount = extraDatas.getInt(MLProperties.BUNDLE_KEY_GAME_RANK_COUNT, 0);
        // 是否是第一次进入
        if (lvlNumber == 0) {
            tvStartGame.setVisibility(View.VISIBLE);
            tvTips.setText("按下【按住朗读】按钮，大声朗读页面字词，与全国参赛者一起挑战");
        } else {
            tvStartGame.setVisibility(View.INVISIBLE);
            tvTips.setText("做的不错\n欢迎再次回来\n继续努力");
        }
        // 音乐准备好之后开始播放
        bgMusicPlayer.setOnPreparedListener(mp -> {
            if (!mp.isPlaying()) {
                mp.start();
            }
            startProgress(3);
        });
        // 缓存第一批数据
        getLevelInfosFromAPI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("loadingGame");
        MobclickAgent.onResume(this);
        // 这种情况代表从后台重新回到这个页面
        if (pbProgress > 0) {
            // 缓存第一批数据
            getLevelInfosFromAPI();
            startProgress(3);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("loadingGame");
        MobclickAgent.onPause(this);
        disposables.clear();
        isCacheData = false;
        pbProgress = pbTime.getProgress();
    }

    public void startProgress(int time) {
        if (time < 0) time = 0;
        final int countTime = time * 1000;
        pbTime.setMax(countTime);
        Observable.interval(1, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                // .take(countTime + 1)
                .map(aLong -> aLong.intValue())
                .filter(integer -> integer % 100 == 0)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i("TianzigeView", "开始计时");
                        disposables.add(d);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        if ((integer + pbProgress) <= countTime) {
                            Log.i("TianzigeView", "当前进度" + (integer + pbProgress));
                            pbTime.setProgress(integer + pbProgress);
                        } else {
                            if (isCacheData) {
                                isCacheData = false;
                                disposables.clear();
                                if (lvlNumber == 0) {
                                    tvStartGame.setEnabled(true);
                                    tvStartGame.setClickable(true);
                                } else {
                                    // 发送数据
                                    EventBus.getDefault().postSticky(new EventLevelInfos(cacheData));
                                    ReadingGameActivity.intentToReadingGame(LoadingGameActivity.this, extraDatas);
                                    LoadingGameActivity.this.finish();
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.i("TianzigeView", "完成计时");
                    }
                });
    }

    @Override
    public void onBackPressed() {
        disposables.clear();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.dispose();
    }

    private void getLevelInfosFromAPI() {
        Observable.create((ObservableOnSubscribe<List<LevelInfo>>) e -> {
            LmsDataService mService = new LmsDataService();
            List<LevelInfo> dataList = mService.getNextLevelsFromAPI(kidID, lvlNumber, 7);
            e.onNext(dataList);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retry(5)
                .subscribe(new Observer<List<LevelInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<LevelInfo> levelInfos) {
                        cacheData.clear();
                        cacheData.addAll(levelInfos);
                        isCacheData = true;
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("way", "onError()");
                        T.showShort(LoadingGameActivity.this, "服务器开小差了，请稍后重试");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}

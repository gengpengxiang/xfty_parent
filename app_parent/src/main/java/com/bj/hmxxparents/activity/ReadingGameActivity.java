package com.bj.hmxxparents.activity;

import android.Manifest;
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
import com.bj.hmxxparents.utils.StringUtils;
import com.bj.hmxxparents.utils.T;
import com.bj.hmxxparents.widget.LoadingDialog;
import com.douhao.game.activity.DouhaoReadingGameActivity;
import com.douhao.game.entity.ChallengeInfo;
import com.douhao.game.entity.LevelInfo;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.pedant.SweetAlert.CancelConfirmAlertDialog;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zz379 on 2017/5/27.
 * 字词朗读挑战赛，闯关页面
 */

public class ReadingGameActivity extends DouhaoReadingGameActivity {

    public static void intentToReadingGame(Context context, Bundle extras) {
        Intent intent = new Intent(context, ReadingGameActivity.class);
        intent.putExtras(extras);
        context.startActivity(intent);
    }

    private LoadingDialog loadingDialog;
    private String kidID;

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
        // 加载数据弹框
        loadingDialog = new LoadingDialog(this);
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        llHeaderLeft.setOnClickListener(view -> {
            showExitDialot();
        });
    }

    private void showExitDialot() {
        CancelConfirmAlertDialog dialog = new CancelConfirmAlertDialog(this);
        dialog.setTitleText("退出挑战");
        dialog.setContentText("您是否确认退出挑战？");
        dialog.setCancelText("取消");
        dialog.setConfirmText("退出挑战");
        dialog.setConfirmClickListener(sweetAlertDialog -> {
            actionExitGame();
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    protected void initView() {
        super.initView();
        kidID = PreferencesUtils.getString(this, MLProperties.BUNDLE_KEY_KID_ID);
    }

    @Override
    protected void initData() {
        Bundle extras = getIntent().getExtras();
        currScoreCount = extras.getInt(MLProperties.BUNDLE_KEY_GAME_SCORE_COUNT);
        currRankCount = extras.getInt(MLProperties.BUNDLE_KEY_GAME_RANK_COUNT);
        tvRank.setText("全国排名：" + currRankCount);
        tvScore.setText("得分：" + currScoreCount);
        // 初始化数据之前最好把第一批数据准备好
        super.initData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(EventLevelInfos event) {
        currGroupLevels.clear();
        currGroupLevels.addAll(event.infoList);
        // 清空粘性数据
        EventBus.getDefault().removeAllStickyEvents();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("readingCompetition");
        MobclickAgent.onResume(this);
        tvRank.setText("全国排名：" + currRankCount);
        tvScore.setText("得分：" + currScoreCount);
        if (currGroupLevels == null || currGroupLevels.size() == 0) {
            getStudentinfoWithError();
        }
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.RECORD_AUDIO)
                .subscribe(success -> {
                    if (success) {
                    } else {
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("readingCompetition");
        MobclickAgent.onPause(this);
    }

    private void actionExitGame() {
        T.showShort(this, "退出游戏");
        // 取消倒计时
        if (gameFragment != null) {
            gameFragment.stopProgress();
        }
        this.finish();
    }

    @Override
    public void onBackPressed() {
        showExitDialot();
    }

    @Override
    public void startRecord() {
        super.startRecord();
    }

    @Override
    public void stopRecord() {
        super.stopRecord();
    }

    @Override
    protected void showLoadingDialog() {
        super.showLoadingDialog();
        if (loadingDialog != null && !loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    @Override
    protected void hideLoadingDialog() {
        super.hideLoadingDialog();
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    @Override
    protected void loadNextGroupData(int levelNumber) {
        Observable.create((ObservableOnSubscribe<List<LevelInfo>>) e -> {
            LmsDataService mService = new LmsDataService();
            List<LevelInfo> dataList = mService.getNextLevelsFromAPI(kidID, levelNumber, 7);
            e.onNext(dataList);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<LevelInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<LevelInfo> levelInfos) {
                        if (nextGroupLevels.size() == 0) {
                            nextGroupLevels.addAll(levelInfos);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("way", "onError()");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void loadNextGroupDataWithError(int levelNumber) {
        showLoadingDialog();
        Observable.create((ObservableOnSubscribe<List<LevelInfo>>) e -> {
            LmsDataService mService = new LmsDataService();
            List<LevelInfo> dataList = mService.getNextLevelsFromAPI(kidID, levelNumber, 7);
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
                        hideLoadingDialog();
                        currIndex = 0;
                        currGroupLevels.clear();
                        nextGroupLevels.clear();
                        currGroupLevels.addAll(levelInfos);
                        showGameInfo();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("way", "onError()");
                        hideLoadingDialog();
                        T.showShort(ReadingGameActivity.this, "服务器开小差了，请待会儿再来挑战");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void saveLevelData(LevelInfo info) {
        super.saveLevelData(info);
        // 上传录音文件
        String voicePath = info.getVoicePath();
        if (voicePath.equals("wu")) {
            // 直接保存数据
            saveLevelScoring(info, "wu");
        } else {
            // 先上传录音文件，完成后再上传分数
            saveVoiceFile(info, voicePath);
        }
    }

    /**
     * 保存录音文件
     */
    private void saveVoiceFile(final LevelInfo info, final String filePath) {
        Observable.create((ObservableOnSubscribe<String[]>) e -> {
            LmsDataService mService = new LmsDataService();
            String[] result = mService.uploadISEVoiceFile(filePath);
            e.onNext(result);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String[]>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String[] result) {
                        if (!StringUtils.isEmpty(result[0]) && "1".equals(result[0])) {
                            // 上传成功，调用积分接口
                            saveLevelScoring(info, result[2]);
                        } else {
                            hideLoadingDialog();
                            // T.showShort(ReadingGameActivity.this, result[1]);
                            saveLevelScoring(info, "wu");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideLoadingDialog();
                        // 保存录音文件失败
                        // T.showShort(ReadingGameActivity.this, "服务器开小差了，请稍后重试");
                        saveLevelScoring(info, "wu");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 保存关卡试题
     */
    private void saveLevelScoring(final LevelInfo info, final String voicePath) {
        Observable.create((ObservableOnSubscribe<String[]>) e -> {
            LmsDataService mService = new LmsDataService();
            String[] result = mService.saveLevelScoreFromAPI(kidID,
                    info.getLvlNumber(), info.getShitiCode(), info.getLvlScore(),
                    voicePath);
            e.onNext(result);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retry(3)
                .subscribe(new Observer<String[]>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String[] result) {
                        // 隐藏加载进度框
                        hideLoadingDialog();
                        // 上传分数成功
                        if (!StringUtils.isEmpty(result[0]) && "1".equals(result[0])) {

                        } else {
                            T.showShort(ReadingGameActivity.this, result[1]);
                        }
                        // 跳转到下个页面
                        showGameResultInfo();
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 保存记录失败
                        hideLoadingDialog();
                        T.showShort(ReadingGameActivity.this, "服务器开小差了，分数累积失败");
                        // 跳转到下个页面
                        showGameResultInfo();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onShareClick(View view) {
        super.onShareClick(view);
        MobclickAgent.onEvent(this, "competition_share");
        getStudentChallengeInfo();
    }

    protected void getStudentinfoWithError() {
        Observable.create((ObservableOnSubscribe<ChallengeInfo>) e -> {
            LmsDataService mService = new LmsDataService();
            ChallengeInfo mChallengeInfo = mService.getStudentChallengeInfoFromAPI(kidID);
            e.onNext(mChallengeInfo);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ChallengeInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ChallengeInfo challengeInfo) {
                        currRankCount = challengeInfo.getChallengeRank();
                        currScoreCount = challengeInfo.getChallengeScore();
                        int currLevelNumber = challengeInfo.getChallengeNumber();
                        tvRank.setText("全国排名：" + currRankCount);
                        tvScore.setText("得分：" + currScoreCount);

                        loadNextGroupDataWithError(currLevelNumber);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 获取学生挑战赛的相关信息
     */
    protected void getStudentChallengeInfo() {
        Observable.create((ObservableOnSubscribe<ChallengeInfo>) e -> {
            LmsDataService mService = new LmsDataService();
            ChallengeInfo mChallengeInfo = mService.getStudentChallengeInfoFromAPI(kidID);
            e.onNext(mChallengeInfo);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ChallengeInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ChallengeInfo challengeInfo) {
                        currRankCount = challengeInfo.getChallengeRank();
                        tvRank.setText("全国排名：" + currRankCount);

                        showShareDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        T.showShort(ReadingGameActivity.this, "服务器开小差了，无法获取最新排名");
                        showShareDialog();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}

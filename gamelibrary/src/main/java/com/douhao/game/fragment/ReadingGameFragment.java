package com.douhao.game.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.douhao.game.R;
import com.douhao.game.entity.LevelInfo;
import com.douhao.game.utils.DensityUtils;
import com.douhao.game.widget.TianzigeView;
import com.douhao.game.widget.WaveView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zz379 on 2017/5/27.
 */

public class ReadingGameFragment extends BaseGameFragment {

    private OnPageItemClickListener mOnPageItemClickListener;
    private PopupWindow popupWindow;

    public void setOnPageItemClickListener(OnPageItemClickListener mOnPageItemClickListener) {
        this.mOnPageItemClickListener = mOnPageItemClickListener;
    }

    public interface OnPageItemClickListener {
        boolean onReadingPressed(View view, MotionEvent event);

        void onTimeOver(boolean isTimeOver);
    }

    protected ProgressBar pbTime;
    protected TextView tvTime;
    public TextView tvPressReading;

    protected TextView tvLevelNumber;
    protected TianzigeView tvTianZiGe;
    public Disposable timeDisposable;
    public WaveView mWaveView;

    private int lastTime = 0;
    private boolean isCacheLastTime = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            lastTime = savedInstanceState.getInt("Progress");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.douhao_fragment_reading_game, container, false);
    }

    @Override
    public void initView() {
        super.initView();
        pbTime = (ProgressBar) getView().findViewById(R.id.progress);
        tvTime = (TextView) getView().findViewById(R.id.tv_timer);
        tvTianZiGe = (TianzigeView) getView().findViewById(R.id.tv_tzg);
        tvPressReading = (TextView) getView().findViewById(R.id.tv_pressReading);
        tvPressReading.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mOnPageItemClickListener != null) {
                    return mOnPageItemClickListener.onReadingPressed(v, event);
                }
                return false;
            }
        });
        tvLevelNumber = (TextView) getView().findViewById(R.id.tv_levelNumber);
        // 获取屏幕的宽高，屏幕高度 - 底部按钮的高度 = popupwindow的高度
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        // PopWindow
        View popView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_pop_record_animation, null);
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT,
                dm.heightPixels - DensityUtils.dp2px(getActivity(), 50));
        // 点击空白处时，隐藏掉pop窗口
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(false);
        // 录音动画
        mWaveView = (WaveView) popView.findViewById(R.id.vw2);
    }

    @Override
    public void initData() {
        super.initData();
        tvLevelNumber.setText("第" + getArguments().getInt("LevelNumber") + "关");
        tvTianZiGe.setText(getArguments().getString("LevelContent"));
        resetProgress(10);
    }

    @Override
    public void onResume() {
        super.onResume();
        // 重新开始进度条
        if (isCacheLastTime && isVisible()) {
            isCacheLastTime = false;
            resetProgress(lastTime);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // 取消进度条
        isCacheLastTime = true;
        stopProgress();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("Progress", lastTime);
    }

    public void refreshData(LevelInfo info) {
        tvPressReading.setEnabled(true);
        tvLevelNumber.setText("第" + info.getLvlNumber() + "关");
        tvTianZiGe.setText(info.getLvlContent());
        resetProgress(10);
    }

    public void stopProgress() {
        if (timeDisposable != null && !timeDisposable.isDisposed()) {
            timeDisposable.dispose();
        }
    }

    public void resetProgress(int time) {
        if (time < 0) time = 0;
        final int countTime = time * 1000;
        pbTime.setProgress(time * 10);
        Observable.interval(1, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .take(countTime + 1)
                .map(new Function<Long, Integer>() {
                    @Override
                    public Integer apply(@NonNull Long aLong) throws Exception {
                        return countTime - aLong.intValue();
                    }
                })
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(@NonNull Integer integer) throws Exception {
                        return integer % 100 == 0;
                    }
                })
                .map(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(@NonNull Integer integer) throws Exception {
                        return integer / 100;
                    }
                })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i("TianzigeView", "开始计时");
                        timeDisposable = d;
                        lastTime = 10;
                        tvTime.setText("还剩" + countTime / 1000 + "s");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.i("TianzigeView", "当前进度" + integer);
                        pbTime.setProgress(integer);
                        if (integer % 10 == 0) {
                            lastTime = integer / 10;
                            tvTime.setText("还剩" + lastTime + "s");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.i("TianzigeView", "完成计时");
                        if (mOnPageItemClickListener != null) {
                            mOnPageItemClickListener.onTimeOver(true);
                        }
                    }
                });
    }

    public void resetWaveView() {
        if (mWaveView != null) {
            mWaveView.stop();
        }
    }

    public void startWaveView() {
        if (mWaveView != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            mWaveView.initialize(dm);
        }
    }

    public void startSpeechWaveView(double volume) {
        if (mWaveView != null) {
            mWaveView.speechStarted(volume);
        }
    }

    public void endSpeechWaveView() {
        if (mWaveView != null) {
            mWaveView.speechEnded();
        }
    }

    public void pauseSpeechWaveView() {
        if (mWaveView != null) {
            mWaveView.speechPaused();
        }
    }

    /**
     * 显示录音动画
     */
    protected void showPopWindowRecordAnimation() {
        if (popupWindow != null && !popupWindow.isShowing()) {
            // 显示在按钮上方
            int[] location = new int[2];
            tvPressReading.getLocationOnScreen(location);
            popupWindow.showAtLocation(tvPressReading, Gravity.NO_GRAVITY, 0, 0);
        }
    }

    /**
     * 隐藏录音动画
     */
    protected void hidePopWindowRecordAnimation() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    /**
     * 开始页面动画
     */
    public void startPageAnimation() {
        showPopWindowRecordAnimation();
        startWaveView();
    }

    /**
     * 取消页面动画
     */
    public void cancelPageAnimation() {
        // 停止倒计时
        stopProgress();
        // 取消录音动画
        resetWaveView();
        // 隐藏录音动画显示框
        hidePopWindowRecordAnimation();
    }
}

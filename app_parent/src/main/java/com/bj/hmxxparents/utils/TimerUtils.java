package com.bj.hmxxparents.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

public class TimerUtils extends CountDownTimer {
    private TextView mTextView;

    public TimerUtils(TextView textView, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.mTextView = textView;
    }

    @Override
    public void onTick(long millisUntilFinished) {


    }

    @Override
    public void onFinish() {
        mTextView.setText("重新获取");
        mTextView.setClickable(true);//重新获得点击
    }
}

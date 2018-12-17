package com.douhao.game.activity;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.douhao.game.R;
import com.douhao.game.utils.SoundPoolUtil;
import com.zhy.autolayout.AutoLayoutActivity;

import java.io.IOException;

/**
 * Created by zz379 on 2017/6/5.
 */

public class DouhaoLoadingGameActivity extends AutoLayoutActivity {

    protected LinearLayout llHeaderLeft;
    protected TextView tvTips;
    protected ProgressBar pbTime;
    protected TextView tvStartGame;
    protected MediaPlayer bgMusicPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.douhao_activity_loading_game);
        SoundPoolUtil.getInstance(this);
        initToolbar();
        initView();
        initData();
    }

    protected void initToolbar() {
        llHeaderLeft = (LinearLayout) findViewById(R.id.header_ll_left);
        llHeaderLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    protected void initView() {
        tvTips = (TextView) this.findViewById(R.id.tv_tips);
        pbTime = (ProgressBar) this.findViewById(R.id.progress);
        tvStartGame = (TextView) this.findViewById(R.id.tv_startGame);
    }

    protected void initData() {
        // 播放音频文件
        try {
            AssetFileDescriptor afd = getAssets().openFd("bg.wav");
            bgMusicPlayer = new MediaPlayer();
            bgMusicPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            bgMusicPlayer.prepare();
            bgMusicPlayer.setLooping(true); // 设置循环
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bgMusicPlayer != null && !bgMusicPlayer.isPlaying()) {
            bgMusicPlayer.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (bgMusicPlayer != null && bgMusicPlayer.isPlaying()) {
            bgMusicPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bgMusicPlayer != null) {
            bgMusicPlayer.release();
            bgMusicPlayer = null;
        }
    }

    @Override
    public void onBackPressed() {
        // 取消进度条
        DouhaoLoadingGameActivity.this.finish();
    }
}

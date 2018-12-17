package com.douhao.game.utils;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.douhao.game.R;

/**
 * Created by zz379 on 2017/6/6.
 */

public class SoundPoolUtil {

    private static final int SOUND_MAX_STEAM = 5;

    private static SoundPoolUtil instance = null;
    private SoundPool soundPool;

    // 音频ID
    private int soundIDTimeOver;
    private int soundIDGood;
    private int soundIDBad;
    private int soundIDNormal;

    private SoundPoolUtil(Context context) {
        initSoundPool(context);
    }

    public static synchronized SoundPoolUtil getInstance(Context context) {
        if (instance == null) {
            instance = new SoundPoolUtil(context);
        }
        return instance;
    }

    private void initSoundPool(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // AudioAttributes是一个封装音频各种属性的方法
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_SYSTEM);//设置音频流的合适的属性
            // 21版本后，SoundPool的创建方式
            SoundPool.Builder builder = new SoundPool.Builder()
                    .setMaxStreams(SOUND_MAX_STEAM)
                    .setAudioAttributes(attrBuilder.build());

            soundPool = builder.build();
        } else {
            soundPool = new SoundPool(SOUND_MAX_STEAM, AudioManager.STREAM_SYSTEM, 0);
        }
        // 加载音频
        soundIDTimeOver = soundPool.load(context.getApplicationContext(), R.raw.time_over, 1);
        soundIDGood = soundPool.load(context.getApplicationContext(), R.raw.good, 1);
        soundIDBad = soundPool.load(context.getApplicationContext(), R.raw.bad, 1);
        soundIDNormal = soundPool.load(context.getApplicationContext(), R.raw.normal, 1);
    }

    /**
     * 播放时间结束的声音
     */
    public void playSoundTimeOver() {
        playSound(soundIDTimeOver);
    }

    /**
     * 播放评测成绩好声音
     */
    public void playSoundGood() {
        playSound(soundIDGood);
    }

    /**
     * 播放评测成绩差声音
     */
    public void playSoundBad() {
        playSound(soundIDBad);
    }

    /**
     * 播放评测成绩正常的声音
     */
    public void playSoundNormal() {
        playSound(soundIDNormal);
    }

    /**
     * soundID：表示流中第几个文件，为load方法的返回值。和load方法配合使用，见过用map进行传递数据。（实际上我感觉没必要）
     * leftVolume：左音量，通常为1。
     * rightVolume：右音量，通常为1。
     * priority：优先度，通常为0。
     * loop：是否循环。0表示不循环，1以上表示循环次数。
     * rate：表示播放速率。0.5-2之间。0.5表示减慢50%，2表示加速播放。
     *
     * @param soundID
     */
    private void playSound(int soundID) {
        soundPool.play(soundID, 1, 1, 0, 0, 1);
    }

    /**
     * 暂停
     */
    public void soundPoolPause() {
        soundPool.autoPause();
    }

    /**
     * 继续
     */
    public void soundPoolResume() {
        soundPool.autoResume();
    }

    /**
     * 销毁
     */
    public void soundPoolDestory() {
        soundPool.release();
        if (instance != null) {
            instance = null;
        }
    }
}

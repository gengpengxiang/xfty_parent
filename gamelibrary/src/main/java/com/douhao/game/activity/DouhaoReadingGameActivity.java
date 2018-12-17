package com.douhao.game.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.douhao.game.Constants;
import com.douhao.game.R;
import com.douhao.game.entity.LevelInfo;
import com.douhao.game.fragment.ReadingGameFragment;
import com.douhao.game.fragment.ReadingResultFragment;
import com.douhao.game.ise.result.Result;
import com.douhao.game.ise.result.xml.XmlResultParser;
import com.douhao.game.utils.ScreenUtils;
import com.douhao.game.utils.SoundPoolUtil;
import com.douhao.game.utils.Util;
import com.douhao.game.widget.TianzigeViewShare;
import com.iflytek.cloud.EvaluatorListener;
import com.iflytek.cloud.EvaluatorResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvaluator;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by zz379 on 2017/5/27.
 */

public class DouhaoReadingGameActivity extends AppCompatActivity implements ReadingGameFragment.OnPageItemClickListener, ReadingResultFragment.OnPageItemClickListener {

    private static final String TAG = DouhaoReadingGameActivity.class.getSimpleName();

    protected LinearLayout llHeaderLeft;
    protected TextView tvRank;
    protected TextView tvScore;
    protected FrameLayout flContent;
    protected ReadingGameFragment gameFragment;
    protected ReadingResultFragment resultFragment;
    private FragmentManager fm;
    // 分享
    protected ScrollView mScrollView;
    protected ImageView ivShareSession;
    protected ImageView ivShareTimeline;

    private Toast mToast;
    // 评测语种
    private String language;
    // 评测题型
    private String category;
    // 结果等级
    private String result_level;

    private String mLastResult;
    private SpeechEvaluator mIse;
    private PopupWindow popupWindow;
    private IWXAPI api;
    // 声音播放控制
    protected SoundPoolUtil soundPoolUtil;
    protected boolean isTimeOver = false;
    protected boolean isListening = false;

    // 当前7关内容
    protected List<LevelInfo> currGroupLevels = new ArrayList<>();
    // 下一组关卡内容
    protected List<LevelInfo> nextGroupLevels = new ArrayList<>();
    // 当前总得分
    protected int currScoreCount;
    // 当前总排名
    protected int currRankCount;
    // 当前下标
    protected int currIndex = 0;
    private TextView tvScoreShare;
    private TextView tvRankShare;
    private TianzigeViewShare tvTZGShare;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.douhao_activity_reading_game);
        if (savedInstanceState != null) {
            currScoreCount = savedInstanceState.getInt("Score");
            currRankCount = savedInstanceState.getInt("Rank");
        }
        // 初始化
        mIse = SpeechEvaluator.createEvaluator(this, null);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_DOUHAO_PARENT_ID);
        soundPoolUtil = SoundPoolUtil.getInstance(this);

        initToolbar();
        initView();
        // initData();
    }

    protected void initToolbar() {
        llHeaderLeft = (LinearLayout) findViewById(R.id.header_ll_left);
        tvRank = (TextView) findViewById(R.id.tv_rank);
        tvScore = (TextView) findViewById(R.id.tv_score);
    }

    protected void initView() {
        flContent = (FrameLayout) findViewById(R.id.fl_content);

        // PopWindow
        View popView = LayoutInflater.from(this).inflate(R.layout.alert_share_challenge_dialog, null);
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        // 点击空白处时，隐藏掉pop窗口
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        mScrollView = (ScrollView) popView.findViewById(R.id.mScrollView);
        tvScoreShare = (TextView) popView.findViewById(R.id.tv_scoreShare);
        tvRankShare = (TextView) popView.findViewById(R.id.tv_rankShare);
        tvTZGShare = (TianzigeViewShare) popView.findViewById(R.id.tv_tzgShare);
        ivShareSession = (ImageView) popView.findViewById(R.id.iv_shareSession);
        ivShareTimeline = (ImageView) popView.findViewById(R.id.iv_shareTimeline);
        ImageView ivClose = (ImageView) popView.findViewById(R.id.iv_close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        ivShareSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToSession();
            }
        });
        ivShareTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToTimeline();
            }
        });

        mToast = Toast.makeText(DouhaoReadingGameActivity.this, "", Toast.LENGTH_SHORT);
    }

    protected void initData() {
        Bundle bundle = new Bundle();
        bundle.putInt("LevelNumber", currGroupLevels.get(currIndex).getLvlNumber());
        bundle.putString("LevelContent", currGroupLevels.get(currIndex).getLvlContent());

        fm = getSupportFragmentManager();
        gameFragment = new ReadingGameFragment();
        gameFragment.setArguments(bundle);
        gameFragment.setOnPageItemClickListener(this);

        resultFragment = new ReadingResultFragment();
        resultFragment.setOnPageItemClickListener(this);
        // 显示页面
        fm.beginTransaction()
                .add(R.id.fl_content, gameFragment)
                .add(R.id.fl_content, resultFragment)
                .hide(resultFragment)
                .show(gameFragment)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mIse) {
            mIse.destroy();
            mIse = null;
        }
        if (null != soundPoolUtil) {
            soundPoolUtil.soundPoolDestory();
            soundPoolUtil = null;
        }
    }

    private void showTip(String str) {
        if (!TextUtils.isEmpty(str)) {
            mToast.setText(str);
            mToast.show();
        }
    }

    /**
     * 显示游戏界面
     */
    public void showGameInfo() {
        if (gameFragment == null) {
            initData();
            return;
        }
        // 如果当前fragment可见，则不再处理
        if (gameFragment.isVisible()) {
            return;
        }
        // 刷新数据
        gameFragment.refreshData(currGroupLevels.get(currIndex));
        FragmentTransaction ft = fm.beginTransaction();

        ft.setCustomAnimations(R.anim.right_left_in, R.anim.right_left_out);
        ft.hide(resultFragment);
        if (!gameFragment.isAdded()) {
            ft.add(R.id.fl_content, gameFragment);
        }
        ft.show(gameFragment).commitAllowingStateLoss();
    }

    /**
     * 显示结果界面
     */
    public void showGameResultInfo() {
        if (resultFragment == null) {
            resultFragment = new ReadingResultFragment();
        }
        // 如果当前fragment可见，则不再处理
        if (resultFragment.isVisible()) {
            return;
        }
        // 再次判断录音动画框是否已经隐藏
        gameFragment.cancelPageAnimation();
        // 刷新数据
        LevelInfo info = currGroupLevels.get(currIndex);
        resultFragment.refreshData(info);
        // 显示结果页面
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.right_left_in, R.anim.right_left_out);
        ft.hide(gameFragment);
        if (!resultFragment.isAdded()) {
            ft.add(R.id.fl_content, resultFragment);
        }
        ft.show(resultFragment).commitAllowingStateLoss();
        // 更新总分数
        tvScore.setText("得分：" + currScoreCount);
        // 播放提示音
        if (!isTimeOver) {
            if (info.getLvlScore() >= 8) {
                soundPoolUtil.playSoundGood();
            } else if (info.getLvlScore() < 5) {
                soundPoolUtil.playSoundBad();
            } else {
                soundPoolUtil.playSoundNormal();
            }
        }
    }

    /**
     * 开始录音
     */
    public void startRecord() {
        if (null == mIse) {
            // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
            Log.d(TAG, "创建对象失败，请确认 libmsc.so 放置正确，且有调用 createUtility 进行初始化");
            return;
        }
        mLastResult = null;
        // 开始评测
        String content = currGroupLevels.get(currIndex).getLvlContent();
        if (content == null) content = "";
        setParams(content);
        mIse.startEvaluating(content, null, mEvaluatorListener);
    }

    private boolean isIseGetError = false;

    /**
     * 结束录音，并开始评测
     */
    public void stopRecord() {
        // 停止动画
        gameFragment.cancelPageAnimation();
        // 显示数据加载框
        if (!isIseGetError) {
            showLoadingDialog();
        }
        // 停止评测
        Log.d(TAG, "评测已停止，等待结果中...");
        if (mIse.isEvaluating()) {
            mIse.stopEvaluating();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("Score", currScoreCount);
        outState.putInt("Rank", currRankCount);
        fm.saveFragmentInstanceState(gameFragment);
        fm.saveFragmentInstanceState(resultFragment);
    }

    private void setParams(String content) {
        // 设置评测语言
        language = "zh_cn";
        // 设置需要评测的类型 read_syllable：单字  read_word：词语  read_sentence：句子
        category = content.length() > 1 ? "read_word" : "read_syllable";
        // 设置结果等级（中文仅支持complete）
        result_level = "complete";
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        String vad_bos = "10000";
        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        String vad_eos = "11000";
        // 语音输入超时时间，即用户最多可以连续说多长时间；
        String speech_timeout = "-1";

        mIse.setParameter(SpeechConstant.LANGUAGE, language);
        mIse.setParameter(SpeechConstant.ISE_CATEGORY, category);
        mIse.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
        mIse.setParameter(SpeechConstant.VAD_BOS, vad_bos);
        mIse.setParameter(SpeechConstant.VAD_EOS, vad_eos);
        mIse.setParameter(SpeechConstant.KEY_SPEECH_TIMEOUT, speech_timeout);
        mIse.setParameter(SpeechConstant.RESULT_LEVEL, result_level);

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIse.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIse.setParameter(SpeechConstant.ISE_AUDIO_PATH, Environment.getExternalStorageDirectory().getAbsolutePath() + "/msc/ise" + currIndex + ".wav");
        currGroupLevels.get(currIndex).setVoicePath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/msc/ise" + currIndex + ".wav");
    }

    // 评测监听接口
    private EvaluatorListener mEvaluatorListener = new EvaluatorListener() {

        @Override
        public void onResult(EvaluatorResult result, boolean isLast) {
            Log.d(TAG, "evaluator result :" + isLast);
            if (isLast) {
                StringBuilder builder = new StringBuilder();
                builder.append(result.getResultString());
                mLastResult = builder.toString();
                // 开始解析结果
                float totalScore = parseSpeechResult(mLastResult) * 2;
                int lvlScore = new BigDecimal(String.valueOf(totalScore))
                        .setScale(0, BigDecimal.ROUND_HALF_UP)
                        .intValue();
                int lvlBeatUsers;
                if (lvlScore > 9) {
                    lvlBeatUsers = (new Random().nextInt(10)) + (lvlScore - 1) * 10;
                } else if (lvlScore < 1) {
                    lvlBeatUsers = 0;
                } else {
                    lvlBeatUsers = (new Random().nextInt(10)) + lvlScore * 10;
                }

                currScoreCount += lvlScore;
                currGroupLevels.get(currIndex).setLvlScore(lvlScore);
                currGroupLevels.get(currIndex).setLvlbeatUsers(lvlBeatUsers);
                // 跳转到结果页面
                saveLevelData(currGroupLevels.get(currIndex));
            }
        }

        @Override
        public void onError(SpeechError error) {
            isIseGetError = true;
            if (error != null) {
                Log.d(TAG, "error:" + error.getErrorCode() + "," + error.getErrorDescription());
                if (error.getErrorCode() == 11401) {
                    showTip("很抱歉，您的声音太小了");
                } else if (error.getErrorCode() == 20006) {
                    showTip("启动录音失败，请检查是否授权录音权限");
                    gameFragment.cancelPageAnimation();
                    return;
                }
            } else {
                Log.d(TAG, "evaluator over");
            }
            // 错误的情况下全部判0
            int lvlScore = 0;
            int lvlBeatUsers = 0;

            currScoreCount += lvlScore;
            currGroupLevels.get(currIndex).setLvlScore(lvlScore);
            currGroupLevels.get(currIndex).setLvlbeatUsers(lvlBeatUsers);
            // 跳转到结果页面
            saveLevelData(currGroupLevels.get(currIndex));
        }

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            Log.d(TAG, "evaluator begin");
            // 开始动画
            gameFragment.startPageAnimation();
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            Log.d(TAG, "evaluator stoped");
            // 显示数据加载的弹框
            showLoadingDialog();
            // 取消动画
            gameFragment.cancelPageAnimation();
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            double v = volume > 0 ? 1.0 : 0.1;
            gameFragment.startSpeechWaveView(v);
            Log.d(TAG, "当前音量：" + volume + " 当前振幅：" + v + " 返回音频数据：" + data.length);
            if (!isListening && mIse.isEvaluating()) {
                stopRecord();
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    private float parseSpeechResult(String lastResult) {
        // 解析最终结果
        if (!TextUtils.isEmpty(lastResult)) {
            XmlResultParser resultParser = new XmlResultParser();
            Result result = resultParser.parse(lastResult);

            if (null != result) {
                Log.d(TAG, "总得分：" + result.total_score);
                return result.total_score;
            } else {
                Log.d(TAG, "解析结果为空");
                return 0;
            }
        } else {
            return 0;
        }
    }

    @Override
    public boolean onReadingPressed(final View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isListening = true;
                view.setPressed(true);
                ((TextView) view).setText("松开结束");
                startRecord();
                return true;
            case MotionEvent.ACTION_MOVE:
                return true;
            case MotionEvent.ACTION_UP:
                if (isListening) {
                    isListening = false;
                    view.setPressed(false);
                    ((TextView) view).setText("按住朗读");
                    stopRecord();
                }
                // 松开按钮后，锁住
                view.setEnabled(false);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onTimeOver(boolean isTimeOver) {
        // timeover 分两种情况：1.按住的情况；2.没有按住情况（这种情况要播放 时间到的音效）
        if (isListening) {
            this.isTimeOver = false;
            isListening = false;
            // 停止录音
            stopRecord();
        } else {
            this.isTimeOver = true;
            // 播放TimeOver 的音效
            soundPoolUtil.playSoundTimeOver();
            // 显示结果页面，上传数据, 没有录音文件
            currGroupLevels.get(currIndex).setVoicePath("wu");
            saveLevelData(currGroupLevels.get(currIndex));
        }
    }

    @Override
    public void onShareClick(View view) {

    }

    protected void showShareDialog() {
        // 更新需要分享的数据
        tvScoreShare.setText("总得分：" + currScoreCount);
        tvRankShare.setText("全国排名：" + currRankCount);
        tvTZGShare.setText(currGroupLevels.get(currIndex).getLvlContent());

        popupWindow.showAtLocation(flContent, Gravity.NO_GRAVITY, 0, 0);
    }

    private long btnClickTimeMillis;

    @Override
    public void onNextClick(View view) {
        if (System.currentTimeMillis() - btnClickTimeMillis < 2000) {
            return;
        }
        btnClickTimeMillis = System.currentTimeMillis();
        // 重置 是否是因为超时跳转到结果页面的判断
        isTimeOver = false;
        isListening = false;
        isIseGetError = false;

        currIndex++;
        // 当本组关卡挑战过一半后开始加载下一组数据
        if (currIndex >= currGroupLevels.size() / 2 && nextGroupLevels.size() == 0) {
            loadNextGroupData(currGroupLevels.get(currGroupLevels.size() - 1).getLvlNumber());
        }
        // 当进入第七关的时候，切换数据
        if (currIndex == currGroupLevels.size()) {
            // 正常情况下下一关数据已经缓存完成
            if (nextGroupLevels.size() > 0) {
                currIndex = 0;
                currGroupLevels.clear();
                currGroupLevels.addAll(nextGroupLevels);
                nextGroupLevels.clear();
            } else {
                currIndex--;
                loadNextGroupDataWithError(currGroupLevels.get(currGroupLevels.size() - 1).getLvlNumber());
                return;
            }
        }
        // 显示下一关
        showGameInfo();
    }

    /**
     * 非正常情况下加载下一组关卡数据
     *
     * @param lvlNumber
     */
    protected void loadNextGroupDataWithError(int lvlNumber) {

    }

    /**
     * 正常情况下加载下一组的数据
     */
    protected void loadNextGroupData(int levelNumber) {

    }

    /**
     * 分享到微信
     */
    public void shareToSession() {
        if (!isWeixinAvilible(this)) {
            Toast.makeText(this, "抱歉！您还没有安装微信", Toast.LENGTH_SHORT).show();
            return;
        }
        if (popupWindow.isShowing() && mScrollView != null) {
            Bitmap shareBmp = ScreenUtils.compressImage(ScreenUtils.getBitmapByView(mScrollView));
            popupWindow.dismiss();

            WXImageObject imgObject = new WXImageObject(shareBmp);
            WXMediaMessage msg = new WXMediaMessage();
            msg.mediaObject = imgObject;
            // 设置缩略图
            if (shareBmp != null && !shareBmp.isRecycled()) {
                Bitmap thumbBmp = Bitmap.createScaledBitmap(shareBmp, shareBmp.getWidth() / 10,
                        shareBmp.getHeight() / 10, true);
                // msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
                msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
                shareBmp.recycle();
            }
            // 构造一个Req
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            // transaction 字段用于唯一标示一个请求
            req.transaction = buildTransaction("img");
            req.message = msg;
            req.scene = SendMessageToWX.Req.WXSceneSession;

            // 调用api接口发送数据到微信
            api.sendReq(req);
        }
    }

    /**
     * 分享到朋友圈
     */
    public void shareToTimeline() {
        if (!isWeixinAvilible(this)) {
            Toast.makeText(this, "抱歉！您还没有安装微信", Toast.LENGTH_SHORT).show();
            return;
        }
        if (popupWindow.isShowing() && mScrollView != null) {
            Bitmap shareBmp = ScreenUtils.compressImage(ScreenUtils.getBitmapByView(mScrollView));
            popupWindow.dismiss();

            WXImageObject imgObject = new WXImageObject(shareBmp);
            WXMediaMessage msg = new WXMediaMessage();
            msg.mediaObject = imgObject;
            // 设置缩略图
            if (shareBmp != null && !shareBmp.isRecycled()) {
                Bitmap thumbBmp = Bitmap.createScaledBitmap(shareBmp, 150,
                        shareBmp.getHeight() * 150 / shareBmp.getWidth(), true);
                msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
                shareBmp.recycle();
            }
            // 构造一个Req
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            // transaction 字段用于唯一标示一个请求
            req.transaction = buildTransaction("img");
            req.message = msg;
            req.scene = SendMessageToWX.Req.WXSceneTimeline;

            // 调用api接口发送数据到微信
            api.sendReq(req);
        }
    }

    /**
     * 显示加载数据弹框
     */
    protected void showLoadingDialog() {
    }

    /**
     * 隐藏加载数据弹框
     */
    protected void hideLoadingDialog() {
    }

    /**
     * 保存当前关卡数据
     *
     * @param info
     */
    protected void saveLevelData(LevelInfo info) {

    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    /***
     * 检查是否安装了微信
     * @param context
     * @return
     */
    private boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }
}

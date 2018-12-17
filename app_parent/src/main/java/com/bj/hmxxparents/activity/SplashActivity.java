package com.bj.hmxxparents.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bj.hmxxparents.BaseActivity;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.api.MLConfig;
import com.bj.hmxxparents.api.MLProperties;
import com.bj.hmxxparents.entity.StudentInfos;
import com.bj.hmxxparents.utils.LL;
import com.bj.hmxxparents.utils.PreferencesUtils;
import com.bj.hmxxparents.utils.StringUtils;
import com.bj.hmxxparents.utils.T;
import com.douhao.game.Constants;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.bj.hmxxparents.api.HttpUtilService.BASE_API_URL;

/**
 * APP启动页
 */
public class SplashActivity extends BaseActivity {

    private IWXAPI api;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.navpage));
        }

        //测试
//        Intent i_getvalue = getIntent();
//        String action = i_getvalue.getAction();
//        if (Intent.ACTION_VIEW.equals(action)) {
//            Uri uri = i_getvalue.getData();
//            if (uri != null) {
//                String code = uri.getQueryParameter("code");
//                Intent intent = new Intent(SplashActivity.this, DoukeDetailActivity.class);
//                intent.putExtra(MLProperties.BUNDLE_KEY_DOUKE_ID, code);
//                intent.putExtra(MLProperties.BUNDLE_KEY_DOUKE_URL, "");
//                startActivity(intent);
//            }
//        }


        setContentView(R.layout.activity_splash);

        // 禁止默认的页面统计方式，这样将不会再自动统计Activity
        MobclickAgent.openActivityDurationTrack(false);
        initView();
        registWx();


        intentToHomePage();
    }

    private void registWx() {
        // 获取IWXAPI 的实例
        api = WXAPIFactory.createWXAPI(this, Constants.APP_DOUHAO_PARENT_ID, true);
        // 将应用的app ID注册到微信
        api.registerApp(Constants.APP_DOUHAO_PARENT_ID);
    }

    private void initView() {
        SimpleDraweeView imgSplash = (SimpleDraweeView) this.findViewById(R.id.img_splash);
        TextView tvTitle = (TextView) this.findViewById(R.id.tv_title);
        Animation animation = new AlphaAnimation(0, 1.0f);
        animation.setStartTime(0);
        animation.setDuration(1500);
        animation.setFillAfter(true);
        imgSplash.startAnimation(animation);

        Animation animation2 = new AlphaAnimation(0, 1.0f);
        animation2.setStartTime(500);
        animation2.setDuration(1000);
        animation2.setFillAfter(true);
        tvTitle.startAnimation(animation2);
    }

    /**
     * 跳转到首页
     */
    private void intentToHomePage() {
        new Handler().postDelayed(() -> {
            // 登录状态判断
            int loginStatus = PreferencesUtils.getInt(SplashActivity.this, MLProperties.PREFER_KEY_LOGIN_STATUS, 0);
            String userPhoneNumber = PreferencesUtils.getString(SplashActivity.this, MLProperties.PREFER_KEY_USER_ID, "");
            if (loginStatus != 1 || StringUtils.isEmpty(userPhoneNumber)) {
//            if (StringUtils.isEmpty(userPhoneNumber)) {

                // Log.e("检查",loginStatus+"=="+userPhoneNumber);
                intentToLoginActivity();
                return;
            }

            // 判断登录时间是否超过了15天
            long lastLoginTime = PreferencesUtils.getLong(SplashActivity.this, MLProperties.PREFER_KEY_LOGIN_Time, 0);
            if (lastLoginTime == 0 || !checkLoginTimeSpan(lastLoginTime)) {
                intentToLoginActivity();
                return;
            }

//
//            if(StringUtils.isEmpty(studentcode)){
//                showTianyuan();
//            }else {
//                intentToMainActivity();
//            }

            //showTianyuan();

            intentToMainActivity();

            // 环信登录状态被取消
            //  login2Ease(userPhoneNumber);
//            if (!IMHelper.getInstance().isLoggedIn()) {
//                login2Ease(userPhoneNumber);
//            } else {
//                intentToMainActivity();
//            }
        }, 1500);
    }

    void intentToLoginActivity() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        SplashActivity.this.finish();
    }

    void intentToMainActivity() {
        // 如果是虚拟用户，直接跳转到首页
        int isUserVirtual = PreferencesUtils.getInt(this, MLProperties.PREFER_KEY_USER_VIRTUAL, 0);
        if (isUserVirtual == 1) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);

            startActivity(intent);
            SplashActivity.this.finish();
            return;
        }
        // 判断用户信息是否完整
        int isUserInfoComplete = PreferencesUtils.getInt(SplashActivity.this, MLProperties.PREFER_KEY_IS_USER_INFO_COMPLETE, 0);
        if (isUserInfoComplete != 1) {
            Intent intent = new Intent(SplashActivity.this, StudentBaseInfoActivity.class);
            startActivity(intent);
            SplashActivity.this.finish();
            return;
        }

        // OK，可以跳转到首页
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        SplashActivity.this.finish();
    }

    private void login2Ease(String userPhoneNumber) {
        String userEaseID = MLConfig.EASE_PARENT_ID_PREFIX + userPhoneNumber;
        EMClient.getInstance().login(userEaseID, "123456", new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                // 加载环信相关数据
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.d("way", "登录聊天服务器成功！");

                intentToMainActivity();
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Log.d("way", "登录聊天服务器失败！");
                // 跳转到登录页面
                intentToLoginActivity();
            }
        });
    }

    private boolean checkLoginTimeSpan(long lastLoginTime) {
        long currTime = System.currentTimeMillis();
        int span = (int) (currTime - lastLoginTime) / 1000 / 60 / 60 / 24;

        if (span <= MLConfig.KEEP_LOGIN_TIME_LENGTH) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("qidongye");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("qidongye");
        MobclickAgent.onPause(this);
    }


}

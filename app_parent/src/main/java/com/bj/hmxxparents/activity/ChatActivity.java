package com.bj.hmxxparents.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.bj.hmxxparents.R;
import com.bj.hmxxparents.api.Constant;
import com.bj.hmxxparents.api.MLProperties;
import com.bj.hmxxparents.utils.PreferencesUtils;
import com.bj.hmxxparents.utils.ScreenUtils;
import com.bj.hmxxparents.utils.StringUtils;
import com.hyphenate.easeui.ui.EaseBaseActivity;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.util.EasyUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.analytics.MobclickAgent;

/**
 * chat activity，EaseChatFragment was used {@link EaseChatFragment}
 * 发消息、聊天页面
 */
public class ChatActivity extends EaseBaseActivity implements EaseChatFragment.OnBackClickListener {
    public static ChatActivity activityInstance;
    private EaseChatFragment chatFragment;
    String toChatUsername;
    String currUserPhoto;
    String currUserNick;
    String userClassName;
    String userRelation;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        // 如果存在虚拟按键，则设置虚拟按键的背景色
        if (ScreenUtils.checkDeviceHasNavigationBar(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setNavigationBarColor(ContextCompat.getColor(this, android.R.color.black));
            }
        }
        setContentView(R.layout.em_activity_chat);
        activityInstance = this;
        Bundle bundle = getIntent().getExtras();
        currUserPhoto = PreferencesUtils.getString(this, MLProperties.BUNDLE_KEY_KID_IMG);
        currUserNick = PreferencesUtils.getString(this, MLProperties.BUNDLE_KEY_KID_NAME);
        userClassName = PreferencesUtils.getString(this, MLProperties.BUNDLE_KEY_CLASS_NAME, "");
        userRelation = PreferencesUtils.getString(this, MLProperties.BUNDLE_KEY_KID_RELATION, "");
        if (StringUtils.isEmpty(userRelation)) {
            currUserNick = currUserNick + "的家长";
        } else if ("baba".equals(userRelation)) {
            currUserNick = currUserNick + "的爸爸";
        } else if ("mama".equals(userRelation)) {
            currUserNick = currUserNick + "的妈妈";
        }

        bundle.putString("currUserPhoto", currUserPhoto);
        bundle.putString("currUserNick", currUserNick);
        bundle.putString("currClassName", userClassName);

        //get user id or group id
        toChatUsername = bundle.getString(Constant.EXTRA_USER_ID);

        //use EaseChatFratFragment
        chatFragment = new EaseChatFragment();
        chatFragment.setBackClickListener(this);
        //pass parameters to chat fragment
        chatFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();

        // 录音、拍照的权限
        requredPermission();
    }

    private void requredPermission() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS)
                .subscribe(success -> {
                    if (success) {

                    } else {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // make sure only one chat activity is opened
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        chatFragment.onBackPressed();
        if (EasyUtils.isSingleActivity(this)) {
            onBackClick();
        }
    }

    public String getToChatUsername() {
        return toChatUsername;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

    @Override
    public void onBackClick() {
        Intent intent = new Intent(ChatActivity.this, MainActivity.class);
        intent.putExtra(MLProperties.BUNDLE_KEY_MAIN_PAGEINDEX, 1);
        startActivity(intent);
        this.finish();
        overridePendingTransition(R.anim.left_right_in, R.anim.left_right_out);

//        if (MainActivity.getInstance() == null) {
//
//        } else {
//            this.finish();
//            overridePendingTransition(R.anim.left_right_in, R.anim.left_right_out);
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}

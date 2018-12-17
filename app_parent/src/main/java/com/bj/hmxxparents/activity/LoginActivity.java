package com.bj.hmxxparents.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bj.hmxxparents.BaseActivity;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.api.LmsDataService;
import com.bj.hmxxparents.api.MLConfig;
import com.bj.hmxxparents.api.MLProperties;
import com.bj.hmxxparents.entity.KidClassInfo;
import com.bj.hmxxparents.entity.KidDataInfo;
import com.bj.hmxxparents.utils.IMMLeaks;
import com.bj.hmxxparents.utils.KeyBoardUtils;
import com.bj.hmxxparents.utils.LL;
import com.bj.hmxxparents.utils.LeakedUtils;
import com.bj.hmxxparents.utils.PreferencesUtils;
import com.bj.hmxxparents.utils.StringUtils;
import com.bj.hmxxparents.utils.T;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.umeng.analytics.MobclickAgent;

import cn.pedant.SweetAlert.HelpAlertDialog;

/**
 * 登录页面
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText edtPhoneNumber, edtCode;
    private SimpleDraweeView btnLogin;
    private TextView tvGetCode, tvWithProblem;
    private int timeRemaining; //剩余时间
    private ScrollView mScrollView;
    private ProgressDialog mProgressDialog;
    private LinearLayout llDouhaoProtocol;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // 初始化页面
        initToolBar();
        initView();
        initDatas();
    }

    private void initToolBar() {
        TextView tvTitle = (TextView) this.findViewById(R.id.header_tv_title);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.app_name);
    }

    private void initView() {
        mScrollView = (ScrollView) this.findViewById(R.id.scrollview);
        LinearLayout llContent = (LinearLayout) this.findViewById(R.id.ll_content);
        llContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtils.closeKeybord(LoginActivity.this.getCurrentFocus().getWindowToken()
                        , LoginActivity.this);
            }
        });
        edtPhoneNumber = (EditText) this.findViewById(R.id.edt_phoneNumber);
        edtCode = (EditText) this.findViewById(R.id.edt_verificationCode);
        btnLogin = (SimpleDraweeView) this.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        tvGetCode = (TextView) this.findViewById(R.id.tv_getCode);
        tvGetCode.setOnClickListener(this);
        tvWithProblem = (TextView) this.findViewById(R.id.tv_withProblem);
        tvWithProblem.setOnClickListener(this);
        llDouhaoProtocol = (LinearLayout) this.findViewById(R.id.ll_douhaoProtocol);

        edtPhoneNumber.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changeScrollView();
                return false;
            }
        });
        edtCode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changeScrollView();
                return false;
            }
        });
        // addLayoutListener(llContent, tvWithProblem);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(android.R.style.Theme_Material_Dialog_Alert);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage("数据加载中...");
        mProgressDialog.setCancelable(false);

        llDouhaoProtocol.setOnClickListener(v -> {
            if (System.currentTimeMillis() - currTimeMillin >= 1000) {
                currTimeMillin = System.currentTimeMillis();
                Intent intent = new Intent(LoginActivity.this, ProtocolActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initDatas() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                actionLogin();
                break;
            case R.id.tv_getCode:
                MobclickAgent.onEvent(LoginActivity.this, "login_code");
                actionGetCode();
                break;
            case R.id.tv_withProblem:
                KeyBoardUtils.closeKeybord(LoginActivity.this.getCurrentFocus().getWindowToken()
                        , LoginActivity.this);
                actionForHelp();
                break;
        }
    }

    private void actionForHelp() {
        HelpAlertDialog dialog = new HelpAlertDialog(LoginActivity.this);
        dialog.setContentText("使用幸福田园过程中遇到任何问题或您有什么建议，请联系小助手。\n微信号：pkugame\n电话：15201635868\n邮箱：douhaojiaoyu@163.com");
        dialog.show();
    }

    private long currTimeMillin;

    /**
     * 点击login按钮
     */
    private void actionLogin() {
        if (System.currentTimeMillis() - currTimeMillin < 1000) {
            currTimeMillin = System.currentTimeMillis();
        } else {
            currTimeMillin = System.currentTimeMillis();

            KeyBoardUtils.closeKeybord(LoginActivity.this.getCurrentFocus().getWindowToken()
                    , LoginActivity.this);
            String phoneNum = edtPhoneNumber.getText().toString().trim();
            String codeNum = edtCode.getText().toString().trim();
            if (StringUtils.isEmpty(phoneNum)) {
                T.showShort(this, "请输入手机号");
                return;
            }
            if (!StringUtils.checkPhoneNumber(phoneNum)) {
                T.showShort(this, "手机号输入有误");
                return;
            }
            if (StringUtils.isEmpty(codeNum)) {
                T.showShort(this, "请输入验证码");
                return;
            }

            // 判断手机号和验证码是否匹配
            MyLoginTask myLoginTask = new MyLoginTask();
            myLoginTask.execute(phoneNum, codeNum);
        }
    }

    private void actionGetCode() {
        if (System.currentTimeMillis() - currTimeMillin < 1000) {
            currTimeMillin = System.currentTimeMillis();
        } else {
            currTimeMillin = System.currentTimeMillis();
            KeyBoardUtils.closeKeybord(LoginActivity.this.getCurrentFocus().getWindowToken()
                    , LoginActivity.this);
            String phoneNum = edtPhoneNumber.getText().toString().trim();
            if (StringUtils.isEmpty(phoneNum)) {
                T.showShort(this, "请输入手机号");
                return;
            }
            if (!StringUtils.checkPhoneNumber(phoneNum)) {
                T.showShort(this, "手机号输入有误");
                return;
            }
            // 获取验证码，并开始倒计时

            MyGetCodeTask myGetCodeTask = new MyGetCodeTask();
            myGetCodeTask.execute(phoneNum);
        }
    }

    private void startTimerSchedule() {
        timeRemaining = 61;
        tvGetCode.setTextColor(ContextCompat.getColor(this, R.color.text_gray));
        tvGetCode.setClickable(false);

        mHandler.post(timerRunnable);
    }

    Handler mHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            timeRemaining--;
            tvGetCode.setText(String.format("%d秒后可重发", timeRemaining));
            if (timeRemaining < 0) {
                tvGetCode.setText("获取验证码");
                tvGetCode.setTextColor(ContextCompat.getColor(LoginActivity.this,
                        R.color.colorPrimary));
                tvGetCode.setClickable(true);
            } else {
                mHandler.postDelayed(this, 1000);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LeakedUtils.fixTextLineCacheLeak();
        IMMLeaks.fixFocusedViewLeak(getApplication());

        mHandler.removeCallbacks(timerRunnable);
    }

    /**
     * 使ScrollView指向底部
     */
    private void changeScrollView() {
        mHandler.postDelayed(() -> mScrollView.scrollTo(0, tvWithProblem.getBottom() - mScrollView.getHeight()), 300);
    }

    private class MyGetCodeTask extends AsyncTask<String, Integer, String[]> {
        @Override
        protected void onPreExecute() {
            tvGetCode.setClickable(false);
            startTimerSchedule();   // 开始计时
        }

        @Override
        protected String[] doInBackground(String... params) {
            LmsDataService mService = new LmsDataService();
            String[] result;
            try {
                result = mService.getCodeFromAPI2(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
                LL.e(e);
                result = new String[2];
                result[0] = "0";
                result[1] = "服务器开小差了，请待会重试";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (!StringUtils.isEmpty(result[0]) && result[0].equals("1")) {
                // startTimerSchedule();   // 开始计时
                T.showShort(LoginActivity.this, "获取验证码成功");
            } else {
                tvGetCode.setClickable(true);
                T.showShort(LoginActivity.this, StringUtils.isEmpty(result[1]) ? "发送验证码失败" : result[1]);
            }
        }
    }

    private class MyLoginTask extends AsyncTask<String, Integer, String[]> {

        @Override
        protected void onPreExecute() {
            btnLogin.setClickable(false);
            mProgressDialog.show();
        }

        @Override
        protected String[] doInBackground(String... params) {
            String phoneNumber = params[0];
            String code = params[1];
            LmsDataService mService = new LmsDataService();
            String[] result;
            try {
                result = mService.loginFromAPI2(phoneNumber, code);
            } catch (Exception e) {
                e.printStackTrace();
                LL.e(e);
                result = new String[2];
                result[0] = "0";
                result[1] = "服务器开小差了，请待会重试";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (!StringUtils.isEmpty(result[0]) && result[0].equals("1")) {
                String phoneNum = edtPhoneNumber.getText().toString().trim();
                // 登录环信，成功后 获取是否绑定了学生
                Log.e("success","登录成功=");
                // 检查是否绑定了学生
                MyCheckRelationKidTask myCheckRelationKidTask = new MyCheckRelationKidTask();
                myCheckRelationKidTask.execute(phoneNum);
//                if (EMClient.getInstance().isLoggedInBefore()) {
//                    EMClient.getInstance().logout(true, new EMCallBack() {
//                        @Override
//                        public void onSuccess() {
//                            login2Ease(phoneNum);
//                        }
//
//                        @Override
//                        public void onError(int code, String error) {
//
//                        }
//
//                        @Override
//                        public void onProgress(int progress, String status) {
//
//                        }
//                    });
//                } else {
//                    login2Ease(phoneNum);
//                }
            } else {
                btnLogin.setClickable(true);
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                T.showShort(LoginActivity.this, StringUtils.isEmpty(result[1]) ? "登录失败" : result[1]);
            }
        }
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
                // 检查是否绑定了学生
                MyCheckRelationKidTask myCheckRelationKidTask = new MyCheckRelationKidTask();
                myCheckRelationKidTask.execute(userPhoneNumber);
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                runOnUiThread(() -> {
                    btnLogin.setClickable(true);
                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    if (code == 200) {
                        // 加载环信相关数据
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();
                        Log.d("way", "登录聊天服务器成功！");
                        // 检查是否绑定了学生
                        MyCheckRelationKidTask myCheckRelationKidTask = new MyCheckRelationKidTask();
                        myCheckRelationKidTask.execute(userPhoneNumber);
                    } else {
                        T.showShort(LoginActivity.this, "登录失败，请重新发送验证码" + " " + code);
                    }
                });
            }
        });
    }

    private class MyCheckRelationKidTask extends AsyncTask<String, Integer, String[]> {

        @Override
        protected String[] doInBackground(String... params) {
            LmsDataService mService = new LmsDataService();
            String[] result;
            try {
                result = mService.checkRelationKidFromAPI(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
                LL.e(e);
                result = new String[3];
                result[0] = "0";
                result[1] = "服务器开小差了，请待会重试";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String[] result) {
            btnLogin.setClickable(true);
            if (StringUtils.isEmpty(result[0]) || result[0].equals("0")) {
                T.showShort(LoginActivity.this, StringUtils.isEmpty(result[1]) ? "获取关联学生失败" : result[1]);
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            } else if (result[0].equals("1")) {

                Log.e("success",edtPhoneNumber.getText().toString().trim());
                // 跳转到学生信息页面
                PreferencesUtils.putString(LoginActivity.this, MLProperties.PREFER_KEY_USER_ID, edtPhoneNumber.getText().toString().trim());
                PreferencesUtils.putString(LoginActivity.this, MLProperties.BUNDLE_KEY_KID_ID, result[1]);
                PreferencesUtils.putString(LoginActivity.this, MLProperties.BUNDLE_KEY_KID_RELATION, result[2]);
                // 如果是虚拟学生，直接跳转到首页
                if (!StringUtils.isEmpty(result[1]) && result[1].startsWith("0003")) {
                    PreferencesUtils.putInt(LoginActivity.this, MLProperties.PREFER_KEY_USER_VIRTUAL, 1);
                    MyGetVirtualKidInfoTask myGetVirtualKidInfoTask = new MyGetVirtualKidInfoTask();
                    myGetVirtualKidInfoTask.execute(result[1]);
                    return;
                } else {
                    PreferencesUtils.putInt(LoginActivity.this, MLProperties.PREFER_KEY_USER_VIRTUAL, 0);
                }
                // 如果关系为空，就跳转到完善信息页面
                if (StringUtils.isEmpty(result[2])) {
                    Intent intent = new Intent(LoginActivity.this, StudentBaseInfoActivity.class);
                    startActivity(intent);
                    LoginActivity.this.finish();
                } else {
                    MyGetKidInfoTask myGetKidInfoTask = new MyGetKidInfoTask();
                    myGetKidInfoTask.execute(result[1]);
                }
            } else {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                // 跳转到关联页面
                PreferencesUtils.putString(LoginActivity.this, MLProperties.PREFER_KEY_USER_ID, edtPhoneNumber.getText().toString().trim());

                Intent intent = new Intent(LoginActivity.this, RelationKidActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
            }
        }
    }

    private class MyGetKidInfoTask extends AsyncTask<String, Integer, String[]> {
        @Override
        protected String[] doInBackground(String... params) {
            String kidId = params[0];

            LmsDataService mService = new LmsDataService();
            String[] result;
            try {
                result = new String[2];
                KidClassInfo kidClassInfo = mService.getKidClassInfoFromAPI2(kidId);
                KidDataInfo kidDataInfo = mService.getStudentDataFromAPI(kidId);

                if (!StringUtils.isEmpty(kidClassInfo.getErrorCode()) && !kidClassInfo.getErrorCode().equals("0") &&
                        !StringUtils.isEmpty(kidDataInfo.getErrorCode()) && !kidDataInfo.getErrorCode().equals("0")) {
                    if (StringUtils.isEmpty(kidClassInfo.getKidGender()) ||
                            StringUtils.isEmpty(kidClassInfo.getKidBirthday())) {
                        result[0] = "2";
                        result[1] = "信息不完整";
                    } else {
                        result[0] = "1";
                        result[1] = kidClassInfo.getKidId();
                        PreferencesUtils.putString(LoginActivity.this, MLProperties.BUNDLE_KEY_KID_GENDER, kidClassInfo.getKidGender());
                        PreferencesUtils.putString(LoginActivity.this, MLProperties.BUNDLE_KEY_KID_BIRTHDAY, kidClassInfo.getKidBirthday());
                        PreferencesUtils.putInt(LoginActivity.this, MLProperties.PREFER_KEY_IS_USER_INFO_COMPLETE, 1);
                    }
                    // 保存学生数据
                    PreferencesUtils.putString(LoginActivity.this, MLProperties.BUNDLE_KEY_KID_ID, kidClassInfo.getKidId());
                    PreferencesUtils.putString(LoginActivity.this, MLProperties.BUNDLE_KEY_KID_NAME, kidClassInfo.getKidName());
                    PreferencesUtils.putString(LoginActivity.this, MLProperties.BUNDLE_KEY_KID_IMG, kidClassInfo.getKidImg());
                    PreferencesUtils.putString(LoginActivity.this, MLProperties.BUNDLE_KEY_SCHOOL_CODE, kidClassInfo.getSchoolId());
                    PreferencesUtils.putString(LoginActivity.this, MLProperties.BUNDLE_KEY_SCHOOL_NAME, kidClassInfo.getSchoolName());
                    PreferencesUtils.putString(LoginActivity.this, MLProperties.BUNDLE_KEY_SCHOOL_IMG, kidClassInfo.getSchoolImg());
                    PreferencesUtils.putString(LoginActivity.this, MLProperties.BUNDLE_KEY_CLASS_CODE, kidClassInfo.getClassId());
                    PreferencesUtils.putString(LoginActivity.this, MLProperties.BUNDLE_KEY_CLASS_NAME, kidClassInfo.getClassName());
                    // 保存学生的数据
                    PreferencesUtils.putString(LoginActivity.this, MLProperties.BUNDLE_KEY_KID_SCORE, kidDataInfo.getScore());
                    PreferencesUtils.putString(LoginActivity.this, MLProperties.BUNDLE_KEY_KID_BADGE, kidDataInfo.getBadge());
                    PreferencesUtils.putString(LoginActivity.this, MLProperties.BUNDLE_KEY_KID_GRADE, kidDataInfo.getGrade());
                    PreferencesUtils.putString(LoginActivity.this, MLProperties.BUNDLE_KEY_KID_PINGYU, kidDataInfo.getPingyu());

                   // PreferencesUtils.putString(LoginActivity.this, MLProperties.BUNDLE_KEY_TIANYUAN_SHOW, kidClassInfo.getTianyuan());


                } else {
                    result[0] = "0";
                    result[1] = "数据异常，请稍后重试";
                }
            } catch (Exception e) {
                e.printStackTrace();
                LL.e(e);
                result = new String[2];
                result[0] = "0";
                result[1] = "服务器开小差了，请待会重试";
            }

            return result;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            if (StringUtils.isEmpty(result[0]) || result[0].equals("0")) {
                T.showShort(LoginActivity.this, result[1]);
            } else if (result[0].equals("1")) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
            } else if (result[0].equals("2")) {
                Intent intent = new Intent(LoginActivity.this, StudentBaseInfoActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
            }
        }
    }

    private class MyGetVirtualKidInfoTask extends AsyncTask<String, Integer, String[]> {
        @Override
        protected String[] doInBackground(String... params) {
            String kidId = params[0];

            LmsDataService mService = new LmsDataService();
            String[] result;
            try {
                result = new String[2];
                KidClassInfo kidClassInfo = mService.getKidClassInfoFromAPI2(kidId);
                KidDataInfo kidDataInfo = mService.getStudentDataFromAPI(kidId);

                if (!StringUtils.isEmpty(kidClassInfo.getErrorCode()) && !kidClassInfo.getErrorCode().equals("0") &&
                        !StringUtils.isEmpty(kidDataInfo.getErrorCode()) && !kidDataInfo.getErrorCode().equals("0")) {
                    result[0] = "1";
                    result[1] = kidClassInfo.getKidId();
                    // 保存学生数据
                    PreferencesUtils.putString(LoginActivity.this, MLProperties.BUNDLE_KEY_KID_ID, kidClassInfo.getKidId());
                    PreferencesUtils.putString(LoginActivity.this, MLProperties.BUNDLE_KEY_KID_NAME, kidClassInfo.getKidName());
                    PreferencesUtils.putString(LoginActivity.this, MLProperties.BUNDLE_KEY_KID_IMG, kidClassInfo.getKidImg());
                    PreferencesUtils.putString(LoginActivity.this, MLProperties.BUNDLE_KEY_SCHOOL_CODE, kidClassInfo.getSchoolId());
                    PreferencesUtils.putString(LoginActivity.this, MLProperties.BUNDLE_KEY_SCHOOL_NAME, kidClassInfo.getSchoolName());
                    PreferencesUtils.putString(LoginActivity.this, MLProperties.BUNDLE_KEY_SCHOOL_IMG, kidClassInfo.getSchoolImg());
                    PreferencesUtils.putString(LoginActivity.this, MLProperties.BUNDLE_KEY_CLASS_CODE, kidClassInfo.getClassId());
                    PreferencesUtils.putString(LoginActivity.this, MLProperties.BUNDLE_KEY_CLASS_NAME, kidClassInfo.getClassName());
                    // 保存学生的数据
                    PreferencesUtils.putString(LoginActivity.this, MLProperties.BUNDLE_KEY_KID_SCORE, kidDataInfo.getScore());
                    PreferencesUtils.putString(LoginActivity.this, MLProperties.BUNDLE_KEY_KID_BADGE, kidDataInfo.getBadge());
                    PreferencesUtils.putString(LoginActivity.this, MLProperties.BUNDLE_KEY_KID_GRADE, kidDataInfo.getGrade());
                    PreferencesUtils.putString(LoginActivity.this, MLProperties.BUNDLE_KEY_KID_PINGYU, kidDataInfo.getPingyu());

                  //  PreferencesUtils.putString(LoginActivity.this, MLProperties.BUNDLE_KEY_TIANYUAN_SHOW, kidClassInfo.getTianyuan());

                } else {
                    result[0] = "0";
                    result[1] = "数据异常，请稍后重试";
                }
            } catch (Exception e) {
                e.printStackTrace();
                LL.e(e);
                result = new String[2];
                result[0] = "0";
                result[1] = "服务器开小差了，请待会重试";
            }

            return result;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            if (StringUtils.isEmpty(result[0]) || result[0].equals("0")) {
                T.showShort(LoginActivity.this, result[1]);
            } else {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        this.exitApp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("login");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("login");
        MobclickAgent.onPause(this);
    }
}

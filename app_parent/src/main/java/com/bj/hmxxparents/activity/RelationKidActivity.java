package com.bj.hmxxparents.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bj.hmxxparents.BaseActivity;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.api.LmsDataService;
import com.bj.hmxxparents.api.MLProperties;
import com.bj.hmxxparents.entity.KidClassInfo;
import com.bj.hmxxparents.entity.KidDataInfo;
import com.bj.hmxxparents.utils.KeyBoardUtils;
import com.bj.hmxxparents.utils.LL;
import com.bj.hmxxparents.utils.PreferencesUtils;
import com.bj.hmxxparents.utils.StringUtils;
import com.bj.hmxxparents.utils.T;
import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.analytics.MobclickAgent;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by he on 2016/12/20.
 * 关联学生页面，当家长第一次使用的时候，需要手动输入学生ID来绑定学生
 */

public class RelationKidActivity extends BaseActivity implements View.OnClickListener {

    private EditText edtStudentId;
    private SimpleDraweeView btnConfirm;
    private TextView tvVirtualUser;
    private LinearLayout llHeaderLeft;
    private ImageView ivBack;
    private String userPhoneNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relation_kid);
        // 初始化页面
        initToolBar();
        initView();
        initDatas();
    }

    private void initToolBar() {
        llHeaderLeft = (LinearLayout) this.findViewById(R.id.header_ll_left);
        ivBack = (ImageView) this.findViewById(R.id.header_img_back);
        TextView tvTitle = (TextView) this.findViewById(R.id.header_tv_title);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.title_relation_kid);
    }

    private void initView() {
        userPhoneNumber = PreferencesUtils.getString(this, MLProperties.PREFER_KEY_USER_ID);

        RelativeLayout rlContent = (RelativeLayout) this.findViewById(R.id.ll_content);
        rlContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtils.closeKeybord(edtStudentId, RelationKidActivity.this);
            }
        });
        edtStudentId = (EditText) this.findViewById(R.id.edt_studentId);
        btnConfirm = (SimpleDraweeView) this.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(this);
        tvVirtualUser = (TextView) this.findViewById(R.id.tv_virtualUser);
        tvVirtualUser.setOnClickListener(this);
    }

    private void initDatas() {
        int isUserVirtual = getIntent().getIntExtra(MLProperties.PREFER_KEY_USER_VIRTUAL, 0);
        // 虚拟用户再次进入该页面时，不显示试用按钮
        if (isUserVirtual == 1) {
            tvVirtualUser.setVisibility(View.GONE);
            ivBack.setVisibility(View.VISIBLE);
            llHeaderLeft.setOnClickListener(view -> {
                finish();
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                actionConfirm();
                break;
            case R.id.tv_virtualUser:
                actionVirtualUser();
                break;
        }
    }

    private void actionVirtualUser() {
        // 获取虚拟用户ID , 如果为 1 则代表当前用户是虚拟用户
        PreferencesUtils.putInt(this, MLProperties.PREFER_KEY_USER_VIRTUAL, 1);
        createVirtualStudent();
    }

    private void actionConfirm() {
        KeyBoardUtils.closeKeybord(edtStudentId, RelationKidActivity.this);
        String studentId = edtStudentId.getText().toString().trim();
        if (StringUtils.isEmpty(studentId)) {
            T.showShort(this, "请输入学生ID");
            return;
        }

//        if (!StringUtils.checkKidID(studentId)) {
//            T.showShort(this, "学生ID格式不正确，请重新输入");
//            return;
//        }

        // 执行异步任务
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute(studentId);
    }

    private void createVirtualStudent() {
        Observable.create((ObservableOnSubscribe<KidClassInfo>) e -> {
            LmsDataService mService = new LmsDataService();
            KidClassInfo kidClassInfo = mService.createVirtualStudent(userPhoneNumber);
            e.onNext(kidClassInfo);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<KidClassInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        showLoadingDialog();
                    }

                    @Override
                    public void onNext(KidClassInfo kidClassInfo) {
                        String studentID = kidClassInfo.getKidId();
                        MyGetVirtualKidInfoTask myGetVirtualKidInfoTask = new MyGetVirtualKidInfoTask();
                        myGetVirtualKidInfoTask.execute(studentID);
                    }

                    @Override
                    public void onError(Throwable e) {
                        T.showShort(RelationKidActivity.this, "服务器开小差了，请待会重试");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private class MyAsyncTask extends AsyncTask<String, Integer, KidClassInfo> {
        @Override
        protected void onPreExecute() {
            showLoadingDialog();
            btnConfirm.setClickable(false);
        }

        @Override
        protected KidClassInfo doInBackground(String... params) {
            LmsDataService mService = new LmsDataService();
            KidClassInfo kidClassInfo;
            try {
                kidClassInfo = mService.getKidClassInfoFromAPI2(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
                LL.e(e);
                kidClassInfo = new KidClassInfo();
                kidClassInfo.setErrorCode("0");
                kidClassInfo.setMessage("服务器开小差了，请待会重试");
            }
            return kidClassInfo;
        }

        @Override
        protected void onPostExecute(KidClassInfo result) {
            hideLoadingDialog();
            String errorCode = result.getErrorCode();
            String message = result.getMessage();
            if (errorCode.equals("0")) {
                btnConfirm.setClickable(true);
                T.showShort(RelationKidActivity.this, message);
            } else {
                // 跳转到学生信息确认页面
                Intent intent = new Intent(RelationKidActivity.this, StudentInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(MLProperties.BUNDLE_KEY_KID_ID, result.getKidId());
                bundle.putString(MLProperties.BUNDLE_KEY_KID_NAME, result.getKidName());
                bundle.putString(MLProperties.BUNDLE_KEY_KID_IMG, result.getKidImg());
                bundle.putString(MLProperties.BUNDLE_KEY_SCHOOL_NAME, result.getSchoolName());
                bundle.putString(MLProperties.BUNDLE_KEY_SCHOOL_CODE, result.getSchoolId());
                bundle.putString(MLProperties.BUNDLE_KEY_SCHOOL_IMG, result.getSchoolImg());
                bundle.putString(MLProperties.BUNDLE_KEY_CLASS_NAME, result.getClassName());
                bundle.putString(MLProperties.BUNDLE_KEY_CLASS_CODE, result.getClassId());
                bundle.putString(MLProperties.BUNDLE_KEY_KID_GENDER, result.getKidGender());
                bundle.putString(MLProperties.BUNDLE_KEY_KID_BIRTHDAY, result.getKidBirthday());

                intent.putExtras(bundle);
                startActivity(intent);
                RelationKidActivity.this.finish();
                overridePendingTransition(R.anim.right_left_in, R.anim.right_left_out);
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
                    PreferencesUtils.putString(RelationKidActivity.this, MLProperties.BUNDLE_KEY_KID_ID, kidClassInfo.getKidId());
                    PreferencesUtils.putString(RelationKidActivity.this, MLProperties.BUNDLE_KEY_KID_NAME, kidClassInfo.getKidName());
                    PreferencesUtils.putString(RelationKidActivity.this, MLProperties.BUNDLE_KEY_KID_IMG, kidClassInfo.getKidImg());
                    PreferencesUtils.putString(RelationKidActivity.this, MLProperties.BUNDLE_KEY_SCHOOL_CODE, kidClassInfo.getSchoolId());
                    PreferencesUtils.putString(RelationKidActivity.this, MLProperties.BUNDLE_KEY_SCHOOL_NAME, kidClassInfo.getSchoolName());
                    PreferencesUtils.putString(RelationKidActivity.this, MLProperties.BUNDLE_KEY_SCHOOL_IMG, kidClassInfo.getSchoolImg());
                    PreferencesUtils.putString(RelationKidActivity.this, MLProperties.BUNDLE_KEY_CLASS_CODE, kidClassInfo.getClassId());
                    PreferencesUtils.putString(RelationKidActivity.this, MLProperties.BUNDLE_KEY_CLASS_NAME, kidClassInfo.getClassName());
                    // 保存学生的数据
                    PreferencesUtils.putString(RelationKidActivity.this, MLProperties.BUNDLE_KEY_KID_SCORE, kidDataInfo.getScore());
                    PreferencesUtils.putString(RelationKidActivity.this, MLProperties.BUNDLE_KEY_KID_BADGE, kidDataInfo.getBadge());
                    PreferencesUtils.putString(RelationKidActivity.this, MLProperties.BUNDLE_KEY_KID_GRADE, kidDataInfo.getGrade());
                    PreferencesUtils.putString(RelationKidActivity.this, MLProperties.BUNDLE_KEY_KID_PINGYU, kidDataInfo.getPingyu());
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
            hideLoadingDialog();

            if (StringUtils.isEmpty(result[0]) || result[0].equals("0")) {
                T.showShort(RelationKidActivity.this, result[1]);
            } else {
                Intent intent = new Intent(RelationKidActivity.this, MainActivity.class);
                startActivity(intent);
                RelationKidActivity.this.finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("addChild");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("addChild");
        MobclickAgent.onPause(this);
    }
}

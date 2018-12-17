package com.bj.hmxxparents.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.bj.hmxxparents.BaseActivity;
import com.bj.hmxxparents.MyApplication;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.api.LmsDataService;
import com.bj.hmxxparents.api.MLProperties;
import com.bj.hmxxparents.entity.KidClassInfo;
import com.bj.hmxxparents.utils.PreferencesUtils;
import com.bj.hmxxparents.utils.StringUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zz379 on 2017/5/5.
 * 学生基本信息选择，选择生日，性别，家长与孩子的关系
 */

public class StudentBaseInfoActivity extends BaseActivity {

    @BindView(R.id.header_tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_nextStep)
    TextView tvNextStep;
    @BindView(R.id.tv_birthday)
    TextView tvBirthday;
    @BindView(R.id.cb_a)
    CheckBox cbA;
    @BindView(R.id.cb_b)
    CheckBox cbB;
    @BindView(R.id.cb_c)
    CheckBox cbC;
    @BindView(R.id.cb_d)
    CheckBox cbD;
    @BindView(R.id.cb_e)
    CheckBox cbE;

    private long exitTime = 0;
    private long currTimeMillin;

    private int studBirYear, studBirMonth, studBirDay;
    private String studentBirthday;

    private String studentGender;
    private String studentRelation;

    private Calendar mCalendar;
    private String userPhoneNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_base_info);
        ButterKnife.bind(this);
        // 手动管理activity
        MyApplication.getInstances().addActivity(this);

        initToolBar();
        initView();
        initData();
    }

    private void initToolBar() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("完善信息");
    }

    private void initView() {
        mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        studBirYear = mCalendar.get(Calendar.YEAR);
        studBirMonth = mCalendar.get(Calendar.MONTH) + 1;
        studBirDay = mCalendar.get(Calendar.DAY_OF_MONTH);
    }

    private void initData() {
        userPhoneNumber = PreferencesUtils.getString(this, MLProperties.PREFER_KEY_USER_ID);
        // 获取学生信息
        getStudentBaseInfoFromAPI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("childInfo_birthday");
        MobclickAgent.onResume(this);
        checkNextStep();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("childInfo_birthday");
        MobclickAgent.onPause(this);
    }

    @OnClick(R.id.tv_nextStep)
    void clickNextStep() {
        // 临时保存数据
        PreferencesUtils.putString(this, MLProperties.BUNDLE_KEY_KID_BIRTHDAY, studentBirthday);
        PreferencesUtils.putString(this, MLProperties.BUNDLE_KEY_KID_GENDER, studentGender);
        PreferencesUtils.putString(this, MLProperties.BUNDLE_KEY_KID_RELATION, studentRelation);
        // 跳转到下个页面
        Intent intent = new Intent(this, StudentHobbyActivity.class);
        intent.putExtra("Type", 0);
        startActivity(intent);
        overridePendingTransition(R.anim.right_left_in, R.anim.right_left_out);
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) <= 2000) {
            // 退出APP
            MainActivity.finishSelf();
            this.finish();
        } else {
            Toast.makeText(this, getString(R.string.toast_home_exit_system), Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        }
    }

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            studBirYear = year;
            studBirMonth = month;
            studBirDay = dayOfMonth;
            studentBirthday = String.format("%d-%02d-%02d", year, month + 1, dayOfMonth);
            tvBirthday.setText(studentBirthday);
            checkNextStep();
        }
    };

    @OnClick(R.id.rl_birthday)
    void clickSelectBirthday() {
        if (System.currentTimeMillis() - currTimeMillin > 2000) {
            DatePickerDialog dialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Dialog,
                    dateSetListener, studBirYear, studBirMonth, studBirDay);
            mCalendar.set(Calendar.YEAR, 1998);
            mCalendar.set(Calendar.MONTH, 00);
            mCalendar.set(Calendar.DAY_OF_MONTH, 01);
            dialog.getDatePicker().setMinDate(mCalendar.getTimeInMillis());
            dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            dialog.setTitle("生日");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            currTimeMillin = System.currentTimeMillis();
        }
    }

    @OnClick(R.id.rl_a)
    void clickCBA() {
        cbA.setChecked(true);
        cbB.setChecked(false);
        studentGender = "1";
        checkNextStep();
    }

    @OnClick(R.id.rl_b)
    void clickCBB() {
        cbA.setChecked(false);
        cbB.setChecked(true);
        studentGender = "0";
        checkNextStep();
    }

    @OnClick(R.id.rl_c)
    void clickCBC() {
        cbC.setChecked(true);
        cbD.setChecked(false);
        cbE.setChecked(false);
        studentRelation = "baba";
        checkNextStep();
    }

    @OnClick(R.id.rl_d)
    void clickCBD() {
        cbC.setChecked(false);
        cbD.setChecked(true);
        cbE.setChecked(false);
        studentRelation = "mama";
        checkNextStep();
    }

    @OnClick(R.id.rl_e)
    void clickCBE() {
        cbC.setChecked(false);
        cbD.setChecked(false);
        cbE.setChecked(true);
        studentRelation = "jiazhang";
        checkNextStep();
    }

    private void checkNextStep() {
        if (!StringUtils.isEmpty(studentBirthday) && !StringUtils.isEmpty(studentGender) && !StringUtils.isEmpty(studentRelation)) {
            tvNextStep.setEnabled(true);
        } else {
            tvNextStep.setEnabled(false);
        }
    }

    private void getStudentBaseInfoFromAPI() {
        showLoadingDialog();
        Observable.create((ObservableOnSubscribe<KidClassInfo>) emitter -> {
            LmsDataService mService = new LmsDataService();
            KidClassInfo kidClassInfo = mService.getStudentBaseInfoFromAPI(userPhoneNumber);
            emitter.onNext(kidClassInfo);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<KidClassInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(KidClassInfo kidClassInfo) {
                        hideLoadingDialog();
                        String gender = kidClassInfo.getKidGender();
                        String birthday = kidClassInfo.getKidBirthday();
                        String relation = kidClassInfo.getKidRelation();

                        if (!StringUtils.isEmpty(birthday)) {
                            studentBirthday = birthday;
                            tvBirthday.setText(birthday);
                            PreferencesUtils.putString(StudentBaseInfoActivity.this, MLProperties.BUNDLE_KEY_KID_BIRTHDAY, birthday);
                            checkNextStep();
                        }

                        if (!StringUtils.isEmpty(gender)) {
                            if (gender.equals("1")) {
                                clickCBA();
                            } else {
                                clickCBB();
                            }
                            PreferencesUtils.putString(StudentBaseInfoActivity.this, MLProperties.BUNDLE_KEY_KID_GENDER, gender);
                        }

                        if (!StringUtils.isEmpty(relation)) {
                            if (relation.equals("baba")) {
                                clickCBC();
                            } else if (relation.equals("mama")) {
                                clickCBD();
                            } else if (relation.equals("jiazhang")) {
                                clickCBE();
                            }
                            PreferencesUtils.putString(StudentBaseInfoActivity.this, MLProperties.BUNDLE_KEY_KID_RELATION, relation);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideLoadingDialog();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}

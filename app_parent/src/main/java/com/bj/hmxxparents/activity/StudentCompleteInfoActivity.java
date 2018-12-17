package com.bj.hmxxparents.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bj.hmxxparents.BaseActivity;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.api.LmsDataService;
import com.bj.hmxxparents.api.MLProperties;
import com.bj.hmxxparents.entity.StudHobbyCategory;
import com.bj.hmxxparents.entity.StudentHobbyInfo;
import com.bj.hmxxparents.utils.PreferencesUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

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
 * Created by zz379 on 2017/5/8.
 * 学生完整信息展示页面
 */

public class StudentCompleteInfoActivity extends BaseActivity {

    @BindView(R.id.header_img_back)
    ImageView imgBack;
    @BindView(R.id.header_tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_gender)
    TextView tvGender;
    @BindView(R.id.tv_birthday)
    TextView tvBirthday;
    @BindView(R.id.tv_hobby)
    TextView tvHobby;
    @BindView(R.id.tv_training)
    TextView tvTraining;

    private String gender;
    private String birthday;
    private String hobby;
    private String training;
    private String userPhoneNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_complete_info);
        ButterKnife.bind(this);

        initToolbar();
        initView();
        initData();
    }

    private void initToolbar() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("孩子完整信息");
        imgBack.setVisibility(View.VISIBLE);
    }

    private void initView() {

    }

    private void initData() {
        userPhoneNumber = PreferencesUtils.getString(this, MLProperties.PREFER_KEY_USER_ID);
        gender = PreferencesUtils.getString(this, MLProperties.BUNDLE_KEY_KID_GENDER, "");
        birthday = PreferencesUtils.getString(this, MLProperties.BUNDLE_KEY_KID_BIRTHDAY, "");
        hobby = PreferencesUtils.getString(this, MLProperties.BUNDLE_KEY_KID_HOBBY_NAME, "");
        training = PreferencesUtils.getString(this, MLProperties.BUNDLE_KEY_KID_TRAINING_NAME, "");

        tvGender.setText(gender.equals("1") ? "男" : "女");
        tvBirthday.setText(birthday);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("childInfo_completeinfo");
        MobclickAgent.onResume(this);
        getHobbyDataListFromAPI();
        getTrainingDataFromAPI();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("childInfo_completeinfo");
        MobclickAgent.onPause(this);
    }

    @OnClick(R.id.header_ll_left)
    void clickBack() {
        this.finish();
        overridePendingTransition(R.anim.left_right_in, R.anim.left_right_out);
    }

    @OnClick(R.id.rl_hobby)
    void clickHobby() {
        Intent intent = new Intent(this, StudentHobbyActivity.class);
        intent.putExtra("Type", 1);
        startActivity(intent);
    }

    @OnClick(R.id.rl_training)
    void clickTraining() {
        Intent intent = new Intent(this, StudentTrainingActivity.class);
        intent.putExtra("Type", 1);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        clickBack();
    }

    private void getHobbyDataListFromAPI() {
        Observable.create((ObservableOnSubscribe<List<StudHobbyCategory>>) emitter -> {
            LmsDataService mService = new LmsDataService();
            List<StudHobbyCategory> dataList = mService.getStudentHobbyFromAPI(userPhoneNumber);
            emitter.onNext(dataList);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<StudHobbyCategory>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<StudHobbyCategory> studHobbyCategories) {
                        updatePageData(studHobbyCategories);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (hobby.length() > 10) {
                            hobby = hobby.substring(0, 10);
                            hobby = hobby + "...";
                        }

                        tvHobby.setText(hobby);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void updatePageData(List<StudHobbyCategory> studHobbyCategories) {
        StringBuffer sbName = new StringBuffer();
        for (StudHobbyCategory category : studHobbyCategories) {
            // 初始化各个分类下选中的子项
            for (StudentHobbyInfo item : category.getHobbyInfoList()) {
                if (item.isHobbyIsChecked()) {
                    sbName.append(item.getHobbyName()).append("，");
                }
            }
        }

        if (sbName.length() > 0) {
            sbName.deleteCharAt(sbName.length() - 1);
        }
        String hobby = sbName.toString();
        if (hobby.length() > 10) {
            hobby = hobby.substring(0, 10);
            hobby = hobby + "...";
        }

        PreferencesUtils.putString(this, MLProperties.BUNDLE_KEY_KID_HOBBY_NAME, hobby);

        tvHobby.setText(hobby);
    }

    private void getTrainingDataFromAPI() {
        Observable.create((ObservableOnSubscribe<List<StudentHobbyInfo>>) emitter -> {
            LmsDataService mService = new LmsDataService();
            List<StudentHobbyInfo> dataList = mService.getStudentTrainingFromAPI(userPhoneNumber);
            emitter.onNext(dataList);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<StudentHobbyInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<StudentHobbyInfo> studHobbyCategories) {
                        updatePageDataList(studHobbyCategories);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (training.length() > 10) {
                            training = training.substring(0, 10);
                            training = training + "...";
                        }
                        tvTraining.setText(training);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void updatePageDataList(List<StudentHobbyInfo> studHobbyCategories) {
        StringBuffer sbName = new StringBuffer();
        // 初始化被选择的列表
        for (StudentHobbyInfo item : studHobbyCategories) {
            if (item.isHobbyIsChecked()) {
                sbName.append(item.getHobbyName()).append("，");
            }
        }
        if (sbName.length() > 0) {
            sbName.deleteCharAt(sbName.length() - 1);
        }
        String training = sbName.toString();
        if (training.length() > 10) {
            training = training.substring(0, 10);
            training = training + "...";
        }

        PreferencesUtils.putString(this, MLProperties.BUNDLE_KEY_KID_TRAINING_NAME, training);

        tvTraining.setText(training);
    }
}

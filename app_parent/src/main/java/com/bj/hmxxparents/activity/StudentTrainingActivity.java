package com.bj.hmxxparents.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bj.hmxxparents.BaseActivity;
import com.bj.hmxxparents.MyApplication;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.adapter.StudentHobbyAdapter;
import com.bj.hmxxparents.api.LmsDataService;
import com.bj.hmxxparents.api.MLProperties;
import com.bj.hmxxparents.entity.StudentHobbyInfo;
import com.bj.hmxxparents.utils.DensityUtils;
import com.bj.hmxxparents.utils.PreferencesUtils;
import com.bj.hmxxparents.utils.StringUtils;
import com.bj.hmxxparents.utils.T;
import com.bj.hmxxparents.widget.SpacesItemDecoration;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.CancelConfirmAlertDialog;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zz379 on 2017/5/5.
 * 学生培训记录 -- Training
 */

public class StudentTrainingActivity extends BaseActivity {

    @BindView(R.id.header_img_back)
    ImageView imgBack;
    @BindView(R.id.header_tv_title)
    TextView tvTitle;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.iv_infoProgress)
    ImageView ivInfoProgress;
    @BindView(R.id.tv_nextStep)
    TextView tvNextStep;

    private List<StudentHobbyInfo> mSelectedList = new ArrayList<>();
    private List<StudentHobbyInfo> mDataList = new ArrayList<>();
    private StudentHobbyAdapter mAdapter;
    private GridLayoutManager layoutManager;
    private int pageType;

    private String userPhoneNumber;
    private String gender, birthday, relation, hobbies;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_training);
        ButterKnife.bind(this);
        // 手动管理activity
        MyApplication.getInstances().addActivity(this);

        initToolBar();
        initView();
        initData();
    }

    private void initToolBar() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("记录培训");
        imgBack.setVisibility(View.VISIBLE);
    }

    private void initView() {
        pageType = getIntent().getIntExtra("Type", 0);
        if (pageType == 1) {
            ivInfoProgress.setVisibility(View.GONE);
        } else {
            ivInfoProgress.setVisibility(View.VISIBLE);
        }

        int padding = DensityUtils.dp2px(this, 4);
        int itemPadding = DensityUtils.dp2px(this, 4);

        // 下拉刷新控件
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setPadding(padding, padding, padding, padding);
        // look as listview
        layoutManager = new GridLayoutManager(this, 4);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mAdapter.isHeader(position)) {
                    return layoutManager.getSpanCount();
                } else {
                    if (mDataList.get(position - 1).getHobbyShowType() != StudentHobbyInfo.SHOW_TYPE_ITEM) {
                        return layoutManager.getSpanCount();
                    } else {
                        return 1;
                    }
                }
            }
        });
        mRecyclerView.setLayoutManager(layoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration(itemPadding);
        mRecyclerView.addItemDecoration(decoration);

        // set Adatper
        mAdapter = new StudentHobbyAdapter(mDataList);
        mAdapter.setOnMyItemClickListener(new StudentHobbyAdapter.OnMyItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                onHobbyItemClick(position - 1);
            }

            @Override
            public void onNoneClick(View view, int position) {
                if (!mDataList.get(position - 1).isHobbyIsChecked()) {
                    actionNoItemSelected(position - 1);
                } else {
                    mDataList.get(position - 1).setHobbyIsChecked(false);
                    mAdapter.notifyDataSetChanged();
                }
                checkNextStep();
            }
        });
        mAdapter.setHeaderView(R.layout.recycler_header_training, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        userPhoneNumber = PreferencesUtils.getString(this, MLProperties.PREFER_KEY_USER_ID);
        gender = PreferencesUtils.getString(this, MLProperties.BUNDLE_KEY_KID_GENDER);
        birthday = PreferencesUtils.getString(this, MLProperties.BUNDLE_KEY_KID_BIRTHDAY);
        relation = PreferencesUtils.getString(this, MLProperties.BUNDLE_KEY_KID_RELATION);
        hobbies = PreferencesUtils.getString(this, MLProperties.BUNDLE_KEY_KID_HOBBY_ID);

        // 获取兴趣数据列表
        getDataListFromAPI(userPhoneNumber);
    }

    private void onHobbyItemClick(int position) {
        StudentHobbyInfo item = mDataList.get(position);
        boolean isSelect = item.isHobbyIsChecked();
        if (isSelect) {
            // 取消选择
            if (mSelectedList.contains(item)) {
                mSelectedList.remove(item);
            }
            item.setHobbyIsChecked(false);
        } else {
            // 选中
            if (mSelectedList.size() < 6) {
                item.setHobbyIsChecked(true);
                mSelectedList.add(item);
            } else {
                T.showShort(this, "最多选6个");
            }
        }
        // 设置最后一项未选中
        mDataList.get(mDataList.size() - 1).setHobbyIsChecked(false);
        mAdapter.notifyDataSetChanged();
        checkNextStep();
    }

    private void actionNoItemSelected(int position) {
        // 最后一项选中
        mDataList.get(position).setHobbyIsChecked(true);
        mAdapter.notifyDataSetChanged();
        // 弹框提示
        CancelConfirmAlertDialog dialog = new CancelConfirmAlertDialog(this)
                .setTitleText("温馨提示")
                .setContentText("选择此项后，我们将无法为您精准推荐内容")
                .setConfirmText("确定")
                .setCancelClickListener(sDialog -> {
                    sDialog.dismiss();
                    mDataList.get(position).setHobbyIsChecked(false);
                    mAdapter.notifyDataSetChanged();

                    checkNextStep();
                })
                .setConfirmClickListener(sDialog -> {
                    sDialog.dismiss();
                    for (StudentHobbyInfo item : mDataList) {
                        item.setHobbyIsChecked(false);
                    }
                    mDataList.get(position).setHobbyIsChecked(true);
                    mAdapter.notifyDataSetChanged();
                    mSelectedList.clear();
                    // 调用接口
                    clickNextStep();
                });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @OnClick(R.id.tv_nextStep)
    void clickNextStep() {
        // 进入页面之后什么都没有选择
        if (mSelectedList.size() == 0 && !mDataList.get(mDataList.size() - 1).isHobbyIsChecked()) {
            return;
        }

        StringBuffer sbID = new StringBuffer();
        StringBuffer sbName = new StringBuffer();
        for (StudentHobbyInfo item : mSelectedList) {
            sbID.append(item.getHobbyID()).append(",");
            sbName.append(item.getHobbyName()).append(",");
        }
        if (sbID.length() > 0) {
            sbID.deleteCharAt(sbID.length() - 1);
        }
        if (sbName.length() > 0) {
            sbName.deleteCharAt(sbName.length() - 1);
        }

        String studentTrainingID = sbID.toString();
        String studentTrainingName = sbName.toString();

        PreferencesUtils.putString(this, MLProperties.BUNDLE_KEY_KID_TRAINING_ID, studentTrainingID);
        PreferencesUtils.putString(this, MLProperties.BUNDLE_KEY_KID_TRAINING_NAME, studentTrainingName);

        // 数据处理完成
        if (pageType == 0) {
            // 提交三个页面的数据
            setAllDataFromAPI(userPhoneNumber, studentTrainingID);
        } else {
            // 只修改本页面的培训数据
            setTrainingDataFromAPI(userPhoneNumber, studentTrainingID);
        }
    }

    @OnClick(R.id.header_ll_left)
    void clickBack() {
        this.finish();
        overridePendingTransition(R.anim.left_right_in, R.anim.left_right_out);
    }

    @Override
    public void onBackPressed() {
        clickBack();
    }

    private void setAllDataFromAPI(String userPhoneNumber, String trainings) {
        showLoadingDialog();
        Observable.create((ObservableOnSubscribe<String[]>) emitter -> {
            String[] result = new String[2];

            LmsDataService mService = new LmsDataService();
            String[] resultBaseInfo = mService.setStudentBaseInfoFromAPI(userPhoneNumber, gender, birthday, relation);
            String[] resultHobby = mService.setStudentHobbyFormAPI(userPhoneNumber, hobbies);
            String[] resultTraining = mService.setStudentTrainingFormAPI(userPhoneNumber, trainings);

            if (resultBaseInfo[0].equals("1") && resultHobby[0].equals("1") && resultTraining[0].equals("1")) {
                result[0] = "1";
            } else {
                result[0] = "0";
            }
            emitter.onNext(result);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String[]>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String[] strings) {
                        hideLoadingDialog();
                        if (!StringUtils.isEmpty(strings[0]) && strings[0].equals("1")) {
                            // 跳转到首页
                            PreferencesUtils.putInt(StudentTrainingActivity.this, MLProperties.PREFER_KEY_IS_USER_INFO_COMPLETE, 1);
                            startActivity(new Intent(StudentTrainingActivity.this, MainActivity.class));
                            MyApplication.getInstances().exitAll();
                        } else {
                            T.showShort(StudentTrainingActivity.this, "提交失败，请重试");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        T.showShort(StudentTrainingActivity.this, "网络连接异常，请重试");
                        hideLoadingDialog();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    private void setTrainingDataFromAPI(String userPhoneNumber, String trainings) {
        showLoadingDialog();
        Observable.create((ObservableOnSubscribe<String[]>) emitter -> {
            LmsDataService mService = new LmsDataService();
            String[] result = mService.setStudentTrainingFormAPI(userPhoneNumber, trainings);
            emitter.onNext(result);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String[]>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String[] strings) {
                        hideLoadingDialog();
                        if (!StringUtils.isEmpty(strings[0]) && strings[0].equals("1")) {
                            // T.showShort(StudentTrainingActivity.this, "修改成功");
                            StudentTrainingActivity.this.finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        T.showShort(StudentTrainingActivity.this, "网络连接异常，请重试");
                        hideLoadingDialog();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getDataListFromAPI(String userPhoneNumber) {
        showLoadingDialog();
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

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void updatePageDataList(List<StudentHobbyInfo> studHobbyCategories) {
        mDataList.addAll(studHobbyCategories);
        // 初始化被选择的列表
        for (StudentHobbyInfo item : studHobbyCategories) {
            if (item.isHobbyIsChecked()) {
                mSelectedList.add(item);
            }
        }
        // 增加任何都不选的item
        StudentHobbyInfo none = new StudentHobbyInfo();
        none.setHobbyShowType(StudentHobbyInfo.SHOW_TYPE_NONE);
        if (pageType == 1 && mSelectedList.size() == 0) {
            none.setHobbyIsChecked(true);
        } else {
            none.setHobbyIsChecked(false);
        }
        mDataList.add(none);

        mAdapter.notifyDataSetChanged();
        hideLoadingDialog();

        checkNextStep();
    }

    private void checkNextStep() {
        if (mDataList.size() > 0) {
            if (mSelectedList.size() != 0 || mDataList.get(mDataList.size() - 1).isHobbyIsChecked()) {
                tvNextStep.setEnabled(true);
            } else {
                tvNextStep.setEnabled(false);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("childInfo_peixun");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("childInfo_peixun");
        MobclickAgent.onPause(this);
    }
}

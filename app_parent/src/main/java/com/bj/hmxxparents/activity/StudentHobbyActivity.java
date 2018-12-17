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
import com.bj.hmxxparents.entity.StudHobbyCategory;
import com.bj.hmxxparents.entity.StudentHobbyInfo;
import com.bj.hmxxparents.utils.DensityUtils;
import com.bj.hmxxparents.utils.PreferencesUtils;
import com.bj.hmxxparents.utils.StringUtils;
import com.bj.hmxxparents.utils.T;
import com.bj.hmxxparents.widget.SpacesItemDecoration;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
 * 学生兴趣爱好
 */

public class StudentHobbyActivity extends BaseActivity {

    @BindView(R.id.header_img_back)
    ImageView imgBack;
    @BindView(R.id.header_tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_nextStep)
    TextView tvNextStep;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.iv_infoProgress)
    ImageView ivInfoProgress;

    private List<StudentHobbyInfo> mDataList = new ArrayList<>();
    private HashMap<String, List<StudentHobbyInfo>> mSelectedMap = new HashMap<>();

    private StudentHobbyAdapter mAdapter;
    private GridLayoutManager layoutManager;
    private int pageType;
    private String userPhoneNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_hobby);
        ButterKnife.bind(this);
        // 手动管理activity
        MyApplication.getInstances().addActivity(this);

        initToolBar();
        initView();
        initData();
    }

    private void initToolBar() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("选择兴趣");
        imgBack.setVisibility(View.VISIBLE);
    }

    private void initView() {
        pageType = getIntent().getIntExtra("Type", 0);
        if (pageType == 1) {
            ivInfoProgress.setVisibility(View.GONE);
            tvNextStep.setText("完成");
        } else {
            ivInfoProgress.setVisibility(View.VISIBLE);
            tvNextStep.setText("下一步");
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

            }
        });
        mAdapter.setHeaderView(R.layout.recycler_header_hobby, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        userPhoneNumber = PreferencesUtils.getString(this, MLProperties.PREFER_KEY_USER_ID);

        // 获取兴趣数据列表
        getDataListFromAPI(userPhoneNumber);
    }

    private void onHobbyItemClick(int position) {
        StudentHobbyInfo item = mDataList.get(position);
        List<StudentHobbyInfo> selectList = mSelectedMap.get(item.getHobbyCategory());
        boolean isSelect = item.isHobbyIsChecked();
        if (isSelect) {
            // 取消选择
            if (selectList.contains(item)) {
                selectList.remove(item);
            }
            item.setHobbyIsChecked(false);
        } else {
            // 选中
            if (selectList.size() < 3) {
                item.setHobbyIsChecked(true);
                selectList.add(item);
            } else {
                T.showShort(this, "一个类别最多选3个");
            }
        }

        mSelectedMap.put(item.getHobbyCategory(), selectList);
        mAdapter.notifyDataSetChanged();
        // 检查下一步
        checkNextStep();
    }

    @OnClick(R.id.tv_nextStep)
    void clickNextStep() {
        Iterator iterator = mSelectedMap.entrySet().iterator();
        StringBuffer sbID = new StringBuffer();
        StringBuffer sbName = new StringBuffer();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            List<StudentHobbyInfo> selectList = (List<StudentHobbyInfo>) entry.getValue();
            if (selectList.size() == 0) {
                T.showShort(this, "一个类别最少选1个");
                return;
            } else {
                for (StudentHobbyInfo info : selectList) {
                    sbID.append(info.getHobbyID()).append(",");
                    sbName.append(info.getHobbyName()).append(",");
                }
            }
        }
        sbID.deleteCharAt(sbID.length() - 1);
        sbName.deleteCharAt(sbName.length() - 1);
        String studentHobbies = sbID.toString();
        String studentHobbiesName = sbName.toString();
        PreferencesUtils.putString(this, MLProperties.BUNDLE_KEY_KID_HOBBY_ID, studentHobbies);
        PreferencesUtils.putString(this, MLProperties.BUNDLE_KEY_KID_HOBBY_NAME, studentHobbiesName);
        // 数据处理完毕
        if (pageType == 0) {
            // 进入下个页面，在下个页面进行数据的提交工作
            Intent intent = new Intent(this, StudentTrainingActivity.class);
            intent.putExtra("Type", 0);
            startActivity(intent);
            overridePendingTransition(R.anim.right_left_in, R.anim.right_left_out);
        } else {
            // 调接口保存数据, 只保存本页面的兴趣数据
            setHobbyDataFromAPI(userPhoneNumber, studentHobbies);
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

    private void setHobbyDataFromAPI(String userPhoneNumber, String hobbiesCode) {
        showLoadingDialog();
        Observable.create((ObservableOnSubscribe<String[]>) emitter -> {
            LmsDataService mService = new LmsDataService();
            String[] result = mService.setStudentHobbyFormAPI(userPhoneNumber, hobbiesCode);
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
                            // T.showShort(StudentHobbyActivity.this, "修改成功");
                            StudentHobbyActivity.this.finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        T.showShort(StudentHobbyActivity.this, "网络连接异常，请重试");
                        hideLoadingDialog();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getDataListFromAPI(String userPhoneNumber) {
        showLoadingDialog();
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

    private void updatePageDataList(List<StudHobbyCategory> studHobbyCategories) {

        for (StudHobbyCategory category : studHobbyCategories) {
            StudentHobbyInfo hobbyCategory = new StudentHobbyInfo();
            hobbyCategory.setHobbyName(category.getTypeName());
            hobbyCategory.setHobbyShowType(StudentHobbyInfo.SHOW_TYPE_CATEGORY);
            mDataList.add(hobbyCategory);

            mDataList.addAll(category.getHobbyInfoList());
            // 初始化各个分类下选中的子项
            List<StudentHobbyInfo> selectList = new ArrayList<>();
            for (StudentHobbyInfo item : category.getHobbyInfoList()) {
                if (item.isHobbyIsChecked()) {
                    selectList.add(item);
                }
            }
            mSelectedMap.put(category.getTypeName(), selectList);
        }
        mAdapter.notifyDataSetChanged();
        hideLoadingDialog();
        checkNextStep();
    }

    private void checkNextStep() {
        tvNextStep.setEnabled(checkSelectedList());
    }

    private boolean checkSelectedList() {
        Iterator iterator = mSelectedMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            List<StudentHobbyInfo> selectList = (List<StudentHobbyInfo>) entry.getValue();
            if (selectList.size() == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("childInfo_xingqu");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("childInfo_xingqu");
        MobclickAgent.onPause(this);
    }
}

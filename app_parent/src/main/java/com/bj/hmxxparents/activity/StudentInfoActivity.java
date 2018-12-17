package com.bj.hmxxparents.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bj.hmxxparents.BaseActivity;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.api.LmsDataService;
import com.bj.hmxxparents.api.MLProperties;
import com.bj.hmxxparents.utils.LL;
import com.bj.hmxxparents.utils.PreferencesUtils;
import com.bj.hmxxparents.utils.StringUtils;
import com.bj.hmxxparents.utils.T;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.analytics.MobclickAgent;
import com.yongchun.library.view.ImageSelectorActivity;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.bj.hmxxparents.utils.BitmapUtils.changeFileSize;

/**
 * Created by he on 2016/12/20.
 * 输入学生ID后的学生信息确认页面
 */

public class StudentInfoActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQUEST_IMAGE = 1;
    private static final int REQUEST_CODE = 0; // 请求码

    private SimpleDraweeView btnBack, btnConfirm, ivSchoolImg;
    private TextView tvStudentName, tvSchoolName, tvClassName;
    private SimpleDraweeView imgKidPhoto;
    private String kidId, parentPhoneNumber;
    private String kidName;
    private String kidImg;
    private String schoolName;
    private String schoolID;
    private String schoolImg;
    private String className;
    private String classID;
    private String kidPicturePath;
    private String kidGender;
    private String kidBirthday;
    private int isUserVirtual;
    private String virtualKidID;

    private long currTimeMillin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info);
        // 初始化页面
        initToolBar();
        initView();
        initDatas();

    }

    private void initToolBar() {
        TextView tvTitle = (TextView) this.findViewById(R.id.header_tv_title);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.title_student_info);
    }

    private void initView() {
        imgKidPhoto = (SimpleDraweeView) this.findViewById(R.id.img_kidPhoto);
        imgKidPhoto.setOnClickListener(this);
        tvStudentName = (TextView) this.findViewById(R.id.tv_studentName);
        tvSchoolName = (TextView) this.findViewById(R.id.tv_schoolName);
        tvClassName = (TextView) this.findViewById(R.id.tv_className);

        btnBack = (SimpleDraweeView) this.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);
        btnConfirm = (SimpleDraweeView) this.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(this);
        ivSchoolImg = (SimpleDraweeView) this.findViewById(R.id.iv_schoolImg);
        isUserVirtual = PreferencesUtils.getInt(this, MLProperties.PREFER_KEY_USER_VIRTUAL, 0);
        if (isUserVirtual == 1) {
            virtualKidID = PreferencesUtils.getString(this, MLProperties.BUNDLE_KEY_KID_ID);
            ImageView ivBack = (ImageView) this.findViewById(R.id.header_img_back);
            ivBack.setVisibility(View.VISIBLE);
            LinearLayout llLeft = (LinearLayout) this.findViewById(R.id.header_ll_left);
            llLeft.setOnClickListener(view -> {
                Intent intent = new Intent(StudentInfoActivity.this, RelationKidActivity.class);
                intent.putExtra(MLProperties.PREFER_KEY_USER_VIRTUAL, isUserVirtual);
                startActivity(intent);
                this.finish();
                overridePendingTransition(R.anim.left_right_in, R.anim.left_right_out);
            });
        }
    }

    private void initDatas() {
        parentPhoneNumber = PreferencesUtils.getString(StudentInfoActivity.this, MLProperties.PREFER_KEY_USER_ID);
        Bundle bundle = getIntent().getExtras();
        kidId = bundle.getString(MLProperties.BUNDLE_KEY_KID_ID);
        kidName = bundle.getString(MLProperties.BUNDLE_KEY_KID_NAME);
        kidImg = bundle.getString(MLProperties.BUNDLE_KEY_KID_IMG);
        kidGender = bundle.getString(MLProperties.BUNDLE_KEY_KID_GENDER);
        kidBirthday = bundle.getString(MLProperties.BUNDLE_KEY_KID_BIRTHDAY);

        // 保存学生数据
        PreferencesUtils.putString(StudentInfoActivity.this, MLProperties.BUNDLE_KEY_KID_ID, kidId);
        PreferencesUtils.putString(StudentInfoActivity.this, MLProperties.BUNDLE_KEY_KID_NAME, kidName);
        PreferencesUtils.putString(StudentInfoActivity.this, MLProperties.BUNDLE_KEY_KID_IMG, kidImg);

        schoolName = bundle.getString(MLProperties.BUNDLE_KEY_SCHOOL_NAME);
        schoolID = bundle.getString(MLProperties.BUNDLE_KEY_SCHOOL_CODE);
        schoolImg = bundle.getString(MLProperties.BUNDLE_KEY_SCHOOL_IMG);
        className = bundle.getString(MLProperties.BUNDLE_KEY_CLASS_NAME);
        classID = bundle.getString(MLProperties.BUNDLE_KEY_CLASS_CODE);

        tvStudentName.setText(String.format("姓名：%s", kidName));
        tvSchoolName.setText(String.format("学校：%s", schoolName));
        tvClassName.setText(String.format("班级：%s", className));

        imgKidPhoto.setImageURI(kidImg);
        if (!StringUtils.isEmpty(schoolImg)) {
            ivSchoolImg.setImageURI(Uri.parse(schoolImg));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                Intent intent = new Intent(StudentInfoActivity.this, RelationKidActivity.class);
                intent.putExtra(MLProperties.PREFER_KEY_USER_VIRTUAL, isUserVirtual);
                startActivity(intent);
                this.finish();
                overridePendingTransition(R.anim.left_right_in, R.anim.left_right_out);
                break;
            case R.id.btn_confirm:
                actionConfirmRelation();
                break;
            case R.id.img_kidPhoto:
                RxPermissions rxPermissions = new RxPermissions(StudentInfoActivity.this);
                rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(success -> {
                            if (success) {
                                actionSelectKidPhoto();
                            } else {
                                T.showShort(StudentInfoActivity.this, "未获取到相机权限");
                            }
                        });
                break;
        }
    }

    private void actionConfirmRelation() {
        if (System.currentTimeMillis() - currTimeMillin < 1000) {
            currTimeMillin = System.currentTimeMillis();
        } else {
            currTimeMillin = System.currentTimeMillis();
            // 如果是虚拟用户，需要先删除关联关系，在绑定真实的学生
            if (isUserVirtual == 1) {
                deleteVirtualStudentRelation(parentPhoneNumber, virtualKidID);
            } else {
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute(parentPhoneNumber, kidId);
            }
        }
    }

    private void actionSelectKidPhoto() {
        ImageSelectorActivity.start(StudentInfoActivity.this, 1, ImageSelectorActivity.MODE_SINGLE
                , true, true, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == ImageSelectorActivity.REQUEST_IMAGE) {
            ArrayList<String> images = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
            // do something
            String filePath = images.get(0);
            Log.i("way", "FilePath:" + filePath);
            if (filePath != null && !filePath.equals("")) {
                File file = new File(filePath);
                Log.i("way", "FileSize:" + file.length() / 1024 + "KB");
                kidPicturePath = changeFileSize(filePath);
                File newFile = new File(kidPicturePath);
                Log.i("way", "newFileSize:" + newFile.length() / 1024 + "KB");
                Uri uri = Uri.parse("file://" + kidPicturePath);
                imgKidPhoto.setImageURI(uri);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (isUserVirtual == 1) {
            Intent intent = new Intent(StudentInfoActivity.this, RelationKidActivity.class);
            intent.putExtra(MLProperties.PREFER_KEY_USER_VIRTUAL, isUserVirtual);
            startActivity(intent);
            this.finish();
            overridePendingTransition(R.anim.left_right_in, R.anim.left_right_out);
        }
    }

    private class MyAsyncTask extends AsyncTask<String, Integer, String[]> {

        @Override
        protected void onPreExecute() {
            showLoadingDialog();
            btnConfirm.setClickable(false);
            btnBack.setClickable(false);
        }

        @Override
        protected String[] doInBackground(String... params) {
            String phoneNumber = params[0];
            String kidId = params[1];
            LmsDataService mService = new LmsDataService();
            String[] result;
            try {
                result = new String[2];
                String[] uploadResult;
                if (!StringUtils.isEmpty(kidPicturePath)) {
                    uploadResult = mService.uploadKidPhoto(kidId, kidPicturePath);
                    if (!StringUtils.isEmpty(uploadResult[0]) && uploadResult[0].equals("1")) {
                        PreferencesUtils.putString(StudentInfoActivity.this, MLProperties.BUNDLE_KEY_KID_IMG, uploadResult[1]);
                    } else {
                        return uploadResult;
                    }
                } else {
                    uploadResult = new String[2];
                    if (!StringUtils.isEmpty(kidImg)) {
                        // 当kidImg 不为空的时候可以继续进行下一步的操作
                        uploadResult[0] = "1";
                        uploadResult[1] = "图片已经上传过";
                    } else {
                        uploadResult[0] = "0";
                        uploadResult[1] = "您需要上传一张照片作为您孩子的头像";
                        return uploadResult;
                    }
                }

                String[] relationResult;
                if (!StringUtils.isEmpty(uploadResult[0]) && uploadResult[0].equals("1")) {
                    relationResult = mService.relationKidFromAPI(phoneNumber, kidId);
                } else {
                    relationResult = new String[2];
                    relationResult[0] = "0";
                    relationResult[1] = uploadResult[1];
                }

                if (!StringUtils.isEmpty(uploadResult[0]) && !uploadResult[0].equals("0") &&
                        !StringUtils.isEmpty(relationResult[0]) && !relationResult[0].equals("0")) {
                    result[0] = "1";
                    result[1] = "成功";
                    return result;
                } else {
                    result[0] = "0";
                    if (!StringUtils.isEmpty(uploadResult[0]) && uploadResult[0].equals("0")) {
                        result[1] = uploadResult[1];
                        return result;
                    }

                    if (!StringUtils.isEmpty(relationResult[0]) && relationResult[0].equals("0")) {
                        result[1] = relationResult[1];
                        return result;
                    }

                    result[1] = "服务器开小差了，请待会重试";
                    return result;
                }

            } catch (Exception e) {
                e.printStackTrace();
                LL.e(e);
                result = new String[2];
                result[0] = "0";
                result[1] = "服务器开小差了，请待会重试";
                return result;
            }
        }

        @Override
        protected void onPostExecute(String[] result) {
            hideLoadingDialog();

            if (StringUtils.isEmpty(result[0]) || result[0].equals("0")) {
                T.showShort(StudentInfoActivity.this, StringUtils.isEmpty(result[1]) ? "服务器开小差了，请待会重试" : result[1]);
                btnConfirm.setClickable(true);
                btnBack.setClickable(true);
            } else {
                // T.showShort(StudentInfoActivity.this, "关联成功");
                PreferencesUtils.putString(StudentInfoActivity.this, MLProperties.BUNDLE_KEY_SCHOOL_CODE, schoolID);
                PreferencesUtils.putString(StudentInfoActivity.this, MLProperties.BUNDLE_KEY_SCHOOL_NAME, schoolName);
                PreferencesUtils.putString(StudentInfoActivity.this, MLProperties.BUNDLE_KEY_SCHOOL_IMG, schoolImg);
                PreferencesUtils.putString(StudentInfoActivity.this, MLProperties.BUNDLE_KEY_CLASS_CODE, classID);
                PreferencesUtils.putString(StudentInfoActivity.this, MLProperties.BUNDLE_KEY_CLASS_NAME, className);
                PreferencesUtils.putString(StudentInfoActivity.this, MLProperties.BUNDLE_KEY_KID_GENDER, kidGender);
                PreferencesUtils.putString(StudentInfoActivity.this, MLProperties.BUNDLE_KEY_KID_BIRTHDAY, kidBirthday);

                // finish 掉首页
                MainActivity.getInstance().finishSelf();
                // 需要根据学生的返回学生的基本信息判断跳转到首页还是跳转到信息完善页面
                Intent intent = new Intent(StudentInfoActivity.this, StudentBaseInfoActivity.class);
                startActivity(intent);
                StudentInfoActivity.this.finish();
            }
        }
    }

    private void deleteVirtualStudentRelation(String userPhoneNumber, String virtualKidId) {
        Observable.create((ObservableOnSubscribe<String[]>) e -> {
            LmsDataService mService = new LmsDataService();
            String[] result = mService.deleteVirtualStudentRelation(userPhoneNumber, virtualKidId);
            e.onNext(result);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String[]>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String[] strings) {
                        // 删除成功后去绑定真正的学生
                        if (!StringUtils.isEmpty(strings[2]) && strings[2].equals("1")) {
                            // 保存虚拟用户的状态
                            PreferencesUtils.putInt(StudentInfoActivity.this, MLProperties.PREFER_KEY_USER_VIRTUAL, 0);
                            // 关联真正的学生
                            MyAsyncTask myAsyncTask = new MyAsyncTask();
                            myAsyncTask.execute(parentPhoneNumber, kidId);
                        } else {
                            T.showShort(StudentInfoActivity.this, "数据异常，请重试");
                            btnConfirm.setClickable(true);
                            btnBack.setClickable(true);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        T.showShort(StudentInfoActivity.this, "服务器开小差了，请待会重试");
                        btnConfirm.setClickable(true);
                        btnBack.setClickable(true);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("checkInfo");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("checkInfo");
        MobclickAgent.onPause(this);
    }
}

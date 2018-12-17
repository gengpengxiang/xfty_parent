package com.bj.hmxxparents.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.andview.refreshview.utils.LogUtils;
import com.bj.hmxxparents.BaseFragment;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.activity.LoginActivity;
import com.bj.hmxxparents.activity.MainActivity;
import com.bj.hmxxparents.activity.RelationKidActivity;
import com.bj.hmxxparents.activity.SettingActivity;
import com.bj.hmxxparents.activity.StudentCompleteInfoActivity;
import com.bj.hmxxparents.api.LmsDataService;
import com.bj.hmxxparents.api.MLConfig;
import com.bj.hmxxparents.api.MLProperties;
import com.bj.hmxxparents.email.EmailActivity;
import com.bj.hmxxparents.entity.AppVersionInfo;
import com.bj.hmxxparents.entity.KidClassInfo;
import com.bj.hmxxparents.entity.StudentInfos;
import com.bj.hmxxparents.manager.UMPushManager;
import com.bj.hmxxparents.service.DownloadAppService;
import com.bj.hmxxparents.utils.AppUtils;
import com.bj.hmxxparents.utils.DataCleanManager;
import com.bj.hmxxparents.utils.LL;
import com.bj.hmxxparents.utils.PermissionsCheckUtisls;
import com.bj.hmxxparents.utils.PreferencesUtils;
import com.bj.hmxxparents.utils.StringUtils;
import com.bj.hmxxparents.utils.T;
import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.hyphenate.chat.EMClient;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.analytics.MobclickAgent;
import com.yongchun.library.view.ImageSelectorActivity;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.CancelConfirmAlertDialog;
import cn.pedant.SweetAlert.CancelConfirmAlertDialog2;
import cn.pedant.SweetAlert.InviteOthersAlertDialog;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;
import static com.bj.hmxxparents.api.HttpUtilService.BASE_API_URL;
import static com.bj.hmxxparents.utils.BitmapUtils.changeFileSize;

/**
 * Created by zz379 on 2017/4/ic_pinde.
 * "我的" 页面
 */

public class UserFragment extends BaseFragment {

    @BindView(R.id.header_tv_title)
    TextView tvTitle;
    @BindView(R.id.img_kidPhoto)
    SimpleDraweeView imgUserPhoto;
    @BindView(R.id.img_schoolBg)
    SimpleDraweeView imgSchoolBg;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_user_phone_number)
    TextView tvUserPhoneNumber;
    @BindView(R.id.tv_schoolName)
    TextView tvSchoolName;
    @BindView(R.id.tv_versionName)
    TextView tvVersionName;
    @BindView(R.id.tv_className)
    TextView tvClassName;
    @BindView(R.id.tv_kidInfo)
    TextView tvKidInfo;
    @BindView(R.id.rl_email)
    RelativeLayout rlEmail;
    @BindView(R.id.iv_email_dian)
    ImageView ivEmailDian;

    private String userID;
    private String userPhotoPath;
    private String userName, userPhoneNumber, userSchoolName, userClassName;
    private String schoolID;
    private String schoolImg;

    private long currMillis = 0;

    private int isUserVirtual;
    private String studentcode;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick(R.id.rl_email)
    public void onClick() {
        Intent intent = new Intent(getActivity(), EmailActivity.class);
        startActivityForResult(intent, 1);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_user_info, container, false);
        ButterKnife.bind(this, view);
        studentcode = PreferencesUtils.getString(getActivity(), MLProperties.BUNDLE_KEY_KID_ID);

        getStudentInfo();

        initToolbar();
        initView();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initToolbar() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(getString(R.string.bottom_tab_2));
    }


    private void initView() {
        userID = PreferencesUtils.getString(getActivity(), MLProperties.BUNDLE_KEY_KID_ID);
        userPhotoPath = PreferencesUtils.getString(getActivity(), MLProperties.BUNDLE_KEY_KID_IMG);
        userName = PreferencesUtils.getString(getActivity(), MLProperties.BUNDLE_KEY_KID_NAME);
        userPhoneNumber = PreferencesUtils.getString(getActivity(), MLProperties.PREFER_KEY_USER_ID);
        userSchoolName = PreferencesUtils.getString(getActivity(), MLProperties.BUNDLE_KEY_SCHOOL_NAME, "");
        schoolID = PreferencesUtils.getString(getActivity(), MLProperties.BUNDLE_KEY_SCHOOL_CODE, "");
        schoolImg = PreferencesUtils.getString(getActivity(), MLProperties.BUNDLE_KEY_SCHOOL_IMG, "");
        userClassName = PreferencesUtils.getString(getActivity(), MLProperties.BUNDLE_KEY_CLASS_NAME, "");

        isUserVirtual = PreferencesUtils.getInt(getActivity(), MLProperties.PREFER_KEY_USER_VIRTUAL, 0);

        imgSchoolBg.setImageURI(Uri.parse("res:///" + R.drawable.bg_user_banner));
        tvVersionName.setText("V" + AppUtils.getVersionName(getActivity()));

        // 用户头像
        if (!StringUtils.isEmpty(userPhotoPath)) {
            imgUserPhoto.setImageURI(Uri.parse(userPhotoPath));
        }
        // 名字和手机号
        tvUserName.setText(userName);
        tvUserPhoneNumber.setText(userPhoneNumber);

        // 学校名字和班级名字，如果为空就去获取学生数据
        if (StringUtils.isEmpty(userSchoolName) || StringUtils.isEmpty(userClassName)) {
            getStudentInfoFromAPI();
        } else {
            tvSchoolName.setText(userSchoolName);
            tvClassName.setText(userClassName);
        }

        // 如果是虚拟用户，显示添加真实孩子
        if (isUserVirtual == 1) {
            tvKidInfo.setText("添加真实孩子");
        } else {
            tvKidInfo.setText("孩子完整信息");
        }


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            userPhotoPath = PreferencesUtils.getString(getActivity(), MLProperties.BUNDLE_KEY_KID_IMG);
            if (!StringUtils.isEmpty(userPhotoPath)) {
                imgUserPhoto.setImageURI(Uri.parse(userPhotoPath));
            }
        }
    }

    @OnClick(R.id.img_kidPhoto)
    void clickUserPhoto() {
        MobclickAgent.onEvent(getActivity(), "mine_changeAvatar");
        RxPermissions rxPermissions = new RxPermissions(getActivity());
        rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(success -> {
                    if (success) {
                        actionSelectKidPhoto();
                    } else {
                        T.showShort(getActivity(), "未获取到相机权限");
                    }
                });
    }

    @OnClick(R.id.rl_kidInfo)
    void clickCompleteKidInfo() {
        if (System.currentTimeMillis() - currMillis <= 1000) {
            return;
        }
        currMillis = System.currentTimeMillis();
        // 如果是虚拟用户，弹框提示删除用户数据、跳转到绑定学生页面
        if (isUserVirtual == 1) {
            showVirtualUserDataDeleteDialog();
            MobclickAgent.onEvent(getActivity(), "mine_addChild");
        } else {    // 否则跳转到学生信息完善页面
            Intent intent = new Intent(getActivity(), StudentCompleteInfoActivity.class);
            startActivity(intent);
        }
    }

    private void showVirtualUserDataDeleteDialog() {
        CancelConfirmAlertDialog2 dialog = new CancelConfirmAlertDialog2(getActivity());
        dialog.setTitleText("添加真实孩子");
        dialog.setContentText("添加真实孩子账户后，将清空虚拟账户的所有数据");
        dialog.setConfirmText("确定");
        dialog.setCancelText("取消");
        dialog.setConfirmClickListener(sweetAlertDialog -> {
            Intent intent = new Intent(getActivity(), RelationKidActivity.class);
            intent.putExtra(MLProperties.PREFER_KEY_USER_VIRTUAL, isUserVirtual);
            startActivity(intent);
            sweetAlertDialog.dismiss();
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


    private void getRequestPermission(String name) {
        RxPermissions rxPermissions = new RxPermissions(getActivity());
        rxPermissions.request(name
                //Manifest.permission.WRITE_EXTERNAL_STORAGE
//                Manifest.permission.RECORD_AUDIO,
//                Manifest.permission.READ_PHONE_STATE
        )
                .subscribe(granted -> {
                    if (granted) {
                        // All requested permissions are granted
                    } else {
                        // At least one permission is denied
                    }
                });
    }

    @OnClick(R.id.rl_question)
    void clickQuestion() {
        if (System.currentTimeMillis() - currMillis > 1000) {
            MobclickAgent.onEvent(getActivity(), "mine_customerService");
            CancelConfirmAlertDialog dialog = new CancelConfirmAlertDialog(getActivity())
                    .setTitleText("联系客服")
                    .setContentText("拨打客服电话 " + getString(R.string.custome_service_phonenumber))
                    .setConfirmText("拨打")
                    .setCancelClickListener(sDialog -> {
                        sDialog.dismiss();
                    })
                    .setConfirmClickListener(sDialog -> {
//                        RxPermissions rxPermissions = new RxPermissions(getActivity());
//                        rxPermissions.request(Manifest.permission.READ_PHONE_STATE
//                        )
//                                .subscribe(granted -> {
//                                    if (granted) {
//                                        // All requested permissions are granted
//                                        sDialog.dismiss();
//                                        actionCallPhone(getString(R.string.custome_service_phonenumber));
//                                    } else {
//                                        // At least one permission is denied
//                                    }
//                                });
                        sDialog.dismiss();
                        actionCallPhone(getString(R.string.custome_service_phonenumber));
                    });
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            currMillis = System.currentTimeMillis();
        }
    }


    /**
     * 拨打电话 -->> 需要动态申请权限
     *
     * @param phoneNumber
     */
    private void actionCallPhone(String phoneNumber) {
        RxPermissions rxPermissions = new RxPermissions(getActivity());
        rxPermissions.request(Manifest.permission.CALL_PHONE)
                .subscribe(success -> {
                    if (success) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + phoneNumber));
                        startActivity(intent);
                    } else {
                        // 未授权拨打电话
                        LL.i("未授权拨打电话");
                    }
                });
    }

    @OnClick(R.id.rl_inviteParents)
    void clickInviteParents() {
        if (System.currentTimeMillis() - currMillis > 1000) {
            MobclickAgent.onEvent(getActivity(), "mine_inviteParent");
            showInviteDialog(R.drawable.ic_invite_parent, R.string.invite_parents_content);
            currMillis = System.currentTimeMillis();
        }
    }

    private void showInviteDialog(int resID, int contentID) {
        InviteOthersAlertDialog dialog = new InviteOthersAlertDialog(getActivity())
                .setContentImage(ContextCompat.getDrawable(getActivity(), resID))
                .setCancelText("去QQ粘贴")
                .setConfirmText("去微信粘贴")
                .setCancelClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.dismiss();
                    actionCallQQ(getString(contentID));
                })
                .setConfirmClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.dismiss();
                    actionCallWeixin(getString(contentID));
                });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void actionCallQQ(String content) {
        // 复制内容到剪切板
        ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText("Label", content);
        cm.setPrimaryClip(data);

        // 检查是否安装了QQ
        if (isQQClientAvailable(getActivity())) {
            // Intent 打开QQ
            Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage("com.tencent.mobileqq");
            startActivity(intent);
        } else {
            T.showShort(getActivity(), "已复制成功，请到QQ粘贴并发送邀请");
        }
    }

    private void actionCallWeixin(String content) {
        // 复制内容到剪切板
        ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText("Label", content);
        cm.setPrimaryClip(data);

        // 检查是否安装了微信
        if (isWeixinAvilible(getActivity())) {
            // Intent 打开微信
            Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
            startActivity(intent);
        } else {
            T.showShort(getActivity(), "已复制成功，请到微信粘贴并发送邀请");
        }
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

    private boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                LogUtils.e("pn = " + pn);
                if (pn.equalsIgnoreCase("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    @OnClick(R.id.rl_about)
    void clickAbout() {
        Intent intent = new Intent(getActivity(), SettingActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.rl_check_version)
    void clickCheckNewVersion() {
        if (System.currentTimeMillis() - currMillis > 1000) {
            MobclickAgent.onEvent(getActivity(), "mine_checkVersion");
            MyCheckNewVersionTask versionTask = new MyCheckNewVersionTask();
            versionTask.execute();
            currMillis = System.currentTimeMillis();


        }
    }

    @OnClick(R.id.btn_logout)
    void clickLogout() {
        MobclickAgent.onEvent(getActivity(), "mine_loginOut");
        // 保存这次登录的时间
        PreferencesUtils.putLong(getActivity(), MLProperties.PREFER_KEY_LOGIN_Time, System.currentTimeMillis());
        PreferencesUtils.putInt(getActivity(), MLProperties.PREFER_KEY_LOGIN_STATUS, 0);
        PreferencesUtils.putString(getActivity(), MLProperties.BUNDLE_KEY_KID_IMG, "");
        // 删除手机号和设备的关联关系
        UMPushManager.getInstance().removePushAlias(userPhoneNumber);
        UMPushManager.getInstance().removeAllTag(schoolID);
        // 清空缓存
        cleanCache();
        cleanAllPreferencesData();
        // 退出环信
        EMClient.getInstance().logout(true);

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
        getActivity().overridePendingTransition(R.anim.right_left_in, R.anim.right_left_out);
    }

    private void cleanAllPreferencesData() {
        // 清除所有app内的数据
        PreferencesUtils.cleanAllData(getActivity());
        // 清除环信数据
        getActivity().getSharedPreferences("EM_SP_AT_MESSAGE", Context.MODE_PRIVATE).edit().clear().apply();
    }

    private void cleanCache() {
        // Fresco 清空缓存
        Fresco.getImagePipeline().clearCaches();
        // Glide 清空缓存
        Observable.create((ObservableOnSubscribe<String>) e -> Glide.get(getActivity().getApplicationContext()).clearDiskCache()).subscribeOn(Schedulers.io()).subscribe();
        // 清空所有缓存
        DataCleanManager.clearAllCache(getActivity().getApplicationContext());
        DataCleanManager.clearAllSharedPrefs(getActivity().getApplicationContext());
    }

    private void actionSelectKidPhoto() {
        ImageSelectorActivity.start(this, 1, ImageSelectorActivity.MODE_SINGLE
                , true, true, true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        LL.i("Fragment onActivityResult()");
        //6aa64393ce184f855c6634db406a56a9.jpeg
        if (resultCode == RESULT_OK && requestCode == Activity.RESULT_FIRST_USER) {
            ArrayList<String> images = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
            // do something
            String filePath = images.get(0);
            Log.i("way", "FilePath:" + filePath);
            if (filePath != null && !filePath.equals("")) {
                File file = new File(filePath);
                Log.i("way", "FileSize:" + file.length() / 1024 + "KB");
                String kidPicturePath = changeFileSize(filePath);
                File newFile = new File(kidPicturePath);
                Log.i("way", "newFileSize:" + newFile.length() / 1024 + "KB");
                if (!StringUtils.isEmpty(kidPicturePath)) {
                    UpdateUserPhotoTask task = new UpdateUserPhotoTask();
                    task.execute(kidPicturePath);
                }
            }
        }
        //add
        if (resultCode == 222 && requestCode == 1) {
            getStudentInfo();
        }
    }

    private void getStudentInfo() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) throws Exception {
                OkGo.<String>post(BASE_API_URL + "jz/getstudentinfo")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("studentcode", studentcode)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String str = response.body().toString();
                                Log.e("获取学生信息", str);
                                e.onNext(str);
                                e.onComplete();
                            }
                        });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        StudentInfos studentInfos = JSON.parseObject(s, new TypeReference<StudentInfos>() {
                        });
                        //邮箱是否显示红点
                        if (studentInfos.getData().getXinxiang_status().equals("1")) {
                            ivEmailDian.setVisibility(View.VISIBLE);
                        } else {
                            ivEmailDian.setVisibility(View.GONE);
                        }
                    }
                });
    }


    private class GetStudentInfoTask extends AsyncTask<String, Integer, KidClassInfo> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected KidClassInfo doInBackground(String... params) {
            LmsDataService mService = new LmsDataService();
            KidClassInfo kidClassInfo;
            try {
                kidClassInfo = mService.getKidClassInfoFromAPI(params[0]);
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
            String errorCode = result.getErrorCode();
            String message = result.getMessage();
            if (errorCode.equals("0")) {

            } else {
                userPhotoPath = result.getKidImg();
                userSchoolName = result.getSchoolName();
                userClassName = result.getClassName();

                PreferencesUtils.putString(getActivity(), MLProperties.BUNDLE_KEY_SCHOOL_NAME, userSchoolName);
                PreferencesUtils.putString(getActivity(), MLProperties.BUNDLE_KEY_CLASS_NAME, userClassName);
                tvSchoolName.setText(userSchoolName);
                tvClassName.setText(userClassName);
            }
        }
    }

    private class MyCheckNewVersionTask extends AsyncTask<String, Integer, AppVersionInfo> {

        @Override
        protected AppVersionInfo doInBackground(String... params) {
            LmsDataService mService = new LmsDataService();
            AppVersionInfo info;
            String versionName = AppUtils.getVersionName(getActivity());
            String qudao = AppUtils.getMetaDataFromApplication(getActivity(), MLConfig.KEY_CHANNEL_NAME);
            try {
                Log.e("版本号=", versionName + "渠道=" + qudao);
//                info = mService.checkNewVersion(versionName, qudao);
                info = mService.checkNewVersion(versionName, qudao);
            } catch (Exception e) {
                e.printStackTrace();
                LL.e(e);
                info = new AppVersionInfo();
                info.setErrorCode("0");
            }
            return info;
        }

        @Override
        protected void onPostExecute(AppVersionInfo info) {
            if (StringUtils.isEmpty(info.getErrorCode()) || info.getErrorCode().equals("0")) {
                return;
            }
            if (info.getErrorCode().equals("1")) {

                String qiangzhigengxin = info.getQiangzhigengxin();

                showNewVersionDialog(qiangzhigengxin, info.getTitle(), info.getContent(), info.getDownloadUrl());
            } else if (info.getErrorCode().equals("2")) {
                T.showShort(getActivity(), info.getMessage());
            }
        }
    }

    private void showNewVersionDialog(String qiangzhigengxin, String title, String content, final String downloadUrl) {
        createUpdateAppDialog(title, content, sweetAlertDialog -> {
            // confirm
            RxPermissions rxPermissions = new RxPermissions(getActivity());
            rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(success -> {
                        if (success) {
                            startDownloadAppService(downloadUrl);
                            sweetAlertDialog.startDownload();
                        } else {
                            sweetAlertDialog.dismiss();
                        }
                    });
        }, sweetAlertDialog -> {
            // cancel
//            sweetAlertDialog.dismiss();
            sweetAlertDialog.dismiss();


        }, sweetAlertDialog -> {
            // cancel Download
            sweetAlertDialog.dismiss();
            stopDownloadAppService(downloadUrl);
        });
    }

    private void startDownloadAppService(String downloadUrl) {
        Intent intent = new Intent(getActivity(), DownloadAppService.class);
        Bundle args = new Bundle();
        args.putString(MLConfig.KEY_BUNDLE_DOWNLOAD_URL, downloadUrl);
        intent.putExtras(args);
        getActivity().startService(intent);
    }

    private void stopDownloadAppService(String downloadUrl) {
        OkHttpUtils.getInstance().cancelTag(DownloadAppService.FILENAME);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onVisible() {
        super.onVisible();
        MobclickAgent.onPageStart("mine");
    }

    @Override
    protected void onInVisible() {
        super.onInVisible();
        MobclickAgent.onPageEnd("mine");
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private class UpdateUserPhotoTask extends AsyncTask<String, Integer, String[]> {

        @Override
        protected void onPreExecute() {
            showLoadingDialog();
        }

        @Override
        protected String[] doInBackground(String... params) {
            String kidPicturePath = params[0];
            LmsDataService mService = new LmsDataService();
            String[] result;
            try {
                result = mService.uploadKidPhoto(userID, kidPicturePath);
            } catch (Exception e) {
                e.printStackTrace();
                LL.e(e);
                result = new String[3];
                result[0] = "0";
                result[1] = "服务器开小差了，请待会重试";
                result[2] = kidPicturePath;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String[] result) {
            hideLoadingDialog();
            if (StringUtils.isEmpty(result[0]) || result[0].equals("0")) {
                T.showShort(getActivity(), StringUtils.isEmpty(result[1]) ? "服务器开小差了，请待会重试" : result[1]);
            } else {
                T.showShort(getActivity(), "头像更新成功");
                userPhotoPath = result[1];
                PreferencesUtils.putString(getActivity(), MLProperties.BUNDLE_KEY_KID_IMG, userPhotoPath);
                if (!StringUtils.isEmpty(userPhotoPath)) {
                    imgUserPhoto.setImageURI(Uri.parse(userPhotoPath));
                }
            }
        }
    }

    private void getStudentInfoFromAPI() {
        Observable.create((ObservableOnSubscribe<KidClassInfo>) emitter -> {
            LmsDataService mService = new LmsDataService();
            KidClassInfo kidClassInfo = mService.getKidClassInfoFromAPI2(userID);
            emitter.onNext(kidClassInfo);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<KidClassInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(KidClassInfo kidClassInfo) {
                        // 保存学生数据
                        userSchoolName = kidClassInfo.getSchoolName();
                        userClassName = kidClassInfo.getClassName();

                        PreferencesUtils.putString(getActivity(), MLProperties.BUNDLE_KEY_KID_ID, kidClassInfo.getKidId());
                        PreferencesUtils.putString(getActivity(), MLProperties.BUNDLE_KEY_KID_NAME, kidClassInfo.getKidName());
                        PreferencesUtils.putString(getActivity(), MLProperties.BUNDLE_KEY_KID_IMG, kidClassInfo.getKidImg());
                        PreferencesUtils.putString(getActivity(), MLProperties.BUNDLE_KEY_SCHOOL_CODE, kidClassInfo.getSchoolId());
                        PreferencesUtils.putString(getActivity(), MLProperties.BUNDLE_KEY_SCHOOL_NAME, kidClassInfo.getSchoolName());
                        PreferencesUtils.putString(getActivity(), MLProperties.BUNDLE_KEY_SCHOOL_IMG, kidClassInfo.getSchoolImg());
                        PreferencesUtils.putString(getActivity(), MLProperties.BUNDLE_KEY_CLASS_CODE, kidClassInfo.getClassId());
                        PreferencesUtils.putString(getActivity(), MLProperties.BUNDLE_KEY_CLASS_NAME, kidClassInfo.getClassName());
                        PreferencesUtils.putString(getActivity(), MLProperties.BUNDLE_KEY_KID_GENDER, kidClassInfo.getKidGender());
                        PreferencesUtils.putString(getActivity(), MLProperties.BUNDLE_KEY_KID_BIRTHDAY, kidClassInfo.getKidBirthday());

                        if (!StringUtils.isEmpty(userSchoolName)) {
                            tvSchoolName.setText(userSchoolName);
                        }
                        if (!StringUtils.isEmpty(userClassName)) {
                            tvClassName.setText(userClassName);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}

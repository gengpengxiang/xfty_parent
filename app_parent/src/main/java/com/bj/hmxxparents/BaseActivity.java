package com.bj.hmxxparents;

import android.content.Intent;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.bj.hmxxparents.activity.MainActivity;
import com.bj.hmxxparents.utils.LL;
import com.bj.hmxxparents.utils.ScreenUtils;
import com.bj.hmxxparents.widget.LoadingDialog;
import com.umeng.message.PushAgent;
import com.zhy.autolayout.AutoLayoutActivity;

import cn.pedant.SweetAlert.TipsAlertDialog;
import cn.pedant.SweetAlert.UpdateAPPAlertDialog;

/**
 * Created by he on 2016/11/9.
 */

public class BaseActivity extends AutoLayoutActivity {

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 统计应用启动数据
        PushAgent.getInstance(this).onAppStart();

        // 设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }

        LL.i("ActivityName", this.getLocalClassName());
        if (this.getLocalClassName().equals("activity.StudentBaseInfoActivity")) {
            LL.i("ActivityName", this.getLocalClassName());
            MyApplication.getInstances().setSingleTaskActivityLaunched(true);
        }

        loadingDialog = new LoadingDialog(this);

        // 如果存在虚拟按键，则设置虚拟按键的背景色
        if (ScreenUtils.checkDeviceHasNavigationBar(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setNavigationBarColor(ContextCompat.getColor(this, android.R.color.black));
            }
        }

        Log.e("当前Activity==", getClass().getSimpleName());
    }

    @Override
    protected void onDestroy() {
        if (this.getLocalClassName().equals("activity.StudentBaseInfoActivity")) {
            MyApplication.getInstances().setSingleTaskActivityLaunched(false);
        }
        super.onDestroy();
    }

    /**
     * 1、获取main在窗体的可视区域
     * 2、获取main在窗体的不可视区域高度
     * 3、判断不可视区域高度
     * 1、大于100：键盘显示  获取Scroll的窗体坐标
     * 算出main需要滚动的高度，使scroll显示。
     * 2、小于100：键盘隐藏
     *
     * @param main   根布局
     * @param scroll 需要显示的最下方View
     */
    public void addLayoutListener(final View main, final View scroll) {
        main.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                main.getWindowVisibleDisplayFrame(rect);
                int mainInvisibleHeight = main.getRootView().getHeight() - rect.bottom;
                if (mainInvisibleHeight > 100) {
                    int[] location = new int[2];
                    scroll.getLocationInWindow(location);
                    int srollHeight = (location[1] + scroll.getHeight()) - rect.bottom;
                    main.scrollTo(0, srollHeight);
                } else {
                    main.scrollTo(0, 0);
                }
            }
        });
    }

    public static final String TAG_EXIT = "exitApp";

    public void exitApp() {
        if (MyApplication.getInstances().isSingleTaskActivityLaunched()) {
            // Intent intent = new Intent(this, HomeActivity.class);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(TAG_EXIT, true);
            startActivity(intent);
            BaseActivity.this.finish();
        } else {
            BaseActivity.this.finish();
        }
    }

    public void showTipsDialog(String content) {
        TipsAlertDialog dialog = new TipsAlertDialog(this);
        dialog.setContentText(content);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /**
     * 显示加载对话框
     */
    public void showLoadingDialog() {
        if (loadingDialog != null && !loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    /**
     * 隐藏加载对话框
     */
    public void hideLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    public void createUpdateAppDialog(String title, String content, UpdateAPPAlertDialog.OnSweetClickListener confirmListener,
                                      UpdateAPPAlertDialog.OnSweetClickListener cancelListener) {
        UpdateAPPAlertDialog dialog = new UpdateAPPAlertDialog(this);
        dialog.setTitleText(title);
        dialog.setContentText(content);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setConfirmClickListener(confirmListener);
        dialog.setCancelClickListener(cancelListener);
        dialog.show();
    }

    public boolean cameraIsCanUse() {
        boolean isCanUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters(); //针对魅族手机
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            isCanUse = false;
        }

        if (mCamera != null) {
            try {
                mCamera.release();
            } catch (Exception e) {
                e.printStackTrace();
                return isCanUse;
            }
        }
        return isCanUse;
    }
}

package com.bj.hmxxparents;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.bj.hmxxparents.widget.LoadingDialog;

import cn.pedant.SweetAlert.TipsAlertDialog;
import cn.pedant.SweetAlert.UpdateAPPAlertDialog;

/**
 * Created by Administrator on 2016/11/14.
 */
public class BaseFragment extends Fragment {
    private LoadingDialog loadingDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingDialog = new LoadingDialog(getActivity());
    }

    public void showTipsDialog(String content) {
        TipsAlertDialog dialog = new TipsAlertDialog(getActivity());
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
                                      UpdateAPPAlertDialog.OnSweetClickListener cancelListener,
                                      UpdateAPPAlertDialog.OnSweetClickListener cancelDownloadListener) {
        UpdateAPPAlertDialog dialog = new UpdateAPPAlertDialog(getActivity());
        dialog.setTitleText(title);
        dialog.setContentText(content);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setConfirmClickListener(confirmListener);
        dialog.setCancelClickListener(cancelListener);
        dialog.setCancelDownloadListener(cancelDownloadListener);


        dialog.show();

//        if(qiangzhigengxin.equals("0")){
//            dialog.showCloseButton(true);
//        }else {
//            dialog.showCloseButton(false);
//        }

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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            onVisible();
        } else {
            onInVisible();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            onVisible();
        } else {
            onInVisible();
        }
    }

    protected boolean isFirstInit = true;

    /**
     * 当界面可见时的操作
     */
    protected void onVisible() {
        if (isFirstInit) {
            isFirstInit = false;
            lazyLoad();
        }
    }

    /**
     * 数据懒加载
     */
    protected void lazyLoad() {

    }

    /**
     * 当界面不可见时的操作
     */
    protected void onInVisible() {

    }
}

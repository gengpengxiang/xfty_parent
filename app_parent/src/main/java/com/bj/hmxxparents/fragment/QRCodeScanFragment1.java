package com.bj.hmxxparents.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bj.hmxxparents.R;
import com.bj.hmxxparents.activity.QRCodeScanActivity;
import com.bj.hmxxparents.utils.LL;
import com.bj.hmxxparents.utils.StringUtils;
import com.bj.hmxxparents.utils.T;
import com.umeng.analytics.MobclickAgent;

import cn.bingoogolapple.qrcode.core.QRCodeView;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.VIBRATOR_SERVICE;

/**
 * Created by zz379 on 2017/4/17.
 * 二维码识别页面，使用的是zbar库
 * 因为直接使用zbar库，在部分OPPO手机上会出现识别结果不正确的现象
 * 而直接使用zxing库，有的二维码会识别不出来
 * 一般zbar识别率已经很高，当出现识别错的情况时，切换到zxing库，基本上可以保证正确识别
 */

public class QRCodeScanFragment1 extends Fragment implements QRCodeView.Delegate {

    private QRCodeView mQRCodeView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_qrcode_scan_1, container, false);

        mQRCodeView = (QRCodeView) view.findViewById(R.id.zbarview);
        mQRCodeView.setDelegate(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mQRCodeView.startCamera();
        // mQRCodeView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
        mQRCodeView.showScanRect();
        mQRCodeView.startSpot();
    }

    @Override
    public void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    public void vibrate() {
        Vibrator vibrator = (Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.e("扫描成功",result);
        if (result.startsWith("http://qr12.cn/") || StringUtils.checkQRCode(result)) {
            // 识别成功
            vibrate();

            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("result", result);
            resultIntent.putExtras(bundle);
            getActivity().setResult(RESULT_OK, resultIntent);
            getActivity().finish();
        } else {
            // 换库重新扫描
            LL.i("换库重新扫描");
            T.showShort(getActivity(), "请保持扫码姿势");
            // 切换到zxing库
            ((QRCodeScanActivity) getActivity()).switchLib();
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        LL.e("打开相机出错");
    }

    /**
     * 将本地图片文件转换成可解码二维码的 Bitmap。为了避免图片太大，这里对图片进行了压缩。感谢 https://github.com/devilsen 提的 PR
     *
     * @param picturePath 本地图片文件路径
     * @return
     */
    private static Bitmap getDecodeAbleBitmap(String picturePath) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(picturePath, options);
            int sampleSize = options.outHeight / 400;
            if (sampleSize <= 0) {
                sampleSize = 1;
            }
            options.inSampleSize = sampleSize;
            options.inJustDecodeBounds = false;

            return BitmapFactory.decodeFile(picturePath, options);
        } catch (Exception e) {
            return null;
        }
    }

    public static void rgba2Yuv420(byte[] src, byte[] dst, int width, int height) {
        // Y
        for (int y = 0; y < height; y++) {
            int dstOffset = y * width;
            int srcOffset = y * width * 4;
            for (int x = 0; x < width && dstOffset < dst.length && srcOffset < src.length; x++) {
                dst[dstOffset] = src[srcOffset];
                dstOffset += 1;
                srcOffset += 4;
            }
        }
        /* Cb and Cr */
        for (int y = 0; y < height / 2; y++) {
            int dstUOffset = y * width + width * height;
            int srcUOffset = y * width * 8 + 1;

            int dstVOffset = y * width + width * height + 1;
            int srcVOffset = y * width * 8 + 2;
            for (int x = 0; x < width / 2 && dstUOffset < dst.length && srcUOffset < src.length && dstVOffset < dst.length && srcVOffset < src.length; x++) {
                dst[dstUOffset] = src[srcUOffset];
                dst[dstVOffset] = src[srcVOffset];

                dstUOffset += 2;
                dstVOffset += 2;

                srcUOffset += 8;
                srcVOffset += 8;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("scan1");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("scan1");
    }
}

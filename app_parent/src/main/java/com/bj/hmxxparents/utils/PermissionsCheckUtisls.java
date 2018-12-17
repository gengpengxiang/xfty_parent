package com.bj.hmxxparents.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by zz379 on 2017/1/27.
 */

public class PermissionsCheckUtisls {

    private static Context mContext;

    public PermissionsCheckUtisls(Context context) {
        mContext = context.getApplicationContext();
    }

    // 判断权限集合
    public static boolean lacksPermissions(String... permissions) {
        for (String permission : permissions) {
            if (lacksPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    // 判断是否缺少权限
    public static boolean lacksPermission(String permission) {
        return ContextCompat.checkSelfPermission(mContext, permission) ==
                PackageManager.PERMISSION_DENIED;
    }

//    public static Context mContext;
//
//    public PermissionsCheckUtisls(Context context) {
//        this.mContext = context.getApplicationContext();
//    }
//
//    // 判断权限集合
//    public static boolean lacksPermissions(String... permissions) {
//        for (String permission : permissions) {
//            if (lacksPermission(permission)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    // 判断是否缺少权限
//    public static boolean lacksPermission(String permission) {
//        return ContextCompat.checkSelfPermission(mContext, permission) ==
//                PackageManager.PERMISSION_DENIED;
//    }
}

package com.bj.hmxxparents;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.multidex.MultiDex;

import com.bj.hmxxparents.countryside.topic.view.BGAGlideImageLoader3;
import com.bj.hmxxparents.manager.IMHelper;
import com.bj.hmxxparents.manager.UMPushManager;
import com.bj.hmxxparents.utils.LL;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.lzy.okgo.OkGo;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import com.umeng.message.PushAgent;
import com.zhy.autolayout.config.AutoLayoutConifg;


import java.io.File;
import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import cn.bingoogolapple.photopicker.imageloader.BGAImage;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.AutoSizeConfig;

/**
 * Created by Administrator on 2016/11/25.
 */
public class MyApplication extends Application {

    public static MyApplication instances;

    public static MyApplication getInstances() {
        return instances;
    }

    private boolean isSingleTaskActivityLaunched = false;
    

    @Override
    public void onCreate() {
        super.onCreate();
        instances = this;
        initFresco();
        initImageLoader();
        // 默认使用的高度是设备的可用高度，也就是不包括状态栏和底部的操作栏的，如果你希望拿设备的物理高度进行百分比化：
        AutoLayoutConifg.getInstance().useDeviceSize();
        // 初始化友盟的相关操作
        initUMPush();
        // 初始化科大讯飞
        initSpeech();
        // 初始化EaseUI
        IMHelper.getInstance().init(this);

        BGAImage.setImageLoader(new BGAGlideImageLoader3());
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());

        initOkGo();
        AutoSizeConfig.getInstance().setCustomFragment(true);
        AutoSize.initCompatMultiProcess(this);

        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                //异常处理
            }
        });

        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

    }

    private void initOkGo() {
        OkGo.getInstance().init(this);
    }

    private void initSpeech() {
        // 将“12345678”替换成您申请的APPID，申请地址：http://www.xfyun.cn
        // 请勿在“=”与appid之间添加任何空字符或者转义符
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=59255106");
    }

    private void initUMPush() {
        // 初始化友盟推送相关信息
        LL.i("制造商：" + Build.MANUFACTURER);
        if (!Build.MANUFACTURER.equals("Xiaomi")) {
            LL.i("初始化友盟推送");
        } else {
            // MIUI系统
            LL.i("初始化小米推送");
        }

        UMPushManager manager = UMPushManager.getInstance();
        PushAgent pushAgent = PushAgent.getInstance(this);
        manager.initUmeng(pushAgent);
    }

    /**
     * 初始化FaceBook的Fresco框架
     */
    private void initFresco() {
        Fresco.initialize(this);//初始化框架
        // Facebook Fresco 初始化
        //Fresco.initialize(this, FrescoImagePipelineConfigFactory.getOkHttpImagePipelineConfig(this));
    }

    private void initImageLoader() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(new ColorDrawable(Color.parseColor("#f0f0f0")))
//                .showImageOnLoading(R.drawable.noimg)
                .showImageOnFail(R.drawable.course_no_img)
                .showImageForEmptyUri(R.drawable.course_no_img)
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        int memClass = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE))
                .getMemoryClass();
        int memCacheSize = 1024 * 1024 * memClass / 8;

        File cacheDir = new File(Environment.getExternalStorageDirectory().getPath() + "/jiecao/cache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .memoryCache(new UsingFreqLimitedMemoryCache(memCacheSize))
                .memoryCacheSize(memCacheSize)
                .diskCacheSize(50 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .imageDownloader(new BaseImageDownloader(this, 5 * 1000, 30 * 1000))
                .defaultDisplayImageOptions(options)
                .build();
        ImageLoader.getInstance().init(config);
    }

    public boolean isSingleTaskActivityLaunched() {
        return isSingleTaskActivityLaunched;
    }

    public void setSingleTaskActivityLaunched(boolean singleTaskActivityLaunched) {
        isSingleTaskActivityLaunched = singleTaskActivityLaunched;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 退出app
     */
    private List<SoftReference<Activity>> activityList = Collections.synchronizedList(new LinkedList<SoftReference<Activity>>());

    /**
     * 关闭指定Activity
     *
     * @param mactivity
     */
    public void destoryActivty(Activity mactivity) {
        for (int i = 0; i < activityList.size(); i++) {
            Activity activity = activityList.get(i).get();
            if (activity == null || mactivity.equals(activity)) {
                activityList.remove(i);
                i--;
            }
        }
    }

    private void destoryActivty(String activityName) {
        for (int i = 0; i < activityList.size(); i++) {
            Activity activity = activityList.get(i).get();
            if (activity != null && activityName.equals(activity.getClass().getSimpleName())) {
                activity.finish();
                activityList.remove(i);
                i--;
            } else if (activity == null) {
                activityList.remove(i);
                i--;
            }
        }
    }

    /**
     * 关闭指定名称的activity
     *
     * @param names
     */
    public void closeActivity(String... names) {
        for (int i = 0; i < names.length; i++) {
            destoryActivty(names[i]);
        }
    }

    /**
     * 添加Activity到集合中
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        SoftReference<Activity> reference = new SoftReference<Activity>(activity);
        activityList.add(reference);
    }

    /**
     * 退出所有activity
     */
    public void exitAll() {
        for (int i = 0; i < activityList.size(); i++) {
            Activity activity = activityList.get(i).get();
            if (activity != null) {
                activity.finish();
            }
            activityList.remove(i);
            i--;
        }
    }

    /**
     * 退出目标Activity外的所有activity
     *
     * @param mActivity
     */
    public void exitAll(Activity mActivity) {
        for (int i = 0; i < activityList.size(); i++) {
            Activity activity = activityList.get(i).get();
            if (activity != null && !mActivity.equals(activity)) {
                activity.finish();
                activityList.remove(i);
                i--;
            } else if (activity == null) {
                activityList.remove(i);
                i--;
            }
        }
    }

}

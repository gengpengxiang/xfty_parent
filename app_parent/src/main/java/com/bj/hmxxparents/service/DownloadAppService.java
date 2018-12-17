package com.bj.hmxxparents.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.bj.hmxxparents.BuildConfig;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.api.MLConfig;
import com.bj.hmxxparents.utils.LL;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.request.RequestCall;

import java.io.File;

import okhttp3.Call;

import static com.bj.hmxxparents.api.HttpUtilService.DOWNLOAD_PATH;

/**
 * Created by zz379 on 2017/3/15.
 * 版本更新，下载APK安装包的service
 */

public class DownloadAppService extends IntentService {

    private String downloadURL;
    public static final String FILENAME = "douhaojiazhang.apk";
    private Notification.Builder builder;
    private NotificationManager manager;
    private int mNotificationId = 0;
    private int downloadProgress = 0;
    private RequestCall call;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public DownloadAppService() {
        super("DownloadAppService");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("Service onBind()......");
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        System.out.println("Service onHandleIntent()......");
        downloadAPP();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("Service onCreate()......");
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        builder = new Notification.Builder(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("Service onStartCommand()......");
        downloadURL = intent.getExtras().getString(MLConfig.KEY_BUNDLE_DOWNLOAD_URL);
        System.out.println("DownloadURL: " + downloadURL);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        System.out.println("Service onDestroy()......");
        super.onDestroy();
    }

    private int downloadIndex = 0;

    private void downloadAPP() {
        File oldFile = new File(DOWNLOAD_PATH, FILENAME);
        if (oldFile.exists()) {
            LL.i("DownloadFile", "存在旧的安装包，已经删除");
            oldFile.delete();
        }
        call = OkHttpUtils.get().url(downloadURL).tag(FILENAME).build();
        // 开始下载
        call.execute(new FileCallBack(DOWNLOAD_PATH, FILENAME) {
            @Override
            public void inProgress(float progress, long total) {
                int currProgress = (int) (100 * progress);
                //防止界面卡死
                if (downloadProgress + 1 < currProgress) {
                    downloadIndex++;
                    sendMyBroadCast("downloading", currProgress);
                    if (downloadIndex >= 5) {
                        downloadIndex = 0;
                        LL.i("DownloadFile", "HttpUtilService -- DownloadFile() -- 下载中：" +
                                currProgress + "%");
                        builder.setTicker("开始下载...")
                                .setContentTitle("幸福田园")
                                //.setSmallIcon(R.drawable.ic_launcher)
                                .setSmallIcon(R.drawable.ic_notification)
                                .setProgress(100, currProgress, false)
                                .setContentText("下载" + currProgress + "%")
                                .setOngoing(true)
                                .setAutoCancel(false);
                        // Displays the progress bar for the first time.
                        manager.notify(mNotificationId, builder.build());
                    }
                    downloadProgress = currProgress;
                } else if (currProgress == 0) {
                    builder.setTicker("开始下载...")
                            .setContentTitle("幸福田园")
                            //.setSmallIcon(R.drawable.ic_launcher)
                            .setSmallIcon(R.drawable.ic_notification)
                            .setProgress(100, currProgress, false)
                            .setContentText("下载" + currProgress + "%")
                            .setOngoing(true)
                            .setAutoCancel(false);
                    // Displays the progress bar for the first time.
                    manager.notify(mNotificationId, builder.build());
                    downloadProgress = currProgress;
                    sendMyBroadCast("start", currProgress);
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                LL.i("Download", "HttpUtilService -- DownloadFile() -- 下载失败");
                File file = new File(DOWNLOAD_PATH, FILENAME);
                file.delete();
                sendMyBroadCast("error", 0);
                builder.setContentText("安装包下载失败").setProgress(0, 0, false);
                send(builder.build());
                new Handler().postDelayed(() -> {
                    manager.cancelAll();
                }, 3000);
            }

            @Override
            public void onResponse(File response) {
                // 点击安装的Intent
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri contentUri = FileProvider.getUriForFile(DownloadAppService.this, BuildConfig.APPLICATION_ID + ".fileProvider", response);
                    intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                } else {
                    intent.setDataAndType(Uri.fromFile(response),
                            "application/vnd.android.package-archive");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                PendingIntent pendingIntent = PendingIntent.getActivity(DownloadAppService.this, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent)
                        .setContentText("安装包下载完成")
                        .setProgress(0, 0, false);
                send(builder.build());
                sendMyBroadCast("finish", 100);
                LL.i("Download", "HttpUtilService -- DownloadFile() -- 下载完成：" + response.getPath());
                Toast.makeText(DownloadAppService.this, "安装包下载完成", Toast.LENGTH_SHORT).show();
                // 开始安装
                DownloadAppService.this.startActivity(intent);
            }
        });
    }

    /**
     * 发送下载进度广播
     *
     * @param status
     * @param progress
     */
    private void sendMyBroadCast(String status, int progress) {
        Intent intent = new Intent("android.intent.action.MY_BROADCAST");
        intent.putExtra("Status", status);
        intent.putExtra("Progress", progress);
        sendBroadcast(intent);
    }

    private void sendNotification() {
        sample("开始下载...", getString(R.string.app_name), "", R.drawable.ic_notification);
        builder.setProgress(100, downloadProgress, false);
        send(builder.build());
    }

    private void send(Notification build) {
        manager.notify(mNotificationId, build);
    }

    public void sample(String ticker, String title, String content, int smallIcon) {
        builder.setTicker(ticker);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSmallIcon(smallIcon);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
        builder.setAutoCancel(false);
        builder.setOngoing(true);
    }
}

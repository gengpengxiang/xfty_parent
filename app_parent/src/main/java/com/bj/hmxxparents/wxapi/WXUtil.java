package com.bj.hmxxparents.wxapi;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.bj.hmxxparents.R;
import com.bj.hmxxparents.api.MLProperties;
import com.bj.hmxxparents.utils.StringUtils;
import com.douhao.game.Constants;
import com.douhao.game.utils.Util;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/4/23 0023.
 */

public class WXUtil {

    static IWXAPI api;

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 把网络资源图片转化成bitmap
     *
     * @param url 网络资源图片
     * @return Bitmap
     */
    public static Bitmap GetLocalOrNetBitmap(String url) {
        Bitmap bitmap = null;
        InputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(new URL(url).openStream(), 1024);
            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            out = new BufferedOutputStream(dataStream, 1024);
            copy(in, out);
            out.flush();
            byte[] data = dataStream.toByteArray();
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            data = null;
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void copy(InputStream in, OutputStream out)
            throws IOException {
        byte[] b = new byte[1024];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }

    /***
     * 检查是否安装了微信
     * @param context
     * @return
     */
    public static boolean isWeixinAvilible(Context context) {
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

    public static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    /**
     * 分享到微信
     * 1.朋友圈 0.微信
     */
    public static void share(final Context context, final int type, String url, String title, String content, final String img) {

        api = WXAPIFactory.createWXAPI(context, Constants.APP_DOUHAO_PARENT_ID, false);

        if (!isWeixinAvilible(context)) {
            Toast.makeText(context, "抱歉！您还没有安装微信", Toast.LENGTH_SHORT).show();
            return;
        }
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        final WXMediaMessage msg = new WXMediaMessage(webpage);

        msg.title = "幸福田园";

        if (content == null || StringUtils.isEmpty(content)) {
            msg.description = "顺天致性、仁智和美、多元幸福";
        } else {
            msg.description = content;
        }

        Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> e) throws Exception {
                if (StringUtils.isEmpty(img)) {
                    Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_small);
                    e.onNext(thumb);
                    e.onComplete();
                } else {
//                    Bitmap thumb =Bitmap.createScaledBitmap(GetLocalOrNetBitmap(img), 120, 120, true);//压缩Bitmap
//                    e.onNext(thumb);
//                    e.onComplete();
                    Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_small);
                    e.onNext(thumb);
                    e.onComplete();
                }


            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        msg.thumbData = WXUtil.bmpToByteArray(bitmap, true);
                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                        req.transaction = buildTransaction("webpage");
                        req.message = msg;
                        req.scene = type;
                        api.sendReq(req);
                    }
                });

    }

    public static void invite(final Context context, final int type, final String url) {

        api = WXAPIFactory.createWXAPI(context, Constants.APP_DOUHAO_PARENT_ID, false);

        //MobclickAgent.onEvent(context, "article_share");
        if (!isWeixinAvilible(context)) {
            Toast.makeText(context, "抱歉！您还没有安装微信", Toast.LENGTH_SHORT).show();
            return;
        }
       /* Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_round);
        msg.thumbData = WXUtil.bmpToByteArray(bitmap, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = type;
        api.sendReq(req);*/

        Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> e) throws Exception {

                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);

                e.onNext(bitmap);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        WXWebpageObject webpage = new WXWebpageObject();
                        webpage.webpageUrl = url;
                        final WXMediaMessage msg = new WXMediaMessage(webpage);
                        msg.title = "来下载逗号老师吧，这里有趣又有料";
                        msg.description = "我发现逗号老师APP有很多有用的资源和有趣的内容，你也下载一个吧";
                        msg.thumbData = WXUtil.bmpToByteArray(bitmap, true);
                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                        req.transaction = buildTransaction("webpage");
                        req.message = msg;
                        req.scene = type;
                        api.sendReq(req);
                    }
                });
    }

    public static void shareImgToSession(final Context context, Bitmap shareBmp,final String type) {
        api = WXAPIFactory.createWXAPI(context, Constants.APP_DOUHAO_PARENT_ID, false);
        if (!isWeixinAvilible(context)) {
            Toast.makeText(context, "抱歉！您还没有安装微信", Toast.LENGTH_SHORT).show();
            return;
        }

        WXImageObject imgObject = new WXImageObject(shareBmp);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObject;
        // 设置缩略图
        if (shareBmp != null && !shareBmp.isRecycled()) {
            Bitmap thumbBmp = Bitmap.createScaledBitmap(shareBmp, shareBmp.getWidth() / 10,
                    shareBmp.getHeight() / 10, true);
            // msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
            msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
            shareBmp.recycle();
        }
        // 构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        // transaction 字段用于唯一标示一个请求
        req.transaction = buildTransaction("img");
        req.message = msg;


        if(type.equals("weixin")){
            req.scene = SendMessageToWX.Req.WXSceneSession;
        }else if(type.equals("pengyouquan")){
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
        }



        // 调用api接口发送数据到微信
        api.sendReq(req);
    }

}

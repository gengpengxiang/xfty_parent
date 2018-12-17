package com.bj.hmxxparents.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bj.hmxxparents.BaseActivity;
import com.bj.hmxxparents.R;
import com.douhao.game.Constants;
import com.douhao.game.utils.Util;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ShareActivity extends BaseActivity {

    @BindView(R.id.img_touxiang2)
    SimpleDraweeView imgTouxiang2;
    @BindView(R.id.tv_title2)
    TextView tvTitle2;
    @BindView(R.id.sv_pet2)
    SimpleDraweeView svPet2;
    @BindView(R.id.tv_name2)
    TextView tvName2;
    @BindView(R.id.iv_shareSession)
    ImageView ivShareSession;
    @BindView(R.id.iv_shareTimeline)
    ImageView ivShareTimeline;
    @BindView(R.id.layout_parent)
    RelativeLayout layoutParent;
    private Unbinder unbinder;
    private String user_touxiang, user_name, pet_pic, pet_name;

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        unbinder = ButterKnife.bind(this);

        Intent intent = getIntent();
        user_touxiang = intent.getStringExtra("user_touxiang");
        user_name = intent.getStringExtra("user_name");
        pet_pic = intent.getStringExtra("pet_pic");
        pet_name = intent.getStringExtra("pet_name");
        tvTitle2.setText(user_name + "获得了新装扮");
        tvName2.setText(pet_name);
        imgTouxiang2.setImageURI(pet_name);
        svPet2.setImageURI(pet_pic);

        api = WXAPIFactory.createWXAPI(this, Constants.APP_DOUHAO_PARENT_ID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_shareSession, R.id.iv_shareTimeline})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_shareSession:
                shareToSession(getBitmap());
                break;
            case R.id.iv_shareTimeline:
                shareToTimeline(getBitmap());
                break;
        }
    }

    private Bitmap getBitmap() {
        layoutParent.setDrawingCacheEnabled(true);
        layoutParent.buildDrawingCache();  //启用DrawingCache并创建位图
        Bitmap bitmap = Bitmap.createBitmap(layoutParent.getDrawingCache()); //创建一个DrawingCache的拷贝，因为DrawingCache得到的位图在禁用后会被回收
        layoutParent.setDrawingCacheEnabled(false);  //禁用DrawingCahce否则会影响性能
        return bitmap;
    }

    /**
     * 分享到微信
     */
    public void shareToSession(Bitmap shareBmp) {
        if (!isWeixinAvilible(this)) {
            Toast.makeText(this, "抱歉！您还没有安装微信", Toast.LENGTH_SHORT).show();
            return;
        }
//        if (popShare.isShowing() && shareBmp != null) {
//            popShare.dismiss();

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
            req.scene = SendMessageToWX.Req.WXSceneSession;

            // 调用api接口发送数据到微信
            api.sendReq(req);
       // }
    }

    /**
     * 分享到朋友圈
     */
    public void shareToTimeline(Bitmap shareBmp) {
        if (!isWeixinAvilible(this)) {
            Toast.makeText(this, "抱歉！您还没有安装微信", Toast.LENGTH_SHORT).show();
            return;
        }
//        if (popShare.isShowing() && shareBmp != null) {
//            popShare.dismiss();

            WXImageObject imgObject = new WXImageObject(shareBmp);
            WXMediaMessage msg = new WXMediaMessage();
            msg.mediaObject = imgObject;
            // 设置缩略图
            if (shareBmp != null && !shareBmp.isRecycled()) {
                Bitmap thumbBmp = Bitmap.createScaledBitmap(shareBmp, 150,
                        shareBmp.getHeight() * 150 / shareBmp.getWidth(), true);
                msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
                shareBmp.recycle();
            }
            // 构造一个Req
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            // transaction 字段用于唯一标示一个请求
            req.transaction = buildTransaction("img");
            req.message = msg;
            req.scene = SendMessageToWX.Req.WXSceneTimeline;

            // 调用api接口发送数据到微信
            api.sendReq(req);
        //}
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
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
}

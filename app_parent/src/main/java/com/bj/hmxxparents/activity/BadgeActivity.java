package com.bj.hmxxparents.activity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bj.hmxxparents.BaseActivity;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.api.MLProperties;
import com.bj.hmxxparents.utils.PermissionsCheckUtisls;
import com.bj.hmxxparents.utils.PreferencesUtils;
import com.bumptech.glide.Glide;
import com.douhao.game.Constants;
import com.douhao.game.utils.ScreenUtils;
import com.douhao.game.utils.Util;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BadgeActivity extends BaseActivity {

    @BindView(R.id.img_kidPhoto)
    SimpleDraweeView imgKidPhoto;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.layout_root)
    LinearLayout layoutRoot;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_shuoming)
    TextView tvShuoming;
    //    @BindView(R.id.iv_badge)
//    ImageView ivBadge;
    @BindView(R.id.tv_huodeNum)
    TextView tvHuodeNum;
    @BindView(R.id.sv_badge)
    SimpleDraweeView svBadge;
    @BindView(R.id.layout_parent)
    LinearLayout layoutParent;
    @BindView(R.id.img_touxiang2)
    SimpleDraweeView imgTouxiang2;
    @BindView(R.id.tv_title2)
    TextView tvTitle2;
    @BindView(R.id.sv_badge2)
    SimpleDraweeView svBadge2;
    @BindView(R.id.tv_name2)
    TextView tvName2;
    @BindView(R.id.layout_share2)
    RelativeLayout layoutShare2;
    @BindView(R.id.iv_badge2)
    ImageView ivBadge2;
    private PopupWindow popShare;
    private IWXAPI api;
    private String userPhotoPath;
    private String userName;
    private String name, shuoming, xueke, xuekeimg;
    private int huodeNum;
    private int resID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badge);
        ButterKnife.bind(this);

        // 设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }


        api = WXAPIFactory.createWXAPI(this, Constants.APP_DOUHAO_PARENT_ID);
        initPopViewShare();

        initViews();
    }


    private void initViews() {
        userPhotoPath = PreferencesUtils.getString(this, MLProperties.BUNDLE_KEY_KID_IMG);
        userName = PreferencesUtils.getString(this, MLProperties.BUNDLE_KEY_KID_NAME);
        imgKidPhoto.setImageURI(userPhotoPath);
        tvTitle.setText(userName + "恭喜你获得新徽章");

        name = getIntent().getStringExtra("name");
        shuoming = getIntent().getStringExtra("shuoming");
        xueke = getIntent().getStringExtra("xueke");
        huodeNum = getIntent().getIntExtra("huodeNum", 0);
        xuekeimg = getIntent().getStringExtra("xueke_img");

        tvName.setText(name);
        tvShuoming.setText(shuoming);
        tvHuodeNum.setText(huodeNum + "");

        String picName = "ic_badge_" + xueke;
        ApplicationInfo appInfo = getApplicationInfo();
        //得到该图片的id(name 是该图片的名字，"mipmap" 是该图片存放的目录，appInfo.packageName是应用程序的包)
        resID = getResources().getIdentifier(picName, "mipmap", appInfo.packageName);

        svBadge.setImageURI(xuekeimg);

        imgTouxiang2.setImageURI(userPhotoPath);
        tvTitle2.setText(userName + "恭喜你获得新徽章");
        tvName2.setText(name);
        svBadge2.setImageURI(xuekeimg);

        Glide.with(this).load(xuekeimg).into(ivBadge2);

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onBackPressed() {
        if (popShare != null && popShare.isShowing()) {
            popShare.dismiss();
            return;
        }
        super.onBackPressed();
    }

    private void showPopViewShareReport() {
        if (popShare != null && !popShare.isShowing()) {
            setBackgroundAlpha(this, 0.5f);
            popShare.showAtLocation(layoutRoot, Gravity.BOTTOM, 0, popShare.getHeight());
        }
    }

    private void initPopViewShare() {
        View popView = LayoutInflater.from(this).inflate(R.layout.pop_share_report, null);
        popShare = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, false);
        popShare.setAnimationStyle(R.style.MyPopupWindow_anim_style);
        popShare.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popShare.setFocusable(false);
        popShare.setOutsideTouchable(true);
        popShare.setOnDismissListener(() -> {
            setBackgroundAlpha(this, 1f);
            new Handler().postDelayed(() -> {
                //llHeaderLeft.setEnabled(true);
                //llHeaderRight.setEnabled(true);
            }, 100);
        });
        ImageView ivShareSession = (ImageView) popView.findViewById(R.id.iv_shareSession);
        ivShareSession.setOnClickListener(view -> {

            //shareToSession(getShareBitmap());

            shareToSession(getBitmap());

        });
        ImageView ivShareTimeline = (ImageView) popView.findViewById(R.id.iv_shareTimeline);
        ivShareTimeline.setOnClickListener(view -> {

            shareToTimeline(getBitmap());
        });
    }

    //add
    private Bitmap getBitmap() {
        layoutParent.setDrawingCacheEnabled(true);
        layoutParent.buildDrawingCache();  //启用DrawingCache并创建位图
        Bitmap bitmap = Bitmap.createBitmap(layoutParent.getDrawingCache()); //创建一个DrawingCache的拷贝，因为DrawingCache得到的位图在禁用后会被回收
        layoutParent.setDrawingCacheEnabled(false);  //禁用DrawingCahce否则会影响性能
        return bitmap;
    }


    private Bitmap getShareBitmap() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_share_badge, null);
        SimpleDraweeView sv = (SimpleDraweeView) view.findViewById(R.id.img_touxiang);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        //ImageView iv = (ImageView) view.findViewById(R.id.iv_badge);
        SimpleDraweeView sv2 = (SimpleDraweeView) view.findViewById(R.id.sv_badge);
        TextView tvName = (TextView) view.findViewById(R.id.tv_name);
        TextView tvJieshao = (TextView) view.findViewById(R.id.tv_jieshao);

        sv.setImageURI(userPhotoPath);
        tvTitle.setText(userName + "获得了");

        tvName.setText(name);
        tvJieshao.setText(shuoming);
//        iv.setBackgroundResource(resID);

        sv2.setImageURI(xuekeimg);
        //add


        DisplayMetrics metric = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;   // 屏幕高度（像素）
        layoutView(view, width, height);//去到指定view大小的函数
        return getBitmapByView(view);
    }

    private Bitmap getBitmapByView(View view) {
        view.setBackgroundColor(Color.parseColor("#FFFFFF"));
        // 创建对应大小的bitmap
        int width = ScreenUtils.getScreenWidth(this);

        Bitmap bitmap = Bitmap.createBitmap(width, view.getHeight(), Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    private void layoutView(View v, int width, int height) {
        // 指定整个View的大小 参数是左上角 和右下角的坐标
        v.layout(0, 0, width, height);
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST);
        /** 当然，measure完后，并不会实际改变View的尺寸，需要调用View.layout方法去进行布局。
         * 按示例调用layout函数后，View的大小将会变成你想要设置成的大小。
         */
        v.measure(measuredWidth, measuredHeight);
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
    }

    public void setBackgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        if (bgAlpha == 1) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        activity.getWindow().setAttributes(lp);
    }

    private void hidePopViewShareReport() {
        if (popShare != null && popShare.isShowing()) {
            popShare.dismiss();
        }
    }

    /**
     * 分享到微信
     */
    public void shareToSession(Bitmap shareBmp) {
        if (!isWeixinAvilible(this)) {
            Toast.makeText(this, "抱歉！您还没有安装微信", Toast.LENGTH_SHORT).show();
            return;
        }
        if (popShare.isShowing() && shareBmp != null) {
            popShare.dismiss();

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
        }
    }

    /**
     * 分享到朋友圈
     */
    public void shareToTimeline(Bitmap shareBmp) {
        if (!isWeixinAvilible(this)) {
            Toast.makeText(this, "抱歉！您还没有安装微信", Toast.LENGTH_SHORT).show();
            return;
        }
        if (popShare.isShowing() && shareBmp != null) {
            popShare.dismiss();

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
        }
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

    @OnClick({R.id.layout_close, R.id.layout_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_close:
                finish();
                break;
            case R.id.layout_share:
                showPopViewShareReport();
                break;
        }
    }
}

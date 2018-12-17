package com.bj.hmxxparents.pet.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bj.hmxxparents.BaseActivity;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.activity.ShareActivity;
import com.bj.hmxxparents.api.MLProperties;
import com.bj.hmxxparents.entity.MsgEvent;
import com.bj.hmxxparents.pet.adapter.PetAdapter;
import com.bj.hmxxparents.pet.model.PetInfo;
import com.bj.hmxxparents.pet.presenter.PetPresenter;
import com.bj.hmxxparents.utils.PreferencesUtils;
import com.bj.hmxxparents.utils.ScreenUtils;
import com.bj.hmxxparents.widget.CustomPopDialog;
import com.bj.hmxxparents.widget.SpacesItemDecoration;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.douhao.game.Constants;
import com.douhao.game.utils.Util;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.bj.hmxxparents.api.HttpUtilService.BASE_RESOURCE_URL;

public class PetALLActivity extends BaseActivity implements IViewPet {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.layout_root)
    RelativeLayout layoutRoot;
    @BindView(R.id.sv_pet)
    SimpleDraweeView svPet;
    @BindView(R.id.layout_share)
    RelativeLayout layoutShare;
    @BindView(R.id.sv_touxiang2)
    SimpleDraweeView svTouxiang2;
    @BindView(R.id.tv_title2)
    TextView tvTitle2;
    @BindView(R.id.sv_pet2)
    SimpleDraweeView svPet2;
    @BindView(R.id.tv_name2)
    TextView tvName2;
    private Unbinder unbinder;
    private PetAdapter adapter;
    private List<PetInfo.DataBean> dataList = new ArrayList<>();
    private PetPresenter petPresenter;
    private String kidId;
    private IWXAPI api;
    private PopupWindow popShare;
    private String userPhotoPath;
    private String userName;
    private String petName;
    private String shareImg;
    private String petid, peturl;

    public int pos;
    private View shareView;
    private TextView tvName;
    private SimpleDraweeView sv;
    private TextView tvTitle;
    private SimpleDraweeView sv2;
    private ImageView ivPet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScreenUtils.setFullScreen(this);
        setContentView(R.layout.activity_pet_all);
        unbinder = ButterKnife.bind(this);
        kidId = PreferencesUtils.getString(this, MLProperties.BUNDLE_KEY_KID_ID);
        petPresenter = new PetPresenter(this, this);

        Glide.with(this).load(R.mipmap.bg_pet_all)
                .into(new ViewTarget<View, GlideDrawable>(layoutRoot) {
                    //括号里为需要加载的控件
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable> glideAnimation) {
                        this.view.setBackground(resource.getCurrent());
                    }
                });
        initViews();
        petPresenter.getAllPets(kidId);

        userPhotoPath = PreferencesUtils.getString(this, MLProperties.BUNDLE_KEY_KID_IMG);
        userName = PreferencesUtils.getString(this, MLProperties.BUNDLE_KEY_KID_NAME);
        shareView = LayoutInflater.from(this).inflate(R.layout.layout_share_pet, null);
        sv = (SimpleDraweeView) shareView.findViewById(R.id.img_touxiang);
        tvTitle = (TextView) shareView.findViewById(R.id.tv_title);
        sv2 = (SimpleDraweeView) shareView.findViewById(R.id.sv_pet);

        ivPet = (ImageView) shareView.findViewById(R.id.iv_pet);




        api = WXAPIFactory.createWXAPI(this, Constants.APP_DOUHAO_PARENT_ID);
        initPopViewShare();
    }

    private void initViews() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(5));
        adapter = new PetAdapter(R.layout.recycler_item_pet, dataList);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter0, View view, int position) {

                if (dataList.get(position).getJiesuo_status() == 1) {
                    adapter.setSelection(position);
                    adapter.notifyDataSetChanged();
                    pos = position;

                    petid = dataList.get(position).getId();
                    peturl = BASE_RESOURCE_URL + dataList.get(position).getImg_s();

                    showGif(svPet, BASE_RESOURCE_URL + dataList.get(position).getImg());

                } else {
                    String shuoming = dataList.get(position).getShuoming();
                    showLockDialog(shuoming);
                }

            }
        });
    }

    private void showGif(SimpleDraweeView sv, String url) {
        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setUri(url)
                .setOldController(sv.getController())
                .setAutoPlayAnimations(true) //自动播放gif动画
                .build();
        sv.setController(controller);
    }

    private void showLockDialog(String shuoming) {
        CustomPopDialog.Builder dialogBuild = new CustomPopDialog.Builder(PetALLActivity.this);
        final CustomPopDialog dialog = dialogBuild.create(R.layout.dialog_pet_no, 0.7);
        dialog.setCanceledOnTouchOutside(true);
        TextView tvShuoming = (TextView) dialog.findViewById(R.id.tv_shuoming);
        tvShuoming.setText(shuoming);
        dialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing())
                    dialog.dismiss();
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }

    private void showConfirmDialog() {

        CustomPopDialog.Builder dialogBuild = new CustomPopDialog.Builder(PetALLActivity.this);
        final CustomPopDialog dialog = dialogBuild.create(R.layout.dialog_pet_confirm, 0.7);
        dialog.setCanceledOnTouchOutside(true);

        dialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing())
                    dialog.dismiss();
                finish();
            }
        });
        dialog.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing())
                    dialog.dismiss();

                petPresenter.replacePet(kidId, petid);
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }

    public void quit() {

        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).getStatus() == 1) {
                if (pos == i) {
                    finish();
                } else {
                    showConfirmDialog();
                }
            }
        }

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
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        petPresenter.onDestroy();
    }

    @Override
    public void getAllPets(PetInfo petInfo) {
        dataList.addAll(petInfo.getData());

        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).getStatus() == 1) {
                adapter.setSelection(i);

                pos = i;

                peturl = BASE_RESOURCE_URL + dataList.get(pos).getImg_s();
            }
        }

        Log.e("图片pos", pos + "");
        showGif(svPet, BASE_RESOURCE_URL + dataList.get(pos).getImg());
        //initPopViewShare();

        svTouxiang2.setImageURI(userPhotoPath);
        tvTitle2.setText(userName + "获得了新装扮");
        tvTitle2.setTextColor(Color.BLACK);
        tvName2.setText(dataList.get(pos).getName());
        tvName2.setTextColor(getResources().getColor(R.color.colorPrimary));
        svPet2.setImageURI(peturl);


        adapter.notifyDataSetChanged();
    }

    @Override
    public void replacePet() {
        EventBus.getDefault().post(new MsgEvent("ReplacePet"));
        finish();
    }

    @OnClick({R.id.layout_close, R.id.bt_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_close:
//                showConfirmDialog();
                quit();
                break;
            case R.id.bt_share:

                //showPopViewShareReport();
                Intent intent = new Intent(PetALLActivity.this, ShareActivity.class);
                intent.putExtra("user_touxiang",userPhotoPath);
                intent.putExtra("user_name",userName);
                intent.putExtra("pet_pic",peturl);
                intent.putExtra("pet_name",dataList.get(pos).getName());
                startActivity(intent);

//                showShareDialog();
                break;
        }
    }

    private void showShareDialog() {
        CustomPopDialog.Builder dialogBuild = new CustomPopDialog.Builder(PetALLActivity.this);
        final CustomPopDialog dialog = dialogBuild.create(R.layout.layout_share_pet,1.0,1.0);
        dialog.setCanceledOnTouchOutside(false);

        TextView tvTitle = (TextView) dialog.findViewById(R.id.tv_title2);
        TextView tvName = (TextView) dialog.findViewById(R.id.tv_name2);


        tvTitle.setText(userName + "获得了新装扮");
        tvName.setText(dataList.get(pos).getName());

        SimpleDraweeView svTouxiang = (SimpleDraweeView)dialog.findViewById(R.id.img_touxiang2);
        SimpleDraweeView svPet = (SimpleDraweeView)dialog.findViewById(R.id.sv_pet2);

        svTouxiang.setImageURI(userPhotoPath);
        svPet.setImageURI(peturl);

        ImageView iv1 = (ImageView)dialog.findViewById(R.id.iv_shareSession);
        ImageView iv2 = (ImageView)dialog.findViewById(R.id.iv_shareTimeline);

        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing())
                    dialog.dismiss();
                shareToSession(getBitmap());
            }
        });
        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing())
                    dialog.dismiss();
                shareToTimeline(getBitmap());
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        if (popShare != null && popShare.isShowing()) {
            popShare.dismiss();
            return;
        }
        super.onBackPressed();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        quit();
        return super.onKeyDown(keyCode, event);

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

            shareToSession(getBitmap());
//            shareToSession(getShareBitmap());
        });
        ImageView ivShareTimeline = (ImageView) popView.findViewById(R.id.iv_shareTimeline);
        ivShareTimeline.setOnClickListener(view -> {

            shareToTimeline(getBitmap());
        });
    }

    private Bitmap getBitmap() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_share_pet, null);
        //打开图像缓存
        view.setDrawingCacheEnabled(true);
        //测量大小
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        //发送尺寸到view和子view
        view.layout(0, 0, view.getMeasuredWidth(),view.getMeasuredHeight());
        //获取可视组件的截图
        Bitmap bitmap = view.getDrawingCache();


        Log.e("name2=",userName);
//        layoutShare.setDrawingCacheEnabled(true);
//        layoutShare.buildDrawingCache();  //启用DrawingCache并创建位图
//        Bitmap bitmap = Bitmap.createBitmap(layoutShare.getDrawingCache()); //创建一个DrawingCache的拷贝，因为DrawingCache得到的位图在禁用后会被回收
//        layoutShare.setDrawingCacheEnabled(false);  //禁用DrawingCahce否则会影响性能

        return bitmap;
    }


    private Bitmap getShareBitmap() {
        tvName = (TextView) shareView.findViewById(R.id.tv_name);
        sv.setImageURI(userPhotoPath);
        tvTitle.setText(userName + "获得了新装扮");
        tvName.setText(dataList.get(pos).getName());
        sv2.setImageURI(peturl);

        DisplayMetrics metric = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;   // 屏幕高度（像素）
        layoutView(shareView, width, height);//去到指定view大小的函数
        return getBitmapByView(shareView);
    }

    private Bitmap getBitmapByView(View view) {
        view.setBackgroundColor(Color.parseColor("#FFFFFF"));
        // 创建对应大小的bitmap
        int width = com.douhao.game.utils.ScreenUtils.getScreenWidth(this);

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
//            if (shareBmp != null && !shareBmp.isRecycled()) {
                Bitmap thumbBmp = Bitmap.createScaledBitmap(shareBmp, shareBmp.getWidth() / 10,
                        shareBmp.getHeight() / 10, true);
                // msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
                msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
                shareBmp.recycle();
//            }
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

}

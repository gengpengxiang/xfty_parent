package com.bj.hmxxparents.countryside.honorroll;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bj.hmxxparents.BaseFragment;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.api.MLProperties;
import com.bj.hmxxparents.countryside.honorroll.adapter.HonorAdapter;
import com.bj.hmxxparents.countryside.honorroll.adapter.HonorChildAdapter;
import com.bj.hmxxparents.countryside.honorroll.adapter.SpaceItemDecoration;
import com.bj.hmxxparents.countryside.honorroll.model.Honor;
import com.bj.hmxxparents.countryside.honorroll.presenter.HonorPresenter;
import com.bj.hmxxparents.countryside.honorroll.view.IViewHonor;
import com.bj.hmxxparents.utils.PreferencesUtils;
import com.bj.hmxxparents.widget.AutoScaleTextView;
import com.bj.hmxxparents.widget.SpacesItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.douhao.game.Constants;
import com.douhao.game.utils.Util;
import com.facebook.drawee.view.SimpleDraweeView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.bj.hmxxparents.api.HttpUtilService.BASE_RESOURCE_URL;


/**
 * Created by zz379 on 2017/3/29.
 * 全校活力榜页面
 */

public class HonorFragment extends BaseFragment implements IViewHonor {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mSmartRefreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    private Unbinder unbinder;

    private HonorPresenter presenter;
    private List<Honor.DataBean> honorList = new ArrayList<>();
    private HonorAdapter honorAdapter;

    private int currentPage = 0;
    private String class_code;

    private RelativeLayout layoutShare;
    private AlertDialog shareDialog;
    private IWXAPI api;
    private ScrollView scrollView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_countryside_honor, container, false);
        unbinder = ButterKnife.bind(this, view);

        class_code = PreferencesUtils.getString(getActivity(), MLProperties.BUNDLE_KEY_CLASS_CODE);

        presenter = new HonorPresenter(getActivity(),this);

        initViews();
        presenter.getHonorList(class_code,currentPage);
        return view;
    }

    private void initViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //mRecyclerView.addItemDecoration(new SpaceItemDecoration(20));

        honorAdapter = new HonorAdapter(R.layout.recycler_item_honor_roll,honorList);
        mRecyclerView.setAdapter(honorAdapter);
        honorAdapter.setEmptyView(R.layout.recycler_item_tianyuan_empty,mRecyclerView);
        honorAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                showShareDialog(position);
            }
        });

        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                currentPage = 0;
                presenter.getHonorList(class_code,currentPage);
            }
        });
        mSmartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                currentPage++;
                presenter.getHonorList(class_code,currentPage);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void showShareDialog(int postion) {

        List<Honor.DataBean.StudentPmBean> bean = honorList.get(postion).getStudent_pm();

        api = WXAPIFactory.createWXAPI(getActivity(), Constants.APP_DOUHAO_PARENT_ID);

        View popViewShare = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_honor_share, null);

        layoutShare = (RelativeLayout) popViewShare.findViewById(R.id.layout_parent);

        scrollView = (ScrollView)popViewShare.findViewById(R.id.scrollView);

        ImageView ivClose = (ImageView) popViewShare.findViewById(R.id.iv_close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shareDialog.isShowing() && shareDialog != null) {
                    shareDialog.dismiss();
                }
            }
        });

        SimpleDraweeView svBadge = (SimpleDraweeView) popViewShare.findViewById(R.id.sv_badge);
        svBadge.setImageURI(BASE_RESOURCE_URL+honorList.get(postion).getFenlei_img());

        TextView tvDate = (TextView) popViewShare.findViewById(R.id.tv_date);
        tvDate.setText(honorList.get(postion).getTime());

        TextView tvClassName = (TextView) popViewShare.findViewById(R.id.tv_classname);
        tvClassName.setText(honorList.get(postion).getClass_name());


        AutoScaleTextView tvScore1 = (AutoScaleTextView) popViewShare.findViewById(R.id.tv_score1);
        AutoScaleTextView tvScore2 = (AutoScaleTextView) popViewShare.findViewById(R.id.tv_score2);
        AutoScaleTextView tvScore3 = (AutoScaleTextView) popViewShare.findViewById(R.id.tv_score3);
        SimpleDraweeView svMedal1 = (SimpleDraweeView) popViewShare.findViewById(R.id.sv_medal1);
        SimpleDraweeView svMedal2 = (SimpleDraweeView) popViewShare.findViewById(R.id.sv_medal2);
        SimpleDraweeView svMedal3 = (SimpleDraweeView) popViewShare.findViewById(R.id.sv_medal3);
        TextView tvStudentName1 = (TextView) popViewShare.findViewById(R.id.tv_studentname1);
        TextView tvStudentName2 = (TextView) popViewShare.findViewById(R.id.tv_studentname2);
        TextView tvStudentName3 = (TextView) popViewShare.findViewById(R.id.tv_studentname3);

//        List<Honor.DataBean.StudentPmBean> bean = honorList.get(postion).getStudent_pm();
        //银牌
        tvScore1.setText(bean.get(1).getHuizhangnum());
        tvStudentName1.setText(bean.get(1).getStudent_name());
        svMedal1.setImageURI(BASE_RESOURCE_URL+bean.get(1).getImg());
        //Glide.with(getActivity()).load(BASE_RESOURCE_URL+bean.get(1).getImg()).into(svMedal1);
        //金牌
        tvScore2.setText(bean.get(0).getHuizhangnum());
        tvStudentName2.setText(bean.get(0).getStudent_name());
        svMedal2.setImageURI(BASE_RESOURCE_URL+bean.get(0).getImg());
        //Glide.with(getActivity()).load(BASE_RESOURCE_URL+bean.get(0).getImg()).into(svMedal2);
        //铜牌
        tvScore3.setText(bean.get(2).getHuizhangnum());
        tvStudentName3.setText(bean.get(2).getStudent_name());
        svMedal3.setImageURI(BASE_RESOURCE_URL+bean.get(2).getImg());
        //Glide.with(getActivity()).load(BASE_RESOURCE_URL+bean.get(2).getImg()).into(svMedal3);

        RecyclerView recyclerView = (RecyclerView) popViewShare.findViewById(R.id.mRecyclerView);
//        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),5));
        recyclerView.addItemDecoration(new SpacesItemDecoration(10));

        //4-8名判断
        if(bean.size()>3){
            recyclerView.setVisibility(View.VISIBLE);

            List<Honor.DataBean.StudentPmBean> childList = new ArrayList<>();

            for(int i=3;i<bean.size();i++){
                childList.add(bean.get(i));
            }
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),childList.size()));

            HonorChildAdapter childdapter = new HonorChildAdapter(R.layout.recycler_item_honor_student, childList);
            recyclerView.setAdapter(childdapter);
        }else {
            recyclerView.setVisibility(View.GONE);
        }

        ImageView ivWechat = (ImageView) popViewShare.findViewById(R.id.iv_shareSession);
        ImageView ivFriend = (ImageView) popViewShare.findViewById(R.id.iv_shareTimeline);

        ivWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToSession(getBitmapByView(scrollView));
            }
        });

        ivFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToTimeline(getBitmapByView(scrollView));
            }
        });

        shareDialog = new AlertDialog.Builder(getActivity()).create();
        shareDialog.setCanceledOnTouchOutside(false);
        shareDialog.show();
        shareDialog.getWindow().setContentView(popViewShare);

    }
    @Override
    public void getHonorList(String result) {

        if(mSmartRefreshLayout.isRefreshing()){
            mSmartRefreshLayout.finishRefresh();
        }
        if(mSmartRefreshLayout.isLoading()){
            mSmartRefreshLayout.finishLoadmore();
        }

        Honor honor = JSON.parseObject(result, new TypeReference<Honor>() {
        });
        if(honor.getData()!=null) {
            if (honor.getRet().equals("1")) {
                if (currentPage == 0) {
                    honorList.clear();
                }
                honorList.addAll(honor.getData());
                honorAdapter.notifyDataSetChanged();
            }
        }
    }

    //add
    private Bitmap getBitmap() {
        layoutShare.setDrawingCacheEnabled(true);
        layoutShare.buildDrawingCache();  //启用DrawingCache并创建位图
        Bitmap bitmap = Bitmap.createBitmap(layoutShare.getDrawingCache()); //创建一个DrawingCache的拷贝，因为DrawingCache得到的位图在禁用后会被回收
        layoutShare.setDrawingCacheEnabled(false);  //禁用DrawingCahce否则会影响性能
        return bitmap;
    }

    /**
     * 分享到微信
     */
    public void shareToSession(Bitmap shareBmp) {
        if (!isWeixinAvilible(getActivity())) {
            Toast.makeText(getActivity(), "抱歉！您还没有安装微信", Toast.LENGTH_SHORT).show();
            return;
        }
        if (shareDialog.isShowing() && shareBmp != null) {
            shareDialog.dismiss();

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
        if (!isWeixinAvilible(getActivity())) {
            Toast.makeText(getActivity(), "抱歉！您还没有安装微信", Toast.LENGTH_SHORT).show();
            return;
        }
        if (shareDialog.isShowing() && shareBmp != null) {
            shareDialog.dismiss();

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

    /**
     * 截取scrollview的屏幕
     * @param scrollView
     * @return
     */
    public static Bitmap getBitmapByView(ScrollView scrollView) {



        int h = 0;
        Bitmap bitmap = null;
        // 获取scrollview实际高度
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
            scrollView.getChildAt(i).setBackgroundResource(R.mipmap.bg_honor_share);
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
                Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }
}

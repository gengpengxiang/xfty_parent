package com.bj.hmxxparents.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bj.hmxxparents.BaseActivity;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.api.LmsDataService;
import com.bj.hmxxparents.api.MLConfig;
import com.bj.hmxxparents.api.MLProperties;
import com.bj.hmxxparents.entity.StudentInfos;
import com.bj.hmxxparents.fragment.CountrySideFragment;
import com.bj.hmxxparents.fragment.DoukeFragment2;
import com.bj.hmxxparents.fragment.HomeFragment;
import com.bj.hmxxparents.fragment.UserFragment;
import com.bj.hmxxparents.manager.UMPushManager;
import com.bj.hmxxparents.utils.DensityUtils;
import com.bj.hmxxparents.utils.IMMLeaks;
import com.bj.hmxxparents.utils.LL;
import com.bj.hmxxparents.utils.LeakedUtils;
import com.bj.hmxxparents.utils.PreferencesUtils;
import com.bj.hmxxparents.utils.ScreenUtils;
import com.bj.hmxxparents.utils.StringUtils;
import com.bj.hmxxparents.widget.CustomViewPager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.bj.hmxxparents.api.HttpUtilService.BASE_API_URL;

/**
 * 首页
 */
public class MainActivity extends BaseActivity {

    private static final int TAB_NUMBER = 4;

    @BindView(R.id.mViewPager)
    CustomViewPager mViewPager;
    @BindView(R.id.iv_notification)
    View ivNotification;
    @BindView(R.id.ll_bottomBar)
    LinearLayout llBottomBar;
    @BindViews({R.id.iv_tab1, R.id.iv_tab2, R.id.iv_tab3, R.id.iv_tab4})
    ImageView[] ivTabs;
    @BindViews({R.id.tv_tab1, R.id.tv_tab2, R.id.tv_tab3, R.id.tv_tab4})
    TextView[] tvTabs;
    int[] resTabImageSelect = {R.drawable.ic_tab_home_parent, R.mipmap.ic_tab_tianyuan_green, R.drawable.ic_tab_read_selected, R.drawable.ic_tab_person_selected};
    int[] resTabImageUnSelect = {R.drawable.ic_tab_home_unselected, R.mipmap.ic_tab_tianyuan_gray, R.drawable.ic_tab_read, R.drawable.ic_tab_person};
    @BindView(R.id.ll_tab1)
    LinearLayout llTab1;
    @BindView(R.id.ll_tab2)
    LinearLayout llTab2;
    @BindView(R.id.ll_tab3)
    LinearLayout llTab3;
    @BindView(R.id.ll_tab4)
    LinearLayout llTab4;
    private Fragment[] mTabFragments;
    private long exitTime = 0;

    private static MainActivity instance = null;
    private LocalBroadcastManager broadcastManager;
    private BroadcastReceiver broadcastReceiver;

    private int currentPageIndex = 0;
    private PopupWindow popupWindow;
    private String studentcode;
    private String tianyuanShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        instance = this;
        // 恢复数据
        if (savedInstanceState != null && 0 != savedInstanceState.getInt("CurrPageIndex")) {
            currentPageIndex = savedInstanceState.getInt("CurrPageIndex");
        }

        studentcode = PreferencesUtils.getString(MainActivity.this, MLProperties.BUNDLE_KEY_KID_ID);

        tianyuanShow = PreferencesUtils.getString(MainActivity.this, MLProperties.BUNDLE_KEY_TIANYUAN_SHOW,"0");


        //为了测试改为0，正式应改为1
//        if(tianyuanShow.equals("1")){
//            llTab2.setVisibility(View.VISIBLE);
//        }else {
//            llTab2.setVisibility(View.GONE);
//        }

        initView();
        initDonation();
        initData();

        int width = ScreenUtils.getScreenWidth(this);
        int height = ScreenUtils.getScreenHeight(this);
        Log.e("宽度==", width + "高度=" + height);
    }

    private void initView() {
        currentPageIndex = getIntent().getIntExtra(MLProperties.BUNDLE_KEY_MAIN_PAGEINDEX, currentPageIndex);
        mTabFragments = new Fragment[TAB_NUMBER];
        mTabFragments[0] = new HomeFragment();
        mTabFragments[1] = new CountrySideFragment();
        mTabFragments[2] = new DoukeFragment2();
        mTabFragments[3] = new UserFragment();

        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mTabFragments[position];
            }

            @Override
            public int getCount() {
                return TAB_NUMBER;
            }
        });

        mViewPager.setCurrentItem(currentPageIndex);
        mViewPager.setOffscreenPageLimit(4);
    }

    private void initData() {
        // 添加标签
        String schoolID = PreferencesUtils.getString(this, MLProperties.BUNDLE_KEY_SCHOOL_CODE, "");
        LL.i("友盟标签 SchoolID: " + schoolID);
        if (!StringUtils.isEmpty(schoolID)) {
            UMPushManager.getInstance().addTag(schoolID);
        }

        actionTabItemSelect(currentPageIndex);
        // 获取捐助功能是否显示
        //getDonationStatus();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // PopupWindow必须在某个事件中显示或者是开启一个新线程去调用，不能直接在onCreate方法中显示一个Popupwindow
        // llBottomBar.post(() -> showDonationEntry());
    }

    @OnClick(R.id.ll_tab1)
    void clickTab1() {
        actionTabItemSelect(0);
    }

    @OnClick(R.id.ll_tab2)
    void clickTab2() {
        actionTabItemSelect(1);
    }

    @OnClick(R.id.ll_tab3)
    void clickTab3() {
        actionTabItemSelect(2);
        //ivNotification.setVisibility(View.GONE);
    }

    @OnClick(R.id.ll_tab4)
    void clickTab4() {
        actionTabItemSelect(3);
    }

    private void actionTabItemSelect(int position) {
        for (int i = 0; i < TAB_NUMBER; i++) {
            if (i == position) {
                ivTabs[i].setImageResource(resTabImageSelect[i]);
                tvTabs[i].setTextColor(ContextCompat.getColor(this, R.color.text_tab_selected));
            } else {
                ivTabs[i].setImageResource(resTabImageUnSelect[i]);
                tvTabs[i].setTextColor(ContextCompat.getColor(this, R.color.text_tab_unselected));
            }
        }
        if (currentPageIndex != position) {
            currentPageIndex = position;
            mViewPager.setCurrentItem(position);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) <= 2000) {
                // 退出APP
                finishSelf();
                // getActivity().exitApp();
            } else {
                Toast.makeText(this, getString(R.string.toast_home_exit_system), Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public static MainActivity getInstance() {
        return instance;
    }

    public static void finishSelf() {
        if (instance != null && !instance.isFinishing()) {
            instance.finish();
            instance = null;
        }
    }

    @Override
    protected void onDestroy() {
        LeakedUtils.fixTextLineCacheLeak();
        IMMLeaks.fixFocusedViewLeak(getApplication());

        if (instance != null) instance = null;
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        //getRequestPermission();
        //updateUnreadLabel();
        //EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //EMClient.getInstance().chatManager().removeMessageListener(messageListener);
    }

    private void getRequestPermission() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE
//                Manifest.permission.RECORD_AUDIO,
//                Manifest.permission.READ_PHONE_STATE
        )
                .subscribe(granted -> {
                    if (granted) {
                        // All requested permissions are granted
                    } else {
                        // At least one permission is denied
                    }
                });
    }

//    EMMessageListener messageListener = new EMMessageListener() {
//
//        @Override
//        public void onMessageReceived(List<EMMessage> messages) {
//            // notify new message
//            for (EMMessage message : messages) {
//                EaseUI.getInstance().getNotifier().onNewMsg(message);
//            }
//            refreshUIWithMessage();
//        }
//
//        @Override
//        public void onCmdMessageReceived(List<EMMessage> messages) {
//            //red packet code : 处理红包回执透传消息
//        }
//
//        @Override
//        public void onMessageRead(List<EMMessage> messages) {
//        }
//
//        @Override
//        public void onMessageDelivered(List<EMMessage> message) {
//        }
//
//        @Override
//        public void onMessageChanged(EMMessage message, Object change) {
//        }
//    };

//    private void refreshUIWithMessage() {
//        runOnUiThread(() -> {
//            // refresh unread count
//            updateUnreadLabel();
//            // refresh conversation list
//            if (mTabFragments[1] != null) {
//                ((ConversationListFragment) mTabFragments[1]).refresh();
//            }
//        });
//    }

    /**
     * update unread message count
     */
    private void updateUnreadLabel() {
        int count = getUnreadMsgCountTotal();
        if (currentPageIndex != 1 && count > 0) {
            ivNotification.setVisibility(View.VISIBLE);
        } else {
            ivNotification.setVisibility(View.GONE);
        }
    }

    /**
     * get unread message count
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        int chatroomUnreadMsgCount = 0;
        unreadMsgCountTotal = EMClient.getInstance().chatManager().getUnreadMessageCount();
        for (EMConversation conversation : EMClient.getInstance().chatManager().getAllConversations().values()) {
            if (conversation.getType() == EMConversation.EMConversationType.ChatRoom)
                chatroomUnreadMsgCount = chatroomUnreadMsgCount + conversation.getUnreadMsgCount();
        }
        return unreadMsgCountTotal - chatroomUnreadMsgCount;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("CurrPageIndex", currentPageIndex);
    }

    /**
     * 初始化捐助入口
     */
    private void initDonation() {
        View popView = LayoutInflater.from(this).inflate(R.layout.pop_donation_us_entry, null);
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, false);
        // 设置点击返回键使其消失，且不影响背景，此时setOutsideTouchable函数即使设置为false
        // 点击PopupWindow 外的屏幕，PopupWindow依然会消失；相反，如果不设置BackgroundDrawable
        // 则点击返回键PopupWindow不会消失，同时，即时setOutsideTouchable设置为true
        // 点击PopupWindow 外的屏幕，PopupWindow依然不会消失
        // popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(false);
        popView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DonationActivity.class);
            startActivity(intent);
        });
    }

    /**
     * 显示入口
     */
    private void showDonationEntry() {
        if (popupWindow != null && !popupWindow.isShowing()) {
            // popupWindow.showAtLocation(llBottomBar, Gravity.BOTTOM | Gravity.RIGHT, 0, DensityUtils.dp2px(this, 55));
            popupWindow.showAsDropDown(llBottomBar, llBottomBar.getWidth() - DensityUtils.dp2px(this, 64),
                    -DensityUtils.dp2px(this, 130));
        }
    }

    private void hideDonationEntry() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    /**
     * 是否显示捐助的入口
     */
    private void getDonationStatus() {
        Observable.create((ObservableOnSubscribe<String[]>) emitter -> {
            LmsDataService mService = new LmsDataService();
            String[] result = mService.getDonationStatusFromAPI(MLConfig.KEY_DONATION_SEARCH_TYPE_JIAZHANG);
            emitter.onNext(result);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String[]>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String[] strings) {
                        if (!StringUtils.isEmpty(strings[0]) && strings[0].equals("1")
                                && "open".equals(strings[2])) {
                            llBottomBar.post(() -> showDonationEntry());
                        } else {
                            hideDonationEntry();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}

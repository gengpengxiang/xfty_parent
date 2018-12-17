package com.bj.hmxxparents.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bj.hmxxparents.activity.ChatActivity;
import com.bj.hmxxparents.api.Constant;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.model.EaseNotifier;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;

import java.util.List;

/**
 * Created by zz379 on 2017/5/15.
 * 环信即时通讯Helper
 */

public class IMHelper {

    private static final String TAG = "IMHelper";

    private EaseUI easeUI;
    private static IMHelper instance = null;
    private Context appContext;
    /**
     * EMEventListener
     */
    protected EMMessageListener messageListener = null;

    private IMHelper() {

    }

    public synchronized static IMHelper getInstance() {
        if (instance == null) {
            instance = new IMHelper();
        }
        return instance;
    }

    public void init(Context context) {
        EMOptions options = initChatOptions();
        //use default options if options is null
        if (EaseUI.getInstance().init(context, options)) {
            appContext = context;
            //debug mode, you'd better set it to false, if you want release your App officially.
            EMClient.getInstance().setDebugMode(true);
            //get easeui instance
            easeUI = EaseUI.getInstance();
            //to set user's profile and avatar
            setEaseUIProviders();
            //initialize profile manager
            // getUserProfileManager().init(context);

            setGlobalListeners();
        }
    }

    private EMOptions initChatOptions() {
        Log.d(TAG, "init HuanXin Options");

        EMOptions options = new EMOptions();
        // 设置自动登录，默认为true
        options.setAutoLogin(true);
        // set if accept the invitation automatically
        options.setAcceptInvitationAlways(true);
        // 获取是否自动接受加群邀请
        options.setAutoAcceptGroupInvitation(true);
        // 是否按照server收到时间进行排序 默认是false
        options.setSortMessageByServerTime(false);
        //设置是否需要接受方已读确认 缺省 true 如果设为true，会要求消息的接受方发送已读回执
        options.setRequireAck(true);
        //设置是否需要接受方送达确认,默认false, 如果设为true，会要求消息的接受方发送送达回执。
        options.setRequireDeliveryAck(false);

        return options;
    }

    protected void setEaseUIProviders() {
        //set notification options, will use default if you don't set it
        easeUI.getNotifier().setNotificationInfoProvider(new EaseNotifier.EaseNotificationInfoProvider() {

            @Override
            public String getTitle(EMMessage message) {
                //you can update title here
                return null;
            }

            @Override
            public int getSmallIcon(EMMessage message) {
                //you can update icon here
                return 0;
            }

            @Override
            public String getDisplayedText(EMMessage message) {
                return "您收到了一条消息";
            }

            @Override
            public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
                return null;
            }

            @Override
            public Intent getLaunchIntent(EMMessage message) {
                // you can set what activity you want display when user click the notification
                Intent intent = new Intent(appContext, ChatActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                EMMessage.ChatType chatType = message.getChatType();
                if (chatType == EMMessage.ChatType.Chat) { // single chat message
                    intent.putExtra(Constant.EXTRA_USER_ID, message.getFrom());
                    intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_SINGLE);
                    try {
                        intent.putExtra(Constant.EXTRA_TO_USER_NICK, message.getStringAttribute(Constant.EXTRA_CURR_USER_NICK));
                        intent.putExtra(Constant.EXTRA_TO_USER_PHOTO, message.getStringAttribute(Constant.EXTRA_CURR_USER_PHOTO));
                    } catch (HyphenateException e) {
                        intent.putExtra(Constant.EXTRA_TO_USER_NICK, "");
                        intent.putExtra(Constant.EXTRA_TO_USER_PHOTO, "");
                    }
                } else { // group chat message
                    // message.getTo() is the group id
                    intent.putExtra("userId", message.getTo());
                    if (chatType == EMMessage.ChatType.GroupChat) {
                        intent.putExtra("chatType", Constant.CHATTYPE_GROUP);
                    } else {
                        intent.putExtra("chatType", Constant.CHATTYPE_CHATROOM);
                    }
                }
                return intent;
            }
        });
    }

    /**
     * set global listener
     */
    protected void setGlobalListeners() {
        //register message event listener
        registerMessageListener();
    }

    /**
     * Global listener
     * If this event already handled by an activity, you don't need handle it again
     * activityList.size() <= 0 means all activities already in background or not in Activity Stack
     */
    protected void registerMessageListener() {
        messageListener = new EMMessageListener() {
            private BroadcastReceiver broadCastReceiver = null;

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    EMLog.d(TAG, "onMessageReceived id : " + message.getMsgId());
                    // in background, do not refresh UI, notify it in notification bar
                    if (!easeUI.hasForegroundActivies()) {
                        getNotifier().onNewMsg(message);
                    }
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {

            }

            @Override
            public void onMessageRead(List<EMMessage> messages) {
            }

            @Override
            public void onMessageDelivered(List<EMMessage> message) {
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                EMLog.d(TAG, "change:");
                EMLog.d(TAG, "change:" + change);
            }
        };

        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    /**
     * get instance of EaseNotifier
     *
     * @return
     */
    public EaseNotifier getNotifier() {
        return easeUI.getNotifier();
    }

    /**
     * if ever logged in
     *
     * @return
     */
    public boolean isLoggedIn() {
        return EMClient.getInstance().isLoggedInBefore();
    }
}

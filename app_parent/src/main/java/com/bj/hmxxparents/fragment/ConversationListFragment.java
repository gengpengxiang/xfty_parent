package com.bj.hmxxparents.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bj.hmxxparents.R;
import com.bj.hmxxparents.activity.ChatActivity;
import com.bj.hmxxparents.api.Constant;
import com.bj.hmxxparents.api.LmsDataService;
import com.bj.hmxxparents.api.MLProperties;
import com.bj.hmxxparents.utils.PreferencesUtils;
import com.bj.hmxxparents.utils.T;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMConversation.EMConversationType;
import com.hyphenate.easeui.model.EaseConversation;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.util.NetUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 消息记录列表页面
 */
public class ConversationListFragment extends EaseConversationListFragment {

    private TextView errorText;
    private int pageIndex = 1;
    private String userPhone;
    public static long lastRefreshTime;

    @Override
    protected void initView() {
        super.initView();
        titleBar.getLeftLayout().setClickable(false);
        mXRefreshView.restoreLastRefreshTime(lastRefreshTime);
        userPhone = PreferencesUtils.getString(getActivity(), MLProperties.PREFER_KEY_USER_ID);
        View errorView = (LinearLayout) View.inflate(getActivity(), R.layout.em_chat_neterror_item, null);
        errorItemContainer.addView(errorView);
        errorText = (TextView) errorView.findViewById(R.id.tv_connect_errormsg);
        View emptyView = (LinearLayout) View.inflate(getActivity(), R.layout.em_chat_empty_item, null);
        emptyItemContainer.addView(emptyView);
    }

    @Override
    protected void setUpView() {
        super.setUpView();
        onConversationRefresh();
        conversationListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EaseConversation easeConversation = conversationListView.getItem(position - 1);
                EMConversation conversation = easeConversation.getEmConversation();
                String username = conversation.conversationId();
                if (username.equals(EMClient.getInstance().getCurrentUser()))
                    Toast.makeText(getActivity(), R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
                else {
                    // start chat acitivity
                    Intent intent = new Intent(getActivity(), ChatActivity.class);

                    if (conversation.isGroup()) {
                        if (conversation.getType() == EMConversationType.ChatRoom) {
                            // it's group chat
                            intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_CHATROOM);
                        } else {
                            intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
                        }

                    }
                    // it's single chat
                    intent.putExtra(Constant.EXTRA_USER_ID, username);
                    intent.putExtra(Constant.EXTRA_TO_USER_NICK, easeConversation.getUserNick());
                    intent.putExtra(Constant.EXTRA_TO_USER_PHOTO, easeConversation.getUserPhoto());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onConnectionDisconnected() {
        super.onConnectionDisconnected();
        if (NetUtils.hasNetwork(getActivity())) {
            errorText.setText(R.string.can_not_connect_chat_server_connection);
        } else {
            errorText.setText(R.string.the_current_network);
        }
    }

    @Override
    protected void onConversationRefresh() {
        super.onConversationRefresh();
        pageIndex = 1;
        getConversationDataFromAPI(pageIndex);
    }

    private void getConversationDataFromAPI(int pageIndex) {
        Observable.create((ObservableOnSubscribe<List<EaseConversation>>) e -> {
            LmsDataService mService = new LmsDataService();
            List<EaseConversation> dataList = mService.getConversationListFromAPI(userPhone, pageIndex);
            e.onNext(dataList);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<EaseConversation>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<EaseConversation> emConversations) {
                        loadData(emConversations);
                    }

                    @Override
                    public void onError(Throwable e) {
                        T.showShort(getActivity(), "服务器开小差了，请重试");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void loadData(List<EaseConversation> list) {
        lastRefreshTime = mXRefreshView.getLastRefreshTime();
        mXRefreshView.stopRefresh();
        conversationList.clear();
        conversationList.addAll(list);

        emptyItemContainer.setVisibility(View.GONE);
        if (conversationList.size() == 0) {
            emptyItemContainer.setVisibility(View.VISIBLE);
        }

        // conversationListView.refresh();
        conversationListView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            MobclickAgent.onPageStart("chatList");
        } else {
            MobclickAgent.onPageEnd("chatList");
        }
    }
    
    @Override
    public void onPause() {
        super.onPause();
    }
}

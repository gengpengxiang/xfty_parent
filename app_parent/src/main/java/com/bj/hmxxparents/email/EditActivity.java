package com.bj.hmxxparents.email;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bj.hmxxparents.BaseActivity;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.api.MLConfig;
import com.bj.hmxxparents.api.MLProperties;
import com.bj.hmxxparents.entity.BaseDataInfo;
import com.bj.hmxxparents.entity.MessageEvent;
import com.bj.hmxxparents.utils.Base64Util;
import com.bj.hmxxparents.utils.KeyBoardUtils;
import com.bj.hmxxparents.utils.PreferencesUtils;
import com.bj.hmxxparents.utils.ScreenUtils;
import com.bj.hmxxparents.utils.StringUtils;
import com.bj.hmxxparents.utils.T;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.bj.hmxxparents.api.HttpUtilService.BASE_API_URL;

public class EditActivity extends BaseActivity {


    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.layout_parent)
    LinearLayout layoutParent;
    @BindView(R.id.tv_header_left)
    TextView tvHeaderLeft;
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.tv_header_right)
    TextView tvHeaderRight;
    private Unbinder unbinder;
    private String userPhoneNumber;
    private PopupWindow mPopupWindow;

    private String xinjianid = "";
    private String title, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        // 如果存在虚拟按键，则设置虚拟按键的背景色
        if (ScreenUtils.checkDeviceHasNavigationBar(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setNavigationBarColor(ContextCompat.getColor(this, android.R.color.black));
            }
        }

        setContentView(R.layout.activity_edit);
        unbinder = ButterKnife.bind(this);
        userPhoneNumber = PreferencesUtils.getString(this, MLProperties.PREFER_KEY_USER_ID);
        xinjianid = getIntent().getStringExtra("xinjianid");
        Log.e("xinjianid==", xinjianid);
        initTitleBar();

        initEditText();
        initPopupWindow();
    }

    private void initPopupWindow() {
        View view = LayoutInflater.from(EditActivity.this).inflate(R.layout.layout_popup_email, null);
        mPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0));

        view.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                sendEmail(userPhoneNumber, "caogao");
            }
        });
        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                finish();
            }
        });
        view.findViewById(R.id.tv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                //finish();
            }
        });
    }

    private void initEditText() {
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");
        etTitle.setText(title);
        etContent.setText(content);

        etTitle.setSelection(title.length());//将光标移至文字末尾
        etTitle.requestFocus();
    }

    private void initTitleBar() {

        tvHeaderTitle.setText("校长信箱");
        tvHeaderLeft.setText("取消");
        tvHeaderRight.setText("发送");


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!StringUtils.isEmpty(etTitle.getText().toString()) && !StringUtils.isEmpty(etContent.getText().toString())) {

                String newTitle = etTitle.getText().toString().trim();
                String newContent = etContent.getText().toString().trim();

                if (!newTitle.equals(title) || !newContent.equals(content)) {
                    mPopupWindow.showAtLocation(layoutParent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                } else {
                    finish();
                }

            } else {
                finish();
            }
//            mPopupWindow.showAtLocation(layoutParent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
        return super.onKeyDown(keyCode, event);

    }


    @OnClick({R.id.tv_header_left, R.id.tv_header_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_header_left:

                KeyBoardUtils.closeKeybord(etContent, this);

                if (!StringUtils.isEmpty(etTitle.getText().toString()) && !StringUtils.isEmpty(etContent.getText().toString())) {

                    String newTitle = etTitle.getText().toString().trim();
                    String newContent = etContent.getText().toString().trim();

                    if (!newTitle.equals(title) || !newContent.equals(content)) {
                        mPopupWindow.showAtLocation(layoutParent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    } else {
                        finish();
                    }
                } else {
                    finish();
                }
                // mPopupWindow.showAtLocation(layoutParent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.tv_header_right:
                if (StringUtils.isEmpty(etTitle.getText().toString())) {
                    T.showShort(EditActivity.this, "标题不能为空");
                } else {
                    if (etContent.getText().length() < 10) {
                        T.showShort(EditActivity.this, "输入内容少于10个字");
                    } else {
                        sendEmail(userPhoneNumber, "fasong");
                    }
                }

                break;
        }
    }

    private void sendEmail(final String phone, final String type) {
        String title = etTitle.getText().toString();
        String content = etContent.getText().toString();

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {

                OkGo.<String>post(BASE_API_URL + "xinxiang/xinjianadd")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("userphone", phone)
                        .params("title", Base64Util.encode(title))
                        .params("content", Base64Util.encode(content))
                        .params("xueqi_code", "201809")
                        .params("type", type)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String str = response.body().toString();
                                Log.e("发送信件结果", str);
                                e.onNext(str);
                                e.onComplete();
                            }

                        });

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {

                        BaseDataInfo dataInfo = JSON.parseObject(s, new TypeReference<BaseDataInfo>() {
                        });
                        if (dataInfo.getRet().equals("1")) {
                            if (!xinjianid.equals("no")) {
                                deleteCaogao(xinjianid);
                            }
                            T.showShort(EditActivity.this, "发送成功");
                            EventBus.getDefault().post(new MessageEvent("writelettersuccess"));


                            finish();
                        }
                        if (dataInfo.getRet().equals("2")) {
                            if (!xinjianid.equals("no")) {
                                Log.e("删除草稿的id=", xinjianid);
                                deleteCaogao(xinjianid);
                            }
                            T.showShort(EditActivity.this, "草稿保存成功");
                            EventBus.getDefault().post(new MessageEvent("writelettersuccess"));


                            finish();
                        }
                    }
                });
    }

    private void deleteCaogao(final String id) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) throws Exception {
                OkGo.<String>post(BASE_API_URL + "xinxiang/xinjiandel")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("xinjianid", id)
                        .params("type", "jiazhang")
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String str = response.body().toString();
                                Log.e("删除结果", str);
                                e.onNext(str);
                                e.onComplete();
                            }

                            @Override
                            public void onFinish() {
                                e.onComplete();
                            }
                        });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        BaseDataInfo dataInfo = JSON.parseObject(s, new TypeReference<BaseDataInfo>() {
                        });
                    }
                });
    }

}

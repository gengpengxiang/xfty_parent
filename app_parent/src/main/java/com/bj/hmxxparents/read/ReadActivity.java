package com.bj.hmxxparents.read;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bj.hmxxparents.BaseActivity;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.api.MLProperties;
import com.bj.hmxxparents.read.model.ReadInfo;
import com.bj.hmxxparents.read.model.SignResult;
import com.bj.hmxxparents.read.presenter.ReadInfoPresenter;
import com.bj.hmxxparents.read.utils.GestureViewBinder;
import com.bj.hmxxparents.read.view.IViewRead;
import com.bj.hmxxparents.utils.PreferencesUtils;
import com.bj.hmxxparents.utils.T;
import com.bj.hmxxparents.widget.AutoScaleTextView;
import com.bj.hmxxparents.widget.CustomPopDialog;
import com.bj.hmxxparents.widget.CustomPopWindow;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.jessyan.autosize.internal.CustomAdapt;

import static com.bj.hmxxparents.api.HttpUtilService.BASE_RESOURCE_URL;

public class ReadActivity extends BaseActivity implements IViewRead, CustomAdapt {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.sv_photo)
    SimpleDraweeView svPhoto;
    @BindView(R.id.bt_sign)
    TextView btSign;

    @BindView(R.id.layout_parent)
    RelativeLayout layoutParent;
    @BindView(R.id.layout_view)
    RelativeLayout layoutView;

    @BindViews({R.id.iv_city_photo1, R.id.iv_city_photo2, R.id.iv_city_photo3, R.id.iv_city_photo4, R.id.iv_city_photo5, R.id.iv_city_photo6, R.id.iv_city_photo7, R.id.iv_city_photo8, R.id.iv_city_photo9, R.id.iv_city_photo10, R.id.iv_city_photo11})
    ImageView[] ivCitys;
    @BindViews({R.id.tv_city_name1, R.id.tv_city_name2, R.id.tv_city_name3, R.id.tv_city_name4, R.id.tv_city_name5, R.id.tv_city_name6, R.id.tv_city_name7, R.id.tv_city_name8, R.id.tv_city_name9, R.id.tv_city_name10, R.id.tv_city_name11})
    TextView[] tvCityNames;
    @BindViews({R.id.iv_city_lock1, R.id.iv_city_lock2, R.id.iv_city_lock3, R.id.iv_city_lock4, R.id.iv_city_lock5, R.id.iv_city_lock6, R.id.iv_city_lock7, R.id.iv_city_lock8, R.id.iv_city_lock9, R.id.iv_city_lock10, R.id.iv_city_lock11})
    ImageView[] ivCityLocks;

    @BindViews({R.id.iv_pet1, R.id.iv_pet2, R.id.iv_pet3, R.id.iv_pet4, R.id.iv_pet5, R.id.iv_pet6, R.id.iv_pet7, R.id.iv_pet8, R.id.iv_pet9, R.id.iv_pet10, R.id.iv_pet11, R.id.iv_pet12, R.id.iv_pet13, R.id.iv_pet14, R.id.iv_pet15, R.id.iv_pet16, R.id.iv_pet17, R.id.iv_pet18, R.id.iv_pet19, R.id.iv_pet20, R.id.iv_pet21, R.id.iv_pet22, R.id.iv_pet23, R.id.iv_pet24, R.id.iv_pet25, R.id.iv_pet26})
    ImageView[] ivPets;

    @BindView(R.id.tv_badge)
    AutoScaleTextView tvBadge;
    @BindView(R.id.progress_honey)
    ProgressBar progressHoney;
    @BindView(R.id.tv_honey)
    TextView tvHoney;
    @BindView(R.id.bt_signed)
    AutoScaleTextView btSigned;
    @BindView(R.id.layout_sign)
    RelativeLayout layoutSign;
    @BindView(R.id.tv_honey_num)
    AutoScaleTextView tvHoneyNum;
    @BindView(R.id.layout_signed)
    RelativeLayout layoutSigned;
    @BindView(R.id.layout_dialog_sign)
    LinearLayout layoutDialogSign;
    @BindView(R.id.blackView)
    View blackView;
    @BindView(R.id.bt_change)
    ImageView btChange;

    private Unbinder unbinder;
//    private PopupWindow mPopupWindow;

    private CustomPopWindow mPopupWindow;

    private int offsetX, offsetX2, viewWidth;

    private ReadInfoPresenter presenter;
    private String student_code;

    private List<ReadInfo.DataBean.CityBean> cityList = new ArrayList<>();

    private PopupWindow popAddBadge;
    private View popview;
    private ImageView ivPopAddBadge;
    private TextView tvPopBadgeNumber;
    private EditText etTitle, etNumber, etDuration;
    private CustomPopDialog dialog, dialog2;
    private TextView tvShuoming, tvShuoming2;
    private LinearLayout layoutCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏


        setContentView(R.layout.activity_read);
        unbinder = ButterKnife.bind(this);
        student_code = PreferencesUtils.getString(this, MLProperties.BUNDLE_KEY_KID_ID);
        presenter = new ReadInfoPresenter(this, this);
        initViews();
        presenter.getReadInfo(student_code, "1");

        if (PreferencesUtils.getInt(this, "firstread", 0) == 0) {
            dialog.show();
        }

        if(getIntent().getStringExtra("yuedudaka_map").equals("1")){
            btChange.setVisibility(View.INVISIBLE);
        }else {
            btChange.setVisibility(View.VISIBLE);
        }

    }

    private void initViews() {
        GestureViewBinder bind = GestureViewBinder.bind(this, layoutParent, layoutView);
        bind.setFullGroup(true);

        initPopuWindow();
        initShuomingDialog();
        initShuomingDialog2();
        initPopAddBadgeView();
    }

    private void initShuomingDialog() {
        CustomPopDialog.Builder dialogBuild = new CustomPopDialog.Builder(ReadActivity.this);
        dialog = dialogBuild.create(R.layout.dialog_read_shuoming, 0.7, 0.48);
        dialog.setCanceledOnTouchOutside(true);
        tvShuoming = (TextView) dialog.findViewById(R.id.tv_shuoming);
        tvShuoming.setMovementMethod(ScrollingMovementMethod.getInstance());


        layoutCheckbox = (LinearLayout) dialog.findViewById(R.id.layout_checkbox);


        CheckBox checkBox = (CheckBox) dialog.findViewById(R.id.checkbox);

        if (PreferencesUtils.getInt(this, "firstread", 0) == 1) {

            layoutCheckbox.setVisibility(View.GONE);

            checkBox.setChecked(true);
            PreferencesUtils.putInt(ReadActivity.this, "firstread", 1);
        } else {

            layoutCheckbox.setVisibility(View.VISIBLE);

            checkBox.setChecked(false);
            PreferencesUtils.putInt(ReadActivity.this, "firstread", 0);
        }


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    PreferencesUtils.putInt(ReadActivity.this, "firstread", 1);
                } else {
                    PreferencesUtils.putInt(ReadActivity.this, "firstread", 0);
                }

            }
        });


        dialog.findViewById(R.id.bt_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing())
                    dialog.dismiss();

                if (PreferencesUtils.getInt(ReadActivity.this, "firstread", 0) == 1) {
                    layoutCheckbox.setVisibility(View.GONE);
                    PreferencesUtils.putInt(ReadActivity.this, "firstread", 1);
                } else {
                    layoutCheckbox.setVisibility(View.VISIBLE);
                    PreferencesUtils.putInt(ReadActivity.this, "firstread", 0);
                }
            }
        });
        dialog.setCancelable(false);
        //dialog.show();
    }

    private void initShuomingDialog2() {
        CustomPopDialog.Builder dialogBuild = new CustomPopDialog.Builder(ReadActivity.this);
        dialog2 = dialogBuild.create(R.layout.dialog_read_shuoming2, 0.7, 0.48);
        dialog2.setCanceledOnTouchOutside(true);
        tvShuoming2 = (TextView) dialog2.findViewById(R.id.tv_shuoming);
        tvShuoming2.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvShuoming2.setText(getResources().getString(R.string.read_shuoming2));

//        layoutCheckbox = (LinearLayout) dialog.findViewById(R.id.layout_checkbox);


//        CheckBox checkBox = (CheckBox) dialog.findViewById(R.id.checkbox);
//
//        if (PreferencesUtils.getInt(this, "firstread", 0) == 1) {
//
//            layoutCheckbox.setVisibility(View.GONE);
//
//            checkBox.setChecked(true);
//            PreferencesUtils.putInt(ReadActivity.this, "firstread", 1);
//        }else {
//
//            layoutCheckbox.setVisibility(View.VISIBLE);
//
//            checkBox.setChecked(false);
//            PreferencesUtils.putInt(ReadActivity.this, "firstread", 0);
//        }


//        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    PreferencesUtils.putInt(ReadActivity.this, "firstread", 1);
//                }else {
//                    PreferencesUtils.putInt(ReadActivity.this, "firstread", 0);
//                }
//
//            }
//        });


        dialog2.findViewById(R.id.bt_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog2.isShowing())
                    dialog2.dismiss();

//                if (PreferencesUtils.getInt(ReadActivity.this, "firstread", 0) == 1) {
//                    layoutCheckbox.setVisibility(View.GONE);
//                    PreferencesUtils.putInt(ReadActivity.this, "firstread", 1);
//                }else {
//                    layoutCheckbox.setVisibility(View.VISIBLE);
//                    PreferencesUtils.putInt(ReadActivity.this, "firstread", 0);
//                }
            }
        });
        dialog2.setCancelable(false);
        //dialog.show();
    }

    private void initPopuWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_read_popuwindow, null);
        mPopupWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(view)
                .setFocusable(true)
                .setOutsideTouchable(true)
                .enableOutsideTouchableDissmiss(false)
                .create();


        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                etTitle.setText("");
                etNumber.setText("");
                etDuration.setText("");
            }
        });


        view.measure(makeDropDownMeasureSpec(mPopupWindow.getWidth()),
                makeDropDownMeasureSpec(mPopupWindow.getHeight()));


        layoutParent.measure(makeDropDownMeasureSpec(btSign.getWidth()),
                makeDropDownMeasureSpec(btSign.getHeight()));

        layoutParent.measure(makeDropDownMeasureSpec(btSigned.getWidth()),
                makeDropDownMeasureSpec(btSigned.getHeight()));

//        offsetX = Math.abs(mPopupWindow.getContentView().getMeasuredWidth() / 2);
        offsetX = Math.abs(mPopupWindow.getWidth() / 2);

        //已签到

        //未签到
        view.findViewById(R.id.tv_record).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReadActivity.this, SignRecordActivity.class);
                startActivity(intent);
            }
        });

        etTitle = (EditText) view.findViewById(R.id.et_title);
        etNumber = (EditText) view.findViewById(R.id.et_number);
        etDuration = (EditText) view.findViewById(R.id.et_duration);

        view.findViewById(R.id.bt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dissmiss();
            }
        });

        view.findViewById(R.id.bt_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(etTitle.getText()) && !TextUtils.isEmpty(etNumber.getText())
                        && !TextUtils.isEmpty(etDuration.getText())) {

                    String title = etTitle.getText().toString().trim();
                    String num = etNumber.getText().toString().trim();
                    String time = etDuration.getText().toString().trim();

                    Log.e("记录", time + num + time);
                    mPopupWindow.dissmiss();

                    presenter.sign(student_code, title, num, time);

                } else {
                    T.showShort(ReadActivity.this, "请补全签到信息");
                }
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // 当新设置中，屏幕布局模式为横排时

        Log.e("onConfigurationChanged", newConfig.orientation + "");
        if (newConfig.orientation == 1) {
            blackView.setVisibility(View.VISIBLE);
        }
        if (newConfig.orientation == 2) {
            blackView.setVisibility(View.GONE);
        }
        super.onConfigurationChanged(newConfig);
        Log.e("onConfigurationChanged", "onConfigurationChanged");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        viewWidth = btSign.getWidth();

    }


    @SuppressWarnings("ResourceType")
    private static int makeDropDownMeasureSpec(int measureSpec) {
        int mode;
        if (measureSpec == ViewGroup.LayoutParams.WRAP_CONTENT) {
            mode = View.MeasureSpec.UNSPECIFIED;
        } else {
            mode = View.MeasureSpec.EXACTLY;
        }
        return View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(measureSpec), mode);
    }

    @Override
    public void getReadInfo(String result) {

        try {
            ReadInfo readInfo = JSON.parseObject(result, new TypeReference<ReadInfo>() {
            });
            if (readInfo.getRet().equals("1")) {
                ReadInfo.DataBean bean = readInfo.getData();
                svPhoto.setImageURI(BASE_RESOURCE_URL + bean.getStudent_img());
                tvName.setText(bean.getStudent_name());

                tvBadge.setText(bean.getHuizhang());
                tvHoneyNum.setText(bean.getFengmi_num());

                tvShuoming.setText(bean.getGuize().replace("\\n", "\n"));

                double d = Double.valueOf(bean.getFengmi()) * 100;
                Log.e("d==", d + "");
                int progress = (new Double(d)).intValue();
                Log.e("d222==", progress + "");
                progressHoney.setMax(100);
                progressHoney.setProgress(progress);
                tvHoney.setText(progress + "%");

                if (bean.getQiandao_status().equals("1")) {
                    btSign.setVisibility(View.GONE);
                    layoutSigned.setVisibility(View.VISIBLE);
                    btSigned.setText(bean.getQiandao() + "t");
                } else {
                    btSign.setVisibility(View.VISIBLE);
                    layoutSigned.setVisibility(View.GONE);
                }

                int petPosition = Integer.valueOf(bean.getLujin_code()) - 1;
                Log.e("d333==", petPosition + "");
                for (int j = 0; j < 26; j++) {
                    if (j == petPosition) {
                        ivPets[j].setVisibility(View.VISIBLE);
                    } else {
                        ivPets[j].setVisibility(View.INVISIBLE);
                    }
                }

                cityList = bean.getCity();

                for (int i = 0; i < cityList.size(); i++) {
//                    tvCityNames[i].setText(cityList.get(i).getName());

                    if (cityList.get(i).getSuo().equals("1")) {
                        ivCityLocks[i].setVisibility(View.GONE);
                        tvCityNames[i].setBackgroundResource(R.mipmap.ic_read_city);
                    } else {
                        ivCityLocks[i].setVisibility(View.VISIBLE);
                        tvCityNames[i].setBackgroundResource(R.mipmap.ic_read_city2);
                    }

                }

                //弹出解锁下一章地图对话框
                if (readInfo.getData().getHuizhang().equals("11")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog2.show();
                        }
                    }, 2000);
                }

            }

        } catch (Exception e) {

        }
    }

    @Override
    public void getSignResult(String result) {
        SignResult signResult = JSON.parseObject(result, new TypeReference<SignResult>() {
        });
        if (signResult.getRet().equals("1")) {
            T.showShort(ReadActivity.this, "签到成功");

            if (signResult.getData().equals("1")) {
                showPopViewAddBadge("1", new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // 隐藏弹框
                        hidePopViewAddBadge();

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }

            presenter.getReadInfo(student_code, "1");

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2) {
            if (requestCode == 1) {
                Log.e("回来", "true");
            }
        }
    }

    @OnClick({R.id.bt_change,R.id.bt_sign, R.id.layout_signed, R.id.bt_shuoming, R.id.bt_close, R.id.iv_city_photo1, R.id.iv_city_photo2, R.id.iv_city_photo3, R.id.iv_city_photo4, R.id.iv_city_photo5, R.id.iv_city_photo6, R.id.iv_city_photo7, R.id.iv_city_photo8, R.id.iv_city_photo9, R.id.iv_city_photo10, R.id.iv_city_photo11})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_change:
                Intent intent0 = new Intent(ReadActivity.this,ReadActivity2.class);
                startActivity(intent0);
                finish();
                break;
            case R.id.bt_sign:
                mPopupWindow.showAsDropDown(btSign, -(offsetX - viewWidth / 2), 2);
                break;
            case R.id.layout_signed:
                Intent intent = new Intent(ReadActivity.this, SignRecordActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_shuoming:
                dialog.show();
                break;
            case R.id.bt_close:
                finish();
                break;
            case R.id.iv_city_photo1:
                if (cityList.get(0).getSuo().equals("1")) {
                    Intent intent1 = new Intent(ReadActivity.this, ReadDetailActivity.class);
                    intent1.putExtra("code", cityList.get(0).getCode());
                    startActivityForResult(intent1, 1);
                } else {
                    T.showShort(ReadActivity.this, "集满蜂蜜才会解锁哦！");
                }
                break;
            case R.id.iv_city_photo2:
                if (cityList.get(1).getSuo().equals("1")) {
                    Intent intent2 = new Intent(ReadActivity.this, ReadDetailActivity.class);
                    intent2.putExtra("code", cityList.get(1).getCode());
                    startActivityForResult(intent2, 1);
                } else {
                    T.showShort(ReadActivity.this, "集满蜂蜜才会解锁哦！");
                }
                break;
            case R.id.iv_city_photo3:
                if (cityList.get(2).getSuo().equals("1")) {
                    Intent intent3 = new Intent(ReadActivity.this, ReadDetailActivity.class);
                    intent3.putExtra("code", cityList.get(2).getCode());
                    startActivityForResult(intent3, 1);
                } else {
                    T.showShort(ReadActivity.this, "集满蜂蜜才会解锁哦！");
                }
                break;
            case R.id.iv_city_photo4:
                if (cityList.get(3).getSuo().equals("1")) {
                    Intent intent4 = new Intent(ReadActivity.this, ReadDetailActivity.class);
                    intent4.putExtra("code", cityList.get(3).getCode());
                    startActivityForResult(intent4, 1);
                } else {
                    T.showShort(ReadActivity.this, "集满蜂蜜才会解锁哦！");
                }
                break;
            case R.id.iv_city_photo5:
                if (cityList.get(4).getSuo().equals("1")) {
                    Intent intent5 = new Intent(ReadActivity.this, ReadDetailActivity.class);
                    intent5.putExtra("code", cityList.get(4).getCode());
                    startActivityForResult(intent5, 1);
                } else {
                    T.showShort(ReadActivity.this, "集满蜂蜜才会解锁哦！");
                }
                break;
            case R.id.iv_city_photo6:
                if (cityList.get(5).getSuo().equals("1")) {
                    Intent intent6 = new Intent(ReadActivity.this, ReadDetailActivity.class);
                    intent6.putExtra("code", cityList.get(5).getCode());
                    startActivityForResult(intent6, 1);
                } else {
                    T.showShort(ReadActivity.this, "集满蜂蜜才会解锁哦！");
                }
                break;
            case R.id.iv_city_photo7:
                if (cityList.get(6).getSuo().equals("1")) {
                    Intent intent7 = new Intent(ReadActivity.this, ReadDetailActivity.class);
                    intent7.putExtra("code", cityList.get(6).getCode());
                    startActivityForResult(intent7, 1);
                } else {
                    T.showShort(ReadActivity.this, "集满蜂蜜才会解锁哦！");
                }
                break;
            case R.id.iv_city_photo8:
                if (cityList.get(7).getSuo().equals("1")) {
                    Intent intent8 = new Intent(ReadActivity.this, ReadDetailActivity.class);
                    intent8.putExtra("code", cityList.get(7).getCode());
                    startActivityForResult(intent8, 1);
                } else {
                    T.showShort(ReadActivity.this, "集满蜂蜜才会解锁哦！");
                }
                break;
            case R.id.iv_city_photo9:
                if (cityList.get(8).getSuo().equals("1")) {
                    Intent intent9 = new Intent(ReadActivity.this, ReadDetailActivity.class);
                    intent9.putExtra("code", cityList.get(8).getCode());
                    startActivityForResult(intent9, 1);
                } else {
                    T.showShort(ReadActivity.this, "集满蜂蜜才会解锁哦！");
                }
                break;
            case R.id.iv_city_photo10:
                if (cityList.get(9).getSuo().equals("1")) {
                    Intent intent10 = new Intent(ReadActivity.this, ReadDetailActivity.class);
                    intent10.putExtra("code", cityList.get(9).getCode());
                    startActivityForResult(intent10, 1);
                } else {
                    T.showShort(ReadActivity.this, "集满蜂蜜才会解锁哦！");
                }
                break;
            case R.id.iv_city_photo11:
                if (cityList.get(10).getSuo().equals("1")) {
                    Intent intent11 = new Intent(ReadActivity.this, ReadDetailActivity.class);
                    intent11.putExtra("code", cityList.get(10).getCode());
                    startActivityForResult(intent11, 1);
                } else {
                    T.showShort(ReadActivity.this, "集满蜂蜜才会解锁哦！");
                }
                break;
        }
    }

    private void initPopAddBadgeView() {
        popview = LayoutInflater.from(ReadActivity.this).inflate(R.layout.pop_read_add_badge, null);
        popAddBadge = new PopupWindow(popview, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, false);
        popAddBadge.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddBadge.setFocusable(false);
        popAddBadge.setOutsideTouchable(false);
        popAddBadge.setOnDismissListener(() -> {
            setBackgroundAlpha(ReadActivity.this, 1f);
        });
        ivPopAddBadge = (ImageView) popview.findViewById(R.id.iv_badge);
        tvPopBadgeNumber = (TextView) popview.findViewById(R.id.tv_badgeNumber);
    }


    private void showPopViewAddBadge(String value, Animator.AnimatorListener listener) {
        if (popAddBadge != null && !popAddBadge.isShowing()) {
            setBackgroundAlpha(ReadActivity.this, 0.5f);
            popAddBadge.setAnimationStyle(R.style.MyPopupWindow_scale_anim_style_3);
            //ivPopAddBadge.setImageResource(R.drawable.ic_homepage_badge_add);
            tvPopBadgeNumber.setTextColor(Color.rgb(247, 220, 191));
            tvPopBadgeNumber.setText("徽章 +" + value);

//            popAddBadge.showAtLocation(tvTitle, Gravity.CENTER, 0, popAddBadge.getHeight());
            popAddBadge.showAtLocation(tvName, Gravity.CENTER, 0, popAddBadge.getHeight());
            // 启动动画
            startAnim(popview, listener);
        }
    }

    private void startAnim(View ivPopAddBadge, Animator.AnimatorListener listener) {
        ObjectAnimator animX1 = ObjectAnimator.ofFloat(ivPopAddBadge, "scaleX", 1f, 1.5f);
        animX1.setDuration(1000);
        animX1.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator animY1 = ObjectAnimator.ofFloat(ivPopAddBadge, "scaleY", 1f, 1.5f);
        animY1.setDuration(1000);
        animY1.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator animX2 = ObjectAnimator.ofFloat(ivPopAddBadge, "scaleX", 1.5f, 1f);
        animX2.setDuration(1000);
        animX2.setInterpolator(new DecelerateInterpolator());

        ObjectAnimator animY2 = ObjectAnimator.ofFloat(ivPopAddBadge, "scaleY", 1.5f, 1f);
        animY2.setDuration(1000);
        animY2.setInterpolator(new DecelerateInterpolator());

        animX2.addListener(listener);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animX1).with(animY1);
        animatorSet.play(animX2).with(animY2).after(animX1);
        animatorSet.start();
    }

    private void hidePopViewAddBadge() {
        if (popAddBadge != null && popAddBadge.isShowing()) {
            popAddBadge.dismiss();
        }

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


    @Override
    public boolean isBaseOnWidth() {
        return true;
    }

    @Override
    public float getSizeInDp() {
        return 640;
    }

}

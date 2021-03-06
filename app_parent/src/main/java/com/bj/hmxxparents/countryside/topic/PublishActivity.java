package com.bj.hmxxparents.countryside.topic;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bj.hmxxparents.BaseActivity;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.api.MLProperties;
import com.bj.hmxxparents.countryside.topic.adapter.SuyangAdapter;
import com.bj.hmxxparents.countryside.topic.model.UploadResult;
import com.bj.hmxxparents.countryside.topic.presenter.TopicPublishPresenter;
import com.bj.hmxxparents.countryside.topic.view.IViewTopicPublish;
import com.bj.hmxxparents.entity.BaseDataInfo;
import com.bj.hmxxparents.entity.MessageEvent;
import com.bj.hmxxparents.huodong.model.HuodongDetail;
import com.bj.hmxxparents.utils.Base64Util;
import com.bj.hmxxparents.utils.PreferencesUtils;
import com.bj.hmxxparents.utils.ScreenUtils;
import com.bj.hmxxparents.utils.StringUtils;
import com.bj.hmxxparents.utils.T;
import com.bj.hmxxparents.widget.CustomPopDialog;
import com.bj.hmxxparents.widget.LoadingDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;

public class PublishActivity extends BaseActivity implements BGASortableNinePhotoLayout.Delegate, IViewTopicPublish {

    private static final int PRC_PHOTO_PICKER = 1;

    private static final int RC_CHOOSE_PHOTO = 1;
    private static final int RC_PHOTO_PREVIEW = 2;
    @BindView(R.id.tv_header_left)
    TextView tvHeaderLeft;
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.tv_header_right)
    TextView tvHeaderRight;
    @BindView(R.id.et)
    EditText et;
    @BindView(R.id.layout_select)
    LinearLayout layoutSelect;
    @BindView(R.id.bgaSortPhotoLayout)
    BGASortableNinePhotoLayout bgaSortPhotoLayout;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mTagFlowLayout)
    TagFlowLayout mTagFlowLayout;
    private Unbinder unbinder;

    private TopicPublishPresenter publishPresenter;
    private String userphone;

    private LoadingDialog loadingDialog = null;

    private String suyangStr, huodong_code;
    private SuyangAdapter adapter;
    private List<HuodongDetail.DataBean.HuodongSuyangBean> mSubjectList = new ArrayList<>();
    private List<HuodongDetail.DataBean.HuodongSuyangBean> mSubjectListNew = new ArrayList<>();

    private LayoutInflater mInflater;
    private TagAdapter<HuodongDetail.DataBean.HuodongSuyangBean> mTagAdapter;
    private List<String> codeList = new ArrayList<>();

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

        setContentView(R.layout.activity_publish);
        unbinder = ButterKnife.bind(this);


        huodong_code = getIntent().getStringExtra("huodong_code");
        suyangStr = getIntent().getStringExtra("suyangStr");

        HuodongDetail huodongDetail = JSON.parseObject(suyangStr, new TypeReference<HuodongDetail>() {
        });
        mSubjectList.clear();
        mSubjectList.addAll(huodongDetail.getData().getHuodong_suyang());
        mSubjectList.remove(0);
        mSubjectListNew.addAll(mSubjectList);

        loadingDialog = new LoadingDialog(this);
        userphone = PreferencesUtils.getString(PublishActivity.this, MLProperties.PREFER_KEY_USER_ID);
        publishPresenter = new TopicPublishPresenter(this, this);

        initTitles();
        initViews();
    }

    private void initViews() {
        bgaSortPhotoLayout.setPlusEnable(false);//不显示已选择图片右侧加号
        bgaSortPhotoLayout.setEditable(true);//是否可被编辑
        bgaSortPhotoLayout.setDelegate(this);


        mInflater = LayoutInflater.from(this);
        mTagAdapter = new TagAdapter<HuodongDetail.DataBean.HuodongSuyangBean>(mSubjectListNew) {
            @Override
            public View getView(FlowLayout parent, int position, HuodongDetail.DataBean.HuodongSuyangBean subjectInfo) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv_suyang_tag, mTagFlowLayout, false);
                tv.setText(subjectInfo.getName());
                return tv;
            }
        };
        mTagFlowLayout.setAdapter(mTagAdapter);

    }

    private void initTitles() {
        tvHeaderTitle.setText("发布动态");
        tvHeaderLeft.setText("取消");
        tvHeaderRight.setText("发布");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_header_left, R.id.tv_header_right, R.id.layout_select})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_header_left:

                if (StringUtils.isEmpty(et.getText().toString().trim()) && bgaSortPhotoLayout.getData().size() == 0) {
                    finish();
                } else {
                    showDialog();
                }

                break;
            case R.id.tv_header_right:
                if (StringUtils.isEmpty(et.getText().toString().trim())) {
                    T.showShort(PublishActivity.this, "请先填写发布的内容，不少于15字");
                } else if (mTagFlowLayout.getSelectedList().size() == 0){
                    T.showShort(PublishActivity.this, "请先选择素养");
                }else if(bgaSortPhotoLayout.getData().size() == 0){
                    T.showShort(PublishActivity.this, "请先选择图片");
                }else if(et.getText().toString().trim().length() < 15){
                    T.showShort(PublishActivity.this, "发布的内容不能少于15字");
                }else {
//                    T.showShort(PublishActivity.this, "可以发布");
                    publishPresenter.uploadPic(bgaSortPhotoLayout.getData());
                }


               /* else {
                    if (et.getText().toString().trim().length() < 15) {
                        T.showShort(PublishActivity.this, "发布的内容不能少于15字");
                    } else {
                        if (mTagFlowLayout.getSelectedList().size() == 0) {
                            T.showShort(PublishActivity.this, "请先选择素养");
                        } else {
                            if (bgaSortPhotoLayout.getData().size() == 0) {

                                String content = Base64Util.encode(et.getText().toString().trim());
                                // publishPresenter.publishTopic(userphone, content, "");
                                codeList.clear();

                                loadingDialog.show();
                                Set<Integer> selectList = mTagFlowLayout.getSelectedList();
                                for (Iterator iterator = selectList.iterator(); iterator.hasNext(); ) {
                                    codeList.add(mSubjectList.get((int) iterator.next()).getCode());
                                }
                                //去除元素两边的“”
                                String codeStr1 = JSON.toJSONString(codeList).replace("\"", "");
                                //去除元素两边的[]
                                String codeStr2 = codeStr1.replace("[", "").replace("]", "");
                                Log.e("元素code", codeStr2);
                                publishPresenter.publishTopic(userphone, content, "", huodong_code, codeStr2);

                            } else {
                                publishPresenter.uploadPic(bgaSortPhotoLayout.getData());
                            }
                        }
                    }

                }*/

                break;
            case R.id.layout_select:
                RxPermissions rxPermissions = new RxPermissions(PublishActivity.this);
                rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(success -> {
                            if (success) {
                                choicePhoto();
                            } else {
                                T.showShort(PublishActivity.this, "未获取到相机权限");
                            }
                        });
//                choicePhoto();
                break;
        }

    }

    private void showDialog() {
        CustomPopDialog.Builder dialogBuild = new CustomPopDialog.Builder(PublishActivity.this);
        final CustomPopDialog dialog = dialogBuild.create(R.layout.dialog_publish_cancel, 0.7);
        dialog.setCanceledOnTouchOutside(false);
        TextView tv = (TextView) dialog.findViewById(R.id.title_text);
        tv.setText("确认要放弃发布？");
        dialog.findViewById(R.id.confirm_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing())
                    dialog.dismiss();
                finish();
            }
        });
        dialog.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing())
                    dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    private void choicePhoto() {
        // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
        File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerTakePhoto");

        Intent photoPickerIntent = new BGAPhotoPickerActivity.IntentBuilder(this)
                .cameraFileDir(takePhotoDir) // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话则不开启图库里的拍照功能
                .maxChooseCount(bgaSortPhotoLayout.getMaxItemCount() - bgaSortPhotoLayout.getItemCount()) // 图片选择张数的最大值
                .selectedPhotos(null) // 当前已选中的图片路径集合
                .pauseOnScroll(false) // 滚动列表时是否暂停加载图片
                .build();
        startActivityForResult(photoPickerIntent, RC_CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == RC_CHOOSE_PHOTO) {
            bgaSortPhotoLayout.addMoreData(BGAPhotoPickerActivity.getSelectedPhotos(data));

        } else if (requestCode == RC_PHOTO_PREVIEW) {
            bgaSortPhotoLayout.setData(BGAPhotoPickerPreviewActivity.getSelectedPhotos(data));
        }
    }

    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {

    }

    @Override
    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        bgaSortPhotoLayout.removeItem(position);
    }

    @Override
    public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        Intent photoPickerPreviewIntent = new BGAPhotoPickerPreviewActivity.IntentBuilder(this)
                .previewPhotos(models) // 当前预览的图片路径集合
                .selectedPhotos(models) // 当前已选中的图片路径集合
                .maxChooseCount(bgaSortPhotoLayout.getMaxItemCount()) // 图片选择张数的最大值
                .currentPosition(position) // 当前预览图片的索引
                .isFromTakePhoto(false) // 是否是拍完照后跳转过来
                .build();
        startActivityForResult(photoPickerPreviewIntent, RC_PHOTO_PREVIEW);
    }

    @Override
    public void onNinePhotoItemExchanged(BGASortableNinePhotoLayout sortableNinePhotoLayout, int fromPosition, int toPosition, ArrayList<String> models) {

    }

    ArrayList<String> pathList = new ArrayList<>();

    @Override
    public void getUploadResult(String result) {
        UploadResult uploadResult = JSON.parseObject(result, new TypeReference<UploadResult>() {
        });
        if (uploadResult.getRet().equals("1")) {
            pathList.add(uploadResult.getData().getImg());
        }

        Log.e("图片上传返回结果", result);
        Log.e("图片已选数量", bgaSortPhotoLayout.getData().size() + "");
        Log.e("图片上传数量", pathList.size() + "");

        if (pathList.size() == bgaSortPhotoLayout.getData().size()) {
            Log.e("图片数组0", JSON.toJSONString(pathList));

            String imgs = JSON.toJSONString(pathList).replace("\"", "");
            Log.e("图片数组1", imgs);
            //去掉数组首尾的符号[]
            String imgs1 = imgs.replace("[", "");
            Log.e("图片数组2", imgs1);

            String imgs2 = imgs1.replace("]", "");
            Log.e("图片数组3", imgs2);
            String content = Base64Util.encode(et.getText().toString().trim());

            // publishPresenter.publishTopic(userphone, content, imgs2);
            codeList.clear();

            loadingDialog.show();
            Set<Integer> selectList = mTagFlowLayout.getSelectedList();
            for (Iterator iterator = selectList.iterator(); iterator.hasNext(); ) {
                codeList.add(mSubjectList.get((int) iterator.next()).getCode());
            }
            //去除元素两边的“”
            String codeStr1 = JSON.toJSONString(codeList).replace("\"", "");
            //去除元素两边的[]
            String codeStr2 = codeStr1.replace("[", "").replace("]", "");
            Log.e("元素code", codeStr2);
            publishPresenter.publishTopic(userphone, content, imgs2, huodong_code, codeStr2);
        }

    }

    @Override
    public void getPublishResult(String result) {
        BaseDataInfo dataInfo = JSON.parseObject(result, new TypeReference<BaseDataInfo>() {
        });
        if (dataInfo.getRet().equals("1")) {

            if (loadingDialog != null) {
                loadingDialog.dismiss();
            }
            EventBus.getDefault().post(new MessageEvent("publishTopicSuccess"));

            T.showShort(PublishActivity.this, "发布成功");
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (StringUtils.isEmpty(et.getText().toString().trim()) && bgaSortPhotoLayout.getData().size() == 0) {
                finish();
            } else {
                showDialog();
            }
        }
        return super.onKeyDown(keyCode, event);

    }
}

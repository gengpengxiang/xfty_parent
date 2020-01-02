package com.bj.hmxxparents.countryside.topic;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bj.hmxxparents.BaseActivity;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.activity.LoginActivity;
import com.bj.hmxxparents.api.MLProperties;
import com.bj.hmxxparents.countryside.honorroll.adapter.SpaceItemDecoration;
import com.bj.hmxxparents.countryside.topic.adapter.SuyangAdapter;
import com.bj.hmxxparents.countryside.topic.adapter.TopicDetailAdapter;
import com.bj.hmxxparents.countryside.topic.model.AgreeResult;
import com.bj.hmxxparents.countryside.topic.model.TopicDetail;
import com.bj.hmxxparents.countryside.topic.presenter.TopicDetailPresenter;
import com.bj.hmxxparents.countryside.topic.view.IViewTopicDetail;
import com.bj.hmxxparents.email.EmailDetailActivity;
import com.bj.hmxxparents.entity.BaseDataInfo;
import com.bj.hmxxparents.entity.MessageEvent;
import com.bj.hmxxparents.utils.Base64Util;
import com.bj.hmxxparents.utils.KeyBoardUtils;
import com.bj.hmxxparents.utils.PreferencesUtils;
import com.bj.hmxxparents.utils.StringUtils;
import com.bj.hmxxparents.utils.T;
import com.bj.hmxxparents.widget.CustomPopDialog;
import com.bj.hmxxparents.wxapi.WXUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;

import static com.bj.hmxxparents.api.HttpUtilService.BASE_RESOURCE_URL;

public class TopicDetailActivity extends BaseActivity implements IViewTopicDetail, BGANinePhotoLayout.Delegate {

    @BindView(R.id.layout_header_left)
    RelativeLayout layoutHeaderLeft;
    @BindView(R.id.layout_header_right)
    RelativeLayout layoutHeaderRight;
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;


    SimpleDraweeView svPhoto;
    TextView tvName;
    TextView tvDate;
    TextView tvContent;
    BGANinePhotoLayout ninePhotoLayout;
    TextView tvLiulanNum;
    TextView tvShareNum;
    TextView tvCommentNum;
    ImageView ivDianzan;
    TextView tvDianzanNum;
    LinearLayout layoutShare, layoutComment, layoutDianzan;
    RelativeLayout btDelete;

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mSmartRefreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.edt_content)
    EditText edtContent;

    private String id;
    private String userphone;
    private Unbinder unbinder;
    private TopicDetailPresenter presenter;
    private int currentPage = 0;

    private View headerView;
    private List<TopicDetail.DataBean.HuifuListBean> dataList = new ArrayList<>();
    private TopicDetailAdapter adapter;

    private BGANinePhotoLayout mCurrentClickNpl;
    private String url_share,title_share,content_share,img_share;

    private RecyclerView childRecyclerView;
    private SuyangAdapter suyangAdapter;
    private List<TopicDetail.DataBean.HuodongSuyangBean> suyangList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_detail);
        unbinder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        presenter = new TopicDetailPresenter(this, this);
        userphone = PreferencesUtils.getString(TopicDetailActivity.this, MLProperties.PREFER_KEY_USER_ID);
        id = getIntent().getStringExtra("id");


        initTitle();
        initRecyclerHeadView();
        initViews();
        presenter.getTopicDetail(id, userphone, "0", currentPage);
    }

    private void initRecyclerHeadView() {
        headerView = getLayoutInflater().inflate(R.layout.recycler_header_topic_detail, null);

        svPhoto = (SimpleDraweeView) headerView.findViewById(R.id.sv_photo);
        tvName = (TextView) headerView.findViewById(R.id.tv_name);
        tvDate = (TextView) headerView.findViewById(R.id.tv_date);
        tvContent = (TextView) headerView.findViewById(R.id.tv_content);
        ninePhotoLayout = (BGANinePhotoLayout) headerView.findViewById(R.id.ninePhotoLayout);
        tvLiulanNum = (TextView) headerView.findViewById(R.id.tv_liulanNum);
        tvDianzanNum = (TextView) headerView.findViewById(R.id.tv_dianzanNum);
        tvCommentNum = (TextView) headerView.findViewById(R.id.tv_commentNum);
        tvShareNum = (TextView) headerView.findViewById(R.id.tv_shareNum);
        ivDianzan = (ImageView) headerView.findViewById(R.id.iv_dianzan);

        btDelete = (RelativeLayout)headerView.findViewById(R.id.bt_delete);

        headerView.findViewById(R.id.layout_dianzan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.agree(id, userphone, "0");
            }
        });
        headerView.findViewById(R.id.layout_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtContent.setFocusable(true);
                edtContent.setFocusableInTouchMode(true);
                edtContent.requestFocus();

                KeyBoardUtils.openKeybord(edtContent,TopicDetailActivity.this);
            }
        });
        headerView.findViewById(R.id.layout_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferencesUtils.putString(TopicDetailActivity.this, "shareLaiyuan","TopicDetailActivity");

                WXUtil.share(TopicDetailActivity.this,0,url_share,title_share,content_share,img_share);
            }
        });

        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        childRecyclerView = (RecyclerView)headerView.findViewById(R.id.childRecyclerView);
        childRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        suyangAdapter = new SuyangAdapter(R.layout.recycler_item_suyang, suyangList);
        childRecyclerView.setAdapter(suyangAdapter);
    }

    private void initViews() {

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(2));
        adapter = new TopicDetailAdapter(R.layout.recycler_item_topic_detail, dataList);
        adapter.addHeaderView(headerView);
        adapter.setHeaderAndEmpty(true);
        mRecyclerView.setAdapter(adapter);

        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                currentPage = 0;
                presenter.getTopicDetail(id, userphone, "0", currentPage);
            }
        });
        mSmartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                currentPage++;
                presenter.getTopicDetail(id, userphone, "0", currentPage);
            }
        });

    }

    private void initTitle() {
        tvHeaderTitle.setText("动态详情");
        layoutHeaderLeft.setVisibility(View.VISIBLE);
        layoutHeaderRight.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void uodate(MessageEvent event){
        if(event.getMessage().equals("shareSuccess")){
            String laiyuan = PreferencesUtils.getString(TopicDetailActivity.this, "shareLaiyuan","");
            if(laiyuan.equals("TopicDetailActivity")) {
                presenter.share(id, userphone, "0");
            }
        }
    }

    @OnClick({R.id.layout_header_left, R.id.layout_header_right, R.id.tv_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_header_left:
                finish();
                break;
            case R.id.layout_header_right:
                WXUtil.share(TopicDetailActivity.this,0,url_share,title_share,content_share,img_share);
                break;
            case R.id.tv_send:
                if (StringUtils.isEmpty(edtContent.getText().toString())) {
                    T.showLong(TopicDetailActivity.this, "回复内容不能为空");
                } else {
                    String content = Base64Util.encode(edtContent.getText().toString().trim());
                    presenter.comment(id,content,userphone,"0");

                    KeyBoardUtils.closeKeybord(this.getCurrentFocus().getWindowToken(), this);
                    edtContent.setText("");
                }
                break;
        }
    }

    @Override
    public void getTopicDetail(String result) {

        if(mSmartRefreshLayout.isRefreshing()){
            mSmartRefreshLayout.finishRefresh();
        }if(mSmartRefreshLayout.isLoading()){
            mSmartRefreshLayout.finishLoadmore();
        }

        mRecyclerView.setVisibility(View.VISIBLE);

        TopicDetail topicDetail = JSON.parseObject(result, new TypeReference<TopicDetail>() {
        });
        if (topicDetail.getRet().equals("1")) {
            TopicDetail.DataBean bean = topicDetail.getData();
            svPhoto.setImageURI(BASE_RESOURCE_URL + bean.getTeacher_img());
            tvName.setText(bean.getTeacher_name());
            tvDate.setText(bean.getDate());


            if (bean.getContent().size() != 0) {
                tvContent.setText(Base64Util.decode(bean.getContent().get(0).getContent()));
            }else {
                tvContent.setVisibility(View.GONE);
            }
            ArrayList<String> imgList = new ArrayList<>();

            for (int i = 0; i < bean.getImg().size(); i++) {
                imgList.add(BASE_RESOURCE_URL + bean.getImg().get(i).getImg());
            }

            ninePhotoLayout.setDelegate(this);
            ninePhotoLayout.setData(imgList);
            tvLiulanNum.setText("浏览" + (StringUtils.isEmpty(bean.getPageview()) ? "0" : bean.getPageview()) + "次");
            tvDianzanNum.setText(bean.getDianzannum());
            tvCommentNum.setText(bean.getCommentnum());
            tvShareNum.setText(bean.getFenxiangnum());

            EventBus.getDefault().post(new MessageEvent("liulanNum",bean.getPageview()));
            EventBus.getDefault().post(new MessageEvent("commentNum",bean.getCommentnum()));
            url_share = bean.getFenxiangurl();
            if (bean.getContent().size() > 0) {
                content_share = Base64Util.decode(bean.getContent().get(0).getContent());
            } else {
                content_share = "";
            }

            if (bean.getImg().size() > 0) {
                img_share = BASE_RESOURCE_URL + bean.getImg().get(0).getImg();
            } else {
                img_share = "";
            }

            if (bean.getDianzan_status() == 1) {
                ivDianzan.setImageResource(R.mipmap.ic_dianzan_red);
            } else {
                ivDianzan.setImageResource(R.mipmap.ic_dianzan_gray);
            }

            if (currentPage == 0) {
                dataList.clear();
            }

            if (bean.getHuifu_list() != null) {
                dataList.addAll(bean.getHuifu_list());
                adapter.notifyDataSetChanged();
            }

            if(bean.getContent().size()!=0){
                content_share = Base64Util.decode(bean.getContent().get(0).getContent());
            }else {
                content_share = "";
            }


            childRecyclerView.setVisibility(View.VISIBLE);
            suyangList.clear();
            suyangList.addAll(bean.getHuodong_suyang());
            suyangAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void getAgreeResult(String result) {
        AgreeResult agreeResult = JSON.parseObject(result, new TypeReference<AgreeResult>() {
        });
        if (agreeResult.getRet().equals("1")) {
            String msg = agreeResult.getMsg();
            int num = agreeResult.getData();
            ivDianzan.setImageResource(R.mipmap.ic_dianzan_red);
            tvDianzanNum.setText(num + "");
            EventBus.getDefault().post(new MessageEvent("dianzanSuccess","add",num+""));
        }
        if (agreeResult.getRet().equals("2")) {
            String msg = agreeResult.getMsg();
            int num = agreeResult.getData();
            ivDianzan.setImageResource(R.mipmap.ic_dianzan_gray);
            tvDianzanNum.setText(num + "");
            EventBus.getDefault().post(new MessageEvent("dianzanSuccess","del",num+""));
        }
    }

    @Override
    public void sendCommentResult(String result) {
        BaseDataInfo dataInfo = JSON.parseObject(result, new TypeReference<BaseDataInfo>() {
        });

        if (dataInfo.getRet().equals("1")) {
            currentPage = 0;
            presenter.getTopicDetail(id, userphone, "0", currentPage);
            T.showLong(TopicDetailActivity.this, "回复成功");
        }
    }

    @Override
    public void getShareResult(String result) {
        AgreeResult resultInfo = JSON.parseObject(result,new TypeReference<AgreeResult>(){});
        if(resultInfo.getRet().equals("1")){
            tvShareNum.setText(resultInfo.getData()+"");
        }
    }

    @Override
    public void getDeleteResult(String result) {
        BaseDataInfo dataInfo = JSON.parseObject(result,new TypeReference<BaseDataInfo>(){});
        if(dataInfo.getRet().equals("1")){
            EventBus.getDefault().post(new MessageEvent("deleteTopicSuccess",id));
            finish();
        }
    }

    private void showDialog() {
        CustomPopDialog.Builder dialogBuild = new CustomPopDialog.Builder(TopicDetailActivity.this);
        final CustomPopDialog dialog = dialogBuild.create(R.layout.dialog_publish_cancel, 0.7);
        dialog.setCanceledOnTouchOutside(false);
        TextView tv = (TextView) dialog.findViewById(R.id.title_text);
        tv.setText("确认要删除这条动态吗？");
        dialog.findViewById(R.id.confirm_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing())
                    dialog.dismiss();
                    presenter.delete(id);
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


    @Override
    public void onClickNinePhotoItem(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {

        mCurrentClickNpl = ninePhotoLayout;
        photoPreview();
    }

    private void photoPreview() {
        File downloadDir = new File(Environment.getExternalStorageDirectory(), "XftyDownload");
        BGAPhotoPreviewActivity.IntentBuilder photoPreviewIntentBuilder = new BGAPhotoPreviewActivity.IntentBuilder(this)
                .saveImgDir(downloadDir); // 保存图片的目录，如果传 null，则没有保存图片功能

        if (mCurrentClickNpl.getItemCount() == 1) {
            // 预览单张图片
            photoPreviewIntentBuilder.previewPhoto(mCurrentClickNpl.getCurrentClickItem());
        } else if (mCurrentClickNpl.getItemCount() > 1) {
            // 预览多张图片
            photoPreviewIntentBuilder.previewPhotos(mCurrentClickNpl.getData())
                    .currentPosition(mCurrentClickNpl.getCurrentClickItemPosition()); // 当前预览图片的索引
        }
        startActivity(photoPreviewIntentBuilder.build());
    }
}

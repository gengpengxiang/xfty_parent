package com.bj.hmxxparents.huodong;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bj.hmxxparents.BaseActivity;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.api.MLProperties;
import com.bj.hmxxparents.countryside.honorroll.adapter.SpaceItemDecoration;
import com.bj.hmxxparents.countryside.topic.PublishActivity;
import com.bj.hmxxparents.countryside.topic.TopicDetailActivity;
import com.bj.hmxxparents.countryside.topic.model.AgreeResult;
import com.bj.hmxxparents.countryside.topic.model.Topic;
import com.bj.hmxxparents.entity.BaseDataInfo;
import com.bj.hmxxparents.entity.MessageEvent;
import com.bj.hmxxparents.huodong.adapter.HuodongChildAdapter;
import com.bj.hmxxparents.huodong.model.HuodongDetail;
import com.bj.hmxxparents.huodong.presenter.HuodongTopicPresenter;
import com.bj.hmxxparents.huodong.view.IViewHuodongTopic;
import com.bj.hmxxparents.utils.Base64Util;
import com.bj.hmxxparents.utils.PreferencesUtils;
import com.bj.hmxxparents.utils.T;
import com.bj.hmxxparents.widget.SpacesItemDecoration;
import com.bj.hmxxparents.wxapi.WXUtil;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;

import static com.bj.hmxxparents.api.HttpUtilService.BASE_RESOURCE_URL;

public class HuodongDetailActivity extends BaseActivity implements IViewHuodongTopic {

    @BindView(R.id.header_img_back)
    ImageView headerImgBack;
    @BindView(R.id.header_tv_title)
    TextView headerTvTitle;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mSmartRefreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.bt_edit)
    Button btEdit;
    private Unbinder unbinder;

    private String huodong_code, student_classcode,suyang_code;
    private TopicAdapter adapter;
    ArrayList<Topic.DataBean> topicList = new ArrayList<>();

    private HuodongTopicPresenter presenter;
    private int currentPage = 0;
    private String userphone;

    private BGANinePhotoLayout mCurrentClickNpl;
    private ImageView ivDianzan;
    private TextView tvDianzanNum, tvCommentNum, tvShareNum, tvLiulanNum;

    private String id_share, url_share, title_share, content_share, img_share;
    private int pos;
    private View headView;
    private ImageView ivLogo;
    private TextView tvTitle, tvTongji;
    private TagFlowLayout mTagFlowLayout;
    private LayoutInflater mInflater;
    private TagAdapter<HuodongDetail.DataBean.HuodongSuyangBean> mTagAdapter;

    private RecyclerView childRecyclerView;
    private HuodongChildAdapter childAdapter;

    private List<HuodongDetail.DataBean.HuodongSuyangBean> mSubjectList = new ArrayList<>();

    private View line;
    private int tabPos = 0;

    private String suyangStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huodong_detail);
        unbinder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initToolBar();

        huodong_code = getIntent().getStringExtra("huodong_code");
        student_classcode = PreferencesUtils.getString(HuodongDetailActivity.this, MLProperties.BUNDLE_KEY_CLASS_CODE);


        userphone = PreferencesUtils.getString(HuodongDetailActivity.this, MLProperties.PREFER_KEY_USER_ID);
        initViews();
        presenter = new HuodongTopicPresenter(HuodongDetailActivity.this, this);

        presenter.getHuodongInfo(huodong_code);

//        presenter.getTopicList(userphone, "0", currentPage, huodong_code, "0", student_classcode);

    }

    private void initViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(HuodongDetailActivity.this));
        //mRecyclerView.addItemDecoration(new SpaceItemDecoration(10));

        adapter = new TopicAdapter(R.layout.recycler_item_countryside_dynamic, topicList);

//        adapter.setEmptyView(R.layout.recycler_item_tianyuan_empty,mRecyclerView);

        headView = getLayoutInflater().inflate(R.layout.recycler_header_huodong, null);
        ivLogo = (ImageView) headView.findViewById(R.id.iv_logo);
        tvTitle = (TextView) headView.findViewById(R.id.tv_title);
        tvTongji = (TextView) headView.findViewById(R.id.tv_tongji);
        mTagFlowLayout = (TagFlowLayout)headView.findViewById(R.id.mTagFlowLayout);
        mInflater = LayoutInflater.from(this);
        mTagAdapter = new TagAdapter<HuodongDetail.DataBean.HuodongSuyangBean>(mSubjectList) {
            @Override
            public View getView(FlowLayout parent, int position, HuodongDetail.DataBean.HuodongSuyangBean subjectInfo) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv_subject_tag, mTagFlowLayout, false);
                tv.setText(subjectInfo.getName());
                return tv;
            }
        };
        mTagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                mTagAdapter.setSelectedList(position);
                suyang_code = mSubjectList.get(position).getCode();
                currentPage = 0;

                if(tabPos==position){

                }else {
                    tabPos = position;
                    mSmartRefreshLayout.autoRefresh();
                    //presenter.getTopicList(userphone, "0", currentPage, huodong_code, mSubjectList.get(position).getCode());
                }

                return true;
            }
        });
        mTagFlowLayout.setAdapter(mTagAdapter);

        adapter.addHeaderView(headView);
        adapter.setHeaderAndEmpty(true);
        mRecyclerView.setAdapter(adapter);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                tvLiulanNum = (TextView) adapter.getViewByPosition(mRecyclerView, position + 1, R.id.tv_liulanNum);
                ivDianzan = (ImageView) adapter.getViewByPosition(mRecyclerView, position + 1, R.id.iv_dianzan);
                tvDianzanNum = (TextView) adapter.getViewByPosition(mRecyclerView, position + 1, R.id.tv_dianzanNum);
                tvCommentNum = (TextView) adapter.getViewByPosition(mRecyclerView, position + 1, R.id.tv_commentNum);
                tvShareNum = (TextView) adapter.getViewByPosition(mRecyclerView, position + 1, R.id.tv_shareNum);
                switch (view.getId()) {
                    case R.id.layout_dianzan:

                        presenter.agree(topicList.get(position).getTianyuanid(), userphone, "0");
                        break;
                    case R.id.layout_comment:
                        id_share = topicList.get(position).getTianyuanid();
                        url_share = topicList.get(position).getFenxiangurl();
                        presenter.browseAdd(topicList.get(position).getTianyuanid(), userphone, "0");

                        Intent intent = new Intent(HuodongDetailActivity.this, TopicDetailActivity.class);
                        intent.putExtra("id", topicList.get(position).getTianyuanid());
                        startActivity(intent);
                        break;
                    case R.id.layout_share:
                        id_share = topicList.get(position).getTianyuanid();
                        url_share = topicList.get(position).getFenxiangurl();

                        if (topicList.get(position).getContent().size() > 0) {
                            content_share = Base64Util.decode(topicList.get(position).getContent().get(0).getContent());
                        } else {
                            content_share = "";
                        }

                        if (topicList.get(position).getImg().size() > 0) {
                            img_share = BASE_RESOURCE_URL + topicList.get(position).getImg().get(0).getImg();
                        } else {
                            img_share = "";
                        }

                        PreferencesUtils.putString(HuodongDetailActivity.this, "shareLaiyuan", "TopicFragment");
                        WXUtil.share(HuodongDetailActivity.this, 0, url_share, title_share, content_share, img_share);
                        break;
                    case R.id.bt_delete:
                        pos = position;

                        break;
                }
            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                tvLiulanNum = (TextView) adapter.getViewByPosition(mRecyclerView, position + 1, R.id.tv_liulanNum);
                ivDianzan = (ImageView) adapter.getViewByPosition(mRecyclerView, position + 1, R.id.iv_dianzan);
                tvDianzanNum = (TextView) adapter.getViewByPosition(mRecyclerView, position + 1, R.id.tv_dianzanNum);
                tvCommentNum = (TextView) adapter.getViewByPosition(mRecyclerView, position + 1, R.id.tv_commentNum);
                tvShareNum = (TextView) adapter.getViewByPosition(mRecyclerView, position + 1, R.id.tv_shareNum);

                id_share = topicList.get(position).getTianyuanid();
                url_share = topicList.get(position).getFenxiangurl();

                presenter.browseAdd(topicList.get(position).getTianyuanid(), userphone, "0");
                Intent intent = new Intent(HuodongDetailActivity.this, TopicDetailActivity.class);
                intent.putExtra("id", topicList.get(position).getTianyuanid());
                startActivity(intent);

            }
        });

        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                currentPage = 0;
                //presenter.getTopicList(userphone, "0", currentPage, huodong_code, suyang_code);
                presenter.getHuodongInfo(huodong_code);
            }
        });
        mSmartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                currentPage++;
                presenter.getTopicList(userphone, "0", currentPage, huodong_code, suyang_code);
            }
        });

        childRecyclerView = (RecyclerView) headView.findViewById(R.id.mRecyclerView);
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(this);
        //flexDirection 属性决定主轴的方向（即项目的排列方向）。类似 LinearLayout 的 vertical 和 horizontal。
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);//主轴为水平方向，起点在左端。
        //flexWrap 默认情况下 Flex 跟 LinearLayout 一样，都是不带换行排列的，但是flexWrap属性可以支持换行排列。
        flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);//按正常方向换行
        //justifyContent 属性定义了项目在主轴上的对齐方式。
        flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);//交叉轴的起点对齐。
        childRecyclerView.setLayoutManager(flexboxLayoutManager);
        childRecyclerView.addItemDecoration(new SpaceItemDecoration(6));
        childAdapter = new HuodongChildAdapter(R.layout.recycler_item_huodong_tab, mSubjectList);

        childRecyclerView.setAdapter(childAdapter);

        childAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter2, View view, int position) {
                suyang_code = mSubjectList.get(position).getCode();
                currentPage = 0;
                for (int i = 0; i < mSubjectList.size(); i++) {
                    if (i == position) {
                        mSubjectList.get(i).setSelect(true);
                    } else {
                        mSubjectList.get(i).setSelect(false);
                    }
                }

                childAdapter.notifyDataSetChanged();

                if (tabPos == position) {

                } else {
                    tabPos = position;
                    //mSmartRefreshLayout.autoRefresh();
                    presenter.getTopicList(userphone, "0", currentPage, huodong_code, mSubjectList.get(position).getCode());

                }
            }
        });

    }

    private void initToolBar() {
        headerTvTitle.setText("活动详情");
        headerTvTitle.setVisibility(View.VISIBLE);
        headerImgBack.setVisibility(View.VISIBLE);

        //kidId = getIntent().getStringExtra(MLProperties.BUNDLE_KEY_KID_ID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void uodate(MessageEvent event) {
        if (event.getMessage().equals("liulanNum")) {
            String num = event.getParam1();
            tvLiulanNum.setText("浏览" + num + "次");
        }
        if (event.getMessage().equals("commentNum")) {
            String num = event.getParam1();
            tvCommentNum.setText(num);
        }
        if (event.getMessage().equals("publishTopicSuccess")) {
            currentPage = 0;
            //presenter.getTopicList(userphone, "0", currentPage, huodong_code, suyang_code);
            presenter.getHuodongInfo(huodong_code);
        }
        if (event.getMessage().equals("dianzanSuccess")) {
            if (event.getParam1().equals("add")) {
                ivDianzan.setImageResource(R.mipmap.ic_dianzan_red);
                tvDianzanNum.setText(event.getParam2() + "");
            } else {
                ivDianzan.setImageResource(R.mipmap.ic_dianzan_gray);
                tvDianzanNum.setText(event.getParam2() + "");
            }
        }
        if (event.getMessage().equals("shareSuccess")) {
            String laiyuan = PreferencesUtils.getString(HuodongDetailActivity.this, "shareLaiyuan", "");
            if (laiyuan.equals("TopicFragment")) {
                presenter.share(id_share, userphone, "0");
            }
        }
        if (event.getMessage().equals("deleteTopicSuccess")) {
            String id = event.getParam1();
            for (int i = 0; i < topicList.size(); i++) {
                if (topicList.get(i).getTianyuanid().equals(id)) {
                    adapter.remove(i);
                    adapter.notifyItemRemoved(i);
                }
            }
        }
    }

    @Override
    public void getTopicList(String result) {

        if (mSmartRefreshLayout.isRefreshing()) {
            mSmartRefreshLayout.finishRefresh();
        }
        if (mSmartRefreshLayout.isLoading()) {
            mSmartRefreshLayout.finishLoadmore();
        }

        if(result.equals("error")){
            topicList.clear();
            adapter.notifyDataSetChanged();
            return;
        }

        try {
            Topic topic = JSON.parseObject(result, new TypeReference<Topic>() {
            });
            if (topic.getRet().equals("1")) {
                if (currentPage == 0) {
                    topicList.clear();
                }
                topicList.addAll(topic.getData());
                adapter.notifyDataSetChanged();
            } else {
                if (currentPage == 0) {
                    topicList.clear();
                }
                adapter.notifyDataSetChanged();
            }
            adapter.setEmptyView(R.layout.recycler_item_huodong_empty, mRecyclerView);
        } catch (Exception e) {

        }
    }

    @Override
    public void getAgreeResult(String result) {
        AgreeResult agreeResult = JSON.parseObject(result, new TypeReference<AgreeResult>() {
        });
        if (agreeResult.getRet().equals("1")) {
            int num = agreeResult.getData();
            ivDianzan.setImageResource(R.mipmap.ic_dianzan_red);
            tvDianzanNum.setText(num + "");
        }
        if (agreeResult.getRet().equals("2")) {
            int num = agreeResult.getData();
            ivDianzan.setImageResource(R.mipmap.ic_dianzan_gray);
            tvDianzanNum.setText(num + "");
            Log.e("222", "222");
        }
    }

    @Override
    public void getShareResult(String result) {
        AgreeResult restltInfo = JSON.parseObject(result, new TypeReference<AgreeResult>() {
        });
        if (restltInfo.getRet().equals("1")) {
            tvShareNum.setText(restltInfo.getData() + "");
        }
    }

    @Override
    public void getDeleteResult(String result) {
        BaseDataInfo dataInfo = JSON.parseObject(result, new TypeReference<BaseDataInfo>() {
        });
        if (dataInfo.getRet().equals("1")) {
            topicList.remove(pos);
            adapter.notifyItemRemoved(pos);
        }
    }

    @Override
    public void getHuodongInfo(String result) {

        suyangStr = result;

        HuodongDetail huodongDetail = JSON.parseObject(result, new TypeReference<HuodongDetail>() {
        });
        if (huodongDetail.getRet().equals("1")) {

            HuodongDetail.DataBean.HuodongInfoBean bean = huodongDetail.getData().getHuodong_info();

            tvTitle.setText(bean.getTitle());
            tvTongji.setText(bean.getTaolun_num() + "讨论" + "    " + bean.getUser_num() + "参与");
            Glide.with(HuodongDetailActivity.this).load(BASE_RESOURCE_URL + bean.getImg()).placeholder(R.mipmap.ic_huodong_default).error(R.mipmap.ic_huodong_default).into(ivLogo);

            mSubjectList.clear();
            mSubjectList.addAll(huodongDetail.getData().getHuodong_suyang());


            for (int i = 0; i < mSubjectList.size(); i++) {
                if (i == tabPos) {
                    mSubjectList.get(i).setSelect(true);
                } else {
                    mSubjectList.get(i).setSelect(false);
                }
            }


            childAdapter.notifyDataSetChanged();


            //childadapter.notifyDataSetChanged();
            mTagAdapter.setSelectedList(0);
            mTagAdapter.notifyDataChanged();

            suyang_code = mSubjectList.get(tabPos).getCode();

            presenter.getTopicList(userphone, "0", currentPage, huodong_code, suyang_code);
        }


    }

    @OnClick({R.id.header_ll_left, R.id.bt_edit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_ll_left:
                finish();
                break;
            case R.id.bt_edit:
                Intent intent = new Intent(HuodongDetailActivity.this, PublishActivity.class);
                intent.putExtra("laiyuan","TopicFragment");
                intent.putExtra("suyangStr",suyangStr);
                intent.putExtra("huodong_code",huodong_code);
                startActivity(intent);
                break;
        }
    }


    class TopicAdapter extends BaseQuickAdapter<Topic.DataBean, BaseViewHolder> {

        public TopicAdapter(int layoutResId, @Nullable List<Topic.DataBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Topic.DataBean item) {

            SimpleDraweeView svPhoto = helper.getView(R.id.sv_photo);
            svPhoto.setImageURI(BASE_RESOURCE_URL + item.getTeacher_img());

            helper.setText(R.id.tv_name, item.getTeacher_name());
            helper.setText(R.id.tv_date, item.getTime());
            if (item.getContent().size() > 0) {
                helper.getView(R.id.tv_content).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_content, Base64Util.decode(item.getContent().get(0).getContent()));
//                if (Base64Util.checkBase64(item.getContent().get(0).getContent())) {
//
//                    helper.setText(R.id.tv_content, Base64Util.decode(item.getContent().get(0).getContent()));
//                } else {
//                    helper.setText(R.id.tv_content, item.getContent().get(0).getContent());
//                }
            } else {
                helper.getView(R.id.tv_content).setVisibility(View.GONE);
            }

            BGANinePhotoLayout bgaNinePhotoLayout = helper.getView(R.id.ninePhotoLayout);

            ArrayList<String> imgList = new ArrayList<>();

            if (item.getImg().size() > 3) {
                for (int i = 0; i < 3; i++) {
                    imgList.add(BASE_RESOURCE_URL + item.getImg().get(i).getImg());
                }
            } else {
                for (int i = 0; i < item.getImg().size(); i++) {
                    imgList.add(BASE_RESOURCE_URL + item.getImg().get(i).getImg());
                }
            }


            bgaNinePhotoLayout.setData(imgList);
            bgaNinePhotoLayout.setDelegate(new BGANinePhotoLayout.Delegate() {
                @Override
                public void onClickNinePhotoItem(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
//                    mCurrentClickNpl = ninePhotoLayout;
//                    photoPreview();
                    for (int i = 0; i < topicList.size(); i++) {
                        if (topicList.get(i).getTianyuanid().equals(item.getTianyuanid())) {
                            tvLiulanNum = (TextView) adapter.getViewByPosition(mRecyclerView, i+1, R.id.tv_liulanNum);
                        }
                    }

                    presenter.browseAdd(item.getTianyuanid(), userphone, "1");
                    Intent intent = new Intent(HuodongDetailActivity.this, TopicDetailActivity.class);
                    intent.putExtra("id", item.getTianyuanid());
                    startActivity(intent);
                }
            });

            ImageView ivDianzan = helper.getView(R.id.iv_dianzan);

            if (item.getDianzan_status() == 1) {
                ivDianzan.setImageResource(R.mipmap.ic_dianzan_red);
            } else {
                ivDianzan.setImageResource(R.mipmap.ic_dianzan_gray);
            }


            helper.setText(R.id.tv_liulanNum, "浏览" + item.getPageview() + "次");
            helper.setText(R.id.tv_shareNum, item.getFenxiangnum());
            helper.setText(R.id.tv_commentNum, item.getCommentnum());
            helper.setText(R.id.tv_dianzanNum, item.getDianzannum());

            helper.addOnClickListener(R.id.layout_dianzan);
            helper.addOnClickListener(R.id.layout_comment);
            helper.addOnClickListener(R.id.layout_share);


            RelativeLayout btDelete = helper.getView(R.id.bt_delete);

            helper.addOnClickListener(R.id.bt_delete);


            TextView tvCheck0 = helper.getView(R.id.tv_check0);//未审核
            TextView tvCheck3 = helper.getView(R.id.tv_check3);//审核未通过
            if(item.getStatus().equals("0")){
                tvCheck0.setVisibility(View.VISIBLE);
                tvCheck3.setVisibility(View.GONE);
            }else if(item.getStatus().equals("3")){
                tvCheck3.setVisibility(View.VISIBLE);
                tvCheck0.setVisibility(View.GONE);
            }else {
                tvCheck0.setVisibility(View.GONE);
                tvCheck3.setVisibility(View.GONE);
            }

        }
    }

}

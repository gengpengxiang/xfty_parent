package com.bj.hmxxparents.countryside.topic;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bj.hmxxparents.BaseFragment;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.api.MLProperties;
import com.bj.hmxxparents.countryside.topic.model.AgreeResult;
import com.bj.hmxxparents.countryside.topic.model.Topic;
import com.bj.hmxxparents.countryside.topic.presenter.TopicPresenter;
import com.bj.hmxxparents.countryside.topic.view.IViewTopic;
import com.bj.hmxxparents.countryside.honorroll.adapter.SpaceItemDecoration;
import com.bj.hmxxparents.entity.BaseDataInfo;
import com.bj.hmxxparents.entity.MessageEvent;
import com.bj.hmxxparents.utils.Base64Util;
import com.bj.hmxxparents.utils.PreferencesUtils;
import com.bj.hmxxparents.utils.T;
import com.bj.hmxxparents.widget.CustomPopDialog;
import com.bj.hmxxparents.wxapi.WXUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
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


public class TopicFragment extends BaseFragment implements IViewTopic {


    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mSmartRefreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.bt_edit)
    Button btEdit;
    private Unbinder unbinder;

    private TopicAdapter adapter;
    ArrayList<Topic.DataBean> topicList = new ArrayList<>();

    private TopicPresenter presenter;
    private int currentPage = 0;
    private String userphone;

    private BGANinePhotoLayout mCurrentClickNpl;
    private ImageView ivDianzan;
    private TextView tvDianzanNum, tvCommentNum, tvShareNum, tvLiulanNum;

    private String id_share, url_share, title_share, content_share,img_share;
    private int pos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_countryside_dynamic, container, false);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        userphone = PreferencesUtils.getString(getActivity(), MLProperties.PREFER_KEY_USER_ID);
        initViews();
        presenter = new TopicPresenter(getActivity(), this);

        presenter.getTopicList(userphone, "0", currentPage);

        return view;
    }

    private void initViews() {

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //mRecyclerView.addItemDecoration(new SpaceItemDecoration(10));

        adapter = new TopicAdapter(R.layout.recycler_item_countryside_dynamic, topicList);

        adapter.setEmptyView(R.layout.recycler_item_tianyuan_empty,mRecyclerView);

        mRecyclerView.setAdapter(adapter);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                tvLiulanNum = (TextView) adapter.getViewByPosition(mRecyclerView, position, R.id.tv_liulanNum);
                ivDianzan = (ImageView) adapter.getViewByPosition(mRecyclerView, position, R.id.iv_dianzan);
                tvDianzanNum = (TextView) adapter.getViewByPosition(mRecyclerView, position, R.id.tv_dianzanNum);
                tvCommentNum = (TextView) adapter.getViewByPosition(mRecyclerView, position, R.id.tv_commentNum);
                tvShareNum = (TextView) adapter.getViewByPosition(mRecyclerView, position, R.id.tv_shareNum);
                switch (view.getId()) {
                    case R.id.layout_dianzan:

                        presenter.agree(topicList.get(position).getTianyuanid(), userphone, "0");
                        break;
                    case R.id.layout_comment:
                        id_share = topicList.get(position).getTianyuanid();
                        url_share = topicList.get(position).getFenxiangurl();
                        presenter.browseAdd(topicList.get(position).getTianyuanid(), userphone, "0");

                        Intent intent = new Intent(getActivity(), TopicDetailActivity.class);
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

                        PreferencesUtils.putString(getActivity(), "shareLaiyuan","TopicFragment");
                        WXUtil.share(getActivity(), 0, url_share, title_share, content_share, img_share);
                        break;
                    case R.id.bt_delete:
                        pos = position;
                        showDialog(topicList.get(position).getTianyuanid());
                        break;
                }
            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                tvLiulanNum = (TextView) adapter.getViewByPosition(mRecyclerView, position, R.id.tv_liulanNum);
                ivDianzan = (ImageView) adapter.getViewByPosition(mRecyclerView, position, R.id.iv_dianzan);
                tvDianzanNum = (TextView) adapter.getViewByPosition(mRecyclerView, position, R.id.tv_dianzanNum);
                tvCommentNum = (TextView) adapter.getViewByPosition(mRecyclerView, position, R.id.tv_commentNum);
                tvShareNum = (TextView) adapter.getViewByPosition(mRecyclerView, position, R.id.tv_shareNum);

                id_share = topicList.get(position).getTianyuanid();
                url_share = topicList.get(position).getFenxiangurl();

                presenter.browseAdd(topicList.get(position).getTianyuanid(), userphone, "0");
                Intent intent = new Intent(getActivity(), TopicDetailActivity.class);
                intent.putExtra("id", topicList.get(position).getTianyuanid());
                startActivity(intent);

            }
        });

        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                currentPage = 0;
                presenter.getTopicList(userphone, "0", currentPage);
            }
        });
        mSmartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                currentPage++;
                presenter.getTopicList(userphone, "0", currentPage);
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        presenter.onDestory();
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
            presenter.getTopicList(userphone, "0", currentPage);
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
            String laiyuan = PreferencesUtils.getString(getActivity(), "shareLaiyuan","");
            if(laiyuan.equals("TopicFragment")) {
                presenter.share(id_share, userphone, "0");
            }
        }
        if (event.getMessage().equals("deleteTopicSuccess")) {
            String id = event.getParam1();
            for(int i=0;i<topicList.size();i++){
                if(topicList.get(i).getTianyuanid().equals(id)){
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

        try {

            Topic topic = JSON.parseObject(result, new TypeReference<Topic>() {
            });
            if (topic.getRet().equals("1")) {
                if (currentPage == 0) {
                    topicList.clear();
                }
                topicList.addAll(topic.getData());
                adapter.notifyDataSetChanged();
            }

        }catch (Exception e){

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


        }
        if (agreeResult.getRet().equals("2")) {
            String msg = agreeResult.getMsg();
            int num = agreeResult.getData();
            ivDianzan.setImageResource(R.mipmap.ic_dianzan_gray);
            tvDianzanNum.setText(num + "");
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

    @OnClick(R.id.bt_edit)
    public void onClick() {
        Intent intent = new Intent(getActivity(), PublishActivity.class);
        startActivity(intent);

    }

    private void showDialog(String id) {
        CustomPopDialog.Builder dialogBuild = new CustomPopDialog.Builder(getActivity());
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


    private void photoPreview() {
        File downloadDir = new File(Environment.getExternalStorageDirectory(), "XftyDownload");
        BGAPhotoPreviewActivity.IntentBuilder photoPreviewIntentBuilder = new BGAPhotoPreviewActivity.IntentBuilder(getActivity())
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

            if(item.getImg().size()>3){
                for (int i = 0; i < 3; i++) {
                    imgList.add(BASE_RESOURCE_URL + item.getImg().get(i).getImg());
                }
            }else {
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
                    for(int i=0;i<topicList.size();i++){
                        if(topicList.get(i).getTianyuanid().equals(item.getTianyuanid())){
                            tvLiulanNum = (TextView) adapter.getViewByPosition(mRecyclerView, i, R.id.tv_liulanNum);
                        }
                    }

                    presenter.browseAdd(item.getTianyuanid(), userphone, "1");
                    Intent intent = new Intent(getActivity(), TopicDetailActivity.class);
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

        }
    }
}

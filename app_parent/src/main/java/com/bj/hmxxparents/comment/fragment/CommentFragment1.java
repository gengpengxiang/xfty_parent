package com.bj.hmxxparents.comment.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bj.hmxxparents.BaseFragment;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.api.MLProperties;
import com.bj.hmxxparents.comment.adapter.CommentAdapter;
import com.bj.hmxxparents.comment.modal.CommentInfo;
import com.bj.hmxxparents.comment.modal.CommentType;
import com.bj.hmxxparents.comment.presenter.CommentPresenter;
import com.bj.hmxxparents.comment.view.IViewComment;
import com.bj.hmxxparents.entity.BaseDataInfo;
import com.bj.hmxxparents.utils.PreferencesUtils;
import com.bj.hmxxparents.utils.T;
import com.bj.hmxxparents.widget.SpacesItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by zz379 on 2017/ic_pinde/3.
 * 学生本学期表现报告页面--第一页
 */

public class CommentFragment1 extends BaseFragment implements IViewComment{

    @BindView(R.id.mTagFlowLayout)
    TagFlowLayout mTagFlowLayout;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mSmartRefreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    private Unbinder unbinder;

    private String kidId;
    private String comment_type = "1";

    private CommentPresenter presenter;
    private LayoutInflater mInflater;
    private TagAdapter<CommentType.DataBean.Z1Bean> mTagAdapter;
    private List<CommentType.DataBean.Z1Bean> mSubjectList = new ArrayList<>();

    private CommentAdapter commentAdapter;
    private List<CommentInfo.DataBean> commentInfoList = new ArrayList<>();
    private View headView;

    private String reasonTypeID = "0";
    private int currentPage = 0;
    private TextView tvTitle;

    private int pos = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        unbinder = ButterKnife.bind(this, view);

        initData();
        initViews();
        presenter = new CommentPresenter(getActivity(),this);

        presenter.getCommentType(kidId,comment_type);
        return view;
    }

    private void initViews() {
        mInflater = LayoutInflater.from(getActivity());
        // 初始化流式布局
        mTagAdapter = new TagAdapter<CommentType.DataBean.Z1Bean>(mSubjectList) {
            @Override
            public View getView(FlowLayout parent, int position, CommentType.DataBean.Z1Bean subjectInfo) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv_subject_tag, mTagFlowLayout, false);
                tv.setText(subjectInfo.getLiyou_name() + " " + subjectInfo.getLiyou_num());
                return tv;
            }
        };
        mTagFlowLayout.setAdapter(mTagAdapter);
        mTagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                mTagAdapter.setSelectedList(position);
                reasonTypeID = mSubjectList.get(position).getLiyou_id();
//
//                mXRefreshView.startRefresh();
                currentPage = 0;
                presenter.getCommentList(kidId,reasonTypeID,comment_type,currentPage);

                return true;
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(5));

        commentAdapter = new CommentAdapter(R.layout.recycler_item_comment, commentInfoList);
        headView = getActivity().getLayoutInflater().inflate(R.layout.recycler_header_line_textview_line2,null);
        commentAdapter.addHeaderView(headView);
        mRecyclerView.setAdapter(commentAdapter);
        commentAdapter.setHeaderAndEmpty(true);

        commentAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                pos = position;
                presenter.thanks(commentInfoList.get(position).getDongtai_id());
            }
        });

        tvTitle = (TextView) headView.findViewById(R.id.tv_title);
        tvTitle.setText("点赞记录");

        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                currentPage = 0;
                presenter.getCommentList(kidId,reasonTypeID,comment_type,currentPage);

            }
        });
        mSmartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                currentPage++;
                presenter.getCommentList(kidId,reasonTypeID,comment_type,currentPage);
            }
        });

    }

    private void initData() {
        kidId = getActivity().getIntent().getStringExtra(MLProperties.BUNDLE_KEY_KID_ID);
    }

    @Override
    public void getCommentType(String result) {


        CommentType commentType = JSON.parseObject(result, new TypeReference<CommentType>() {
        });
        if(commentType.getRet().equals("1")){
            mSubjectList.clear();
            mSubjectList.addAll(commentType.getData().getZ1());

            if (mSubjectList.size() > 0) {
                mTagAdapter.setSelectedList(0);
            }
            mTagAdapter.notifyDataChanged();

            presenter.getCommentList(kidId,reasonTypeID,comment_type,currentPage);
        }

    }

    @Override
    public void getCommentList(String result) {

        if(mSmartRefreshLayout.isRefreshing()){
            mSmartRefreshLayout.finishRefresh();
        }if(mSmartRefreshLayout.isLoading()){
            mSmartRefreshLayout.finishLoadmore();
        }

        commentAdapter.setEmptyView(R.layout.recycler_item_tianyuan_empty,mRecyclerView);

        CommentInfo commentInfo = JSON.parseObject(result, new TypeReference<CommentInfo>() {
        });
        if(commentInfo.getRet().equals("1")){
            if (currentPage == 0) {
                commentInfoList.clear();
            }
            commentInfoList.addAll(commentInfo.getData());
            commentAdapter.notifyDataSetChanged();
        }

    }

    @Override
        public void getThanksResult(String result) {
        BaseDataInfo baseDataInfo = JSON.parseObject(result, new TypeReference<BaseDataInfo>() {
        });
        if(baseDataInfo.getRet().equals("1")){
            T.showShort(getActivity(),"感谢成功");
            commentInfoList.get(pos).setDongtai_ganxiestatus("2");
            commentAdapter.notifyDataSetChanged();
        }

        }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.onDestory();
    }
}

package com.douhao.game.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.douhao.game.R;
import com.douhao.game.entity.LevelInfo;

/**
 * Created by zz379 on 2017/5/27.
 */

public class ReadingResultFragment extends BaseGameFragment implements View.OnClickListener {

    protected TextView tvShare;
    protected TextView tvNext;
    protected TextView tvLevelScore;
    protected TextView tvBeatUsers;
    protected TextView tvTrueReading;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.douhao_fragment_reading_result, container, false);
    }

    @Override
    public void initView() {
        super.initView();
        tvShare = (TextView) getView().findViewById(R.id.tv_share);
        tvShare.setOnClickListener(this);
        tvNext = (TextView) getView().findViewById(R.id.tv_next);
        tvNext.setOnClickListener(this);

        tvLevelScore = (TextView) getView().findViewById(R.id.tv_levelScore);
        tvBeatUsers = (TextView) getView().findViewById(R.id.tv_beatUsers);
        tvTrueReading = (TextView) getView().findViewById(R.id.tv_trueReading);
    }

    @Override
    public void initData() {
        super.initData();
    }

    public void refreshData(LevelInfo info) {
        tvLevelScore.setText("本关得分" + info.getLvlScore() + "分");
        tvBeatUsers.setText("恭喜你本题击败" + info.getLvlbeatUsers() + "%的玩家");
        tvTrueReading.setText(info.getLvlTrueReading());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_share) {
            if (mOnPageItemClickListener != null) {
                mOnPageItemClickListener.onShareClick(v);
            }
        } else if (v.getId() == R.id.tv_next) {
            if (mOnPageItemClickListener != null) {
                mOnPageItemClickListener.onNextClick(v);
            }
        }
    }

    private OnPageItemClickListener mOnPageItemClickListener;

    public void setOnPageItemClickListener(OnPageItemClickListener mOnPageItemClickListener) {
        this.mOnPageItemClickListener = mOnPageItemClickListener;
    }

    public interface OnPageItemClickListener {
        void onShareClick(View view);

        void onNextClick(View view);
    }
}

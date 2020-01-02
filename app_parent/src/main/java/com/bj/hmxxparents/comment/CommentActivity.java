package com.bj.hmxxparents.comment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.widget.TextView;

import com.bj.hmxxparents.BaseActivity;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.comment.fragment.CommentFragment1;
import com.bj.hmxxparents.comment.fragment.CommentFragment2;
import com.bj.hmxxparents.widget.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CommentActivity extends BaseActivity {

    @BindView(R.id.bt_dianzan)
    TextView btDianzan;
    @BindView(R.id.bt_gaijin)
    TextView btGaijin;
    @BindView(R.id.mViewPager)
    CustomViewPager mViewPager;

    private Unbinder unbinder;

    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        unbinder = ButterKnife.bind(this);

        initData();
        initViews();

    }

    private void initViews() {
        fragmentList.add(new CommentFragment1());
        fragmentList.add(new CommentFragment2());

        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });

        mViewPager.setPagingEnabled(false);
        mViewPager.setCurrentItem(0, false);
    }

    private void initData() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick({R.id.bt_back, R.id.bt_dianzan, R.id.bt_gaijin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                finish();
                break;
            case R.id.bt_dianzan:
                btDianzan.setTextColor(getResources().getColor(R.color.colorPrimary));
                btDianzan.setBackgroundResource(R.drawable.shape_corner_reason_tab_left_1);
                btGaijin.setTextColor(Color.WHITE);
                btGaijin.setBackgroundResource(R.drawable.shape_corner_reason_tab_right_2);

                mViewPager.setCurrentItem(0, false);
                break;
            case R.id.bt_gaijin:
                btDianzan.setTextColor(Color.WHITE);
                btDianzan.setBackgroundResource(R.drawable.shape_corner_reason_tab_left_2);
                btGaijin.setTextColor(getResources().getColor(R.color.colorPrimary));
                btGaijin.setBackgroundResource(R.drawable.shape_corner_reason_tab_right_1);

                mViewPager.setCurrentItem(1, false);
                break;
        }
    }

}

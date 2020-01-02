package com.bj.hmxxparents.huodong.view;

import com.bj.hmxxparents.mvp.MvpView;

/**
 * Created by Administrator on 2018/11/23 0023.
 */

public interface IViewHuodongTopic extends MvpView{

    void getTopicList(String result);

    void getAgreeResult(String result);

    void getShareResult(String result);

    void getDeleteResult(String result);

    void getHuodongInfo(String result);

}

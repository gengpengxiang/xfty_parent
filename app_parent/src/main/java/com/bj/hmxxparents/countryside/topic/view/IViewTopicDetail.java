package com.bj.hmxxparents.countryside.topic.view;

import com.bj.hmxxparents.mvp.MvpView;

/**
 * Created by Administrator on 2018/11/23 0023.
 */

public interface IViewTopicDetail extends MvpView{

    void getTopicDetail(String result);
    void getAgreeResult(String result);
    void sendCommentResult(String result);
    void getShareResult(String result);
    void getDeleteResult(String result);

}

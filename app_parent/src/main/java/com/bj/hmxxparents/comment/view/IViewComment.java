package com.bj.hmxxparents.comment.view;


import com.bj.hmxxparents.mvp.MvpView;

/**
 * Created by Administrator on 2018/10/29 0029.
 */

public interface IViewComment extends MvpView {

    void getCommentType(String result);
    void getCommentList(String result);
    void getThanksResult(String result);

}

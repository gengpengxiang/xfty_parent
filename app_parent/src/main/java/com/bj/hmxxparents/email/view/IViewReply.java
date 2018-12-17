package com.bj.hmxxparents.email.view;


import com.bj.hmxxparents.email.model.Reply;
import com.bj.hmxxparents.mvp.MvpView;

/**
 * Created by Administrator on 2018/10/29 0029.
 */

public interface IViewReply extends MvpView {

    void getEmailReply(String reply);
    void sendReply(String result);

}

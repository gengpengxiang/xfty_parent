package com.bj.hmxxparents.email.view;


import com.bj.hmxxparents.email.model.Letter;
import com.bj.hmxxparents.mvp.MvpView;

/**
 * Created by Administrator on 2018/10/29 0029.
 */

public interface IViewEmail extends MvpView {

    void getEmailList(String result);
    void delete(String result);

}

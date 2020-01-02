package com.bj.hmxxparents.comment.presenter;

import android.content.Context;
import android.util.Log;

import com.bj.hmxxparents.api.MLConfig;
import com.bj.hmxxparents.comment.view.IViewComment;
import com.bj.hmxxparents.email.view.IViewReply;
import com.bj.hmxxparents.mvp.Presenter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.bj.hmxxparents.api.HttpUtilService.BASE_API_URL;


public class CommentPresenter extends Presenter {

    private Context mContext;
    private IViewComment iView;

    public CommentPresenter(Context context, IViewComment iView) {
        this.mContext = context;
        this.iView = iView;
    }

    public void getCommentType(final String studentId,final String dianzan_type){

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) throws Exception {
                OkGo.<String>post(BASE_API_URL+"jz/sdznumber")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("dianzan_type",dianzan_type)
                        .params("studentid",studentId)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String str = response.body().toString();
                                Log.e("类型详情", str);

                                e.onNext(str);
                                e.onComplete();
                            }
                        });

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String reply) throws Exception {

                        iView.getCommentType(reply);
                    }
                });

    }

    public void getCommentList(final String studentId,final String liyouId,final String dianzanType,final int page){
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) throws Exception {
                OkGo.<String>post(BASE_API_URL+"jz/sdianzans")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("studentid",studentId)
                        .params("liyou",liyouId)
                        .params("dianzan_type",dianzanType)//"1"是点赞，"2"是待改进
                        .params("limit","10")
                        .params("offset",String.valueOf((page) *10))
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String str = response.body().toString();
                                Log.e("评论结果",str);
                                e.onNext(str);
                                e.onComplete();
                            }
                        });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        iView.getCommentList(s);
                    }
                });
    }

    public void thanks(final String dongtaiid){
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) throws Exception {
                OkGo.<String>post(BASE_API_URL+"jz/ganxie")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("dongtaiid",dongtaiid)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String str = response.body().toString();
                                Log.e("感谢结果",str);
                                e.onNext(str);
                                e.onComplete();
                            }
                        });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        iView.getThanksResult(s);
                    }
                });
    }


    @Override
    public void onDestory() {
        mContext = null;
        iView = null;
    }
}

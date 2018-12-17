package com.bj.hmxxparents.countryside.topic.presenter;

import android.content.Context;
import android.util.Log;

import com.bj.hmxxparents.api.MLConfig;
import com.bj.hmxxparents.countryside.topic.view.IViewTopic;
import com.bj.hmxxparents.countryside.topic.view.IViewTopicDetail;
import com.bj.hmxxparents.mvp.Presenter;
import com.bj.hmxxparents.wxapi.WXUtil;
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
import static com.bj.hmxxparents.api.HttpUtilService.BASE_URL;


public class TopicDetailPresenter extends Presenter {

    private Context mContext;
    private IViewTopicDetail iView;

    public TopicDetailPresenter(Context context, IViewTopicDetail iView) {
        this.mContext = context;
        this.iView = iView;
    }

    public void getTopicDetail(final String tianyuanid,final String userphone,final String usertype,final int page){

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) throws Exception {
                OkGo.<String>post(BASE_API_URL+"tianyuan/tianyuaninfo")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("tianyuanid",tianyuanid)
                        .params("userphone",userphone)
                        .params("usertype",usertype)
                        .params("limit","10")
                        .params("offset",String.valueOf((page) *10))
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String str = response.body().toString();
                                Log.e("最新动态详情", str);

                                e.onNext(str);
                                e.onComplete();
                            }
                        });

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String result) throws Exception {
                        iView.getTopicDetail(result);
                    }
                });

    }

    public void agree(final String tianyuanid, final String userphone, final String usertype) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                OkGo.<String>post(BASE_URL + "tianyuan/dianzan")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("tianyuanid", tianyuanid)
                        .params("userphone", userphone)
                        .params("usertype", usertype)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String str = response.body().toString();
                                Log.e("点赞结果返回数据", str);
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
                        iView.getAgreeResult(s);
                    }
                });
    }

    public void comment(final String tianyuanid, final String content,final String userphone, final String usertype) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                OkGo.<String>post(BASE_URL + "tianyuan/huifuset")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("tianyuanid", tianyuanid)
                        .params("content",content)
                        .params("userphone", userphone)
                        .params("usertype", usertype)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String str = response.body().toString();
                                Log.e("评论结果返回数据", str);
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
                        iView.sendCommentResult(s);
                    }
                });
    }

    public void share(final String tianyuanid, final String userphone, final String usertype) {
        Log.e("分享参数=","id="+tianyuanid+"phone="+userphone+"type="+usertype);
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                OkGo.<String>post(BASE_URL + "tianyuan/fenxiang")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("tianyuanid", tianyuanid)
                        .params("userphone", userphone)
                        .params("usertype", usertype)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String str = response.body().toString();
                                Log.e("动态分享返回数据", str);
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
                        iView.getShareResult(s);
//                        WXUtil.share(mContext,0,url,title,content,"");
                    }
                });
    }

    public void delete(final String tianyuanid) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                OkGo.<String>post(BASE_URL + "tianyuan/tianyuandel")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("tianyuan_id", tianyuanid)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String str = response.body().toString();
                                Log.e("动态删除返回数据", str);
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
                        iView.getDeleteResult(s);

                    }
                });
    }


    @Override
    public void onDestory() {
        mContext = null;
        iView = null;
    }
}

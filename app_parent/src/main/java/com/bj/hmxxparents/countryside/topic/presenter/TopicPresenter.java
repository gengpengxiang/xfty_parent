package com.bj.hmxxparents.countryside.topic.presenter;

import android.content.Context;
import android.util.Log;

import com.bj.hmxxparents.api.MLConfig;
import com.bj.hmxxparents.countryside.topic.view.IViewTopic;
import com.bj.hmxxparents.mvp.Presenter;
import com.bj.hmxxparents.wxapi.WXUtil;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.bj.hmxxparents.api.HttpUtilService.BASE_API_URL;
import static com.bj.hmxxparents.api.HttpUtilService.BASE_URL;


public class TopicPresenter extends Presenter {

    private Context mContext;
    private IViewTopic iView;

    public TopicPresenter(Context context, IViewTopic iView) {
        this.mContext = context;
        this.iView = iView;
    }

    public void getTopicList(final String userphone, final String usertype, final int page) {

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) throws Exception {
                OkGo.<String>post(BASE_API_URL + "tianyuan/tianyuanlist")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("userphone", userphone)
                        .params("usertype", usertype)
                        .params("limit", "10")
                        .params("offset", String.valueOf((page) * 10))
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

                        iView.getTopicList(result);
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

    public void browseAdd(final String tianyuanid, final String userphone, final String usertype) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                OkGo.<String>post(BASE_URL + "tianyuan/tianyuanpageview")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("tianyuanid", tianyuanid)
                        .params("userphone", userphone)
                        .params("usertype", usertype)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String str = response.body().toString();
                                Log.e("浏览次数返回数据", str);
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
                        //iView.getAgreeResult(s);
                    }
                });
    }

    public void share(final String tianyuanid, final String userphone, final String usertype) {
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

package com.bj.hmxxparents.huodong.presenter;

import android.content.Context;
import android.util.Log;

import com.bj.hmxxparents.api.MLConfig;
import com.bj.hmxxparents.countryside.topic.view.IViewTopic;
import com.bj.hmxxparents.huodong.view.IViewHuodongTopic;
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
import static com.bj.hmxxparents.api.HttpUtilService.BASE_URL;


public class HuodongTopicPresenter extends Presenter {

    private Context mContext;
    private IViewHuodongTopic iView;

    public HuodongTopicPresenter(Context context, IViewHuodongTopic iView) {
        this.mContext = context;
        this.iView = iView;
    }

    public void getHuodongInfo(final String  huodong_code) {

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) throws Exception {
                OkGo.<String>post(BASE_API_URL+"jzhuodong/info")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("code",huodong_code)
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
                    public void accept(String result) throws Exception {
                        iView.getHuodongInfo(result);
                    }
                });

    }

    public void getTopicList(final String userphone, final String usertype, final int page,final String huodong_code,final String  suyang_code) {

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) throws Exception {
                OkGo.<String>post(BASE_API_URL + "tianyuan/tianyuanlist")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("userphone", userphone)
                        .params("usertype", usertype)
                        .params("limit", "5")
                        .params("offset", String.valueOf((page) * 5))
                        .params("huodong","1")
                        .params("huodong_code",huodong_code)
                        .params("suyang_code", suyang_code)
                        //.params("student_classcode",student_classcode)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String str = response.body().toString();
                                Log.e("活动动态详情", str);

                                e.onNext(str);
                                e.onComplete();
                            }

                            @Override
                            public void onError(Response<String> response) {
                                super.onError(response);

                                e.onNext("error");
                                e.onComplete();

                                iView.getTopicList("error");

                                Log.e("网络错误","error="+e.toString());
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

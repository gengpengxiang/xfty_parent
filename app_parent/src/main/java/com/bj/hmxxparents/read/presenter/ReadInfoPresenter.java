package com.bj.hmxxparents.read.presenter;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bj.hmxxparents.api.MLConfig;
import com.bj.hmxxparents.pet.model.PetInfo;
import com.bj.hmxxparents.pet.view.IViewPet;
import com.bj.hmxxparents.read.view.IViewRead;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.bj.hmxxparents.api.HttpUtilService.BASE_URL;

/**
 * Created by Administrator on 2018/7/24 0024.
 */

public class ReadInfoPresenter {
    private IViewRead iView;
    private Context context;

    public ReadInfoPresenter(IViewRead iView, Context context) {
        this.iView = iView;
        this.context = context;
    }

    public void getReadInfo(String student_code,String map_code) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                OkGo.<String>post(BASE_URL + "index.php/jz_yuedu/index")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("student_code", student_code)
                        .params("map_code",map_code)
                        .cacheKey("readInfo")
                        .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String str = response.body().toString();
                                Log.e("阅读信息", str);
                                e.onNext(str);
                                e.onComplete();
                            }
                        });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String result) {

                        iView.getReadInfo(result);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void sign(String student_code,String title,String num,String time) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {

                Log.e("签到结果222", "123");
                OkGo.<String>post(BASE_URL + "index.php/jz_yuedu/qiandao")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("student_code", student_code)
                        .params("title",title)
                        .params("num",num)
                        .params("time",time)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String str = response.body().toString();
                                Log.e("签到结果", str);
                                e.onNext(str);
                                e.onComplete();
                            }
                        });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String result) {
                        iView.getSignResult(result);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void onDestroy() {
        iView = null;
        context = null;
    }
}

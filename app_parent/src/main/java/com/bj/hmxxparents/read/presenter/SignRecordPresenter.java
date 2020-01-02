package com.bj.hmxxparents.read.presenter;

import android.content.Context;
import android.util.Log;

import com.bj.hmxxparents.api.MLConfig;
import com.bj.hmxxparents.read.view.IViewRead;
import com.bj.hmxxparents.read.view.IViewSignRecord;
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

public class SignRecordPresenter {
    private IViewSignRecord iView;
    private Context context;

    public SignRecordPresenter(IViewSignRecord iView, Context context) {
        this.iView = iView;
        this.context = context;
    }

    public void getSignRecord(String student_code,String qiandao_date) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                OkGo.<String>post(BASE_URL + "index.php/jz_yuedu/qiandaojilu")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("student_code", student_code)
                        .params("qiandao_date",qiandao_date)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String str = response.body().toString();
                                Log.e("签到记录", str);
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

                        iView.getSignRecord(result);
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

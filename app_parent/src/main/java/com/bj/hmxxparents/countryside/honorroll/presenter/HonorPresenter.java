package com.bj.hmxxparents.countryside.honorroll.presenter;

import android.content.Context;
import android.util.Log;

import com.bj.hmxxparents.api.MLConfig;
import com.bj.hmxxparents.countryside.honorroll.view.IViewHonor;
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


public class HonorPresenter extends Presenter {

    private Context mContext;
    private IViewHonor iView;

    public HonorPresenter(Context context, IViewHonor iView) {
        this.mContext = context;
        this.iView = iView;
    }

    public void getHonorList(final String class_code,final int page){

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) throws Exception {
                OkGo.<String>post(BASE_API_URL+"grbang/hzbang")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("class_code",class_code)
                        .params("limit","10")
                        .params("offset",String.valueOf((page) *10))
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String str = response.body().toString();
                                Log.e("荣誉榜详情", str);

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

                        iView.getHonorList(result);
                    }
                });

    }


    @Override
    public void onDestory() {
        mContext = null;
        iView = null;
    }
}

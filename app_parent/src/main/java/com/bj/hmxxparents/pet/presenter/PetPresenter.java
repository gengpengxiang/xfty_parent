package com.bj.hmxxparents.pet.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bj.hmxxparents.api.MLConfig;
import com.bj.hmxxparents.pet.model.PetInfo;
import com.bj.hmxxparents.pet.view.IViewPet;
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

public class PetPresenter {
    private IViewPet iView;
    private Context context;

    public PetPresenter(IViewPet iView, Context context) {
        this.iView = iView;
        this.context = context;
    }

    public void getAllPets(String studentcode) {
        Observable.create(new ObservableOnSubscribe<PetInfo>() {
            @Override
            public void subscribe(ObservableEmitter<PetInfo> e) throws Exception {
                OkGo.<String>post(BASE_URL + "index.php/jz/chongwulist")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("studentcode", studentcode)
                        .cacheKey("petsList")
                        .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String str = response.body().toString();
                                PetInfo petInfo = JSON.parseObject(str, new TypeReference<PetInfo>() {
                                });
                                e.onNext(petInfo);
                                e.onComplete();
                            }
                        });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PetInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(PetInfo petInfo) {
                        iView.getAllPets(petInfo);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void replacePet(String studentcode, String petid) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                OkGo.<String>post(BASE_URL + "index.php/jz/setchongwu")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("studentcode", studentcode)
                        .params("chongwu_id", petid)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String str = response.body().toString();
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
                    public void onNext(String s) {
                        JSONObject json = JSON.parseObject(s);
                        String ret = (String) json.get("ret");
                        if(ret.equals("1")){
                            iView.replacePet();
                        }
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

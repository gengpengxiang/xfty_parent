package com.bj.hmxxparents.countryside.topic.presenter;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bj.hmxxparents.api.MLConfig;
import com.bj.hmxxparents.countryside.topic.view.IViewTopic;
import com.bj.hmxxparents.countryside.topic.view.IViewTopicPublish;
import com.bj.hmxxparents.mvp.Presenter;
import com.bj.hmxxparents.utils.FileSizeUtil;
import com.bj.hmxxparents.utils.PicUtils;
import com.bj.hmxxparents.widget.LoadingDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static anet.channel.util.Utils.context;
import static com.bj.hmxxparents.api.HttpUtilService.BASE_API_URL;
import static com.bj.hmxxparents.api.HttpUtilService.BASE_URL;


public class TopicPublishPresenter extends Presenter {

    private Context mContext;
    private IViewTopicPublish iView;

    public TopicPublishPresenter(Context context, IViewTopicPublish iView) {
        this.mContext = context;
        this.iView = iView;
    }

    public void uploadPic(final ArrayList<String> pathList) {

        Observable observable = Observable.fromIterable(pathList);

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
            }
            @Override
            //观察者接收到通知,进行相关操作
            public void onNext(final String aLong) {
                Log.e("源文件路径", aLong);
                Luban.with(context)
                        .load(aLong)//传入原图
                        .ignoreBy(100)//不压缩的阈值，单位为K
                        .putGear(4)
                        .setTargetDir("/storage/emulated/0/Android/data/")
                        .setCompressListener(new OnCompressListener() {//压缩回调接口
                            @Override
                            public void onStart() {
                            }
                            @Override
                            public void onSuccess(final File file) {

                                String size = null;
                                try {
                                    size = FileSizeUtil.FormetFileSize(FileSizeUtil.getFileSize(file));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                long time = System.currentTimeMillis();
                                //String newPath2 = PicUtils.renameFile(file.getPath(), file.getPath().substring(0, file.getPath().lastIndexOf("/") + 1) + time+".jpg");

                               // String newPath3 = file.getPath().substring(0, file.getPath().lastIndexOf("/") + 1) + time+".jpg";
                                String newPath3 = file.getPath().substring(0, file.getPath().lastIndexOf("/") + 1)+time+".jpg";

                                if(!PicUtils.isJpgFile(file)){
                                    PicUtils.convertToJpg(file.getPath(),newPath3);
                                    File newFile = new File(newPath3);
                                    Log.e("转化后3",newFile.getPath());
                                    OkGo.<String>post(BASE_URL + "tianyuan_ex/tianyuanimg")
                                            .params("appkey", MLConfig.HTTP_APP_KEY)
                                            .params("userfile", newFile)
                                            .execute(new StringCallback() {
                                                @Override
                                                public void onSuccess(Response<String> response) {

                                                    String str = response.body().toString();
                                                    Log.e("图片上传结果==",str);
                                                    iView.getUploadResult(str);

                                                }
                                            });
                                }else {
                                    OkGo.<String>post(BASE_URL + "tianyuan_ex/tianyuanimg")
                                            .params("appkey", MLConfig.HTTP_APP_KEY)
                                            .params("userfile", file)
                                            .execute(new StringCallback() {
                                                @Override
                                                public void onSuccess(Response<String> response) {

                                                    String str = response.body().toString();
                                                    Log.e("图片上传结果==",str);
                                                    iView.getUploadResult(str);

                                                }
                                            });
                                }
                            }
                            @Override
                            public void onError(Throwable e) {
                            }
                        })
                        .launch();
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
                Log.e("完成","complete");
            }
        };
        observable.subscribe(observer);

    }

    public void publishTopic(final String userphone,final String tianyuan_content,final String tianyuan_img,final String huodong_code,final String suyang_code){

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
//                OkGo.<String>post(BASE_URL + "tianyuan/tianyuanadd")
//                        .params("appkey", MLConfig.HTTP_APP_KEY)
//                        .params("userphone",userphone)
//                        .params("tianyuan_content",tianyuan_content)
//                        .params("tianyuan_img",tianyuan_img)
//                        .execute(new StringCallback() {
//                            @Override
//                            public void onSuccess(Response<String> response) {
//
//                                String str = response.body().toString();
//                                Log.e("发布动态结果",str);
//                                e.onNext(str);
//                                e.onComplete();
//
//                            }
//                        });
                OkGo.<String>post(BASE_URL + "tianyuan/tianyuanadd")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("userphone",userphone)
                        .params("tianyuan_content",tianyuan_content)
                        .params("tianyuan_img",tianyuan_img)
                        .params("huodong","1")
                        .params("huodong_code",huodong_code)
                        .params("suyang_code",suyang_code)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {

                                String str = response.body().toString();
                                Log.e("发布活动动态结果",str);
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
                        iView.getPublishResult(s);
                    }
                });
    }


    @Override
    public void onDestory() {
        mContext = null;
        iView = null;

    }
}

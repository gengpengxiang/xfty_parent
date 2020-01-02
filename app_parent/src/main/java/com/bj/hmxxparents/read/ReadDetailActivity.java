package com.bj.hmxxparents.read;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bj.hmxxparents.BaseActivity;
import com.bj.hmxxparents.R;
import com.bj.hmxxparents.api.MLConfig;
import com.bj.hmxxparents.read.model.CityPicture;
import com.bj.hmxxparents.utils.ScreenUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.signature.StringSignature;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.shizhefei.view.largeimage.LargeImageView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.autosize.internal.CustomAdapt;

import static com.bj.hmxxparents.api.HttpUtilService.BASE_URL;

public class ReadDetailActivity extends BaseActivity implements CustomAdapt {

    @BindView(R.id.bt_close)
    ImageView btClose;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;

    @BindView(R.id.bt_left)
    ImageView btLeft;
    @BindView(R.id.bt_right)
    ImageView btRight;
    @BindView(R.id.blackView)
    View blackView;
    private Unbinder unbinder;

    private List<String> urlList = new ArrayList<>();

    private String code;
    private PagerAdapter mPagerAdapter;

    int pageIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏

        setContentView(R.layout.activity_read_detail);
        unbinder = ButterKnife.bind(this);

        code = getIntent().getStringExtra("code");

        initViews();

        getPictureUrls();


    }


    private void getPictureUrls() {
        int width = ScreenUtils.getScreenWidth(this);
        int height = ScreenUtils.getScreenHeight(this);
        Log.e("宽度==", width + "高度=" + height + "code=" + code);

        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        String result = numberFormat.format((float) height / (float) width);
        Log.e("宽高比=", result);

        //因为是横屏，宽高相反
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                OkGo.<String>post(BASE_URL + "index.php/jz_yuedu/chengshi")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("code", code)
                        .params("os", "android")
                        .params("width", String.valueOf(height))
                        .params("height", String.valueOf(width))
                        .params("wh", result)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String str = response.body().toString();
                                Log.e("图片信息", str);
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
                        urlList.clear();

                        try {
                            CityPicture cityPicture = JSON.parseObject(s, new TypeReference<CityPicture>() {
                            });
                            if (cityPicture.getRet().equals("1")) {
                                urlList.addAll(cityPicture.getData());

                                mPagerAdapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {

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

    private void initViews() {

        btLeft.setVisibility(View.INVISIBLE);


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                pageIndex = position;
                if (pageIndex == (urlList.size() - 1)) {
                    btRight.setVisibility(View.INVISIBLE);
                } else {
                    btRight.setVisibility(View.VISIBLE);
                }
                if (pageIndex == 0) {
                    btLeft.setVisibility(View.INVISIBLE);
                } else {
                    btLeft.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //数据适配器
        mPagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return urlList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = LayoutInflater.from(ReadDetailActivity.this).inflate(R.layout.layout_read_detail, container, false);
//                ImageView imageView = (ImageView) view.findViewById(R.id.iv);
//                imageView.setImageResource(mBitmapIds[position]);

                LargeImageView largeImageView = (LargeImageView) view.findViewById(R.id.mLargeImageView);
                Glide.with(ReadDetailActivity.this).load(urlList.get(position)).asBitmap().signature(new StringSignature("01")).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {


                        int width = ScreenUtils.getScreenWidth(ReadDetailActivity.this);
                        int height = ScreenUtils.getScreenHeight(ReadDetailActivity.this);


                        ViewGroup.LayoutParams para = largeImageView.getLayoutParams();
                        para.height = height;
                        para.width = width;

                        largeImageView.setImage(resource);


                    }
                }); //方法中设置asBitmap可以设置回调类型
                largeImageView.setEnabled(true);
                largeImageView.setCriticalScaleValueHook(new LargeImageView.CriticalScaleValueHook() {
                    @Override
                    public float getMinScale(LargeImageView largeImageView, int imageWidth, int imageHeight, float suggestMinScale) {
                        return 1;
                    }

                    @Override
                    public float getMaxScale(LargeImageView largeImageView, int imageWidth, int imageHeight, float suggestMaxScale) {
                        return 2;
                    }
                });


                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        };


        //绑定适配器
        mViewPager.setAdapter(mPagerAdapter);


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // 当新设置中，屏幕布局模式为横排时
        if (newConfig.orientation == 1) {
            blackView.setVisibility(View.VISIBLE);
        }
        if (newConfig.orientation == 2) {
            blackView.setVisibility(View.GONE);
        }
        super.onConfigurationChanged(newConfig);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick(R.id.bt_close)
    public void onClick() {
        setResult(2);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(2);
            //return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean isBaseOnWidth() {
        return true;
    }

    @Override
    public float getSizeInDp() {
        return 640;
    }

    @OnClick({R.id.bt_left, R.id.bt_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_left:

                if (pageIndex > 0) {
                    mViewPager.setCurrentItem(pageIndex - 1, true);
                }

                break;
            case R.id.bt_right:

                if (pageIndex < urlList.size() - 1) {
                    mViewPager.setCurrentItem(pageIndex + 1, true);
                }

                break;
        }
    }
}

package com.bj.hmxxparents.read.utils;

import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.bj.hmxxparents.utils.ScreenUtils;


/**
 * Created by JarvisLau on 2018/5/29.
 * Description :
 */

public class ScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener/*, GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener */ {

    private View targetView;
    private float scale = 1;
    private float scaleTemp = 1;

    private boolean isFullGroup = false;

    private ViewGroup viewGroup;

    ScaleGestureListener(View targetView, ViewGroup viewGroup) {
        this.targetView = targetView;
        this.viewGroup = viewGroup;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        scale = scaleTemp * detector.getScaleFactor();

        if(scale<1){

            scale = 1.0f;
            targetView.setScaleX(1);
            targetView.setScaleY(1);
            fullGroup();

        } if(scale>2){
            scale = 2.0f;
            targetView.setScaleX(2);
            targetView.setScaleY(2);
        }else {
            targetView.setScaleX(scale);
            targetView.setScaleY(scale);
        }

       // Log.e("缩放",scale+"");
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        scaleTemp = scale;
    }



    float getScale() {
        return scale;
    }

    public boolean isFullGroup() {
        return isFullGroup;
    }

    void setFullGroup(boolean fullGroup) {
        isFullGroup = fullGroup;
    }

    void onActionUp() {
        if (isFullGroup && scaleTemp < 1) {
            scale = 1;
            targetView.setScaleX(scale);
            targetView.setScaleY(scale);
            scaleTemp = scale;
        }
    }

    private void fullGroup() {
        targetView.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        targetView.getViewTreeObserver().removeOnPreDrawListener(this);
                        float viewWidth = targetView.getWidth();
                        float viewHeight = targetView.getHeight();
                        float groupWidth = viewGroup.getWidth();
                        float groupHeight = viewGroup.getHeight();
                        ViewGroup.LayoutParams layoutParams = targetView.getLayoutParams();
                        float widthFactor = groupWidth / viewWidth;
                        float heightFactor = groupHeight / viewHeight;
//                        if (viewWidth < groupWidth && widthFactor * viewHeight <= groupHeight) {
//                            layoutParams.width = (int) groupWidth;
//                            layoutParams.height = (int) (widthFactor * viewHeight);
//                        } else if (viewHeight < groupHeight && heightFactor * viewWidth <= groupWidth) {
//                            layoutParams.height = (int) groupHeight;
//                            layoutParams.width = (int) (heightFactor * viewWidth);
//                        }

                        layoutParams.height = ScreenUtils.getScreenHeight(viewGroup.getContext());
                        layoutParams.width = ScreenUtils.getScreenWidth(viewGroup.getContext());

                        targetView.setLayoutParams(layoutParams);
                        return true;
                    }
                });
    }
}
package com.bj.hmxxparents.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Created by nercdev on 2017/1/16.
 */

public class LegendGridLayoutManager extends GridLayoutManager {
    private int mMaxLines = 0;

    public LegendGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public LegendGridLayoutManager(Context context, int spanCount, int maxLines) {
        super(context, spanCount);
        mMaxLines = maxLines;
    }

    public LegendGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        int height = 0;
        Log.i("way", "onMeasure---MeasureSpec-" + View.MeasureSpec.getSize(heightSpec));
        int childCount = getItemCount();
        if (mMaxLines > 0) {
            childCount = Math.min(childCount, mMaxLines * getSpanCount());
        }
        for (int i = 0; i < childCount; i++) {
            View child = recycler.getViewForPosition(i);
            measureChildWithMargins(child, 0, 0);
            if (i % getSpanCount() == 0) {
                //把宽高拿到，宽高都是包含ItemDecorate的尺寸
                int measuredHeight = getDecoratedMeasuredHeight(child);
                height += measuredHeight;
            }
        }
        Log.i("way", "onMeasure---height-" + height);
        setMeasuredDimension(View.MeasureSpec.getSize(widthSpec), height);
    }
}

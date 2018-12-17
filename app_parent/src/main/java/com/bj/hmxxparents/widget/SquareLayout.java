package com.bj.hmxxparents.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.zhy.autolayout.utils.AutoLayoutHelper;

/**
 * Created by he on 2016/12/20.
 */

public class SquareLayout extends FrameLayout {

    private final AutoLayoutHelper mHelper = new AutoLayoutHelper(this);

    public SquareLayout(Context context) {
        super(context);
    }

    public SquareLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!isInEditMode()) {
            mHelper.adjustChildren();
        }
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}

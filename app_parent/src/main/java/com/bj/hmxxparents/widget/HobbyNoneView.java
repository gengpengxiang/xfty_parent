package com.bj.hmxxparents.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Checkable;

/**
 * Created by zz379 on 2017/5/6.
 */

public class HobbyNoneView extends android.support.v7.widget.AppCompatTextView implements Checkable {

    private boolean isChecked;
    private static final int[] CHECK_STATE = new int[]{android.R.attr.state_checked};

    public HobbyNoneView(Context context) {
        super(context);
        this.isChecked = false;

    }

    public HobbyNoneView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.isChecked = false;
    }

    public HobbyNoneView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.isChecked = false;
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        int[] states = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(states, CHECK_STATE);
        }
        return states;
    }

    @Override
    public void setChecked(boolean checked) {
        if (this.isChecked != checked) {
            this.isChecked = checked;
            refreshDrawableState();
        }
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked);
    }
}

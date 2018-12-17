package com.douhao.game.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by zz379 on 2017/5/31.
 */

public class TianzigeViewShare extends AppCompatTextView {

    private static final String TAG = "TianzigeView";

    protected Paint linePaint1; // 边框画笔
    protected Paint linePaint2; // 米字线画笔
    protected Paint bgPaint;
    protected Paint textPaint;  // 文字画笔
    protected int tzgWidth;     // 每个汉字的宽度

    public TianzigeViewShare(Context context) {
        super(context);
        init();
    }

    public TianzigeViewShare(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TianzigeViewShare(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    protected void init() {
        linePaint1 = new Paint();
        linePaint1.setAntiAlias(true);
        linePaint1.setStyle(Paint.Style.STROKE);
        linePaint1.setColor(Color.parseColor("#4aa003"));
        linePaint1.setStrokeWidth(3f);
        DashPathEffect effects = new DashPathEffect(new float[]{30, 15, 30, 15}, 15);
        linePaint1.setPathEffect(effects);

        linePaint2 = new Paint();
        linePaint2.setAntiAlias(true);
        linePaint2.setStyle(Paint.Style.STROKE);
        linePaint2.setColor(Color.parseColor("#F3AFA0"));
        linePaint2.setStrokeWidth(3f);
        linePaint2.setPathEffect(effects);

        bgPaint = new Paint();
        bgPaint.setColor(Color.parseColor("#FFFFFF"));

        textPaint = new Paint();
        textPaint.setStrokeWidth(3);
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setStrokeCap(Paint.Cap.ROUND);
        textPaint.setStrokeJoin(Paint.Join.ROUND);
        textPaint.setColor(Color.parseColor("#4aa003"));
        textPaint.setTextAlign(Paint.Align.LEFT);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i(TAG, "宽和高：" + getMeasuredWidth() + " : " + getMeasuredHeight());
        tzgWidth = (getMeasuredWidth() - getPaddingLeft() - getPaddingRight()) / 2;
        textPaint.setTextSize(tzgWidth / 4 * 3);
        String textString = getText().toString().trim();
        if (textString == null || "".equals(textString) || textString.length() > 4) {
            Log.i(TAG, "没有输入汉字或者汉字个数超过4个");
            return;
        }
        int textCounts = textString.length();
        for (int i = 0; i < textString.length(); i++) {
            if (i == 0) {
                drawFirst(canvas, textCounts, String.valueOf(textString.charAt(i)));
            } else if (i == 1) {
                drawSecond(canvas, textCounts, String.valueOf(textString.charAt(i)));
            } else if (i == 2) {
                drawThird(canvas, textCounts, String.valueOf(textString.charAt(i)));
            } else if (i == 3) {
                drawFourth(canvas, textCounts, String.valueOf(textString.charAt(i)));
            } else {
                // 超过4个字
                Log.i(TAG, "超过4个汉字，暂时不适配");
                throw new IndexOutOfBoundsException("超过4个汉字，暂时不适配");
            }
        }
    }

    private void drawFirst(Canvas canvas, int textCounts, String text) {
        if (textCounts == 1) {
            drawTianzige(canvas, getMeasuredWidth() / 4, getPaddingTop(), 1, textCounts);
            drawHanzi(canvas, getMeasuredWidth() / 4, getPaddingTop(), text);
        } else {
            drawTianzige(canvas, getPaddingLeft(), getPaddingTop(), 1, textCounts);
            drawHanzi(canvas, getPaddingLeft(), getPaddingTop(), text);
        }
    }

    private void drawSecond(Canvas canvas, int textCounts, String text) {
        drawTianzige(canvas, getMeasuredWidth() / 2, getPaddingTop(), 2, textCounts);
        drawHanzi(canvas, getMeasuredWidth() / 2, getPaddingTop(), text);
    }

    private void drawThird(Canvas canvas, int textCounts, String text) {
        if (textCounts == 3) {
            drawTianzige(canvas, getMeasuredWidth() / 4, getMeasuredWidth() / 2, 3, textCounts);
            drawHanzi(canvas, getMeasuredWidth() / 4, getMeasuredWidth() / 2, text);
        } else {
            drawTianzige(canvas, getPaddingLeft(), getMeasuredWidth() / 2, 3, textCounts);
            drawHanzi(canvas, getPaddingLeft(), getMeasuredWidth() / 2, text);
        }
    }

    private void drawFourth(Canvas canvas, int textCounts, String text) {
        drawTianzige(canvas, getMeasuredWidth() / 2, getMeasuredWidth() / 2, 4, textCounts);
        drawHanzi(canvas, getMeasuredWidth() / 2, getMeasuredWidth() / 2, text);
    }

    /**
     * 画田字格
     *
     * @param canvas
     * @param x
     * @param y
     */
    private void drawTianzige(Canvas canvas, float x, float y, int index, int textCount) {
        // 设置白色背景
        canvas.drawRect(x, y, x + tzgWidth, y + tzgWidth, bgPaint);
        // 画四条边
        if (textCount == 3 && index == 3) {
            canvas.drawLine(getPaddingLeft(), getMeasuredWidth() / 2,
                    getPaddingLeft() + tzgWidth * 2, getMeasuredWidth() / 2, linePaint1);
        } else {
            canvas.drawLine(x, y, x + tzgWidth, y, linePaint1);
        }
        canvas.drawLine(x, y, x, y + tzgWidth, linePaint1);
        if (textCount > 2 && index <= 2) {
            // 不画底部的线
        } else {
            canvas.drawLine(x, y + tzgWidth, x + tzgWidth, y + tzgWidth, linePaint1);
        }
        canvas.drawLine(x + tzgWidth, y, x + tzgWidth, y + tzgWidth, linePaint1);
        // 画米字线
        canvas.drawLine(x, y, x + tzgWidth, y + tzgWidth, linePaint2);
        canvas.drawLine(x, y + tzgWidth, x + tzgWidth, y, linePaint2);
        canvas.drawLine(x, y + tzgWidth / 2, x + tzgWidth, y + tzgWidth / 2, linePaint2);
        canvas.drawLine(x + tzgWidth / 2, y, x + tzgWidth / 2, y + tzgWidth, linePaint2);
    }

    /**
     * 画汉字
     *
     * @param canvas
     * @param x
     * @param y
     * @param text
     */
    private void drawHanzi(Canvas canvas, float x, float y, String text) {
        Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        int baseline = tzgWidth / 2 + (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent;
        canvas.drawText(text, x + (tzgWidth / 2 - bounds.width() / 2), y + baseline, textPaint);
    }
}

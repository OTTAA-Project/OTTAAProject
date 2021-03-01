package com.stonefacesoft.ottaa.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.stonefacesoft.ottaa.R;

public class CircularProgressBar extends View {

    /**
     * Start the progress at 12 o'clock
     */
    private final int startAngle = -90;
    /**
     * ProgressBar's line thickness
     */
    private float strokeWidth = 4;
    private float progress = 0;
    private int min = 0;
    private int max = 1000;
    private int radius = 10;
    private int color = Color.DKGRAY;
    private RectF rectF;
    private Paint backgroundPaint;
    private Paint foregroundPaint;

    public CircularProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        rectF = new RectF();
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.CircleProgressBar,
                0, 0);

        //Reading values from the XML layout
        try {
            strokeWidth = typedArray.getDimension(R.styleable.CircleProgressBar_progressBarThickness, strokeWidth);
            progress = typedArray.getFloat(R.styleable.CircleProgressBar_progress, progress);
            color = typedArray.getInt(R.styleable.CircleProgressBar_progressbarColor, color);
            min = typedArray.getInt(R.styleable.CircleProgressBar_min, min);
            max = typedArray.getInt(R.styleable.CircleProgressBar_max, max);
            radius = typedArray.getInt(R.styleable.CircleProgressBar_radius, radius);
        } finally {
            typedArray.recycle();
        }

        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(adjustAlpha(color, 0.3f));
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(strokeWidth);

        foregroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        foregroundPaint.setColor(color);
        foregroundPaint.setStyle(Paint.Style.STROKE);
        foregroundPaint.setStrokeWidth(strokeWidth);
    }

    private int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int min = Math.min(width, height);
        setMeasuredDimension(min, min);
        rectF.set(0 + strokeWidth / 2, 0 + strokeWidth / 2, min - strokeWidth / 2, min - strokeWidth / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawOval(rectF, backgroundPaint);
        float angle = 360 * progress / max;
        canvas.drawArc(rectF, startAngle, angle, false, foregroundPaint);

    }

    public void setProgress(float progress) {
        this.progress = progress * 10;
        invalidate();// Notify the view to redraw it self (the onDraw method is called)
    }
}
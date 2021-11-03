package com.stonefacesoft.ottaa.Games;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;


/**
 *
 * https://stackoverflow.com/questions/51624500/android-graphics-draw-a-line-from-one-view-pointing-to-another-view
 *
 */
public class MatchPicto extends RelativeLayout {

    private static final String TAG = "MatchPicto";
        public static final String PROPERTY_X = "PROPERTY_X";
        public static final String PROPERTY_Y = "PROPERTY_Y";

        private final static double ARROW_ANGLE = Math.PI / 8;
        private final static double ARROW_SIZE = 250;

        private Paint mPaint;

        private boolean mDrawArrow = false;
        private final Point mPointFrom = new Point();   // current (during animation) arrow start point
        private final Point mPointTo = new Point();     // current (during animation)  arrow end point

        public MatchPicto(Context context) {
            super(context);
            init();
        }

        public MatchPicto(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public MatchPicto(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        public MatchPicto(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
            init();
        }

        private void init() {
            setWillNotDraw(false);
            mPaint = new Paint();
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setAntiAlias(true);
            mPaint.setColor(Color.GREEN);
            mPaint.setStrokeWidth(55);
        }

        @Override
        public void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
            canvas.save();
            if (mDrawArrow) {
                drawArrowLines(mPointFrom, mPointTo, canvas);
            }
            canvas.restore();
        }

        private Point calcPointFrom(Rect fromViewBounds, Rect toViewBounds) {
            Point pointFrom = new Point();
            pointFrom.x = fromViewBounds.centerX();
            pointFrom.y = fromViewBounds.top;
            Log.d(TAG, "calcPointFrom: X "+pointFrom.x+ " Y "+pointFrom.y );
            return pointFrom;
        }


        private Point calcPointTo(Rect fromViewBounds, Rect toViewBounds) {
            Point pointTo = new Point();
            pointTo.x = toViewBounds.centerX();
            pointTo.y = toViewBounds.bottom - 50;
            Log.d(TAG, "calcPointTo: X "+pointTo.x+ " Y "+pointTo.y );
            return pointTo;
        }


        private void drawArrowLines(Point pointFrom, Point pointTo, Canvas canvas) {
            canvas.drawLine(pointFrom.x, pointFrom.y, pointTo.x, pointTo.y, mPaint);

            double angle = Math.atan2(pointTo.y - pointFrom.y, pointTo.x - pointFrom.x);

            int arrowX, arrowY;

            //arrowX = (int) (pointTo.x - ARROW_SIZE * Math.cos(angle + ARROW_ANGLE));
            //arrowY = (int) (pointTo.y - ARROW_SIZE * Math.sin(angle + ARROW_ANGLE));
            //canvas.drawLine(pointTo.x, pointTo.y, arrowX, arrowY, mPaint);

            //arrowX = (int) (pointTo.x - ARROW_SIZE * Math.cos(angle - ARROW_ANGLE));
            //arrowY = (int) (pointTo.y - ARROW_SIZE * Math.sin(angle - ARROW_ANGLE));
            //canvas.drawLine(pointTo.x, pointTo.y, arrowX, arrowY, mPaint);
        }

        public void animateArrows(ViewGroup from, ViewGroup to, int duration) {
            mDrawArrow = true;


            // find from and to views bounds
            Rect fromViewBounds = new Rect();
            from.getGlobalVisibleRect(fromViewBounds);
            Log.d(TAG, "animateArrows: RectFROM: centerX: "+fromViewBounds.centerX() +" RectFROM: centerY: "+fromViewBounds.centerY());
            //offsetDescendantRectToMyCoords(fromView, fromViewBounds);

            Rect toViewBounds = new Rect();
            to.getGlobalVisibleRect(toViewBounds);
            Log.d(TAG, "animateArrows: RectTO: centerX: "+toViewBounds.centerX() +" RectTO: centerY: "+toViewBounds.centerY());
            //offsetDescendantRectToMyCoords(toView, toViewBounds);

            // calculate arrow sbegin and end points
            Point pointFrom = calcPointFrom(fromViewBounds, toViewBounds);
            Point pointTo = calcPointTo(fromViewBounds, toViewBounds);

            ValueAnimator arrowAnimator = createArrowAnimator(pointFrom, pointTo, duration);
            arrowAnimator.start();
        }

        private ValueAnimator createArrowAnimator(Point pointFrom, Point pointTo, int duration) {

            final double angle = Math.atan2(pointTo.y - pointFrom.y, pointTo.x - pointFrom.x);

            mPointFrom.x = pointFrom.x;
            mPointFrom.y = pointFrom.y;

            //int firstX = (int) (pointFrom.x + ARROW_SIZE * Math.cos(angle));
            //int firstY = (int) (pointFrom.y + ARROW_SIZE * Math.sin(angle));
            int firstX = pointFrom.x;
            int firstY = pointFrom.y;

            PropertyValuesHolder propertyX = PropertyValuesHolder.ofInt(PROPERTY_X, firstX, pointTo.x);
            PropertyValuesHolder propertyY = PropertyValuesHolder.ofInt(PROPERTY_Y, firstY, pointTo.y);

            ValueAnimator animator = new ValueAnimator();
            animator.setValues(propertyX, propertyY);
            animator.setDuration(duration);
            // set other interpolator (if needed) here:
            animator.setInterpolator(new AccelerateDecelerateInterpolator());

            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    mPointTo.x = (int) valueAnimator.getAnimatedValue(PROPERTY_X);
                    mPointTo.y = (int) valueAnimator.getAnimatedValue(PROPERTY_Y);

                    invalidate();
                }
            });

            return animator;
        }
    }

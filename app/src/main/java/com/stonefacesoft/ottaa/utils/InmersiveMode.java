package com.stonefacesoft.ottaa.utils;


import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class InmersiveMode implements View.OnTouchListener {
    private static final String TAG = "InmersiveMode";
    public static final int NOACTIONBAR = 141;
    public static final int WITHACTIONBAR = 142;

    private final View decorView;
    private final AppCompatActivity mActivity;
    private boolean immersive;
    private final Handler handler;
    private final GestureDetector detector;

    public InmersiveMode(AppCompatActivity appCompatActivity){
        this.mActivity=appCompatActivity;
        this.handler=new Handler();
        this.decorView = this.mActivity.getWindow().getDecorView();
        hideUI();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    immersive = false;
                    handler.postDelayed(autoHideRunner, 5000);
                }
                else{
                    immersive = true;
                }
            }
        });

        detector=new GestureDetector(mActivity,new GestureListener());

        decorView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                detector.onTouchEvent(motionEvent);
                return false;
            }
        });
    }

    private void hideUI() {
        immersive = true;
        if (mActivity.getSupportActionBar() != null)
            mActivity.getSupportActionBar().hide();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public void toggleState() {
        if (immersive) {
            showUI();
        } else {
            hideUI();
        }
    }

    private void showUI() {
        immersive = false;
        if (mActivity.getSupportActionBar() != null)
            mActivity.getSupportActionBar().show();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

    }

    private final Runnable autoHideRunner = new Runnable() {
        @Override
        public void run() {
            hideUI();
        }
    };

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                        result = true;
                    }
                }
                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                    result = true;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    public void onSwipeRight() {
    }

    public void onSwipeLeft() {
    }

    public void onSwipeTop() {

    }

    public void onSwipeBottom() {
        toggleState();
    }
}

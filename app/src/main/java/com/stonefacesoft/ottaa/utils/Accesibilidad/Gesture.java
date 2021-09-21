package com.stonefacesoft.ottaa.utils.Accesibilidad;

import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.drawerlayout.widget.DrawerLayout;

public class Gesture extends GestureDetector.SimpleOnGestureListener {
    private final DrawerLayout drawer;
    public Gesture(DrawerLayout drawerLayout){
        this.drawer=drawerLayout;
    }
    @Override
    public boolean onDown(MotionEvent e) {
        return super.onDown(e);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return super.onSingleTapConfirmed(e);
    }

    @Override
    public void onLongPress(MotionEvent e) {
        super.onLongPress(e);
    }



    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        boolean result=false;

        return result;
    }

}

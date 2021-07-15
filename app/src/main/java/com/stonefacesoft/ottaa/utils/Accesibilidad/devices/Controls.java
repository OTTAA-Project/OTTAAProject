package com.stonefacesoft.ottaa.utils.Accesibilidad.devices;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.MotionEvent;

import androidx.preference.PreferenceManager;

public class Controls {
    protected SharedPreferences preferences;
    protected Context mContext;

    public Controls(Context mContext){
        this.mContext=mContext;
        preferences= PreferenceManager.getDefaultSharedPreferences(this.mContext);
    }
    //this function allowed the click
    public boolean makeClick(MotionEvent event){
        return false;
    }

    public boolean makePrimaryClick(MotionEvent event){
        return false;
    }

    public boolean makeSecondaryClick(MotionEvent event){
        return false;
    }
    public boolean makeTertiaryClick(MotionEvent event){
        return false;
    }
    public boolean pressBackButton(){return false; }
}

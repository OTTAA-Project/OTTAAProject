package com.stonefacesoft.ottaa.utils;

import android.util.Log;

import com.stonefacesoft.ottaa.BuildConfig;

public class LogClass {
    public String TAG;

    public LogClass(String tag ){
        this.TAG=tag;
    }

    public void setTag(String tag){
        this.TAG=tag;
    }
    public void logErrorClass(String message){
        if(BuildConfig.DEBUG){
            Log.e(TAG, message );
        }
    }

    public void logDebugClass(String message){
        if(BuildConfig.DEBUG){
            Log.d(TAG,message);
        }
    }

    public void logInfoClass(String message){
        if(BuildConfig.DEBUG){
            Log.i(TAG,message);
        }
    }

    public void logWarn(String message){
        if(BuildConfig.DEBUG){
            Log.w(TAG,message);
        }
    }

    public void logVerbose(String message){
        if(BuildConfig.DEBUG){
            Log.v(TAG,message);
        }
    }

    public void logWhatATerribleFailure(String message){
        if (BuildConfig.DEBUG){
            Log.wtf(TAG,message);
        }
    }
}

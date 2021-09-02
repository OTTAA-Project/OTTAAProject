package com.stonefacesoft.ottaa.utils;

import android.app.Activity;
import android.content.Context;

public class ActivityUtilsEstatus {

    public boolean isValidContext(Context context){
        Activity activity=getmActivityFromContext(context);
        if(activity==null)
            return false;
        return !isActivityDestroyed(activity);
    }
    public Activity getmActivityFromContext(Context context){
        if(context instanceof Activity){
            return (Activity) context;
        }
        return null;
    }
    public boolean isActivityDestroyed(Activity activity){
        return  activity.isDestroyed()||activity.isFinishing()||activity.isChangingConfigurations();
    }
}

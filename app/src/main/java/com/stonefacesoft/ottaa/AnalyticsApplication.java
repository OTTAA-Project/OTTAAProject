package com.stonefacesoft.ottaa;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.preference.PreferenceManager;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.stonefacesoft.ottaa.utils.Firebase.CrashlyticsUtils;
import com.stonefacesoft.ottaa.utils.StrictModePolicyClass;


/**
 * Created by Hector on 23/03/2016.
 */

public class AnalyticsApplication extends Application {
    private Tracker mTracker;

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker("UA-74780233-1");
        }
        return mTracker;
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);

    }


    @Override
    public void onCreate() {
        super.onCreate();
        CrashlyticsUtils.getInstance();
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedPrefs.getString("Email_id","").equalsIgnoreCase("")) {
            CrashlyticsUtils.getInstance().getCrashlytics().setUserId("");
        }
       // turnOnStrictMode();
    }

    private void turnOnStrictMode() {
        if (BuildConfig.DEBUG){
            StrictModePolicyClass.enableStrictMode();
            StrictModePolicyClass.allowDiskReadAndWrite(super::onCreate);
        }
    }
}
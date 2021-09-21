package com.stonefacesoft.ottaa.utils.Firebase;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

import androidx.annotation.NonNull;

/**
 * <h3>Objective</h3>
 * Tracks all the events in the application
 * <h3>How to declare</h3>
 * <code>AnalyticsFirebase analyticsFirebase=new AnalyticsFirebase(Context mContext)</code>
 * <h3>Examples of implementation</h3>
 * <h4>How to implement a custom event</h4>
 * <code>String name="test"</code><br>
 * <code>String activity="testActivity"</code><br>
 * <code>String action="Button"</code><br>
 * <code>analyticsFirebase.customEvents(name,activity,action);</code>
 * */
public class AnalyticsFirebase {
    /**
     * Use this this class to track all diferents events
     * */
    private final FirebaseAnalytics mFirebaseAnalytics;
    private Context mContext;
    private Activity mActivity;
    private Bundle bundle;


    public AnalyticsFirebase(@NonNull Activity mActivity){
        this.mActivity=mActivity;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this.mActivity);
    }

    public AnalyticsFirebase(@NonNull Context mContext){
        this.mContext=mContext;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this.mContext);
    }

    public void setUnlockAchievement(String name){
         createBundle();
        bundle.putString(FirebaseAnalytics.Param.ACHIEVEMENT_ID, name);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.UNLOCK_ACHIEVEMENT, bundle);
    }

    public void shareEvents(){
        createBundle();
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SHARE, bundle);
    }

    public void postScoreEvent(int value){
        createBundle();
        bundle.putInt(FirebaseAnalytics.Param.SCORE,value);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.POST_SCORE,bundle);
    }

    public void levelNameGame(@NonNull String name){
        createBundle();
        bundle.putString(FirebaseAnalytics.Param.LEVEL_NAME,name);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LEVEL_START,bundle);
    }
    /**
     *
     * */
    public void TutorialBundle(){
        createBundle();
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.TUTORIAL_COMPLETE, bundle);
    }
    /**
     * Use this class if you want to make a control about a custom event
     * */
    public void customEvents(@NonNull String event,@NonNull String activity,@NonNull String action){
        createBundle();
        bundle.putString("Activity", activity);
        bundle.putString("Action", action);
        mFirebaseAnalytics.logEvent(event, bundle);
    }

    private void createBundle(){
        bundle= new Bundle();
    }


}

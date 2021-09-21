package com.stonefacesoft.ottaa.utils.AvatarPackage;

import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.stonefacesoft.ottaa.utils.Firebase.AnalyticsFirebase;
import com.stonefacesoft.ottaa.utils.MovableFloatingActionButton;

public class Avatar {
    long lastTimestamp = 0;
    Context mContext;
    MovableFloatingActionButton movableFloatingActionButton;
    AnalyticsFirebase analyticsFirebase;


    public Avatar(Context context, MovableFloatingActionButton movableFloatingActionButton){
        this.mContext = context;
        this.analyticsFirebase = new AnalyticsFirebase(this.mContext);
        this.movableFloatingActionButton = movableFloatingActionButton;
    }

    //This method register when a Click on the App is made to measure multiple clicks, and other functions
    public void registerClick(int id){
        if (isClickingMultipleTimes()) {
            //TODO fire interface to let the user know is clicking multipletimes
        }
        


    }

    //This method handles the avatar talking
    public String animateTalk(String utterance){
        movableFloatingActionButton.setVisibility(View.VISIBLE);
        //Alpha animation
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f,1.0f);
        alphaAnimation.setRepeatMode(Animation.ABSOLUTE);
        alphaAnimation.setInterpolator(new AccelerateInterpolator());
        alphaAnimation.setDuration(1500);
        alphaAnimation.setFillAfter(true);
        analyticsFirebase.customEvents("Talk", "Principal", "Avatar");
        movableFloatingActionButton.startAnimation(alphaAnimation);
        return utterance;

    }

    public void finishTalking(){
        //Alpha animation
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.0f);
        alphaAnimation.setRepeatMode(Animation.ABSOLUTE);
        alphaAnimation.setInterpolator(new AccelerateInterpolator());
        alphaAnimation.setDuration(1500);
        alphaAnimation.setFillAfter(true);
        movableFloatingActionButton.startAnimation(alphaAnimation);
    }

    //This method knows if the user is clicking multiple times repeatedly
    private boolean isClickingMultipleTimes(){
        long newTimestamp = System.currentTimeMillis();
        if (newTimestamp - lastTimestamp > 800) {
            lastTimestamp = newTimestamp;
            return true;
        } else
            return false;
    }

    //This method knows if the user has created multiple times the same sentence
    private boolean isCreatingSameSentence(){
        return true;
    }

    //This method retrieve a remote config message and shows it
    public void remoteConfigMessage(){

    }

    public MovableFloatingActionButton getMovableFloatingActionButton() {
        return movableFloatingActionButton;
    }
}

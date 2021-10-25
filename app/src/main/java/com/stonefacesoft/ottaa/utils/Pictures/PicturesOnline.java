package com.stonefacesoft.ottaa.utils.Pictures;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;

import com.stonefacesoft.ottaa.DrawableManager;
import com.stonefacesoft.ottaa.Interfaces.preparePictures;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PicturesOnline {
    public String path;
    private boolean isLoaded;
    private Drawable drawable;
    private preparePictures preparePictures;
    private Context context;
    private PowerManager.WakeLock mWakeLock;


    public PicturesOnline(Context context) {
        this.context = context;
    }

    public PicturesOnline preparePicture(preparePictures picture){
        this.preparePictures = picture;
        return this;
    }

    public class PrepareDrawable{
        private String url;
        public PrepareDrawable(String url){
            this.url = url;
        }

        public void execute(){
            Executor executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());
            executor.execute(()->{
                DrawableManager drawableManager = new DrawableManager();
                drawable = drawableManager.fetchDrawable(url);
                handler.post(()->{
                    preparePictures.setDrawable();
                });
            });
        }

    }


    public void prepareDrawable(String url) {
        new PrepareDrawable(url).execute();
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public boolean isLoaded() {
        return isLoaded;
    }


}

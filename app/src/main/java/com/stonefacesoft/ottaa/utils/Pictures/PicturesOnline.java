package com.stonefacesoft.ottaa.utils.Pictures;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.PowerManager;

import com.stonefacesoft.ottaa.DrawableManager;
import com.stonefacesoft.ottaa.Interfaces.preparePictures;

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

    public class PrepareDrawable extends AsyncTask<Void,Void,Void>{
        private String url;
        public PrepareDrawable(String url){
            this.url = url;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            DrawableManager drawableManager = new DrawableManager();
            drawable = drawableManager.fetchDrawable(url);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            preparePictures.setDrawable();
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

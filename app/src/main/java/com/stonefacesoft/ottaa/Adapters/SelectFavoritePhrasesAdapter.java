package com.stonefacesoft.ottaa.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.stonefacesoft.ottaa.FavModel;
import com.stonefacesoft.ottaa.Interfaces.LoadOnlinePictograms;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.JSONutils;
import com.stonefacesoft.ottaa.utils.Phrases.CustomFavoritePhrases;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SelectFavoritePhrasesAdapter extends PhrasesAdapter{
    private final String TAG ="SelectFavorite";

    public SelectFavoritePhrasesAdapter(Context mContext) {
        super(mContext);

    }

    @Override
    public void onBindViewHolder(@NonNull PhraseAdapter holder, int position) {
        execute(holder,position);
    }



    public void execute(PhraseAdapter mHolder,int mPosition){
            Executor executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());
            executor.execute(()->{
                Bitmap mBitmap;
                try {
                    mHolder.phrase = userPhrases.getJSONObject(mPosition);
                    mHolder.position = mPosition;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.post(()->{
                    gestionarBitmap.getBitmapDeFrase(mHolder.phrase,new LoadOnlinePictograms() {
                        @Override
                        public void preparePictograms() {
                        }

                        @Override
                        public void loadPictograms(Bitmap bitmap) {
                            glideAttatcher.UseCornerRadius(true).loadDrawable(bitmap, mHolder.img);
                        }
                        @Override
                        public void FileIsCreated() {

                        }

                        @Override
                        public void FileIsCreated(Bitmap bitmap) {

                        }
                    });
                    boolean isExist = phrases.isExist(mHolder.phrase);
                    if (isExist)
                        mHolder.img.setBackground(mContext.getResources().getDrawable(R.drawable.picto_shape_select));
                    else
                        mHolder.img.setBackgroundColor(mContext.getResources().getColor(R.color.FondoApp));
                });
            });
    }





    @Override
    public void itemAction(JSONObject phrase, View v) {
        super.itemAction(phrase,v);
        boolean isExist = phrases.isExist(phrase);
        setBackgroundColor(!isExist,phrase,v);
        addOrRemovePhrase(isExist,phrase);
    }

    public void setBackgroundColor(boolean isExist,JSONObject phrase,View v){
        if (isExist) {
            v.setBackground(mContext.getResources().getDrawable(R.drawable.picto_shape_select));
        } else {
            v.setBackgroundColor(mContext.getResources().getColor(R.color.FondoApp));
        }
    }
    public void addOrRemovePhrase(boolean exist,JSONObject phrase){
        if (!exist) {
            phrases.addFavoritePhrase(phrase);
        } else {
            phrases.removeFavoritePhrase(phrase);
        }
    }


    public void saveList() {
        phrases.saveFavoritePhrases();
    }

    public void loadHolder(PhraseAdapter holder,int position){
        try{
            holder.img.setImageBitmap(mFavImagesArrayList.get(position).getImagen());
            holder.setPhrase(mFavImagesArrayList.get(position).getPictogram());
            holder.position= mFavImagesArrayList.get(position).getPosition();
            setBackgroundColor(phrases.isExist(holder.phrase), holder.phrase, holder.img);
        }catch (Exception ex){

        }
    }
}

package com.stonefacesoft.ottaa.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.NonNull;

import com.stonefacesoft.ottaa.Interfaces.LoadOnlinePictograms;
import com.stonefacesoft.ottaa.R;

import org.json.JSONObject;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SelectFavoritePhrasesAdapter extends PhrasesAdapter{

    public SelectFavoritePhrasesAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull PhraseAdapter holder, int position) {
        new CargarFrasesAsync(position,holder).execute();
    }

    protected class CargarFrasesAsync {

        private final int mPosition;
        private final PhrasesAdapter.PhraseAdapter mHolder;
        private String mStringTexto;
        private Drawable mDrawableIcono;

        public CargarFrasesAsync(int mPosition, PhrasesAdapter.PhraseAdapter mHolder) {
            this.mPosition = mPosition;
            this.mHolder = mHolder;
        }

        public void execute(){
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
                    });
                    boolean isExist = phrases.isExist(mHolder.phrase);
                    if (isExist)
                        mHolder.img.setBackground(mContext.getResources().getDrawable(R.drawable.picto_shape_select));
                    else
                        mHolder.img.setBackgroundColor(mContext.getResources().getColor(R.color.FondoApp));
                });
            });
        }
    }



    @Override
    public void itemAction(JSONObject phrase, View v) {
        super.itemAction(phrase,v);
        boolean isExist = phrases.isExist(phrase);
        if (!isExist) {
            v.setBackground(mContext.getResources().getDrawable(R.drawable.picto_shape_select));
            phrases.addFavoritePhrase(phrase);
        } else {
            v.setBackgroundColor(mContext.getResources().getColor(R.color.FondoApp));
            phrases.removeFavoritePhrase(phrase);
        }
    }


    public void saveList() {
        phrases.saveFavoritePhrases();
    }
}

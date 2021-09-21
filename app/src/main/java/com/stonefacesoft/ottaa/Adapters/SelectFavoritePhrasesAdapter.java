package com.stonefacesoft.ottaa.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stonefacesoft.ottaa.Bitmap.GestionarBitmap;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.Phrases.CustomFavoritePhrases;
import com.stonefacesoft.ottaa.utils.textToSpeech;
import com.stonefacesoft.pictogramslibrary.utils.GlideAttatcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SelectFavoritePhrasesAdapter extends PhrasesAdapter{

    public SelectFavoritePhrasesAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull PhraseAdapter holder, int position) {
        new CargarFrasesAsync(position,holder).execute();
    }

    protected class CargarFrasesAsync extends AsyncTask<Void, Void, Void> {

        private final int mPosition;
        private final PhrasesAdapter.PhraseAdapter mHolder;
        private String mStringTexto;
        private Drawable mDrawableIcono;

        public CargarFrasesAsync(int mPosition, PhrasesAdapter.PhraseAdapter mHolder) {
            this.mPosition = mPosition;
            this.mHolder = mHolder;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            Bitmap mBitmap;
            try {
                mHolder.phrase = userPhrases.getJSONObject(mPosition);
                mHolder.position = mPosition;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            glideAttatcher.UseCornerRadius(true).loadDrawable(gestionarBitmap.getBitmapDeFrase(mHolder.phrase), mHolder.img);
            boolean isExist = phrases.isExist(mHolder.phrase);
            if (isExist)
                mHolder.img.setBackground(mContext.getResources().getDrawable(R.drawable.picto_shape_select));
            else
                mHolder.img.setBackgroundColor(mContext.getResources().getColor(R.color.FondoApp));
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

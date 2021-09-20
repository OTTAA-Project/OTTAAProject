package com.stonefacesoft.ottaa.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
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

public class PhrasesAdapter extends RecyclerView.Adapter<PhrasesAdapter.PhraseAdapter> {
    protected final Context mContext;
    protected final CustomFavoritePhrases phrases;
    protected JSONArray userPhrases;
    protected final textToSpeech myTTs;
    protected final GlideAttatcher glideAttatcher;
    protected final GestionarBitmap gestionarBitmap;


    public PhrasesAdapter(Context mContext) {
        this.mContext = mContext;
        phrases = CustomFavoritePhrases.getInstance(mContext);
        userPhrases = phrases.getJson().getmJSONArrayTodasLasFrases();
        myTTs = new textToSpeech(this.mContext);
        glideAttatcher = new GlideAttatcher(this.mContext);
        gestionarBitmap = new GestionarBitmap(this.mContext);
        gestionarBitmap.setColor(android.R.color.white);
    }


    @NonNull
    @Override
    public PhraseAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_favoritos_row, parent, false);
        return new PhraseAdapter(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull  PhraseAdapter holder, int position) {
        new CargarFrasesAsync(position, holder).execute();
    }

    @Override
    public int getItemCount() {
        return userPhrases.length();
    }

    public JSONArray getUserPhrases() {
        return userPhrases;
    }

    public void setUserPhrases(JSONArray favoritesPhrases) {
        this.userPhrases = favoritesPhrases;
    }



    public class PhraseAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected ImageView img;
        protected JSONObject phrase;
        protected int position;
        protected boolean isExist;

        public PhraseAdapter(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.frase);
            try {
                phrase = userPhrases.getJSONObject(position);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            img.setOnClickListener(this);
        }

        public void setImg(ImageView img) {
            this.img = img;
        }

        public void setPhrase(JSONObject phrase) {
            this.phrase = phrase;
        }

        @Override
        public void onClick(View v) {
            itemAction(phrase,v);
        }

    }

    protected class CargarFrasesAsync extends AsyncTask<Void, Void, Void> {

        private final int mPosition;
        private final PhraseAdapter mHolder;
        private String mStringTexto;
        private Drawable mDrawableIcono;

        public CargarFrasesAsync(int mPosition, PhraseAdapter mHolder) {
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
        }
    }

    public void itemAction(JSONObject phrase,View v){
        try {
            myTTs.hablar(phrase.getString("frase"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
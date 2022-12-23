package com.stonefacesoft.ottaa.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stonefacesoft.ottaa.Bitmap.GestionarBitmap;
import com.stonefacesoft.ottaa.FavModel;
import com.stonefacesoft.ottaa.Interfaces.LoadOnlinePictograms;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.Phrases.CustomFavoritePhrases;
import com.stonefacesoft.ottaa.utils.textToSpeech;
import com.stonefacesoft.pictogramslibrary.utils.GlideAttatcher;
import com.stonefacesoft.pictogramslibrary.utils.ValidateContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PhrasesAdapter extends RecyclerView.Adapter<PhrasesAdapter.PhraseAdapter> {
    protected final Context mContext;
    protected CustomFavoritePhrases phrases;
    protected JSONArray userPhrases;
    protected final textToSpeech myTTs;
    protected final GlideAttatcher glideAttatcher;
    protected final GestionarBitmap gestionarBitmap;
    protected   ArrayList<FavModel> mFavImagesArrayList;


    public PhrasesAdapter(Context mContext) {
        this.mContext = mContext;
        mFavImagesArrayList = new ArrayList<>();
        phrases = new CustomFavoritePhrases(mContext);
        userPhrases = phrases.getJson().getPhrasesByLanguage();
        myTTs = textToSpeech.getInstance(this.mContext);
        glideAttatcher = new GlideAttatcher(this.mContext);
        gestionarBitmap = new GestionarBitmap(this.mContext);
        gestionarBitmap.setColor(android.R.color.white);
     //    new CargarFrasesAsync().execute();
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
        execute(holder,position);
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
        new CargarFrasesAsync().execute();
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

            });
        });
    }

    protected class CargarFrasesAsync  extends AsyncTask<Void, Void, Void> {

        private String mStringTexto;
        private Drawable mDrawableIcono;
        private FavModel favModel;


        @Override
        protected Void doInBackground(Void... voids) {
            mFavImagesArrayList = new ArrayList<>();
            for (int i = 0; i < userPhrases.length(); i++) {
                try {
                    FavModel favModel = new FavModel();
                    favModel.setPosition(i);
                    favModel.setPictogram(userPhrases.getJSONObject(i));
                    favModel.setTexto(userPhrases.getString(i));
                    gestionarBitmap.getBitmapDeFrase(favModel.getPictogram(),new LoadOnlinePictograms() {
                        @Override
                        public void preparePictograms() {
                        }
                        @Override
                        public void loadPictograms(Bitmap bitmap) {
                            favModel.setImagen(bitmap);
                        }

                        @Override
                        public void FileIsCreated() {

                        }

                        @Override
                        public void FileIsCreated(Bitmap bitmap) {

                        }
                    });
                    mFavImagesArrayList.add(favModel);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            notifyDataSetChanged();
        }
    }

    public void itemAction(JSONObject phrase,View v){
        try {
            myTTs.hablar(phrase.getString("frase"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void loadHolder(PhraseAdapter holder,int position){
        try{
            holder.img.setImageBitmap(mFavImagesArrayList.get(position).getImagen());
            holder.setPhrase(mFavImagesArrayList.get(position).getPictogram());
            holder.position= mFavImagesArrayList.get(position).getPosition();
        }catch (Exception ex){

        }
    }
}

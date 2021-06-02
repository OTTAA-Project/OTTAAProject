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

public class SelectFavoritePhrasesAdapter extends RecyclerView.Adapter<SelectFavoritePhrasesAdapter.FavoritePhrases> {
    private Context mContext;
    private CustomFavoritePhrases phrases;
    private JSONArray favoritesPhrases;
    private textToSpeech myTTs;
    private GlideAttatcher glideAttatcher;
    private GestionarBitmap gestionarBitmap;


    public SelectFavoritePhrasesAdapter(Context mContext) {
        this.mContext = mContext;
        phrases=new CustomFavoritePhrases(this.mContext);
        favoritesPhrases=phrases.getJson().getmJSONArrayTodasLasFrases();
        myTTs=new textToSpeech(this.mContext);
        glideAttatcher=new GlideAttatcher(this.mContext);
        gestionarBitmap=new GestionarBitmap(this.mContext);
        gestionarBitmap.setColor(android.R.color.white);
    }



    @NonNull
    @Override
    public SelectFavoritePhrasesAdapter.FavoritePhrases onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_favorite_phrases_selector, parent, false);

        return new SelectFavoritePhrasesAdapter.FavoritePhrases(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritePhrases holder, int position) {
        new CargarFrasesAsync(position,holder).execute();
        /*
        holder.position
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.isExist){
                    phrases.removeFavoritePhrase(holder.phrase);
                    holder.img.setBackgroundColor(mContext.getResources().getColor(android.R.color.transparent));
                    holder.isExist=!holder.isExist;
                }
                else{
                    if(holder.phrase!=null){
                        phrases.addFavoritePhrase(holder.phrase);
                        holder.img.setBackground(mContext.getResources().getDrawable(R.drawable.picto_shape_select));
                        holder.isExist=!holder.isExist;
                    }
                }
            }
        });*/
    }

    public void saveList(){
        phrases.saveFavoritePhrases();
    }


    @Override
    public int getItemCount() {
            return favoritesPhrases.length();
    }

    public class FavoritePhrases extends RecyclerView.ViewHolder implements View.OnClickListener  {
        private ImageView img;
        private JSONObject phrase;
        private int position;
        private boolean isExist;
        public FavoritePhrases(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.frase);
            try {
                phrase=favoritesPhrases.getJSONObject(position);
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
            boolean isExist=phrases.isExist(phrase);
            if(!isExist){
                v.setBackground(mContext.getResources().getDrawable(R.drawable.picto_shape_select));
                phrases.addFavoritePhrase(phrase);
            }
            else{
                v.setBackgroundColor(mContext.getResources().getColor(R.color.FondoApp));
                phrases.removeFavoritePhrase(phrase);
            }
        }
    }
    private class CargarFrasesAsync extends AsyncTask<Void, Void, Void> {

        private final int mPosition;
        private final SelectFavoritePhrasesAdapter.FavoritePhrases mHolder;
        private String mStringTexto;
        private Drawable mDrawableIcono;

        public CargarFrasesAsync(int mPosition,SelectFavoritePhrasesAdapter.FavoritePhrases mHolder) {
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
                mHolder.phrase=favoritesPhrases.getJSONObject(mPosition);
                mHolder.position=mPosition;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            glideAttatcher.UseCornerRadius(true).loadDrawable(gestionarBitmap.getBitmapDeFrase(mHolder.phrase),mHolder.img);
            boolean isExist=phrases.isExist(mHolder.phrase);
            if(isExist)
                mHolder.img.setBackground(mContext.getResources().getDrawable(R.drawable.picto_shape_select));
            else
                mHolder.img.setBackgroundColor(mContext.getResources().getColor(R.color.FondoApp));
            }

            //Le asignamos al grupo su texto e icono
            // Glide.with(mContext).load(mDrawableIcono).transform(new RoundedCorners(16)).into(mHolder.mGrupoImageView);
            //   mHolder.mGrupoImageView.setImageDrawable(mDrawableIcono);

        }

}

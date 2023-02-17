package com.stonefacesoft.ottaa.Adapters;

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

import com.stonefacesoft.ottaa.Bitmap.CombineImages;
import com.stonefacesoft.ottaa.Bitmap.GestionarBitmap;
import com.stonefacesoft.ottaa.FavModel;
import com.stonefacesoft.ottaa.Interfaces.LoadOnlinePictograms;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.Phrases.CustomFavoritePhrases;
import com.stonefacesoft.ottaa.utils.textToSpeech;
import com.stonefacesoft.pictogramslibrary.utils.GlideAttatcher;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CustomFavoritePhrasesAdapter extends RecyclerView.Adapter<CustomFavoritePhrasesAdapter.FavoritePhrases>{
    private final Context mContext;
    private CustomFavoritePhrases phrases;
    private final textToSpeech myTTs;
    private final GlideAttatcher glideAttatcher;
    private final GestionarBitmap gestionarBitmap;
    protected volatile ArrayList<FavModel> mFavImagesArrayList;
    private final String TAG = "CustomFavorite";

    public CustomFavoritePhrasesAdapter(Context mContext) {
        this.mContext = mContext;
        phrases = new CustomFavoritePhrases(mContext);
        myTTs = textToSpeech.getInstance(this.mContext);
        glideAttatcher=new GlideAttatcher(this.mContext);
        gestionarBitmap=new GestionarBitmap(this.mContext);
        gestionarBitmap.setColor(android.R.color.white);
        new CargarFrasesAsync().execute();
    }

    public void updateData(){
        phrases = new CustomFavoritePhrases(mContext);
        new CargarFrasesAsync().execute();
    }

    @NonNull
    @Override
    public FavoritePhrases onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_favoritos_row, parent, false);
        return new CustomFavoritePhrasesAdapter.FavoritePhrases(itemView);
    }



    @Override
    public void onBindViewHolder(@NonNull FavoritePhrases holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritePhrases holder, int position) {
        loadObject(holder,position);
      // loadHolder(holder,position);
    }

    @Override
    public int getItemCount() {
            return phrases.getPhrases().length();
    }

    public CustomFavoritePhrases getPhrases() {
        return phrases;
    }

    public textToSpeech getMyTTs() {
        return myTTs;
    }

    public class FavoritePhrases extends RecyclerView.ViewHolder   {
        private ImageView img;
        private JSONObject phrase;
        private int position;
        public FavoritePhrases(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.frase);
            try {
                phrase = phrases.getPhrases().getJSONObject(position);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        public void setImg(ImageView img) {
            this.img = img;
        }

        public void setPhrase(JSONObject phrase) {
            this.phrase = phrase;
        }

    }

    public void loadHolder(FavoritePhrases holder, int position){
        try{
            holder.img.setImageBitmap(mFavImagesArrayList.get(position).getImagen());
            holder.setPhrase(mFavImagesArrayList.get(position).getPictogram());
            holder.position= mFavImagesArrayList.get(position).getPosition();
        }catch (Exception ex){
            Log.e(TAG, "doInBackground: "+ ex.getMessage() );
        }
    }

    private void loadObject(FavoritePhrases holder, int pos){
        try {
            holder.position=pos;
            JSONObject phrase=phrases.getPhrases().getJSONObject(pos);
            holder.phrase=phrase;
            gestionarBitmap.getBitmapDeFrase(phrase,new LoadOnlinePictograms() {
                @Override
                public void preparePictograms() {
                }
                @Override
                public void loadPictograms(Bitmap bitmap) {
                    holder.img.setImageBitmap(bitmap);
                }

                @Override
                public void FileIsCreated() {
                }

                @Override
                public void FileIsCreated(Bitmap bitmap) {

                }
            });
            holder.img.setOnClickListener(v -> {
                if(phrase!=null) {
                    try {
                        myTTs.hablar(phrase.getString("frase"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected class CargarFrasesAsync  extends AsyncTask<Void, Void, Void> {



        @Override
        protected Void doInBackground(Void... voids) {
            mFavImagesArrayList = new ArrayList<>();
            for (int i = 0; i < phrases.getPhrases().length(); i++) {
                try {
                    FavModel favModel = new FavModel();
                    favModel.setPosition(i);
                    favModel.setPictogram(phrases.getPhrases().getJSONObject(i));
                    favModel.setTexto(phrases.getPhrases().getString(i));
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


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "doInBackground: "+ e.getMessage() );
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            notifyDataSetChanged();
        }
    }
}

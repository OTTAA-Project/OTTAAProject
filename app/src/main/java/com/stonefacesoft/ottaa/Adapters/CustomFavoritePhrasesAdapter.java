package com.stonefacesoft.ottaa.Adapters;

import android.content.Context;
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

public class CustomFavoritePhrasesAdapter extends RecyclerView.Adapter<CustomFavoritePhrasesAdapter.FavoritePhrases>{
    private final Context mContext;
    private final CustomFavoritePhrases phrases;
    private final JSONArray favoritesPhrases;
    private final textToSpeech myTTs;
    private final GlideAttatcher glideAttatcher;
    private final GestionarBitmap gestionarBitmap;

    public CustomFavoritePhrasesAdapter(Context mContext) {
        this.mContext = mContext;
        phrases=new CustomFavoritePhrases(this.mContext);
        favoritesPhrases=phrases.getPhrases();
        myTTs=new textToSpeech(this.mContext);
        glideAttatcher=new GlideAttatcher(this.mContext);
        gestionarBitmap=new GestionarBitmap(this.mContext);
        Log.e("TAG", "onBindViewHolder: "+favoritesPhrases.toString() );
    }





    @NonNull
    @Override
    public FavoritePhrases onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_favoritos_row, parent, false);
        return new CustomFavoritePhrasesAdapter.FavoritePhrases(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritePhrases holder, int position) {
        try {
            holder.position=position;
            JSONObject phrase=favoritesPhrases.getJSONObject(position);

            holder.phrase=phrase;
            glideAttatcher.UseCornerRadius(true).loadDrawable(gestionarBitmap.getBitmapDeFrase(phrase),holder.img);
            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(phrase!=null) {
                        try {
                            myTTs.hablar(phrase.getString("frase"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
            return favoritesPhrases.length();
    }

    public class FavoritePhrases extends RecyclerView.ViewHolder   {
        private ImageView img;
        private JSONObject phrase;
        private int position;
        public FavoritePhrases(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.frase);
        }

        public void setImg(ImageView img) {
            this.img = img;
        }

        public void setPhrase(JSONObject phrase) {
            this.phrase = phrase;
        }
    }
}

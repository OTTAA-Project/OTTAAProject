package com.stonefacesoft.ottaa.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stonefacesoft.ottaa.Bitmap.GestionarBitmap;
import com.stonefacesoft.ottaa.Interfaces.LoadOnlinePictograms;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.Phrases.CustomFavoritePhrases;
import com.stonefacesoft.ottaa.utils.textToSpeech;
import com.stonefacesoft.pictogramslibrary.utils.GlideAttatcher;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CustomFavoritePhrasesAdapter extends RecyclerView.Adapter<CustomFavoritePhrasesAdapter.FavoritePhrases>{
    private final Context mContext;
    private final CustomFavoritePhrases phrases;
    private final textToSpeech myTTs;
    private final GlideAttatcher glideAttatcher;
    private final GestionarBitmap gestionarBitmap;

    public CustomFavoritePhrasesAdapter(Context mContext) {
        this.mContext = mContext;
        phrases=CustomFavoritePhrases.getInstance(mContext);
        myTTs = textToSpeech.getInstance(this.mContext);
        glideAttatcher=new GlideAttatcher(this.mContext);
        gestionarBitmap=new GestionarBitmap(this.mContext);

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
            img=itemView.findViewById(R.id.frase);
        }

        public void setImg(ImageView img) {
            this.img = img;
        }

        public void setPhrase(JSONObject phrase) {
            this.phrase = phrase;
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
                    glideAttatcher.UseCornerRadius(true).loadDrawable(bitmap,holder.img);
                }

                @Override
                public void FileIsCreated() {
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
}

package com.stonefacesoft.ottaa.Adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.stonefacesoft.ottaa.Bitmap.GestionarBitmap;
import com.stonefacesoft.ottaa.FavModel;
import com.stonefacesoft.ottaa.Interfaces.ProgressBarListener;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.DatosDeUso;
import com.stonefacesoft.ottaa.utils.exceptions.FiveMbException;
import com.stonefacesoft.ottaa.utils.textToSpeech;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MostUsedFavoritePhrasesAdapter extends RecyclerView.Adapter<MostUsedFavoritePhrasesAdapter.FrasesFavoritasViewHolder> {

    private final Context mContext;
    private final ArrayList<FavModel> mFavImagesArrayList;
    private textToSpeech myTTs;
    private ProgressBarListener progressBarListener;
    private GestionarBitmap mGestionarBitmap;





    public MostUsedFavoritePhrasesAdapter(Context mContext, ProgressBarListener progressBarListener){
        this.mContext = mContext;
        this.mFavImagesArrayList = new ArrayList<>();
        this.myTTs = new textToSpeech(mContext);
        this.progressBarListener = progressBarListener;
        new cargarFavoritos().execute();
    }


    @Override
    public FrasesFavoritasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_favoritos_row, parent, false);
        return new FrasesFavoritasViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FrasesFavoritasViewHolder holder, int position) {
        holder.imagenFav.setImageBitmap(mFavImagesArrayList.get(position).getImagen());
        holder.imagFav = mFavImagesArrayList.get(position).getTexto();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        if (mFavImagesArrayList != null)
            return mFavImagesArrayList.size();
        else
            return 0;
    }

    public class FrasesFavoritasViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView imagenFav;
        private String imagFav;

        public FrasesFavoritasViewHolder(View itemView) {
            super(itemView);//this class extends from recycler view
            imagenFav = itemView.findViewById(R.id.frase);
            imagenFav.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (myTTs != null)
                myTTs.hablar(imagFav);// the system talk and say the phrases selected by the user
            else if (myTTs == null && mContext != null) {
                myTTs = new textToSpeech(mContext);
                myTTs.hablar(imagFav);
            }
        }
    }

    private class cargarFavoritos extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           if(progressBarListener!=null)
           progressBarListener.initProgressDialog();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            mGestionarBitmap = new GestionarBitmap(mContext);
            try {
                DatosDeUso mDatosDeUso = new DatosDeUso(mContext);
                List frases = mDatosDeUso.getArrayListFrasesMasUsadas(4);
                for (int i = 0; i < frases.size(); i++) {
                    FavModel model = new FavModel();
                    if (mGestionarBitmap.getBitmapDeFrase(mDatosDeUso.getFrasesOrdenadas().get(i)) != null) {

                        model.setImagen(mGestionarBitmap.getBitmapDeFrase(mDatosDeUso.getFrasesOrdenadas().get(i)));
                        try {
                            model.setTexto(mDatosDeUso.getFrasesOrdenadas().get(i).getString("frase"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mFavImagesArrayList.add(model);
                    }
                }
            } catch (FiveMbException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(progressBarListener!=null)
                progressBarListener.dismisProgressBar();
        }
    }
}

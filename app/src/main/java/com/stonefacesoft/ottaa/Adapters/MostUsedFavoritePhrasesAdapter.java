package com.stonefacesoft.ottaa.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.stonefacesoft.ottaa.Bitmap.GestionarBitmap;
import com.stonefacesoft.ottaa.FavModel;
import com.stonefacesoft.ottaa.Interfaces.LoadOnlinePictograms;
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
    private final ProgressBarListener progressBarListener;
    private GestionarBitmap mGestionarBitmap;





    public MostUsedFavoritePhrasesAdapter(Context mContext, ProgressBarListener progressBarListener){
        this.mContext = mContext;
        this.mFavImagesArrayList = new ArrayList<>();
        this.myTTs = textToSpeech.getInstance(this.mContext);
        this.progressBarListener = progressBarListener;
        new cargarFavoritos().execute();
    }

    public textToSpeech getMyTTs() {
        return myTTs;
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

    public ArrayList<FavModel> getmFavImagesArrayList() {
        return mFavImagesArrayList;
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
                myTTs = textToSpeech.getInstance(mContext);
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
            mGestionarBitmap.setColor(android.R.color.white);
            try {
                DatosDeUso mDatosDeUso = new DatosDeUso(mContext);
                List frases = mDatosDeUso.getArrayListFrasesMasUsadas(4);
                for (int i = 0; i < frases.size(); i++) {
                    FavModel model = new FavModel();
                        mGestionarBitmap.getBitmapDeFrase(mDatosDeUso.getFrasesOrdenadas().get(i),new LoadOnlinePictograms() {
                            @Override
                            public void preparePictograms() {
                            }

                            @Override
                            public void loadPictograms(Bitmap bitmap) {
                                model.setImagen(bitmap);
                            }

                            @Override
                            public void FileIsCreated() {

                            }

                            @Override
                            public void FileIsCreated(Bitmap bitmap) {

                            }
                        });
                        try {
                            model.setTexto(mDatosDeUso.getFrasesOrdenadas().get(i).getString("frase"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mFavImagesArrayList.add(model);
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
            notifyDataSetChanged();
        }
    }
}

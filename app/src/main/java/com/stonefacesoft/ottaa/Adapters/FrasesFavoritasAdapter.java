package com.stonefacesoft.ottaa.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.stonefacesoft.ottaa.FavModel;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.exceptions.FiveMbException;
import com.stonefacesoft.ottaa.utils.textToSpeech;

import java.util.ArrayList;

public class FrasesFavoritasAdapter extends RecyclerView.Adapter<FrasesFavoritasAdapter.FrasesFavoritasViewHolder> {

    private int mLayoutResourceId;
    private Context mContext;
    private ArrayList<FavModel> mFavImagesArrayList;
    private Dialog dialogDismiss;
    private textToSpeech myTTs;



    public FrasesFavoritasAdapter(int mLayoutResourceId, Context mContext, ArrayList<FavModel> mFavImagesArrayList, Dialog dialogToDismiss) throws FiveMbException {
        this.mLayoutResourceId = mLayoutResourceId;
        this.mContext = mContext;
        this.mFavImagesArrayList = mFavImagesArrayList;
        this.dialogDismiss = dialogToDismiss;
        this.myTTs = new textToSpeech(mContext);
    }


    @Override
    public FrasesFavoritasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(mContext)
                .inflate(mLayoutResourceId, parent, false);
        return new FrasesFavoritasViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FrasesFavoritasViewHolder holder, int position) {
        FavModel model = mFavImagesArrayList.get(position);
        holder.imagenFav.setImageBitmap(model.getImagen());
        holder.imagFav = model.getTexto();
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

        private ImageView imagenFav;
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
                dialogDismiss.dismiss();

            }
        }
    }
}

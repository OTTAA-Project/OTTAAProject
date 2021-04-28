package com.stonefacesoft.ottaa.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.stonefacesoft.ottaa.utils.Games.CalculaPuntos;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.DatosDeUso;
import com.stonefacesoft.ottaa.utils.exceptions.FiveMbException;
import com.stonefacesoft.ottaa.utils.textToSpeech;

import org.json.JSONException;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ScoreListItem extends RecyclerView.Adapter<ScoreListItem.ScoreViewHolder>{
    private int mLayoutResourceId;
    private final Context mContext;
    private final int id;
    private DatosDeUso mDatosDeUso;
    private Dialog dialogDismiss;
    private final textToSpeech myTTs;
    private final Json json;


    public ScoreListItem( Context mContext,int id)  {
        this.mContext = mContext;
        json=Json.getInstance();
        json.setmContext(this.mContext);
        this.id=id;
        try {
            this.mDatosDeUso = new DatosDeUso(mContext);
        } catch (FiveMbException e) {
            e.printStackTrace();
        }
        this.myTTs = new textToSpeech(mContext);
    }


    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.view_adapter_group_game_score, parent, false);
        return new ScoreListItem.ScoreViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        try {
            holder.imagenFav.setImageDrawable(json.getIcono(json.getmJSONArrayTodosLosGrupos().getJSONObject(position)));
            holder.title.setText(json.getNombre(json.getmJSONArrayTodosLosGrupos().getJSONObject(position)));
            CalculaPuntos calculaPuntos=new CalculaPuntos();
            calculaPuntos.setAciertos(json.getAciertos(json.getmJSONArrayTodosLosGrupos().getJSONObject(position),id));
            calculaPuntos.setIntentos(json.getIntentos(json.getmJSONArrayTodosLosGrupos().getJSONObject(position),id));
            holder.mRatingBar.setRating(calculaPuntos.calcularValor());
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        if (json.getmJSONArrayTodosLosGrupos()!=null)
            return json.getmJSONArrayTodosLosGrupos().length();
        else
            return 0;
    }

    public class ScoreViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imagenFav;
        private final TextView title;
        private final RatingBar mRatingBar;

        public ScoreViewHolder(View itemView) {
            super(itemView);//this class extends from recycler view
            imagenFav = itemView.findViewById(R.id.icon);
            title=itemView.findViewById(R.id.name);
            mRatingBar=itemView.findViewById(R.id.score);

        }



    }
}

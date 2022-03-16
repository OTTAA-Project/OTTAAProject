package com.stonefacesoft.ottaa.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;
import com.stonefacesoft.pictogramslibrary.view.PictoView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.TagsViewHolder> {


    //ArrayList de elementos a cargar dentro del RecyclerView
    private ArrayList<JSONObject> mArrayListElementos;
    //Layout que carga el custom row, por ejemplo row_view
    private int mLayoutResourceId;
    //Context
    private Context mContext;
    //Obtengo el arraylist de los tags seleccionados para pintarlos
    private ArrayList<JSONObject> mArrayListSelectedTags;
    //Instancia clase json para traer los valores
    private Json json;
    private final String TAG="TagsAdapter";


    //Constructor para inicializar en AsignarTags y pasarle los datos a cargar, la layout a inflar como la vista de cada row, context, arrays de tags seleccionados y totales y grupo a pintar
    public TagsAdapter(ArrayList<JSONObject> mArrayListElementos, int mLayoutResourceId, ArrayList<JSONObject> mArrayListTagsSeleccionados, Context context) {
        this.mArrayListElementos = mArrayListElementos;
        this.mLayoutResourceId = mLayoutResourceId;
        this.mContext = context;
        this.mArrayListSelectedTags = mArrayListTagsSeleccionados;
        Json.getInstance().setmContext(mContext);
        json = Json.getInstance();
    }

    public TagsAdapter(){

    }

    public void setmArrayListSelectedTags(ArrayList<JSONObject> mArrayListSelectedTags) {
        this.mArrayListSelectedTags = mArrayListSelectedTags;
    }

    public void setmArrayListElementos(ArrayList<JSONObject> mArrayListElementos) {
        this.mArrayListElementos = mArrayListElementos;
    }



    //Inflamos la vista que vamos a usar para cada elemento
    @Override
    public TagsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(mLayoutResourceId, parent, false);
        return new TagsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TagsViewHolder holder, int position) {

        //Le ponemos los atributos a cada elemento, texto, imagen, tipo
        try {
            holder.pictoView.setCustom_Texto(mArrayListElementos.get(position).getJSONObject("texto").getString(getLanguage(ConfigurarIdioma.getLanguaje())));
            int id = mContext.getResources().getIdentifier(mArrayListElementos.get(position).getJSONObject("imagen").getString("picto"), "drawable", mContext.getPackageName());
            holder.pictoView.getImageView().setImageResource(id);
            //holder.mPictoImageView.setBackgroundColor(cargarColor(json.getTipo(mArrayListElementos.get(position))));
            if (mArrayListSelectedTags.contains(mArrayListElementos.get(position)))
                holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.colorVincular));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private String getLanguage(String value){
        switch (value){
            case "es":
                return value;
            case "en":
                return value;
            default:
                return "en";
        }
    }


    //Estos dos metodos si o si para que no se borre el elemento o se cambie (shuffle) de posicion cuando la view no es mas visible
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //--------------------------------------------

    //Carga los colores que son usados para el tipo de view.
    private Integer cargarColor(int tipo) {
        switch (tipo) {
            case 1:
                return mContext.getResources().getColor(R.color.Yellow);
            case 2:
                return mContext.getResources().getColor(R.color.Orange);
            case 3:
                return mContext.getResources().getColor(R.color.YellowGreen);
            case 4:
                return mContext.getResources().getColor(R.color.DodgerBlue);
            case 5:
                return mContext.getResources().getColor(R.color.Magenta);
            case 6:
                return mContext.getResources().getColor(R.color.Black);
            default:
                return mContext.getResources().getColor(R.color.White);
        }
    }


    //Devolvemos la cantidad de elementos del array , como es estatico deberia devolver 16
    @Override
    public int getItemCount() {
        if (mArrayListElementos == null)
            return 0;

        return mArrayListElementos.size();
    }

    //Tags ViewHolder donde llamamos a cada elemento del layout a inflar por cada vista
    public class TagsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        PictoView pictoView;

        public TagsViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            pictoView = view.findViewById(R.id.pictogram);
            Log.d(TAG, "TagsViewHolder: Llamando pintarTags ");


        }

        @Override
        public void onClick(View view) {

            //Manejamos el clicka de cada tag para pintarlo y agregarlo al ArrayList de tags seleccionados
            try {

                if (!mArrayListSelectedTags.contains(json.getPictoFromCustomArrayById(mArrayListElementos, mArrayListElementos.get(getLayoutPosition()).getInt("id")))) {
                    view.setBackgroundColor(mContext.getResources().getColor(R.color.colorVincular));
                    mArrayListSelectedTags.add(json.getPictoFromCustomArrayById(mArrayListElementos, mArrayListElementos.get(getLayoutPosition()).getInt("id")));

                } else {
                    view.setBackgroundColor(mContext.getResources().getColor(R.color.colorTransparent));
                    mArrayListSelectedTags.remove(json.getPictoFromCustomArrayById(mArrayListElementos, mArrayListElementos.get(getLayoutPosition()).getInt("id")));

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


}

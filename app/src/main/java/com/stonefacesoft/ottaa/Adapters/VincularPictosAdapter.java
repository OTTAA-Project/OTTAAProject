package com.stonefacesoft.ottaa.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.JSONutils.sortPictogramsUtils.SortPictograms;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;
import com.stonefacesoft.pictogramslibrary.Classes.Pictogram;
import com.stonefacesoft.pictogramslibrary.utils.GlideAttatcher;
import com.stonefacesoft.pictogramslibrary.view.PictoView;

import org.jetbrains.annotations.TestOnly;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VincularPictosAdapter extends RecyclerView.Adapter<VincularPictosAdapter.VincularViewHolder> implements ListPreloader.PreloadModelProvider {

    //private Context mContext;
    private final int layoutID;
    private final JSONArray mSelectedPictos;
    private final Json json;
    private final ArrayList<Integer> listadoIdPictos;
    private JSONArray mVincularArray;
    private boolean esFiltrado;
    private GlideAttatcher glideAttatcher;
    private Context mContext;

    public VincularPictosAdapter(Context mContext, int layoutID, JSONArray mVincularArray, boolean filtro) {
        this.mContext = mContext;
        Json.getInstance().setmContext(this.mContext);
        this.json = Json.getInstance();
        this.layoutID = layoutID;
        this.mVincularArray = mVincularArray;
        this.esFiltrado = filtro;
        this.mSelectedPictos = new JSONArray();
        this.listadoIdPictos = new ArrayList<>();
        int position =  Json.getInstance().getPosPictoBinarySearch(this.mVincularArray,0);
        if(position != -1)
        this.mVincularArray.remove(position);

    }

    @TestOnly
    public VincularPictosAdapter(int layoutID, JSONArray mVincularArray, boolean filtro) {
        this.json = Json.getInstance();
        this.layoutID = layoutID;
        this.mVincularArray = mVincularArray;
        this.esFiltrado = filtro;
        this.mSelectedPictos = new JSONArray();
        this.listadoIdPictos = new ArrayList<>();
        try {
            new SortPictograms().quickSort(mVincularArray,0,mVincularArray.length()-1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        int position =  Json.getInstance().getPosPictoBinarySearch(this.mVincularArray,0);
        if(position != -1)
            this.mVincularArray.remove(0);
    }

    public VincularPictosAdapter initGlideAttatcher() {
        this.glideAttatcher = new GlideAttatcher(this.mContext);
        return this;
    }

    @Override
    public VincularViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(layoutID, parent, false);
        return new VincularViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VincularViewHolder holder, int position) {
        new VincularAsync(holder, position).execute();
    }

    @Override
    public int getItemCount() {
        if (mVincularArray == null)
            return 0;
        return mVincularArray.length();
    }


    //Uso esto por que el array de pictos seleccionados hace falta pasarlo a galeria pictos
    //cuando apretamos el boton de aceptar en vincular (el tilde) pide por el array de pictos seleccionados
    //para poner tod o esos elementos en el grupo seleccionado. Al no hacer esto no tengo forma de decirle
    // que pictos se seleccionaron de vincular para asignar el grupo, y de esa forma siempre me va a agregar 1 solo
    public JSONArray getmSelectedPictos() {
        return mSelectedPictos;
    }

    //Sin este get y set no le puedo decir al adaptador que los datos cambiaron en galeria picto
    public JSONArray getmVincularArray() {
        return mVincularArray;
    }

    public void setmVincularArray(JSONArray mVincularArray) {
        this.mVincularArray = mVincularArray;
    }

    //Para actualizar en el caso de ser filtrado o no


    public boolean isEsFiltrado() {
        return esFiltrado;
    }

    public void setEsFiltrado(boolean esFiltrado) {
        this.esFiltrado = esFiltrado;
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

    @NonNull
    @Override
    public List getPreloadItems(int position) {
        JSONObject picto = null;
        Drawable drawable;
        try {
            picto = json.getPictoFromId2(mVincularArray.getJSONObject(position).getInt("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //json.getPictoFromCustomArrayById2(json.getmJSONArrayTodosLosPictos(),mVincularArray.getJSONObject(mPosition).getInt("id"));

        drawable = json.getIcono(picto);
        if (drawable == null)
            drawable = AppCompatResources.getDrawable(mContext, R.drawable.ic_cloud_download_orange);
        return Collections.singletonList(drawable);
    }

    @Nullable
    @Override
    public RequestBuilder<?> getPreloadRequestBuilder(@NonNull Object item) {
        return null;
    }

    @Override
    public void onViewRecycled(@NonNull VincularViewHolder holder) {
        super.onViewRecycled(holder);
        glideAttatcher.clearMemory();
    }

    public class VincularViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        PictoView pictoView;
        boolean isSelected;

        public VincularViewHolder(View itemView) {
            super(itemView);
            pictoView = itemView.findViewById(R.id.pictogram);
            pictoView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            try {
                if (!esFiltrado) {
                    setBackGroundShape(v, isSelected);
                    if (isSelected) {
                        mSelectedPictos.put(json.getPictoFromCustomArrayById2(mVincularArray, json.getId(mVincularArray.getJSONObject(getAdapterPosition()))));
                    } else {
                        int removePos = json.getPosPicto(mSelectedPictos, json.getId( mVincularArray.getJSONObject(getAdapterPosition())));
                        if (removePos != -1)
                            mSelectedPictos.remove(removePos);
                    }
                } else {
                    setBackGroundShape(v, isSelected);
                    if (isSelected) {
                        mSelectedPictos.put(json.getPictoFromCustomArrayById2(mVincularArray, json.getId(mVincularArray.getJSONObject(getAdapterPosition()))));
                    } else {
                        int removePos = json.getPosPicto(mSelectedPictos, json.getId( mVincularArray.getJSONObject(getAdapterPosition())));
                        if (removePos != -1)
                            mSelectedPictos.remove(removePos);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("TAG", "onClick: error al filtrar pictograma");
            }
            notifyDataSetChanged();
        }

        public void setBackGroundShape(View v, boolean isSelected) {
            if (isSelected)
                v.setBackground(mContext.getResources().getDrawable(R.drawable.picto_shape_select));
            else
                v.setBackground(mContext.getResources().getDrawable(R.drawable.picto_shape));
            this.isSelected = !isSelected;
        }

        public int isSelected(int position) {
            int posSelected = -1;
            if (!isEsFiltrado()) {
                try {
                    posSelected = json.getPosPicto(mSelectedPictos, mVincularArray.getJSONObject(position).getInt("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    posSelected = json.getPosPicto(mSelectedPictos, mVincularArray.getJSONObject(position).getInt("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (posSelected != -1)
                isSelected = true;
            return posSelected;
        }


    }



    private class VincularAsync extends AsyncTask<Void, Void, Void> {

        private final VincularViewHolder mHolder;
        private final int mPosition;
        private JSONObject picto;


        public VincularAsync(VincularViewHolder mHolder, int mPosition) {
            this.mHolder = mHolder;
            this.mPosition = mPosition;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                picto = json.getPictoFromId2(mVincularArray.getJSONObject(mPosition).getInt("id"));
                if(picto!= null){
                    mHolder.pictoView.setUpContext(mContext);
                    mHolder.pictoView.setUpGlideAttatcher(mContext);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (picto != null) {
                try {
                    mHolder.pictoView.setPictogramsLibraryPictogram(new Pictogram(mVincularArray.getJSONObject(mPosition), ConfigurarIdioma.getLanguaje()));
                    if (mHolder.isSelected(mPosition) != -1)
                        mHolder.itemView.setBackground(mContext.getResources().getDrawable(R.drawable.picto_shape_select));
                    else
                        mHolder.itemView.setBackground(mContext.getResources().getDrawable(R.drawable.picto_shape));
                } catch (Exception ex) {
                    ex.printStackTrace();

                }
            }
        }
    }


}

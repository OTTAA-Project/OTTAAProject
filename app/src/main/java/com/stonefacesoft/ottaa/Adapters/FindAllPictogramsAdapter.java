package com.stonefacesoft.ottaa.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;
import com.stonefacesoft.ottaa.Helper.ItemTouchHelperAdapter;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;
import com.stonefacesoft.pictogramslibrary.Classes.Pictogram;
import com.stonefacesoft.pictogramslibrary.utils.GlideAttatcher;
import com.stonefacesoft.pictogramslibrary.view.PictoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

public class FindAllPictogramsAdapter extends RecyclerView.Adapter<FindAllPictogramsAdapter.FindAllPictogramsHolder> implements ListPreloader.PreloadModelProvider, ItemTouchHelperAdapter {
    private final String TAG = "FindAllPictogramsAD";
    private final int layoutID;
    private final JSONArray mSelectedPictos;
    private final Context mContext;
    private JSONArray mArrayPictos;
    private boolean esFiltrado;
    private Json json;


    public FindAllPictogramsAdapter(Context mContext, int layoutID, JSONArray mVincularArray, boolean filtro) {
        this.mContext = mContext;
        this.layoutID = layoutID;
        this.mArrayPictos = mVincularArray;
        this.esFiltrado = filtro;
        this.mSelectedPictos = new JSONArray();
    }

    @NonNull
    @Override
    public FindAllPictogramsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(layoutID, parent, false);
        return new FindAllPictogramsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FindAllPictogramsHolder holder, int position) {
        if(!mArrayPictos.isNull(position)){
            new VincularAsync(holder, position).execute();
        }
        else{
            mArrayPictos.remove(position);
        }
    }

    @Override
    public int getItemCount() {
        if (mArrayPictos == null)
            return 0;
        return mArrayPictos.length();
    }


    //Uso esto por que el array de pictos seleccionados hace falta pasarlo a galeria pictos
    //cuando apretamos el boton de aceptar en vincular (el tilde) pide por el array de pictos seleccionados
    //para poner tod o esos elementos en el grupo seleccionado. Al no hacer esto no tengo forma de decirle
    // que pictos se seleccionaron de vincular para asignar el grupo, y de esa forma siempre me va a agregar 1 solo
    public JSONArray getmSelectedPictos() {
        return mSelectedPictos;
    }

    //Sin este get y set no le puedo decir al adaptador que los datos cambiaron en galeria picto
    public JSONArray getmArrayPictos() {
        return mArrayPictos;
    }

    public void setmArrayPictos(JSONArray mArrayPictos) {
        this.mArrayPictos = mArrayPictos;
    }

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

    @NonNull
    @Override
    public List getPreloadItems(int position) {
        JSONObject picto = null;
        Drawable drawable;
        try {
            picto = json.getPictoFromId2(mArrayPictos.getJSONObject(position).getInt("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    @SuppressLint("CheckResult")
    @Override
    public void onViewRecycled(@NonNull FindAllPictogramsHolder holder) {
        holder.pictoView.setGlideAttatcher(new GlideAttatcher(holder.pictoView.getContext()));
        holder.pictoView.getGlideAttatcher().loadDrawable(holder.pictoView.getCustom_Imagen(),holder.pictoView.getImageView());
        super.onViewRecycled(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull FindAllPictogramsHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    /**
     *
     * */
    public void loadDrawable( int id,PictoView pictoView) {
        boolean showKindOfView = json.getPictoFromId2(id) == null;
        if (showKindOfView) {
            pictoView.getKindOfPictogramImageView().setVisibility(View.VISIBLE);
        } else {
            pictoView.getKindOfPictogramImageView().setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemMove(int fromIndex, int toIndex) {

    }

    @Override
    public void onItemDismiss(int position) {

    }

    @Override
    public void onDropItem() {

    }
    public class FindAllPictogramsHolder extends RecyclerView.ViewHolder {
        PictoView pictoView;
        public FindAllPictogramsHolder(View itemView) {
            super(itemView);
            pictoView = itemView.findViewById(R.id.pictogram);
        }
    }




    private class VincularAsync extends AsyncTask<Void, Void, Void> {

        private final FindAllPictogramsHolder mHolder;
        private final int mPosition;
        private JSONObject picto;
        private Pictogram pictogram;


        public VincularAsync(FindAllPictogramsHolder mHolder, int mPosition) {
            this.mHolder = mHolder;
            this.mPosition = mPosition;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            json = Json.getInstance();
            Json.getInstance().setmContext(mContext);
        }


        @Override
        protected Void doInBackground(Void... voids) {try {
                picto = mArrayPictos.getJSONObject(mPosition);
                if(picto!=null){
                    mHolder.pictoView.setUpContext(mContext);
                    mHolder.pictoView.setUpGlideAttatcher(mContext);
                }
            } catch (Exception e) {
                e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
                try {
                    if(picto!=null) {
                        mHolder.pictoView.setPictogramsLibraryPictogram(new Pictogram(picto,ConfigurarIdioma.getLanguaje()));
                        loadDrawable(json.getId(picto), mHolder.pictoView);
                    }
                } catch (Exception ex) {
                    ex.getMessage();
                }

        }

    }

    @SuppressLint("NotifyDataSetChanged")
    public FindAllPictogramsAdapter removeOldFiles(){
        JSONArray aux = new JSONArray();
        for (int i = 0; i < mArrayPictos.length(); i++) {
            try {
                if(mArrayPictos.getJSONObject(i).has("imagen")){
                    aux.put(mArrayPictos.getJSONObject(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mArrayPictos = aux;
        notifyDataSetChanged();
        return this;
    }
}

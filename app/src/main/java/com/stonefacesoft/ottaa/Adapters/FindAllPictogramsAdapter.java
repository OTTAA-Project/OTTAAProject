package com.stonefacesoft.ottaa.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;
import com.stonefacesoft.ottaa.Helper.ItemTouchHelperAdapter;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.ConnectionDetector;
import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;
import com.stonefacesoft.ottaa.utils.JSONutils;
import com.stonefacesoft.ottaa.utils.MemoryUtils;
import com.stonefacesoft.pictogramslibrary.Classes.Pictogram;
import com.stonefacesoft.pictogramslibrary.utils.GlideAttatcher;
import com.stonefacesoft.pictogramslibrary.view.PictoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FindAllPictogramsAdapter extends RecyclerView.Adapter<FindAllPictogramsAdapter.FindAllPictogramsHolder> implements ListPreloader.PreloadModelProvider, ItemTouchHelperAdapter {
    private final String TAG = "FindAllPictogramsAD";
    //private Context mContext;
    private final int layoutID;
    private JSONArray mSelectedPictos;
    private final ArrayList<Integer> listadoIdPictos;
    private final Context mContext;
    private JSONArray mVincularArray;
    private boolean esFiltrado;
    private Json json;


    public FindAllPictogramsAdapter(Context mContext, int layoutID, JSONArray mVincularArray, boolean filtro) {
        this.mContext = mContext;
        this.layoutID = layoutID;
        this.mVincularArray = mVincularArray;
        this.esFiltrado = filtro;
        this.mSelectedPictos = new JSONArray();
        this.listadoIdPictos = new ArrayList<>();


    }

    @Override
    public FindAllPictogramsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(layoutID, parent, false);
        return new FindAllPictogramsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FindAllPictogramsHolder holder, int position) {
        holder.setIsRecyclable(true);
        if(!mVincularArray.isNull(position)){
            new VincularAsync(holder, position).execute();
        }
        else{
            mVincularArray.remove(position);
        }
    }

    @Override
    public void setStateRestorationPolicy(@NonNull StateRestorationPolicy strategy) {
        super.setStateRestorationPolicy(strategy);
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull FindAllPictogramsHolder holder) {
        return super.onFailedToRecycleView(holder);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull FindAllPictogramsHolder holder) {
        super.onViewDetachedFromWindow(holder);
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
    public JSONArray getmArrayPictos() {
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
        boolean showKindOfView = false;
        if (json.getPictoFromId2(id)== null){
            showKindOfView = true;
        }
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
                picto = mVincularArray.getJSONObject(mPosition);
                pictogram = new Pictogram(picto,ConfigurarIdioma.getLanguaje());
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
                        mHolder.pictoView.setPictogramsLibraryPictogram(pictogram);
                        loadDrawable(json.getId(picto), mHolder.pictoView);
                    }
                } catch (Exception ex) {
                    ex.getMessage();
                }

        }

    }

    public FindAllPictogramsAdapter removeOldFiles(){
        JSONArray aux = new JSONArray();
        for (int i = 0; i < mVincularArray.length(); i++) {
            try {
                if(mVincularArray.getJSONObject(i).has("imagen")){
                    aux.put(mVincularArray.getJSONObject(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mVincularArray = aux;
        notifyDataSetChanged();
        return this;
    }
}

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
import com.google.firebase.auth.FirebaseAuth;
import com.stonefacesoft.ottaa.FirebaseRequests.SubirArchivosFirebase;
import com.stonefacesoft.ottaa.Helper.ItemTouchHelperAdapter;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;
import com.stonefacesoft.ottaa.utils.constants.Constants;
import com.stonefacesoft.pictogramslibrary.Classes.Pictogram;
import com.stonefacesoft.pictogramslibrary.view.PictoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;


public class GaleriaPictosAdapter extends RecyclerView.Adapter<GaleriaPictosAdapter.PictosViewHolder> implements ItemTouchHelperAdapter, ListPreloader.PreloadModelProvider {

    private final Context mContext;
    private final int layoutID;
    private JSONArray mArrayPictos;
    private Json json;
    private final SubirArchivosFirebase uploadFirebaseFile;
    private final FirebaseAuth mAuth;
    private static final String TAG = "GaleriaPictosAdapter";
    private int cantCambios;



    public GaleriaPictosAdapter(Context mContext,JSONArray mArrayPictos, int layoutID, FirebaseAuth auth ) {
        this.mContext = mContext;
        this.layoutID = layoutID;
        this.mArrayPictos = mArrayPictos;
        this.uploadFirebaseFile = new SubirArchivosFirebase(mContext);
        this.mAuth = auth;
        removeOldFiles();
    }
    public GaleriaPictosAdapter removeOldFiles(){
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



    @Override
    public PictosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(layoutID, parent, false);

        return new PictosViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PictosViewHolder holder, int position) {
        if(!mArrayPictos.isNull(position)) {
            new cargarPictosAsync(holder, position).execute();
        }else{
            try {
                mArrayPictos.remove(position);
                notifyDataSetChanged();
            }catch (Exception ex){
                onViewDetachedFromWindow(holder);
            }
        }
    }



    @Override
    public void onViewAttachedToWindow(@NonNull PictosViewHolder holder) {

    }

    @Override
    public void onViewDetachedFromWindow(@NonNull PictosViewHolder holder) {
        if(holder.pictoView == null){
            mArrayPictos.remove(holder.position);
            notifyDataSetChanged();
        }
    }

    //metodo que se encarga de mover el json
    @Override
    public void onItemMove(int fromIndex, int toIndex) {
                try {
                    JSONObject json1 = mArrayPictos.getJSONObject(fromIndex);
                    JSONObject json2 = mArrayPictos.getJSONObject(toIndex);
                    mArrayPictos.put(toIndex, json1);
                    mArrayPictos.put(fromIndex, json2);

                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    notifyItemMoved(fromIndex, toIndex);
                    if (toIndex > fromIndex)
                        notifyItemRangeChanged(fromIndex, toIndex - fromIndex + 1);
                    else if (toIndex < fromIndex)
                        notifyItemRangeChanged(toIndex, fromIndex - toIndex + 1);

                }
        }




    //Seteamos y obtenemos el array de pictos modificados, para cuando lo cambiamos de lugar y cuando buscamos en el searchview
    public JSONArray getmArrayPictos() {
        return mArrayPictos;
    }

    public JSONArray getmArrayPictosRelacion() {
        JSONArray mArrayRelacion = new JSONArray();
        for (int i = 0; i < mArrayPictos.length(); i++) {

                try {
                    mArrayRelacion.put(i, new JSONObject().put("id", mArrayPictos.getJSONObject(i).getInt("id")));
                } catch (JSONException e) {
                    e.printStackTrace();

            }

        }
        return mArrayRelacion;
    }

    public void setmArrayPictos(JSONArray mArrayPictos) {
        this.mArrayPictos = mArrayPictos;

    }




    public void onDropItem() {
        uploadFirebaseFile.subirGruposFirebase(uploadFirebaseFile.getmDatabase(mAuth, Constants.Grupos), uploadFirebaseFile.getmStorageRef(mAuth, Constants.Grupos));
    }


    @Override
    public void onItemDismiss(int position) {
        if (mArrayPictos.length() < position) {
            mArrayPictos.remove(position);
            notifyItemRemoved(position);
            notifyDataSetChanged();

        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (mArrayPictos != null)
            return mArrayPictos.length();
        return 0;
    }

    @Override
    public void onViewRecycled(@NonNull PictosViewHolder holder) {
        super.onViewRecycled(holder);

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
    //
    @Nullable
    @Override
    public RequestBuilder<?> getPreloadRequestBuilder(@NonNull Object item) {
        return null;
    }

    public class PictosViewHolder extends RecyclerView.ViewHolder {
        int position = -1;
        PictoView pictoView;
        public PictosViewHolder(View itemView) {
            super(itemView);
            pictoView = itemView.findViewById(R.id.pictogram);
        }
    }



    public class cargarPictosAsync extends AsyncTask<Void, Void, Void> {

        //Pasamos el holder y la posicion que nos da el onBindViewHolder para poder asignarle a cada elemento
        // en el onPostExecute que cargue el texto y la imagen de ese picto.
        //De esta forma el onBindViewHolder ejecuta el AsyncTask y esto va cargando asyncronamente mientras se desplaza
        private final PictosViewHolder mHolder;
        private final int mPosition;
        private JSONObject aux;

        cargarPictosAsync(PictosViewHolder holder, int position) {
            this.mHolder = holder;
            this.mPosition = position;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            json = Json.getInstance();
            Json.getInstance().setmContext(mContext);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                aux = mArrayPictos.getJSONObject(mPosition);
                mHolder.position = mPosition;
                if(aux !=  null){
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
                if (aux != null) {
                    mHolder.pictoView.setPictogramsLibraryPictogram(new Pictogram(aux, ConfigurarIdioma.getLanguaje()));
                }
            } catch (Exception ex) {

            }
        }
    }


}

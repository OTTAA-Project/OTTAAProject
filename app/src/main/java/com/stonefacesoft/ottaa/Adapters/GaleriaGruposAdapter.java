package com.stonefacesoft.ottaa.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.stonefacesoft.ottaa.FirebaseRequests.SubirArchivosFirebase;
import com.stonefacesoft.ottaa.Helper.ItemTouchHelperAdapter;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;
import com.stonefacesoft.ottaa.utils.Constants;
import com.stonefacesoft.ottaa.utils.JSONutils;
import com.stonefacesoft.pictogramslibrary.Classes.Group;
import com.stonefacesoft.pictogramslibrary.Classes.Pictogram;
import com.stonefacesoft.pictogramslibrary.utils.GlideAttatcher;
import com.stonefacesoft.pictogramslibrary.view.GroupView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class GaleriaGruposAdapter extends RecyclerView.Adapter<GaleriaGruposAdapter.GruposViewHolder> implements ItemTouchHelperAdapter {

    private final Context mContext;
    private final int layoutID;
    private JSONArray mArrayGrupos;
    private Json json;
    private final SubirArchivosFirebase uploadFirebaseFile;
    private final FirebaseAuth mAuth;
    private static final String TAG = "GaleriaGruposAdapter";
    private final GlideAttatcher glideAttatcher; // esto se encarga de adjuntar el glide


    public GaleriaGruposAdapter(Context mContext, int layoutID, JSONArray mArrayGrupos, FirebaseAuth mAuth) {
        this.mContext = mContext;
        this.layoutID = layoutID;
        this.uploadFirebaseFile = new SubirArchivosFirebase(mContext);
        this.mArrayGrupos = mArrayGrupos;
        this.mAuth = mAuth;
        glideAttatcher=new GlideAttatcher(this.mContext);
    }
    public GaleriaGruposAdapter(Context mContext, int layoutID, FirebaseAuth mAuth) {
        this.mContext = mContext;
        this.layoutID = layoutID;
        this.uploadFirebaseFile = new SubirArchivosFirebase(mContext);
        this.mAuth = mAuth;
        glideAttatcher=new GlideAttatcher(this.mContext);
        json = Json.getInstance();
        Json.getInstance().setmContext(mContext);
        if(mArrayGrupos==null)
            mArrayGrupos = json.getmJSONArrayTodosLosGrupos();
    }

    @Override
    public GruposViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(layoutID, parent, false);
        return new GruposViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GruposViewHolder holder, int position) {
        new CargarGruposAsync(position, holder).execute();
    }


    //Encargados de setear y obtener el array de los grupos mediante sus cambios
    public JSONArray getmArrayGrupos() {
        return mArrayGrupos;
    }

    public void setmArrayGrupos(JSONArray mArrayGrupos) {
        this.mArrayGrupos = mArrayGrupos;
    }

    public void onDropItem() {



    }



    @Override
    public void onItemMove(int fromIndex, int toIndex) {
        try {
            JSONObject json1 = mArrayGrupos.getJSONObject(fromIndex);
            JSONObject json2 = mArrayGrupos.getJSONObject(toIndex);
            mArrayGrupos.put(toIndex, json1);
            mArrayGrupos.put(fromIndex, json2);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        notifyItemMoved(fromIndex,toIndex);
        if(toIndex>fromIndex)
            notifyItemRangeChanged(fromIndex,toIndex-fromIndex+1);
        else if(toIndex<fromIndex)
            notifyItemRangeChanged(toIndex,fromIndex-toIndex+1);

    }

    @Override
    public void onItemDismiss(int position) {
        if (mArrayGrupos.length() < position) {
            mArrayGrupos.remove(position);
            notifyItemRemoved(position);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        if (mArrayGrupos == null)
            return 0;
        else
            return mArrayGrupos.length();
    }

    @Override
    public void onViewRecycled(@NonNull GruposViewHolder holder) {
        super.onViewRecycled(holder);
        glideAttatcher.clearMemory();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class GruposViewHolder extends RecyclerView.ViewHolder {

        GroupView groupView;
        public GruposViewHolder(View itemView) {
            super(itemView);
            groupView = itemView.findViewById(R.id.group);
        }
    }

    private class CargarGruposAsync extends AsyncTask<Void, Void, Void> {

        private final int mPosition;
        private final GruposViewHolder mHolder;
        private JSONObject aux;

        public CargarGruposAsync(int mPosition, GruposViewHolder mHolder) {
            this.mPosition = mPosition;
            this.mHolder = mHolder;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            Bitmap mBitmap;
            try {
                aux = mArrayGrupos.getJSONObject(mPosition);
                mHolder.groupView.setUpContext(mContext);
                mHolder.groupView.setUpGlideAttatcher(mContext);

            } catch (Exception e) {
                Log.e(TAG, "doInBackground: " + e.getMessage());

                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try{
                if(aux.has("imagen")&&!aux.isNull("imagen")) {
                    mHolder.groupView.setPictogramsLibraryGroup(new Group(aux, ConfigurarIdioma.getLanguaje()));
                    mHolder.groupView.loadAgeIcon(json.tieneTag(aux, Constants.EDAD));
                    mHolder.groupView.loadGenderIcon(json.tieneTag(aux, Constants.SEXO));
                    mHolder.groupView.loadLocationIcon(json.tieneTag(aux, Constants.UBICACION));
                    mHolder.groupView.loadHourIcon(json.tieneTag(aux, Constants.HORA));
                }
            }catch (Exception ex){
                Log.d(TAG, "auxException: "+ aux.toString() );
                notifyItemRemoved(mPosition);
            }

            //Le asignamos al grupo su texto e icono
            // Glide.with(mContext).load(mDrawableIcono).transform(new RoundedCorners(16)).into(mHolder.mGrupoImageView);
            //   mHolder.mGrupoImageView.setImageDrawable(mDrawableIcono);

        }
    }





}

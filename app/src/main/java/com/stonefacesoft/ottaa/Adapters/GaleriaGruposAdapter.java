package com.stonefacesoft.ottaa.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.stonefacesoft.ottaa.utils.Constants;
import com.stonefacesoft.pictogramslibrary.Classes.Pictogram;
import com.stonefacesoft.pictogramslibrary.utils.GlideAttatcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class GaleriaGruposAdapter extends RecyclerView.Adapter<GaleriaGruposAdapter.GruposViewHolder> implements ItemTouchHelperAdapter {

    private Context mContext;
    private int layoutID;
    private JSONArray mArrayGrupos;
    private Json json;
    private SubirArchivosFirebase uploadFirebaseFile;
    private FirebaseAuth mAuth;
    private static final String TAG = "GaleriaGruposAdapter";
    private GlideAttatcher glideAttatcher; // esto se encarga de adjuntar el glide


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

        ImageView mGrupoImageView, imageTimer, imageLocation, imageEdad, imageEvent;
        TextView mTextGrupo, textCargarGrupos;

        public GruposViewHolder(View itemView) {
            super(itemView);

            mTextGrupo = itemView.findViewById(R.id.grid_text);
            mGrupoImageView = itemView.findViewById(R.id.grid_image);
            imageTimer = itemView.findViewById(R.id.tagHora);
            imageLocation = itemView.findViewById(R.id.tagUbicacion);
            imageEdad = itemView.findViewById(R.id.tagClima);
            imageEvent = itemView.findViewById(R.id.tagCalendario);
            textCargarGrupos = itemView.findViewById(R.id.textoCargandoGrupos);

        }
    }

    private class CargarGruposAsync extends AsyncTask<Void, Void, Void> {

        private final int mPosition;
        private final GruposViewHolder mHolder;
        private String mStringTexto;
        private Drawable mDrawableIcono;

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
                mStringTexto = json.getNombre(mArrayGrupos.getJSONObject(mPosition));
                mDrawableIcono = json.getIcono(mArrayGrupos.getJSONObject(mPosition));
                if (mDrawableIcono == null)
                    mDrawableIcono = AppCompatResources.getDrawable(mContext, R.drawable.ic_cloud_download_orange);
                mBitmap = ThumbnailUtils.extractThumbnail(((BitmapDrawable) mDrawableIcono).getBitmap(), 150, 150);
                mDrawableIcono = new BitmapDrawable(mContext.getResources(), mBitmap);
                mHolder.imageLocation.setImageResource(json.tieneTag(mArrayGrupos.getJSONObject(mPosition), Constants.UBICACION) ? R.drawable.ic_location_on_black_24dp : R.drawable.ic_location_off_black_24dp);
                mHolder.imageTimer.setImageResource(json.tieneTag(mArrayGrupos.getJSONObject(mPosition), Constants.HORA) ? R.drawable.ic_timer_black_24dp : R.drawable.ic_baseline_timer_off_gray_24);
                mHolder.imageEvent.setImageResource(json.tieneTag(mArrayGrupos.getJSONObject(mPosition), Constants.SEXO) ? R.drawable.ic_wc_black_24dp : R.drawable.ic_wc_block_24dp);
                mHolder.imageEdad.setImageResource(json.tieneTag(mArrayGrupos.getJSONObject(mPosition), Constants.EDAD) ? R.drawable.ic_face_black_on_24dp : R.drawable.ic_face_black_24dp);

            } catch (Exception e) {
                Log.e(TAG, "doInBackground: " + e.getMessage());

                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //Le asignamos al grupo su texto e icono
            mHolder.mTextGrupo.setText(mStringTexto);
            try {
                Log.d(TAG, "loadDrawable: "+ mArrayGrupos.getJSONObject(mPosition).toString());
                Pictogram pictogram=new Pictogram(mArrayGrupos.getJSONObject(mPosition),json.getIdioma());
                loadDrawable(glideAttatcher,pictogram,mHolder.mGrupoImageView);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Glide.with(mContext).load(mDrawableIcono).transform(new RoundedCorners(16)).into(mHolder.mGrupoImageView);
            //   mHolder.mGrupoImageView.setImageDrawable(mDrawableIcono);

        }
    }

        public  void loadDrawable(GlideAttatcher attatcher, Pictogram pictogram, ImageView imageView){
            if(pictogram.getEditedPictogram().isEmpty()){
                JSONObject picto=pictogram.toJsonObject();
                Log.d(TAG, "loadDrawable: "+ picto.toString());
               Drawable drawable=json.getIcono(picto);
                if(drawable!=null)
                    attatcher.loadCircleDrawable(drawable,imageView);
                else
                    attatcher.loadCircleDrawable(mContext.getResources().getDrawable(R.drawable.ic_cloud_download_orange),imageView);
            }else{
                File picto=new File(pictogram.getEditedPictogram());
                if(picto.exists())
                    attatcher.loadCircleDrawable(picto,imageView);
                else
                    attatcher.loadCircleDrawable(Uri.parse(pictogram.getUrl()),imageView);
            }
    }



}

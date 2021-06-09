package com.stonefacesoft.ottaa.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
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
import com.google.firebase.auth.FirebaseAuth;
import com.stonefacesoft.ottaa.FirebaseRequests.SubirArchivosFirebase;
import com.stonefacesoft.ottaa.Helper.ItemTouchHelperAdapter;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.Constants;
import com.stonefacesoft.ottaa.utils.JSONutils;
import com.stonefacesoft.pictogramslibrary.Classes.Pictogram;
import com.stonefacesoft.pictogramslibrary.utils.GlideAttatcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Collections;
import java.util.List;

//import android.support.v7.content.res.AppCompatResources;
//import android.support.v7.widget.RecyclerView;

public class GaleriaPictosAdapter extends RecyclerView.Adapter<GaleriaPictosAdapter.PictosViewHolder> implements ItemTouchHelperAdapter, ListPreloader.PreloadModelProvider {

    private final Context mContext;
    private final int layoutID;
    private JSONArray mArrayPictos;
    private Json json;
    private final SubirArchivosFirebase uploadFirebaseFile;
    private final FirebaseAuth mAuth;
    private static final String TAG = "GaleriaPictosAdapter";
    private final GlideAttatcher glideAttatcher; // esto se encarga de adjuntar el glide
    private int cantCambios;



    public GaleriaPictosAdapter(Context mContext,JSONArray mArrayPictos, int layoutID, FirebaseAuth auth ) {
        this.mContext = mContext;
        this.layoutID = layoutID;
        this.mArrayPictos = mArrayPictos;
        this.uploadFirebaseFile = new SubirArchivosFirebase(mContext);
        this.mAuth = auth;
    }
    public GaleriaPictosAdapter loadGlideAttacher(){
        glideAttatcher=new GlideAttatcher(mContext);
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
        new cargarPictosAsync(holder, position).execute();
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

    @Override
    public void onViewRecycled(@NonNull PictosViewHolder holder) {
        super.onViewRecycled(holder);
        glideAttatcher.clearMemory();
    }

    // precargamos las imagenes en glide
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
        //json.getPictoFromCustomArrayById2(json.getmJSONArrayTodosLosPictos(),mVincularArray.getJSONObject(mPosition).getInt("id"));

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

        ImageView mPictoImageView;
        ImageView mPictoImageColor;
        TextView mTextoPicto;

        public PictosViewHolder(View itemView) {
            super(itemView);

            mTextoPicto = itemView.findViewById(R.id.grid_text);
            mPictoImageView = itemView.findViewById(R.id.grid_image);
            mPictoImageColor = itemView.findViewById(R.id.color_Picto);
        }



    }

    public class cargarPictosAsync extends AsyncTask<Void, Void, Void> {

        //Pasamos el holder y la posicion que nos da el onBindViewHolder para poder asignarle a cada elemento
        // en el onPostExecute que cargue el texto y la imagen de ese picto.
        //De esta forma el onBindViewHolder ejecuta el AsyncTask y esto va cargando asyncronamente mientras se desplaza
        private String mStringTexto="";
        private Drawable mDrawableIcono;
        private final PictosViewHolder mHolder;
        private final int mPosition;

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

            Bitmap mBitmap;
            try {
                SharedPreferences sharedPrefsDefault = PreferenceManager.getDefaultSharedPreferences(mContext);
                mStringTexto = JSONutils.getNombre(mArrayPictos.getJSONObject(mPosition),sharedPrefsDefault.getString(mContext.getString(R.string.str_idioma), "en"));
                mDrawableIcono = json.getIcono(mArrayPictos.getJSONObject(mPosition));

                if (mDrawableIcono == null)
                    mDrawableIcono = AppCompatResources.getDrawable(mContext, R.drawable.ic_cloud_download_orange);

                mBitmap = ThumbnailUtils.extractThumbnail(((BitmapDrawable) mDrawableIcono).getBitmap(), 150, 150);
                mDrawableIcono = new BitmapDrawable(mContext.getResources(), mBitmap);



            } catch (Exception e) {
                e.getMessage();
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //Le asignamos al picto su texto e icono, junto al color
            try {
                if (mArrayPictos.getJSONObject(mPosition) != null) {
                    mHolder.mTextoPicto.setText(mStringTexto);
                    Pictogram pictogram=new Pictogram(mArrayPictos.getJSONObject(mPosition),json.getIdioma());
                    loadDrawable(glideAttatcher,pictogram,mHolder.mPictoImageView);
                    mHolder.mPictoImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    try {
                        mHolder.mPictoImageColor.setColorFilter(cargarColor(JSONutils.getTipo(mArrayPictos.getJSONObject(mPosition))));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    public  void loadDrawable(GlideAttatcher attatcher, Pictogram pictogram, ImageView imageView){
        if(pictogram.getEditedPictogram().isEmpty()){
            Drawable drawable=json.getIcono(pictogram.toJsonObject());
            if(drawable!=null)
                attatcher.UseCornerRadius(true).loadDrawable(drawable,imageView);
            else
                attatcher.UseCornerRadius(true).loadDrawable(mContext.getResources().getDrawable(R.drawable.ic_cloud_download_orange),imageView);
        }else{
            File picto=new File(pictogram.getEditedPictogram());
            if(picto.exists())
                attatcher.UseCornerRadius(true).loadDrawable(picto,imageView);
            else
                attatcher.UseCornerRadius(true).loadDrawable(Uri.parse(pictogram.getUrl()),imageView);
        }
    }


}

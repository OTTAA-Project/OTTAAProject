package com.stonefacesoft.ottaa.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
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
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.JSONutils;
import com.stonefacesoft.pictogramslibrary.Classes.Pictogram;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.stonefacesoft.pictogramslibrary.utils.GlideAttatcher;

public class VincularPictosAdapter extends RecyclerView.Adapter<VincularPictosAdapter.VincularViewHolder> implements ListPreloader.PreloadModelProvider {

    //private Context mContext;
    private int layoutID;
    private JSONArray mVincularArray;
    private JSONArray mSelectedPictos;
    private boolean esFiltrado;
    private Json json;
    private ArrayList<Integer> listadoIdPictos;
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
        this.glideAttatcher=new GlideAttatcher(this.mContext);


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

        ImageView mPictoImageView;
        ImageView mPictoImageColor;
        TextView mTextoPicto;
        boolean isSelected;

        public VincularViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            mTextoPicto = itemView.findViewById(R.id.grid_text);
            mPictoImageView = itemView.findViewById(R.id.grid_image);
            mPictoImageColor = itemView.findViewById(R.id.color_Picto);
            itemView.setBackgroundColor(mContext.getResources().getColor(R.color.White));

        }

        @Override
        public void onClick(View v) {
            try {
                if (!esFiltrado) {
                    if (!isSelected) {
                        v.setBackground(mContext.getResources().getDrawable(R.drawable.picto_shape_select));
                        isSelected = true;
                        mSelectedPictos.put(json.getPictoFromCustomArrayById2(mVincularArray, mVincularArray.getJSONObject(getAdapterPosition()).getInt("id")));
                    } else {
                        v.setBackground(mContext.getResources().getDrawable(R.drawable.picto_shape));
                        isSelected = false;
                        int removePos = json.getPosPicto(mSelectedPictos, mVincularArray.getJSONObject(getAdapterPosition()).getInt("id"));
                        if (removePos != -1)
                            mSelectedPictos.remove(removePos);
                    }

                } else {
                    if (!isSelected) {
                        v.setBackground(mContext.getResources().getDrawable(R.drawable.picto_shape_select));
                        isSelected = true;
                        mSelectedPictos.put(json.getPictoFromCustomArrayById2(mVincularArray, mVincularArray.getJSONObject(getAdapterPosition()).getInt("id")));
                    } else {
                        v.setBackground(mContext.getResources().getDrawable(R.drawable.picto_shape));

                        isSelected = false;
                        int removePos = json.getPosPicto(mSelectedPictos, mVincularArray.getJSONObject(getAdapterPosition()).getInt("id"));
                        if (removePos != -1)
                            mSelectedPictos.remove(removePos);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("TAG", "onClick: error al filtrar pictograma" );
            }
        }

        public int isSelected(int position){
            int posSelected=-1;
            if(!isEsFiltrado()){
                try {
                   posSelected= json.getPosPicto(mSelectedPictos,mVincularArray.getJSONObject(position).getInt("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    posSelected= json.getPosPicto(mSelectedPictos,mVincularArray.getJSONObject(position).getInt("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(posSelected!=-1)
            isSelected=true;
           return posSelected;
        }


    }

    private class VincularAsync extends AsyncTask<Void, Void, Void> {

        private String mStringTexto;
        private Drawable mDrawableIcono;
        private VincularViewHolder mHolder;
        private int mPosition;
        private JSONObject picto;
        private Handler handler = new Handler();




        public VincularAsync(VincularViewHolder mHolder, int mPosition) {

            this.mHolder = mHolder;
            this.mPosition = mPosition;

        }

        @Override
        protected Void doInBackground(Void... voids) {
            Bitmap mBitmap;

            try {
                picto = json.getPictoFromId2(mVincularArray.getJSONObject(mPosition).getInt("id"));
                //json.getPictoFromCustomArrayById2(json.getmJSONArrayTodosLosPictos(),mVincularArray.getJSONObject(mPosition).getInt("id"));
                SharedPreferences sharedPrefsDefault = PreferenceManager.getDefaultSharedPreferences(mContext);
                mStringTexto = JSONutils.getNombre(picto,sharedPrefsDefault.getString(mContext.getString(R.string.str_idioma), "en"));
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
                    mHolder.mTextoPicto.setText(mStringTexto);
                    Pictogram pictogram=new Pictogram(mVincularArray.getJSONObject(mPosition),json.getIdioma());
                    loadDrawable(glideAttatcher,pictogram,mHolder.mPictoImageView);
                    try {
                        mHolder.mPictoImageColor.setColorFilter(cargarColor(JSONutils.getTipo(mVincularArray.getJSONObject(mPosition))));
                        if(mHolder.isSelected(mPosition)!=-1)
                            mHolder.itemView.setBackground(mContext.getResources().getDrawable(R.drawable.picto_shape_select));
                        else
                            mHolder.itemView.setBackground(mContext.getResources().getDrawable(R.drawable.picto_shape));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
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

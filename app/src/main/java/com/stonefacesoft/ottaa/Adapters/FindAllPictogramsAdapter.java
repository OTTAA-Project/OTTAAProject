package com.stonefacesoft.ottaa.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
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
import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;
import com.stonefacesoft.ottaa.utils.JSONutils;
import com.stonefacesoft.pictogramslibrary.Classes.Pictogram;
import com.stonefacesoft.pictogramslibrary.utils.GlideAttatcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FindAllPictogramsAdapter extends RecyclerView.Adapter<FindAllPictogramsAdapter.FindAllPictogramsHolder> implements ListPreloader.PreloadModelProvider, ItemTouchHelperAdapter {
    private final String TAG = "FindAllPictogramsAD";
    //private Context mContext;
    private final int layoutID;
    private final JSONArray mSelectedPictos;
    private final Json json;
    private final ArrayList<Integer> listadoIdPictos;
    private final GlideAttatcher glideAttatcher;
    private final Context mContext;
    private JSONArray mVincularArray;
    private boolean esFiltrado;

    public FindAllPictogramsAdapter(Context mContext, int layoutID, JSONArray mVincularArray, boolean filtro) {
        this.mContext = mContext;
        Json.getInstance().setmContext(this.mContext);
        this.json = Json.getInstance();
        this.layoutID = layoutID;
        this.mVincularArray = mVincularArray;
        this.esFiltrado = filtro;
        this.mSelectedPictos = new JSONArray();
        this.listadoIdPictos = new ArrayList<>();
        this.glideAttatcher = new GlideAttatcher(this.mContext);


    }

    @Override
    public FindAllPictogramsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(layoutID, parent, false);

        return new FindAllPictogramsAdapter.FindAllPictogramsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FindAllPictogramsHolder holder, int position) {
        new FindAllPictogramsAdapter.VincularAsync(holder, position).execute();
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
    public void onViewRecycled(@NonNull FindAllPictogramsAdapter.FindAllPictogramsHolder holder) {
        super.onViewRecycled(holder);
        glideAttatcher.clearMemory();
    }

    public void loadDrawable(GlideAttatcher attatcher, Pictogram pictogram, ImageView imageView,ImageView kindOfView) {
        boolean showKindOfView=false;
        if (pictogram.getEditedPictogram().isEmpty()) {
            Drawable drawable = json.getIcono(pictogram.toJsonObject());
            showKindOfView=false;
            if (drawable != null)
                attatcher.UseCornerRadius(true).loadDrawable(drawable, imageView);
            else
                attatcher.UseCornerRadius(true).loadDrawable(mContext.getResources().getDrawable(R.drawable.ic_cloud_download_orange), imageView);
        } else {
            File picto = new File(pictogram.getEditedPictogram());
            if (picto.exists())
                attatcher.UseCornerRadius(true).loadDrawable(picto, imageView);
            else{
                attatcher.UseCornerRadius(true).loadDrawable(Uri.parse(pictogram.getUrl()), imageView);
                showKindOfView=true;
            }

        }
        if(showKindOfView){
            kindOfView.setVisibility(View.VISIBLE);
            attatcher.UseCornerRadius(true).loadDrawable(mContext.getResources().getDrawable(R.drawable.ic_cloud_download_orange),kindOfView);
        }else{
            kindOfView.setVisibility(View.GONE);
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

        ImageView mPictoImageView;
        ImageView mPictoImageColor;
        ImageView mKindOfPictogram;
        TextView mTextoPicto;
        boolean isSelected;

        public FindAllPictogramsHolder(View itemView) {
            super(itemView);
            mTextoPicto = itemView.findViewById(R.id.grid_text);
            mPictoImageView = itemView.findViewById(R.id.Imagen_Picto);
            mPictoImageColor = itemView.findViewById(R.id.color_Picto);
            mKindOfPictogram = itemView.findViewById(R.id.kind_of_Picto);
            itemView.setBackgroundColor(mContext.getResources().getColor(R.color.White));
        }
    }

    private class VincularAsync extends AsyncTask<Void, Void, Void> {

        private final FindAllPictogramsAdapter.FindAllPictogramsHolder mHolder;
        private final int mPosition;
        private final Handler handler = new Handler();
        private String mStringTexto;
        private Drawable mDrawableIcono;
        private JSONObject picto;


        public VincularAsync(FindAllPictogramsAdapter.FindAllPictogramsHolder mHolder, int mPosition) {

            this.mHolder = mHolder;
            this.mPosition = mPosition;

        }

        @Override
        protected Void doInBackground(Void... voids) {
            Bitmap mBitmap;

            try {
                processPictogram(mVincularArray.getJSONObject(mPosition));
                mStringTexto = JSONutils.getNombre(picto, ConfigurarIdioma.getLanguaje());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (picto != null) {
                try {
                    mHolder.mTextoPicto.setText(mStringTexto);
                    Pictogram pictogram = new Pictogram(mVincularArray.getJSONObject(mPosition), ConfigurarIdioma.getLanguaje());
                    loadDrawable(glideAttatcher, pictogram, mHolder.mPictoImageView,mHolder.mKindOfPictogram);
                    mHolder.mPictoImageColor.setColorFilter(cargarColor(JSONutils.getTipo(picto)));
                    mHolder.itemView.setBackground(mContext.getResources().getDrawable(R.drawable.picto_shape));

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        }

        public void processPictogram(JSONObject object) {

            try {
                picto = json.getPictoFromId2(object.getInt("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (picto == null) {
                picto = object;
            }

        }
    }
}

package com.stonefacesoft.ottaa.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.stonefacesoft.ottaa.FirebaseRequests.SubirArchivosFirebase;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;
import com.stonefacesoft.ottaa.utils.Games.Juego;
import com.stonefacesoft.pictogramslibrary.Classes.GameGroup;
import com.stonefacesoft.pictogramslibrary.utils.GlideAttatcher;
import com.stonefacesoft.pictogramslibrary.view.GameGroupView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GaleriaJuegosAdapter extends RecyclerView.Adapter <GaleriaJuegosAdapter.GruposViewHolder>{
    private final Context mContext;
    private final int layoutID;
    private Json json;
    private final SubirArchivosFirebase uploadFirebaseFile;
    private final FirebaseAuth mAuth;
    private static final String TAG = "GaleriaGruposAdapter";
    private final int id;
    private final GlideAttatcher glideAttatcher;

    public GaleriaJuegosAdapter(Context mContext, int layoutID, FirebaseAuth mAuth,int id) {
        this.mContext = mContext;
        this.layoutID = layoutID;
        json=Json.getInstance();
        json.setmContext(this.mContext);
        this.uploadFirebaseFile = new SubirArchivosFirebase(mContext);
        this.mAuth = mAuth;
        this.id=id;
        glideAttatcher=new GlideAttatcher(this.mContext);

    }


    @Override
    public GaleriaJuegosAdapter.GruposViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(layoutID, parent, false);
        return new GaleriaJuegosAdapter.GruposViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GruposViewHolder holder, int position) {
        new GaleriaJuegosAdapter.CargarGruposAsync(position,holder).execute();
    }

    @Override
    public void onViewRecycled(@NonNull GruposViewHolder holder) {
        super.onViewRecycled(holder);
        glideAttatcher.clearMemory();
    }

    //Encargados de setear y obtener el array de los grupos mediante sus cambios
    public JSONArray getmArrayGrupos() {
        return json.getmJSONArrayTodosLosGrupos();
    }









    @Override
    public int getItemCount() {
        if (json.getmJSONArrayTodosLosGrupos() == null)
            return 0;
        else
            return json.getmJSONArrayTodosLosGrupos().length();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class GruposViewHolder extends RecyclerView.ViewHolder {

        private final GameGroupView mGameGroupView;

        public GruposViewHolder(View itemView) {
            super(itemView);
            mGameGroupView = itemView.findViewById(R.id.gamegroup);
        }
    }

    private class CargarGruposAsync extends AsyncTask<Void, Void, Void> {

        private final int mPosition;
        private final GaleriaJuegosAdapter.GruposViewHolder mHolder;
        private String mStringTexto;
        private JSONObject aux;
        private Drawable mDrawableIcono;

        public CargarGruposAsync(int mPosition, GaleriaJuegosAdapter.GruposViewHolder mHolder) {
            this.mPosition = mPosition;
            this.mHolder = mHolder;

        }

        public void execute(){
            Executor executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    Bitmap mBitmap;
                    try {
                        json = Json.getInstance();
                        aux = json.getmJSONArrayTodosLosGrupos().getJSONObject(mPosition);
                        mHolder.mGameGroupView.setUpContext(mContext);
                        mHolder.mGameGroupView.setUpGlideAttatcher(mContext);
                    } catch (Exception e) {
                        Log.e(TAG, "doInBackground: " + e.getMessage());

                        e.printStackTrace();
                    }
                    handler.post(()->{
                        try{
                            mHolder.mGameGroupView.setPictogramsLibraryGameGroup(new GameGroup(aux,ConfigurarIdioma.getLanguaje()));
                            int levelId=json.getId(json.getmJSONArrayTodosLosGrupos().getJSONObject(mPosition));
                            Juego juego=new Juego(mContext,id,levelId);
                            Drawable drawable=juego.getSmiley();
                            drawable.setTint(mContext.getResources().getColor(R.color.NaranjaOTTAA));
                            if(juego.getScoreClass().getIntentos()>0)
                                glideAttatcher.loadDrawable(drawable,mHolder.mGameGroupView.getKindOfPictogramImageView());
                            else
                                glideAttatcher.loadDrawable(mContext.getResources().getDrawable(R.drawable.ic_remove_orange_24dp),mHolder.mGameGroupView.getKindOfPictogramImageView());
                        }catch (Exception ex) {

                        }
                    });
                }
            });
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
                json = Json.getInstance();
                aux = json.getmJSONArrayTodosLosGrupos().getJSONObject(mPosition);
                this.mHolder.mGameGroupView.setUpContext(mContext);
                this.mHolder.mGameGroupView.setUpGlideAttatcher(mContext);
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

            //   mHolder.mGrupoImageView.setImageDrawable(mDrawableIcono);

        }
    }
}

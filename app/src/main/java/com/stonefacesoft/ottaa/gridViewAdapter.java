package com.stonefacesoft.ottaa;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.utils.StringFormatter;
import com.stonefacesoft.ottaa.utils.constants.Constants;
import com.stonefacesoft.ottaa.utils.JSONutils;
import com.stonefacesoft.ottaa.utils.exceptions.FiveMbException;
import com.stonefacesoft.pictogramslibrary.utils.GlideAttatcher;
import com.stonefacesoft.pictogramslibrary.utils.ValidateContext;
import com.stonefacesoft.pictogramslibrary.view.PictoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;


/**
 * Created by H&eacutector on 25/09/2015.
 * Edited By Gonzalo Juarez on 2/10/2020
 */


public class gridViewAdapter extends ArrayAdapter {
    private final Context context;
    private final int layoutResourceId;
    private ArrayList<JSONObject> data = new ArrayList();
    private ArrayList<JSONObject> data1 = new ArrayList();
    private final Json json;
    private final ArrayList<Integer> posicionPictos;
    private final ArrayList<int[][]> posicionPicto;
    private final DrawableManager drawableManager;
    private final ArrayList<JSONObject> pictosSeleccionados1;
    private final SharedPreferences sharedPrefsDefault;
    private final String lang;
    private final GlideAttatcher glideAttatcher;

    public gridViewAdapter(Context context, int layoutResourceId, ArrayList data, Json json) throws FiveMbException {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.json = json;
        glideAttatcher=new GlideAttatcher(this.context);
        pictosSeleccionados1 = new ArrayList<>();//genero el listado de pictos a elegir
        posicionPictos = new ArrayList<>();//genero el listado de pictos a elegir
        posicionPicto=new ArrayList<int[][]>();
        drawableManager = new DrawableManager();
        data1 = new ArrayList();//
        sharedPrefsDefault = PreferenceManager.getDefaultSharedPreferences(context);
        lang = sharedPrefsDefault.getString(context.getResources().getString(R.string.str_idioma),
                Locale.getDefault().getLanguage());
        data1.addAll(data);

    }

    public void setData1(ArrayList<JSONObject> data1) {
        this.data1 = data1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResourceId, null);
            holder = new ViewHolder();
            holder.pictoView = row.findViewById(R.id.pictogram);
            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }
        holder.position = position;

         if(posicionPictos.size()>0) {
            for(int i=0;i<posicionPicto.size();i++) {
              try {
                     if(posicionPicto.get(i)[0][1]==position) {
                         Log.d("gridView_getView_picto", ""+pictosSeleccionados1.get(i).toString());
                         Log.d("gridView_getView_pos", ""+posicionPictos.get(i));
                         Log.d("gridView_getView_json",json.getId(pictosSeleccionados1.get(i))+"");

                     }
              } catch (Exception e) {
                   e.printStackTrace();
               }
         }
        }

         new CargarRow(position, holder).execute();
        try {
            holder.pictoView.setCustom_Texto(JSONutils.getStringByApi(data.get(position)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.pictoView.setCustom_Color(cargarColor(JSONutils.getTypeAsInteger(data.get(position))));


       return row;
    }

    private Integer cargarColor (int tipo)
    {
        switch (tipo){
            case 1:
                return  context.getResources().getColor(R.color.Yellow);
            case 2:
                return  context.getResources().getColor(R.color.Orange);
            case 3:
                return  context.getResources().getColor(R.color.YellowGreen);
            case 4:
                return  context.getResources().getColor(R.color.DodgerBlue);
            case 5:
                return  context.getResources().getColor(R.color.Magenta);
            case 6:
                return  context.getResources().getColor(R.color.Black);
            default:
                return  context.getResources().getColor(R.color.White);
        }
    }



    public ArrayList<JSONObject> getPictosSeleccionados() {

        return pictosSeleccionados1;
    }


    private class CargarRow extends AsyncTask<Void, Void, Void> {
        private final int mPosition;
        private final ViewHolder mHolder;
        private String uri;
        //String texto;
        Drawable img;

        public CargarRow(int position, ViewHolder holder) {
            mPosition = position;
            mHolder = holder;
        }

        @Override
        protected Void doInBackground(Void... voids) {
                if (data.size() > 0){
                    try {
                        uri =JSONutils.getUriByApi(data.get(mPosition));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            return null;
        }

        /**
         * Una vez que paso el tiempo de espera ilumino la parte adecuando
         * @param unused
         */
        protected void onPostExecute(final Void unused) {
            if (mHolder.position == mPosition) {
                try {
                    glideAttatcher.loadDrawable( uri,mHolder.pictoView.getImageView());
                } catch (Exception e) {
                    e.printStackTrace();
                }
              //  glideAttatcher.loadDrawable(img,mHolder.pictoView.getImageView());
            }
        }
    }

    static class ViewHolder {
        PictoView pictoView;
        int position;
    }




}

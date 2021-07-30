package com.stonefacesoft.ottaa;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.utils.Constants;
import com.stonefacesoft.ottaa.utils.JSONutils;
import com.stonefacesoft.ottaa.utils.exceptions.FiveMbException;
import com.stonefacesoft.pictogramslibrary.utils.GlideAttatcher;

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
    private JSONArray data2 = new JSONArray();
    private final Json json;
    private final ArrayList<Integer> posicionPictos;
    private final ArrayList<int[][]> posicionPicto;
    private final boolean esInet;
    private final DrawableManager drawableManager;
    private final ArrayList<JSONObject> pictosSeleccionados1;
    private final SharedPreferences sharedPrefsDefault;
    private final String lang;
    private final GlideAttatcher glideAttatcher;

    public gridViewAdapter(Context context, int layoutResourceId, ArrayList data, Json json, boolean esInet) throws FiveMbException {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.json = json;
        this.esInet = esInet;
        glideAttatcher=new GlideAttatcher(this.context);
        pictosSeleccionados1 = new ArrayList<>();//genero el listado de pictos a elegir
        posicionPictos = new ArrayList<>();//genero el listado de pictos a elegir
        posicionPicto=new ArrayList<int[][]>();
        drawableManager = new DrawableManager();
        data1 = new ArrayList();//
        sharedPrefsDefault = PreferenceManager.getDefaultSharedPreferences(context);
        lang = sharedPrefsDefault.getString(context.getResources().getString(R.string.str_idioma),
                Locale.getDefault().getLanguage());

        data1.addAll(data);//tomo todosu el listado
        try {
            data2 = JSONutils.getHijosGrupo2(json.getmJSONArrayTodosLosPictos(),json.readJSONArrayFromFile(Constants.ARCHIVO_GRUPOS).getJSONObject(JSONutils.getIDfromNombre("ALL", json.readJSONArrayFromFile(Constants.ARCHIVO_GRUPOS))));
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            holder.imageTitle = row.findViewById(R.id.grid_text);
            holder.colorItem =row.findViewById(R.id.color_Picto);
            holder.image = row.findViewById(R.id.grid_image);
            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }
        holder.position = position;

       if(posicionPictos.size()>0)
       {for(int i=0;i<posicionPicto.size();i++) {

             try {

                    if(posicionPicto.get(i)[0][1]==position)
                    {
                        Log.d("gridView_getView_picto", ""+pictosSeleccionados1.get(i).toString());
                        Log.d("gridView_getView_pos", ""+posicionPictos.get(i));
                        Log.d("gridView_getView_json",json.getId(pictosSeleccionados1.get(i))+"");

                    }

             } catch (Exception e) {
                  e.printStackTrace();
              }


       }}/*else
        {

            color = 0xFF0000FF; // Opaque Blue

        }*/




//        new CargarRow(position, holder).execute();
        // vvvvvvvvvveeeeeeeeeeeeeerrrrrrrrrrrrrrrr http://lucasr.org/2012/04/05/performance-tips-for-androids-listview/
        if(esInet)
        {
            try {
                new CargarRow(position, holder).execute();
                holder.imageTitle.setText(data.get(position).getString("name"));
                holder.colorItem.setColorFilter(cargarColor(JSONutils.getWordType(data.get(position))));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else
        {
            new CargarImg(position, holder).execute();
            holder.imageTitle.setText(JSONutils.getNombre(data.get(position),sharedPrefsDefault.getString(context.getString(R.string.str_idioma), "en")));
            holder.colorItem.setColorFilter(cargarColor(JSONutils.getWordType(data.get(position))));

        }
//        }
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

    private class CargarImg extends AsyncTask<Void, Void, Void> {

        private final int mPosition;
        private final ViewHolder mHolder;
        //String texto;
        Drawable img;

        public CargarImg(int position, ViewHolder holder) {
            mPosition = position;
            mHolder = holder;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                img = json.getIcono(data.get(mPosition));
                Bitmap b;
                ThumbnailUtils thumbnailUtils = new ThumbnailUtils();

                b = ThumbnailUtils.extractThumbnail(((BitmapDrawable)img).getBitmap(),150,150);
                img = new BitmapDrawable(context.getResources(), b);
                }catch (Exception e){
                showToast(context.getString(R.string.sync_pictos));

                }
            return null;
        }

        //Creamos este metodo para poder llamar al custom_toast para evitar el error can't call this from a non-UI thread o
        //Can't create handler inside thread that has not called Looper.prepare() , por que no se puede crear un Toast adentro de un doInBackground
        public void showToast(final String toast) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    // do something

                    Toast.makeText(getContext(), toast, Toast.LENGTH_SHORT).show();
                }
            });

        }

        /**
         * Una vez que paso el tiempo de espera ilumino la parte adecuando
         * @param unused
         */
        protected void onPostExecute(final Void unused) {
            if (mHolder.position == mPosition) {
                glideAttatcher.attachedOnImaView(mHolder.image,img);
            }
            else {
                glideAttatcher.attachedOnImaView(mHolder.image,context.getResources().getDrawable(R.drawable.ic_agregar_nuevo));
            }
        }
    }

    private class CargarRow extends AsyncTask<Void, Void, Void> {
        private final int mPosition;
        private final ViewHolder mHolder;
        //String texto;
        Drawable img;

        public CargarRow(int position, ViewHolder holder) {
            mPosition = position;
            mHolder = holder;
        }

        @Override
        protected Void doInBackground(Void... voids) {
//            texto = json.getNombre(data.get(mPosition));
            try {
                if (data.size() > 0)
                    img = drawableManager.fetchDrawable((String) data.get(mPosition).get("imagePNGURL"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * Una vez que paso el tiempo de espera ilumino la parte adecuando
         * @param unused
         */
        protected void onPostExecute(final Void unused) {
            if (mHolder.position == mPosition) {
//                mHolder.imageTitle.setText(texto);
                glideAttatcher.attachedOnImaView(mHolder.image,img);
            }
        }
    }

    static class ViewHolder {
        TextView imageTitle;
        ImageView image;
        ImageView colorItem;
        int position;
    }

    public void filter(String charText) {
        String s=null;
        HashSet hashSet= new HashSet<String>();

        charText = charText.toLowerCase(Locale.getDefault());
        data.clear();
        if (charText.length() == 0) {
            data.addAll(data1);
        }
        else {
                String lang;
                SharedPreferences sharedPrefsDefault = PreferenceManager.getDefaultSharedPreferences(context);
                lang = sharedPrefsDefault.getString(context.getResources().getString(R.string.str_idioma), Locale.getDefault().getLanguage());
                for (JSONObject wp : data1) {
                if ((wp.optJSONObject("texto").optString(lang).toLowerCase(Locale.getDefault()).contains(charText)))//&&(headerText.get(i).toLowerCase(Locale.getDefault()).contains(wp.brandName.substring(0,1))))
                {
                    data.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}

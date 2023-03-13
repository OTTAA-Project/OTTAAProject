package com.stonefacesoft.ottaa.Bitmap;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.appcompat.content.res.AppCompatResources;

import com.stonefacesoft.ottaa.DrawableManager;
import com.stonefacesoft.ottaa.Interfaces.DrawableInterface;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;
import com.stonefacesoft.ottaa.utils.ConnectionDetector;
import com.stonefacesoft.pictogramslibrary.Classes.Pictogram;
import com.stonefacesoft.pictogramslibrary.view.PictoView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
/***
 * @version 2
 * @Since 15/06/2022
 * @author Gonzalo Juarez
 */

public class CombineImages implements DrawableInterface {
    private final ArrayList<Drawable> images;
    private final Context context;
    private final String TAG = "CombineImages";

    public CombineImages(Context context) {
        this.context = context;
        images = new ArrayList<>();
    }



    public Drawable loadPictogram(Json json,JSONObject child){
            Drawable imagen = null;
            imagen = json.getIcono(child);
            return imagen;
    }

    public PictoView preparePictoView(){
        PictoView pictoView = new PictoView(context);
        pictoView.setUpContext(context);
        pictoView.setUpGlideAttatcher(context);
        return pictoView;
    }

    public Bitmap getDrawableFromPictoView(JSONObject json,GestionarBitmap gestionarBitmap){
        Bitmap bitmap = null;
            PictoView pictoView = preparePictoView();
            pictoView.setPictogramsLibraryPictogram(new Pictogram(json, ConfigurarIdioma.getLanguaje()));
            Drawable draw = pictoView.getImageView().getDrawable();
            bitmap = gestionarBitmap.drawableToBitmap(draw);

        return bitmap;
    }
    public Drawable getDrawableToPhrase(JSONObject json){
            PictoView pictoView = preparePictoView();
            pictoView.setPictogramsLibraryPictogram(new Pictogram(json, ConfigurarIdioma.getLanguaje()));
            return pictoView.getImageView().getDrawable();
    }

    public ArrayList<Drawable> getImages() {
        return images;
    }
    public Drawable loadPictogramsLogo(JSONObject json) {
        Drawable nube = AppCompatResources.getDrawable(context, R.drawable.ic_baseline_cloud_download_24_big);//para evitar que no funcionen las frases mas usadas se pone el icono de la nube
        Drawable icon = null;
        if (json != null) {
                if(ConnectionDetector.isNetworkAvailable(context)) {
                    DrawableManager drawableManager = new DrawableManager();
                    try {
                      icon =  drawableManager.fetchDrawable(json.getJSONObject("imagen").getString("urlFoto"));
                    } catch (JSONException e) {
                        Log.e(TAG, "exception: "+e.getMessage() );
                       icon = nube;
                    }

            }
        }
        return icon;
    }

    public Drawable getOnlineImage(Drawable resource,Drawable resourceAux){
        Drawable drawable = resource;
            if(drawable == null)
                drawable = resourceAux;
        return drawable;
    }

    @Override
    public Drawable getDrawable(Drawable drawable) {
        return drawable;
    }

    @Override
    public void fetchDrawable(Drawable drawable) {
        images.add(drawable);

    }

    @Override
    public void fetchDrawable(Drawable drawable, int position) {
        images.add(position,drawable);
    }

}

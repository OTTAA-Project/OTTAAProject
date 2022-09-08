package com.stonefacesoft.ottaa.Bitmap;

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
import com.stonefacesoft.pictogramslibrary.Classes.Pictogram;
import com.stonefacesoft.pictogramslibrary.view.PictoView;

import org.json.JSONObject;

import java.util.ArrayList;
/***
 * @version 2
 * @Since 15/06/2022
 * @author Gonzalo Juarez
 */

public class CombineImages implements DrawableInterface {
    private ArrayList<Drawable> images;
    private Context context;
    private final String TAG = "CombineImages";

    public CombineImages(Context context){
        this.context = context;
        images = new ArrayList<>();
    }

    public void loadPictogram(Json json,JSONObject child)throws Exception{

            Drawable nube = AppCompatResources.getDrawable(context, R.drawable.ic_baseline_cloud_download_24_big);//para evitar que no funcionen las frases mas usadas se pone el icono de la nube
            Drawable imagen = json.getIconWithNullOption(child);
            if(imagen != null)
                images.add(imagen);
            else
                loadPictogramsLogo(child,imagen,nube);


    }

    public PictoView preparePictoView() throws Exception{
        PictoView pictoView = new PictoView(context);
        pictoView.setUpContext(context);
        pictoView.setUpGlideAttatcher(context);
        return pictoView;
    }

    public Bitmap getDrawableFromPictoView(JSONObject json,GestionarBitmap gestionarBitmap){
        Bitmap bitmap = null;
        try {
            PictoView pictoView = preparePictoView();
            pictoView.setPictogramsLibraryPictogram(new Pictogram(json, ConfigurarIdioma.getLanguaje()));
            Drawable draw = pictoView.getImageView().getDrawable();
            bitmap = gestionarBitmap.drawableToBitmap(draw);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public ArrayList<Drawable> getImages() {
        return images;
    }
    public void loadPictogramsLogo(JSONObject json,Drawable imagen,Drawable nube) {
        if (json != null) {
            PictoView pictoView = new PictoView(context);
            pictoView.setUpContext(context);
            pictoView.setUpGlideAttatcher(context);
            if(imagen == null){
                try {
                    DrawableManager drawableManager = new DrawableManager();
                    Drawable drawable = drawableManager.fetchDrawable(json.getJSONObject("imagen").getString("urlFoto"),this);
                } catch (Exception ex) {
                    images.add(nube);
                }
            }
        }
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
}

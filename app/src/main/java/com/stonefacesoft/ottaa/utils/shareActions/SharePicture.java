package com.stonefacesoft.ottaa.utils.shareActions;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.stonefacesoft.ottaa.Bitmap.CombineImages;
import com.stonefacesoft.ottaa.Bitmap.GestionarBitmap;
import com.stonefacesoft.ottaa.BuildConfig;
import com.stonefacesoft.ottaa.DrawableManager;
import com.stonefacesoft.ottaa.Interfaces.LoadOnlinePictograms;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SharePicture extends ShareAction{

    private final ArrayList<JSONObject> historial;
    private final GestionarBitmap gestionarBitmap;
    private Bitmap picture;
    private final Json json;


    public SharePicture(AppCompatActivity mContext, String phrase, ArrayList<JSONObject> historial) {
        super(mContext, phrase);
        this.historial = historial;
        this.gestionarBitmap = new GestionarBitmap(mContext);
        json = Json.getInstance();
    }

    @Override
    public void prepareFile() {
        super.prepareFile();
        sentMessage.setType("image/*");
        preparePicture();

    }

    public void preparePicture(){
        gestionarBitmap.setImagenes();
        gestionarBitmap.setNoTemp(true);
        if (historial != null) {
            for (int i = 0; i < this.historial.size(); i++) {
                Log.d("jsonfile", this.historial.get(i).toString());

                CombineImages combineImages = new CombineImages(mContext);
                final int position = i ;
                LoadOnlinePictograms loadOnlinePictograms = new LoadOnlinePictograms() {
                    @Override
                    public void preparePictograms() {
                        try {
                            Drawable draw = null;
                            draw = json.getIconWithNullOption(json.getPictoFromId2(historial.get(position).getInt("id")));
                            if (draw != null) {
                                Bitmap archivo = gestionarBitmap.drawableToBitmap(draw);
                                picture = archivo;
                            }else{
                                picture = combineImages.getDrawableFromPictoView(json.getPictoFromId2(historial.get(position).getInt("id")),gestionarBitmap);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        loadPictograms(picture);
                    }

                    @Override
                    public void loadPictograms(Bitmap bitmap) {
                        if(bitmap != null) {
                            gestionarBitmap.getImagenes().add(picture);
                        }else{
                            Bitmap aux = gestionarBitmap.drawableToBitmap(mContext.getResources().getDrawable(R.drawable.ic_baseline_cloud_download_24_big));
                            gestionarBitmap.getImagenes().add(aux);
                        }
                        FileIsCreated();
                    }

                    @Override
                    public void FileIsCreated() {
                        gestionarBitmap.getIdjson().add(historial.get(position));
                    }

                    @Override
                    public void FileIsCreated(Bitmap bitmap) {

                    }
                };

                loadOnlinePictograms.preparePictograms();

            }
            if (gestionarBitmap.getImagenes().size() > 0) {
                gestionarBitmap.setNombre("imagen.png");
                gestionarBitmap.setTexto(phrase);
                gestionarBitmap.createImage(new LoadOnlinePictograms() {
                    @Override
                    public void preparePictograms() {

                    }

                    @Override
                    public void loadPictograms(Bitmap bitmap) {
                        file = gestionarBitmap.getImgs();
                    }

                    @Override
                    public void FileIsCreated() {
                        if (file.exists()) {
                            try {
                                share();
                            }catch (Exception ex){
                                Log.e(TAG, "FileIsCreated: " );
                            }
                        }

                    }

                    @Override
                    public void FileIsCreated(Bitmap bitmap) {

                    }
                });
            }

        } else {
            Log.d(TAG, "compartirImagenes: ");
        }
    }

    @Override
    public void share() {
        sentMessage.putExtra(Intent.EXTRA_TEXT, phrase);
        if (Build.VERSION.SDK_INT >= 30)
            sentMessage.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".fileprovider", gestionarBitmap.getImgs()));
        else
            sentMessage.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(gestionarBitmap.getImgs()));
        mContext.startActivity(Intent.createChooser(sentMessage,mContext.getResources().getString(R.string.pref_enviar)));
    }
}

package com.stonefacesoft.ottaa;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.stonefacesoft.ottaa.Bitmap.CombineImages;
import com.stonefacesoft.ottaa.Bitmap.GestionarBitmap;
import com.stonefacesoft.ottaa.Interfaces.LoadOnlinePictograms;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.utils.exceptions.FiveMbException;
import com.stonefacesoft.ottaa.utils.textToSpeech;
import com.stonefacesoft.pictogramslibrary.utils.GlideAttatcher;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;

//import cafe.adriel.androidaudioconverter.AndroidAudioConverter;
//import cafe.adriel.androidaudioconverter.callback.IConvertCallback;
//import cafe.adriel.androidaudioconverter.callback.ILoadCallback;
//import cafe.adriel.androidaudioconverter.model.AudioFormat;

/**
 * Created by gonzalo on 1/26/18.
 */

public class CompartirArchivos {
    private final Context mContext;
    //   private Dialog dialog;
    private final Json json;
    private final GestionarBitmap gestionarBitmap;
    private final textToSpeech myTTS;
    private final String TAG = "CompartirArchivos_";
    private final String text = "";
    private File audio;
    private ArrayList<JSONObject> historial;
    private Bitmap imagen;
    private boolean actionShare;
    private File file;
    private GlideAttatcher attatcher;


    public CompartirArchivos(Context context1, textToSpeech myTTS) {
        this.mContext = context1;
        this.gestionarBitmap = new GestionarBitmap(mContext);
        actionShare = true;
        attatcher = new GlideAttatcher(mContext);
        Json.getInstance().setmContext(mContext);
        this.json = Json.getInstance();
        this.myTTS = myTTS;

    }

    public void compartirAudioPictogramas() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        Uri uri = Uri.fromFile(file);
        sharingIntent.setType("audio/*");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
        mContext.startActivity(sharingIntent);
    }

    //metodo para tomar los pictogramas
    public void setHistorial(ArrayList<JSONObject> historial) {
        this.historial = historial;
    }

    public void compartirTexto(String Oracion) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, Oracion);
        mContext.startActivity(Intent.createChooser(sharingIntent, mContext.getResources().getString(R.string.pref_enviar)));

    }

    //metodo para compartir las imagenes
    public void compartirImagenes(String Oracion) throws FiveMbException {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        sharingIntent.setType("*/*");
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
                                imagen = archivo;
                            }else{
                                imagen = combineImages.getDrawableFromPictoView(json.getPictoFromId2(historial.get(position).getInt("id")),gestionarBitmap);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        loadPictograms(imagen);
                    }

                    @Override
                    public void loadPictograms(Bitmap bitmap) {
                        if(bitmap != null) {
                            gestionarBitmap.getImagenes().add(imagen);
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
                };

                loadOnlinePictograms.preparePictograms();

            }
            if (gestionarBitmap.getImagenes().size() > 0) {
                gestionarBitmap.setNombre("imagen.png");
                gestionarBitmap.setTexto(Oracion);
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
                            sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(gestionarBitmap.getImgs()));
                            sharingIntent.putExtra(Intent.EXTRA_TEXT, Oracion);
                            mContext.startActivity(Intent.createChooser(sharingIntent, mContext.getResources().getString(R.string.pref_enviar)));
                        }

                    }
                });
            }

        } else {
            Log.d(TAG, "compartirImagenes: ");
        }
    }


    //metodo para elegir si comparto audio o video
    public void seleccionarFormato(final String Oracion) {
        Dialog dialog = new Dialog(this.mContext);
        dialog.setTitle(R.string.action_share);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_compartir_frases);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        Log.d("oracion1", Oracion);

        ImageButton compartirAudio = dialog.getWindow().findViewById(R.id.Camara);//boton para compartir audio
        ImageButton compartirImagen = dialog.getWindow().findViewById(R.id.Galeria);//boton para compartir imagen
        ImageButton compartirTexto = dialog.getWindow().findViewById(R.id.Texto); //boton para compartir texto

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(dialog);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        if (!Oracion.isEmpty()) {
            //Use this function from the tts, to storage a file temporal

        } else {
            Toast.makeText(mContext, "Genere una frase para compartir.", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
        // the audio files is shared when  click on the button

        compartirAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    file = File.createTempFile("audio", ".wav", mContext.getExternalCacheDir());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Bundle params = new Bundle();
                params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, Oracion);
                synchronized (file) {
                }
                myTTS.synthesizeToFile(Oracion, params, file);
                compartirAudioPictogramas();
            }
        });

        //the image files is shared when click on the button
        compartirImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    compartirImagenes(Oracion);
                } catch (FiveMbException e) {

                    e.printStackTrace();
                }catch (Exception ex){
                }
            }
        });
        compartirTexto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compartirTexto(Oracion);
            }
        });

        //digo que se pueda cancelar si se toca afuera de los bordes con el fin de que se pueda salir de esto
        dialog.setCanceledOnTouchOutside(true);


    }

    public void waitUserFile(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                compartirAudioPictogramas();
            }
        },150);
    }


}
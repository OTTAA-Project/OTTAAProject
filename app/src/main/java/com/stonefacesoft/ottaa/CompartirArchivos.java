package com.stonefacesoft.ottaa;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.stonefacesoft.ottaa.Interfaces.AudioTransformationListener;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.utils.exceptions.FiveMbException;
import com.stonefacesoft.ottaa.utils.shareActions.ShareAudio;
import com.stonefacesoft.ottaa.utils.shareActions.SharePicture;
import com.stonefacesoft.ottaa.utils.shareActions.ShareText;
import com.stonefacesoft.ottaa.utils.textToSpeech;
import com.stonefacesoft.pictogramslibrary.utils.GlideAttatcher;

import org.json.JSONObject;

import java.io.File;
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
    private final AppCompatActivity mContext;
    //   private Dialog dialog;

    private final textToSpeech myTTS;
    private final String TAG = "CompartirArchivos_";
    private final String text = "";
    private File audio;
    private ArrayList<JSONObject> historial;
    private Bitmap imagen;
    private final boolean actionShare;
    private File file;
    private final GlideAttatcher attatcher;
    private final AudioTransformationListener transformerListener;



    public CompartirArchivos(AppCompatActivity context1, textToSpeech myTTS, AudioTransformationListener transformerListener) {
        this.transformerListener = transformerListener;
        this.mContext = context1;
        actionShare = true;
        attatcher = new GlideAttatcher(mContext);
        Json.getInstance().setmContext(mContext);
        this.myTTS = myTTS;

    }


    //metodo para tomar los pictogramas
    public void setHistorial(ArrayList<JSONObject> historial) {
        this.historial = historial;
    }



    //metodo para compartir las imagenes
    public void compartirImagenes(String Oracion) throws FiveMbException {
       new SharePicture(mContext,Oracion,historial).prepareFile();
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
                new ShareAudio(mContext,Oracion,myTTS,transformerListener,dialog).prepareFile();
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
                new ShareText(mContext,Oracion).prepareFile();
            }
        });

        //digo que se pueda cancelar si se toca afuera de los bordes con el fin de que se pueda salir de esto
        dialog.setCanceledOnTouchOutside(true);


    }

}
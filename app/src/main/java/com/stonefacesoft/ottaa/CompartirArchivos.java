package com.stonefacesoft.ottaa;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.stonefacesoft.ottaa.Bitmap.GestionarBitmap;
import com.stonefacesoft.ottaa.Interfaces.TTSListener;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.utils.exceptions.FiveMbException;
import com.stonefacesoft.ottaa.utils.textToSpeech;

import org.json.JSONException;
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
    private File audio;
    private ArrayList<JSONObject> historial;
    private Bitmap imagen;
    private final Context mContext;
    private boolean actionShare;
    //   private Dialog dialog;
    private final Json json;
    private final GestionarBitmap gestionarBitmap;
    private File file;
    private final textToSpeech myTTS;
    private final String TAG = "CompartirArchivos_";

    public CompartirArchivos(Context context1, textToSpeech myTTS) {
        this.mContext = context1;
        this.gestionarBitmap = new GestionarBitmap(mContext);

        Json.getInstance().setmContext(mContext);
        this.json = Json.getInstance();

        this.myTTS = myTTS;

//        AndroidAudioConverter.load(mContext, new ILoadCallback() {
//            @Override
//            public void onSuccess() {
//                // Great!
//                Log.e(TAG + "Audio", "cargado");
//
//
//            }
//
//            @Override
//            public void onFailure(Exception error) {
//                // FFmpeg is not supported by device
//                Log.e(TAG + "error", "couldn't create the audio file");
//            }
//        });


    }

    public void compartirAudioPictogramas(File file) {
        actionShare = true;
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        sharingIntent.setType("audio/*");
        Uri uri =  Uri.fromFile(file);
        if(file.canWrite())
        sharingIntent.putExtra(Intent.EXTRA_STREAM,uri);
        mContext.startActivity(sharingIntent);
        //convertAudio(file);


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
                Drawable draw = null;
                try {
                    draw = json.getIcono(json.getPictoFromId2(this.historial.get(i).getInt("id")));
                    if (draw != null) {
                        Bitmap archivo = gestionarBitmap.drawableToBitmap(draw);
                        imagen = archivo;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                gestionarBitmap.getImagenes().add(imagen);
                gestionarBitmap.getIdjson().add(historial.get(i));

            }
            if (gestionarBitmap.getImagenes().size() > 0) {
                gestionarBitmap.setNombre("imagen.png");
                gestionarBitmap.setTexto(Oracion);
                gestionarBitmap.generarImagenesMasUsadas();
            }
            File file = gestionarBitmap.getImgs();
            if (file.exists()) {
                sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(gestionarBitmap.getImgs()));
                sharingIntent.putExtra(Intent.EXTRA_TEXT, Oracion);
                mContext.startActivity(Intent.createChooser(sharingIntent, mContext.getResources().getString(R.string.pref_enviar)));
            }
        } else {
            Log.d(TAG, "compartirImagenes: ");
        }
    }


    public void convertAudio(File file) {

        /**
         *  Update with a valid audio file!
         *  Supported formats: {@link AndroidAudioConverter.AudioFormat}
         */

//        IConvertCallback callback = new IConvertCallback() {
//            @Override
//            public void onSuccess(File convertedFile) {
//                audio = convertedFile;
//                Log.e("paths1", convertedFile.getAbsolutePath());
//
//                if (actionShare) {
//                    final Intent share = new Intent(Intent.ACTION_SEND);
//                    share.setType("audio/.mp3");
//                    if (audio != null) {
//                        share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(audio));
//                        mContext.startActivity(Intent.createChooser(share, mContext.getResources().getString(R.string.pref_enviar)));
//                    }
//
//                }
//
//
//            }
//
//            @Override
//            public void onFailure(Exception error) {
//                Log.e("paths", error.getMessage());
//            }
//        };

//        AndroidAudioConverter.with(mContext)
//                .setFile(file)
//                .setFormat(AudioFormat.MP3)
//                .setCallback(callback)
//                .convert();
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


        if (Oracion.isEmpty()){
            Toast.makeText(mContext, "Genere una frase para compartir.", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
        //compartirAudio.setEnabled(false);
        // the audio files is shared when  click on the button

        compartirAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myTTS.getUtilsTTS().setTtsListener(new TTSListener() {
                    @Override
                    public void TTSonDone() {

                    }

                    @Override
                    public void TTSOnAudioAvailable() {
                        compartirAudioPictogramas(file);
                    }

                    @Override
                    public void TTSonError() {

                    }
                });

                    File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                            + "/Android/data/"
                            + mContext.getPackageName()
                            + "/Files");
                    //direccionn real donde se va a ubicar el archivo
                    String path = (mediaStorageDir.getPath() + File.separator +"oracion.wav");
                    file = new File(path);
                myTTS.grabar(file,Oracion);

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


}
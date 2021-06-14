package com.stonefacesoft.ottaa.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import com.stonefacesoft.ottaa.utils.Firebase.AnalyticsFirebase;
import com.stonefacesoft.ottaa.utils.Ttsutils.UtilsTTS;

import java.io.File;
import java.util.HashMap;


/**
 * Created by gonzalo on 1/15/18.
 */

public class textToSpeech {

    private final CustomToast alerta;
    private final SharedPreferences sharedPrefsDefault;
    private final Context context;
    private boolean esprincipal;
    private String oracion;
    private File file;
    private String outputFile;
    private final UtilsTTS prepare;

    public textToSpeech(Context context) {
        this.context = context;
        alerta = CustomToast.getInstance(context);
        this.sharedPrefsDefault = PreferenceManager.getDefaultSharedPreferences(context);
        prepare=new UtilsTTS(this.context,alerta,sharedPrefsDefault);

    }



    public void hablar(String frase) {
        Log.e("texToSpeech_hablar", "Hablar");
        this.oracion = frase;
        prepare.hablarConDialogo(oracion);

    }


    public void hablarConListener(String frase) {
        Log.e("texToSpeech_hablar", "Hablar");
        this.oracion = frase;
        prepare.hablarConDialogoListener(oracion);

    }

    public void hablarSinMostrarFrase(String frase) {
        Log.e("texToSpeech_hablar", "Hablar");
        this.oracion = frase;
        prepare.hablar(oracion);

    }

    public void hablar(String frase, AnalyticsFirebase mTracker) {
        Log.e("texToSpeech_hablar", "Hablar con Tracker");
        this.oracion = frase;
        alerta.mostrarFrase(frase, mTracker);
        prepare.hablar(frase);

    }


    public void mute() {
        prepare.getmTTS().stop();
    }

    public UtilsTTS getUtilsTTS(){
        return prepare;
    }

    public void grabar(File file,String phrase) {
           prepare.SyntetizeFile(phrase,file);
    }

    public File devolverPathAudio() {
        return file;
    }

    public void reproducir() {
        if (file.exists()) {
            MediaPlayer media = MediaPlayer.create(context, Uri.fromFile(file));
            media.start();
            media.getDuration();

        }
    }

    private HashMap<String, String> createParams(String utterance) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utterance);
        return params;
    }


    public HashMap<String, File> createParams1(File utterance) {
        HashMap<String, File> params = new HashMap<>();
        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utterance);
        return params;
    }


    public void compartirAudio() {

        String textToShare = oracion;
        TextToSpeech hablar=prepare.getmTTS();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            hablar.synthesizeToFile(textToShare, null, file, file.getAbsolutePath());
        }
        hablar.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
            }

            @Override
            public void onAudioAvailable(String utteranceId, byte[] audio) {
                final Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("*/*");
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(file.getAbsolutePath()));

                if (shareIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(shareIntent);
                }
                super.onAudioAvailable(utteranceId, audio);
            }

            @Override
            public void onDone(String utteranceId) {


            }

            @Override
            public void onError(String utteranceId) {
            }
        });
    }

    public TextToSpeech getTTS() {
        return prepare.getmTTS();
    }

    public void mostrarAlerta(String mensaje) {
        alerta.mostrarFrase(mensaje);
    }

    public void mostrarAlerta(String mensaje, AnalyticsFirebase mTracker) {
        alerta.mostrarFrase(mensaje, mTracker);

    }




}


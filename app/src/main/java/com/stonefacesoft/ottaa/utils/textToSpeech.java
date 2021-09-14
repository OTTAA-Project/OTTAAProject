package com.stonefacesoft.ottaa.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

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
    public textToSpeech(AppCompatActivity context) {
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




    public void hablarSinMostrarFrase(String frase) {
        Log.e("texToSpeech_hablar", "Hablar");
        this.oracion = frase;
        prepare.hablar(oracion);
    }

    public void hablar(String frase, AnalyticsFirebase mTracker) {
        Log.e("texToSpeech_hablar", "Hablar con Tracker");
        this.oracion = frase;
        prepare.hablarConDialogo(frase);
        mTracker.customEvents("Talk","Principal","Created Phrase");

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



    public TextToSpeech getTTS() {
        return prepare.getmTTS();
    }

    public void mostrarAlerta(String mensaje) {
        alerta.mostrarFrase(mensaje);
    }

    public void mostrarAlerta(String mensaje, AnalyticsFirebase mTracker) {
        alerta.mostrarFrase(mensaje);
        mTracker.customEvents("Talk","Principal","Created Phrase");
    }






}


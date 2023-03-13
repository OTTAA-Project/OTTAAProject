package com.stonefacesoft.ottaa.utils.Ttsutils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import com.stonefacesoft.ottaa.Interfaces.TTSListener;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;
import com.stonefacesoft.ottaa.utils.CustomToast;
import com.stonefacesoft.ottaa.utils.Firebase.CrashlyticsUtils;
import com.stonefacesoft.ottaa.utils.verificarPaqueteInstalado;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
/**
 * @author Gonzalo Juarez
 * <h3>Objetive</h3>
 * <p>This class implement the action to speak in high voice the phrase or the pictogram name.</p>
 * <h3>How to declare</h3>
 *   <code>
 *       Declare UtilsTTS utilTts=new UtilsTTS(Context,CustomToast,SharedPreferences);
 *   </code>
 *   <h3>Examples of implementation</h3>
 *     <h4>Prepare the tts engine</h4>
*      <code>utilsTts.preparEngineTTS();</code>
 *      <h4>Talk with dialog</h4>
 *      <code>String word="hello";</code> <br>
 *      <code>utilsTts.hablarConDialogo(world);</code>
 *      <h4>Talk without the dialog screen</h4>
 *      <code>String word="hello";</code> <br>
 *      <code>utilsTts.hablar(world);</code>
 *
 * */
public class UtilsTTS {
    protected SharedPreferences sharedPrefsDefault;
    protected TextToSpeech mTTS;
    protected Context mContext;
    protected CustomToast alerta;
    protected boolean speak;
    private TTSListener ttsListener;
    private final String id="TTSOTTAAID";
    private static UtilsTTS _UtilsTTS;
    private final UtteranceProgressListener utteranceProgressListener = new UtteranceProgressListener() {
        @Override
        public void onStart(String utteranceId) {

        }

        @Override
        public void onDone(String utteranceId) {
            if(utteranceId.equalsIgnoreCase(id)&&ttsListener!=null){
                ttsListener.TTSonDone();
                ttsListener= null;
            }
        }

        @Override
        public void onError(String utteranceId) {
            if(ttsListener!=null) {
                ttsListener.TTSonError();
                ttsListener =null;
            }
        }
    };



    public UtilsTTS(Context mContext, CustomToast alerta, SharedPreferences sharedPrefsDefault){
        this.mContext=mContext;
        this.alerta=alerta;
        this.sharedPrefsDefault=sharedPrefsDefault;
        this.mTTS=new TextToSpeech(this.mContext, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    preparEngineTTS();
                    speak=true;
                } else if (status == TextToSpeech.ERROR) {
                    speak=false;
                }
            }
        });
        this.mTTS.setOnUtteranceProgressListener(utteranceProgressListener);
    }
    /**
     * <h5>Objetive :</h5>
     * <p> Prepare the Text to Speech Engine.</p>
     * */
    public void preparEngineTTS(){
        try {

            // Primero se fija en que idioma est&aacute configurada la traducci&oacuten para poner el TTS en el idioma correcto
//            Locale localeTTS = new Locale(sharedPrefsDefault.getString(mContext.getString(R
//                    .string.str_idioma),Locale.getDefault().getLanguage()),"en");
            Locale localeTTS = new Locale(sharedPrefsDefault.getString(mContext.getString(R.string
                    .str_idioma), "en"));
            Log.d("texToSpeech_refreshtts", "GetCountry: " + localeTTS.getCountry());
            Log.d("texToSpeech_refreshtts", "GetLanguage: " + localeTTS.getLanguage());

            switch (ConfigurarIdioma.getLanguaje()) {
                case "ar":
                    Log.d("texToSpeech_refreshtts", "ARABE");
//                    TODO chequear primero si ya esta insltalada
                   /* boolean instaladoAr = new verificarPaqueteInstalado(mContext).estaInstalado("com.acapelagroup.android.tts");
                    if (!instaladoAr) {
                        Log.d("instalarAcapella", "si");
                        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.acapelagroup.android.tts")).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    } else {
                        Log.d("instalarAcapella", "no");

                        mTTS.setEngineByPackageName("com.acapelagroup.android.tts");
                    }*/
                    localeTTS = new Locale("ar");
                    break;

                case "ca":
                    Log.d("texToSpeech_refreshtts", "CATALA");
//                    TODO chequear primero si ya esta insltalada
                    boolean instaladoCat = new verificarPaqueteInstalado(mContext).estaInstalado("com.acapelagroup.android.tts");
                    if (!instaladoCat) {
                        Intent goToMarketCa = new Intent(Intent.ACTION_VIEW)
                                .setData(Uri.parse("market://details?id=com.acapelagroup.android.tts"));
                        mContext.startActivity(goToMarketCa);
                    } else {
                        mTTS.setEngineByPackageName("com.acapelagroup.android.tts");
                    }
                    break;

                case "es":
                    Log.d("texToSpeech_refreshtts", "ESPANOL");
                    localeTTS = new Locale("spa");
                    break;

                default:

                    if (mTTS.isLanguageAvailable(localeTTS) == TextToSpeech.LANG_AVAILABLE) {
                        Log.d("texToSpeech_refreshtts", "OTRO IDIOMA " + localeTTS);
                        mTTS.setEngineByPackageName("com.google.android.tts");
                        break;
                    } else {
                        Log.d("texToSpeech_refreshtts", "IDIOMA NO DISPONIBLE ");

                        Intent installTTSIntent = new Intent();
                        installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                        mTTS.setEngineByPackageName("com.google.android.tts");
                        mContext.startActivity(installTTSIntent);
                    }
            }

            mTTS.setLanguage(localeTTS);

        } catch (Exception e) {
            Log.d("texToSpeech_refreshtts", e.getMessage());
            CrashlyticsUtils.getInstance().getCrashlytics().recordException(e);
            alerta.mostrarFrase(mContext.getString(R.string.pref_error_TTS));
        }
        // Seteamos los valores de velocidad y tono por defecto
        setUpTTSVOICE();
    }

    private void setUpTTSVOICE(){
        float pitch = sharedPrefsDefault.getInt(mContext.getString(R.string.str_pitch_tts), 10);
        float vel = sharedPrefsDefault.getInt(mContext.getString(R.string.str_velocidad_tts), 10);
        //verificamos si el tts personalizado no esta activado, en caso de no estarlo se setea la velocidad del  tts a su velocidad estandar
        if (!sharedPrefsDefault.getBoolean(mContext.getString(R.string.bool_TTS), false)) {
            pitch = 10;
            vel = 10;
        }
        // esto se encarga de establecer la velocidad del tts  y el tono de voz
        mTTS.setPitch(pitch / 10);
        mTTS.setSpeechRate(vel / 10);
    }

    /**
     * <h5>Objetive :</h5>
     * <p>Speak the phrase without dialog.</p>
     * @param frase Text to said in high voice with the TTS.
     * */
    public void hablar(String frase){
        speak=true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Bundle bundle = new Bundle();
            bundle.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"");
            mTTS.speak(frase, TextToSpeech.QUEUE_FLUSH, bundle, id);
        } else {
            HashMap<String, String> hashTts = new HashMap<String, String>();
            hashTts.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, id);
            mTTS.speak(frase, TextToSpeech.QUEUE_FLUSH, hashTts);
        }
    }
    /**
     * <h5>Objetive :</h5>
     * <p>Speak the phrase with dialog screen.</p>
     * @param frase Text to said in high voice with the TTS.
     * */
    public void hablarConDialogo(String frase){
        Log.d("texToSpeech_hablar", "Hablar");
        alerta.mostrarFrase(frase);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Bundle bundle = new Bundle();
            bundle.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"");
            mTTS.speak(frase, TextToSpeech.QUEUE_FLUSH, bundle, id);
        } else {
            HashMap<String, String> hashTts = new HashMap<String, String>();
            hashTts.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, id);
            mTTS.speak(frase, TextToSpeech.QUEUE_FLUSH, hashTts);
        }
    }


    public void SyntetizeFile(String frase, File file){
        alerta.mostrarFrase(frase);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Bundle bundle = new Bundle();
            bundle.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"");
            bundle.putInt(TextToSpeech.Engine.KEY_PARAM_STREAM, TextToSpeech.Engine.DEFAULT_STREAM);
            bundle.putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME,0.5f);
            bundle.putFloat(TextToSpeech.Engine.KEY_PARAM_PAN,0);
            mTTS.synthesizeToFile(frase, bundle,file, id);
        } else {
            HashMap<String, String> hashTts = new HashMap<String, String>();
            hashTts.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, id);
            mTTS.synthesizeToFile(frase,hashTts,file.getAbsolutePath());
        }
    }


    public TextToSpeech getmTTS() {
        return mTTS;
    }

    public boolean isSpeak() {
        return speak;
    }

    public void setTtsListener(TTSListener ttsListener){
        this.ttsListener = ttsListener;
    }


}

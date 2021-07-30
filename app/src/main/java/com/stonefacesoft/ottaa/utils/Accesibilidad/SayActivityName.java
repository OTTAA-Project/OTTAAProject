package com.stonefacesoft.ottaa.utils.Accesibilidad;

import android.content.Context;

import com.stonefacesoft.ottaa.utils.textToSpeech;

public class SayActivityName {
    private boolean isEnabled = true;
    private textToSpeech myTTS;
    private Context mContext;
    private static SayActivityName _sayActivityName;

    private SayActivityName(Context mContext){
        this.mContext = mContext;
        myTTS = new textToSpeech(mContext);
    }
    public static synchronized SayActivityName getInstance(Context mContext){
        if(_sayActivityName == null)
            _sayActivityName = new SayActivityName(mContext);
        return _sayActivityName;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
    public void sayTitle(String title){
        if(isEnabled)
            myTTS.hablar(title);
    }

}

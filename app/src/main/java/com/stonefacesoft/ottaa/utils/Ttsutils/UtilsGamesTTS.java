package com.stonefacesoft.ottaa.utils.Ttsutils;

import android.content.Context;
import android.content.SharedPreferences;
import android.speech.tts.TextToSpeech;

import com.stonefacesoft.ottaa.Interfaces.Lock_Unlocked_Pictograms;
import com.stonefacesoft.ottaa.utils.CustomToast;

public class UtilsGamesTTS extends UtilsTTS  {
    public Lock_Unlocked_Pictograms lockUnlockedPictograms;
    public UtilsGamesTTS(Context mContext, TextToSpeech mTTS, CustomToast alerta, SharedPreferences sharedPrefsDefault,Lock_Unlocked_Pictograms lock_unlocked_pictograms) {
        super(mContext, alerta, sharedPrefsDefault);
        this.lockUnlockedPictograms=lock_unlocked_pictograms;
    }

    @Override
    public void hablar(String frase) {
        super.hablar(frase);
        while (mTTS.isSpeaking())
            lockUnlockedPictograms.lockPictogram(mTTS.isSpeaking());
        lockUnlockedPictograms.unlockPictogram();
    }
    public void stop(){
        mTTS.shutdown();
    }
}

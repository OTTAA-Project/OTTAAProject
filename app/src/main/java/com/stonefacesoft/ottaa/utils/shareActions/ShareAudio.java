package com.stonefacesoft.ottaa.utils.shareActions;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.stonefacesoft.ottaa.BuildConfig;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.Audio.AudioEncoder;
import com.stonefacesoft.ottaa.utils.textToSpeech;

import java.util.Random;
//https://stackoverflow.com/questions/23299582/android-tts-synthesizetofile-doesnt-work review that example
public class ShareAudio extends ShareAction implements com.stonefacesoft.ottaa.Interfaces.ShareAudio {

    private final textToSpeech myTTS;
    private AudioEncoder audioEncoder;
    private int result;

    public ShareAudio(Context mContext, String phrase, textToSpeech myTTS) {
        super(mContext, phrase);
        this.myTTS = myTTS;
    }

    @Override
    public void prepareFile() {
        sentMessage.setType("audio/mp4");
        prepareAudio();
    }

    private void prepareAudio(){
        if(audioEncoder == null)
            audioEncoder = new AudioEncoder(mContext);
        audioEncoder.createFile();

        String  mostRecentUtteranceID = (new Random().nextInt() % 12000) + "";
        Bundle params = new Bundle();
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"audio.wav");
        params.putString(TextToSpeech.Engine.KEY_PARAM_STREAM,String.valueOf(AudioManager.MODE_NORMAL));

        result = myTTS.getTTS().setOnUtteranceProgressListener(new UtteranceProgressListener() {
            boolean isFinish;
            @Override
            public void onStart(String utteranceId) {
                Log.e(TAG, "onStart: "+utteranceId );
            }

            @Override
            public void onDone(String utteranceId) {
                if(isFinish)
                    getResult();
                else
                    Log.d(TAG, "onDone: false");
            }


            @Override
            public void onError(String utteranceId) {
            }

            @Override
            public void onAudioAvailable(String utteranceId, byte[] audio) {
                super.onAudioAvailable(utteranceId, audio);
                isFinish = true;
            }

        });
        myTTS.getTTS().synthesizeToFile(phrase+"  ",params,audioEncoder.getFile(),"audio.m4a");
    }

    @Override
    public void share() {
        if (Build.VERSION.SDK_INT >= 30)
            sentMessage.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".fileprovider", audioEncoder.getFile()));
        else
            sentMessage.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(audioEncoder.getFile()));
        sentMessage.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        mContext.startActivity(Intent.createChooser(sentMessage,mContext.getResources().getString(R.string.pref_enviar)));
    }

    @Override
    public void getResult() {
        if(result == TextToSpeech.SUCCESS)
            share();
    }
}

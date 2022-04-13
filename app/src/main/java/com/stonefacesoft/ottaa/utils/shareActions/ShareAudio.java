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

public class ShareAudio extends ShareAction{

    private final textToSpeech myTTS;
    private AudioEncoder audioEncoder;

    public ShareAudio(Context mContext, String phrase, textToSpeech myTTS) {
        super(mContext, phrase);
        this.myTTS = myTTS;
    }

    @Override
    public void prepareFile() {
        sentMessage.setType("audio/wav");
        prepareAudio();
    }

    private void prepareAudio(){
        if(audioEncoder == null)
            audioEncoder = new AudioEncoder(mContext);
        audioEncoder.createFile();
        file = audioEncoder.getFile();
        String  mostRecentUtteranceID = (new Random().nextInt() % 12000) + "";
        Bundle params = new Bundle();
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"audio.wav");
        params.putString(TextToSpeech.Engine.KEY_PARAM_STREAM,String.valueOf(AudioManager.MODE_NORMAL));

        myTTS.getTTS().setOnUtteranceProgressListener(new UtteranceProgressListener() {
            boolean isFinish;
            @Override
            public void onStart(String utteranceId) {
                Log.e(TAG, "onStart: "+utteranceId );
            }

            @Override
            public void onDone(String utteranceId) {
                if(isFinish){
                    //compartirAudioPictogramas();
                   share();
                }
            }

            @Override
            public void onError(String utteranceId) {
            }

            @Override
            public void onAudioAvailable(String utteranceId, byte[] audio) {
                super.onAudioAvailable(utteranceId, audio);
                isFinish = true;
            }

            @Override
            public void onStop(String utteranceId, boolean interrupted) {
                super.onStop(utteranceId, interrupted);
            }

            @Override
            public void onRangeStart(String utteranceId, int start, int end, int frame) {
                super.onRangeStart(utteranceId, start, end, frame);
            }
        });
        myTTS.getTTS().synthesizeToFile(phrase+"  ",params,file,"audio.wav");
    }

    @Override
    public void share() {
        if (Build.VERSION.SDK_INT >= 30)
            sentMessage.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".fileprovider", file));
        else
            sentMessage.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        sentMessage.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        mContext.startActivity(Intent.createChooser(sentMessage,mContext.getResources().getString(R.string.pref_enviar)));
    }
}

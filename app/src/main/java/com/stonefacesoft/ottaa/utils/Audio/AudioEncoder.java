package com.stonefacesoft.ottaa.utils.Audio;

import static java.nio.ByteOrder.BIG_ENDIAN;
import static java.nio.ByteOrder.LITTLE_ENDIAN;

import android.content.Context;
import android.media.MediaDataSource;
import android.media.MediaRecorder;
import android.net.rtp.AudioCodec;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.RequiresApi;


import java.io.File;
import java.io.IOException;


public class AudioEncoder {
    private Context mContext;
    private File file;


    public AudioEncoder( Context mContext){
        this.mContext = mContext;
    }
    public void playFile(){
        MediaPlayerAudio playerAudio = new MediaPlayerAudio(mContext);
        playerAudio.playedCustomFile(file);
        Log.e("AudioEncoder", "playFile: "+playerAudio.getPlayer().getTrackInfo());
    }

    public File getFile() {
        return file;
    }

    public void createFile(String name){

        try {
            file = File.createTempFile(name,".wav");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Context getmContext() {
        return mContext;
    }


}
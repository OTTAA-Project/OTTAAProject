package com.stonefacesoft.ottaa.utils.Audio;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class AudioEncoder {
    private Context mContext;
    private File file;
    private  AudioEncoder audioEncoder;

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

    public void createFile(){
        File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        try {
            file = File.createTempFile("audio",".wav",storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Context getmContext() {
        return mContext;
    }
}
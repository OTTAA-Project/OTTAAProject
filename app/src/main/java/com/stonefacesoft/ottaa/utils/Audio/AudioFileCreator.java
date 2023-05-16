package com.stonefacesoft.ottaa.utils.Audio;


import android.content.Context;

import android.media.MediaRecorder;
import android.net.rtp.AudioCodec;
import android.os.Environment;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.transformer.Transformer;
import com.google.android.exoplayer2.util.MimeTypes;
import com.stonefacesoft.ottaa.Interfaces.AudioTransformationListener;

import java.io.File;
import java.io.IOException;



public class AudioFileCreator extends AppCompatActivity {
    private final Context mContext;
    private File file = null;
    private File aux = null;




    public AudioFileCreator(Context mContext){
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
            file = getFile(name,".wav");
            file.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            aux = getFile(name,".ogg");
            aux.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public File getFile(String name,String suffix) throws IOException {
        return File.createTempFile(name,suffix,mContext.getExternalCacheDir());
    }

    public Context getmContext() {
        return mContext;
    }

    public void transformation(AudioTransformationListener transformationListener, Transformer.Listener listener){
      if(file!=null)
        transformationListener.startAudioTransformation(listener,file.getPath(),aux.getPath());
    }

    public File getAux() {
        return aux;
    }

}
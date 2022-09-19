package com.stonefacesoft.ottaa.utils.Audio;


import android.content.Context;

import android.media.MediaMuxer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerLibraryInfo;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.transformer.Codec;
import com.google.android.exoplayer2.transformer.Transformer;
import com.google.android.exoplayer2.util.MimeTypes;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;

import javax.activation.MimeType;


public class AudioEncoder extends AppCompatActivity {
    private Context mContext;
    private File file;
    private File aux;



    public AudioEncoder(Context mContext){
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

        File directory = new  File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC),"ottaaaproject");
        if(!directory.exists())
            directory.mkdir();
        aux = new File(directory.getPath()+File.separator+name+".mp3");
    }

    public Context getmContext() {
        return mContext;
    }

    public void transformation(Transformer transformer,Transformer.Listener listener,File file){
        MediaItem mediaItem =  MediaItem.fromUri(Uri.fromFile(file));
        try {
            transformer.startTransformation(mediaItem, String.valueOf(aux));
        } catch (IOException e) {
            e.printStackTrace();
        }
// Start the transformation.

    }

    public File getAux() {
        return aux;
    }

}
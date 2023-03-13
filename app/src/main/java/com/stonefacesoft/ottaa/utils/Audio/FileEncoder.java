package com.stonefacesoft.ottaa.utils.Audio;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.transformer.TransformationRequest;
import com.google.android.exoplayer2.transformer.Transformer;
import com.google.android.exoplayer2.util.MimeTypes;

import java.io.File;
import java.io.IOException;



public class FileEncoder {

    private Transformer transformer;
    private final Context mContext;

    public FileEncoder(Context mContext){
      this.mContext = mContext;
    }
    /*
    * That method transform the file
    * */
    public void encodeAudioFile(Transformer.Listener listener, String filePath, String locationPath) {
        Handler handler = new Handler(mContext.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                TransformationRequest transformationRequest = new TransformationRequest.Builder().build();
                transformationRequest.buildUpon().setAudioMimeType(MimeTypes.AUDIO_OGG);
                transformer = new  Transformer.Builder(mContext).setLooper(Looper.myLooper()).setTransformationRequest(transformationRequest).addListener(listener).build();
                MediaItem mediaItem = MediaItem.fromUri(Uri.fromFile(new File(filePath)));
                transformer.startTransformation(mediaItem,locationPath);

            }
        });


    }

}

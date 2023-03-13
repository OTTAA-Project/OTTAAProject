package com.stonefacesoft.ottaa.Interfaces;


import com.google.android.exoplayer2.transformer.Transformer;

public interface AudioTransformationListener {
    void startAudioTransformation(Transformer.Listener listener, String filePath, String locationPath);
}

package com.stonefacesoft.ottaa.utils.Audio;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;

public class FileAudioConverter  {
    private MediaCodec codec;
    private FileOutputStream fos;

    public FileAudioConverter(){

    }
    private String getEncoderNamesForType(String mime) {
        int n = MediaCodecList.getCodecCount();
        String name = null;
        for (int i = 0; i < n; ++i) {
            MediaCodecInfo info = MediaCodecList.getCodecInfoAt(i);
            if (!info.isEncoder()) {
                continue;
            }
            if (!info.getName().startsWith("")) {
                continue;
            }
            String[] supportedTypes = info.getSupportedTypes();
            for (int j = 0; j < supportedTypes.length; ++j) {
                if (supportedTypes[j].equalsIgnoreCase(mime)) {
                    name = info.getName();
                    return name;
                }
            }
        }
        return name;
    }

    public void flow(byte[] bytes, int size) {
        try {
            ByteBuffer[] inputBuffers = codec.getInputBuffers();
            ByteBuffer[] outputBuffers = codec.getOutputBuffers();
            int inputBufferIndex = codec.dequeueInputBuffer(-1);
            if (inputBufferIndex >= 0) {
                ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
                inputBuffer.clear();
                inputBuffer.put(bytes);
                codec.queueInputBuffer(inputBufferIndex, 0, size, 0, 0);
            }

            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            int outputBufferIndex = codec.dequeueOutputBuffer(bufferInfo, 0);

            while (outputBufferIndex >= 0) {
                ByteBuffer outputBuffer = outputBuffers[outputBufferIndex];
                byte[] outData = new byte[bufferInfo.size];
                outputBuffer.get(outData);
                fos.write(outData, 0, outData.length);

                codec.releaseOutputBuffer(outputBufferIndex, false);
                outputBufferIndex = codec.dequeueOutputBuffer(bufferInfo, 0);

            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}

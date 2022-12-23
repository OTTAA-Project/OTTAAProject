package com.stonefacesoft.ottaa.utils.Audio;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.pictogramslibrary.utils.ValidateContext;

import java.io.File;

public class MediaPlayerAudio implements MediaPlayer.OnPreparedListener {
    private final Context mContext;
    private MediaPlayer player;
    private float audioLevel = 0.5f;
    private boolean muted;
    private boolean complete;

    public MediaPlayerAudio(Context mContext) {
        this.mContext = mContext;

    }

    private void playSound(boolean completo) {
        if (player != null) {
            setPlayerSound();
            player.start();
            if (!completo)
                player.seekTo(1100);
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    if (player != null && !player.isLooping()) {
                        mediaPlayer.reset();
                        mediaPlayer.release();
                        player = null;
                    }

                }
            });
        }
    }

    public void playYesSound() {
        if(ValidateContext.isValidContext(mContext)){
            createPlayer(mContext,R.raw.yay);
            prepareListener();
        }
    }

    public void playYupi1Sound() {
        if(ValidateContext.isValidContext(mContext)){
            createPlayer(mContext,R.raw.yupi_1);
            prepareListener();
        }
    }

    public void playYupi2Sound() {
        if(ValidateContext.isValidContext(mContext)) {
                createPlayer(mContext,R.raw.yupi_2);
                prepareListener();
        }
    }



    public void playOhOhSound() {
        if(ValidateContext.isValidContext(mContext)) {
            createPlayer(mContext,R.raw.ohoh);
            prepareListener();
        }
    }

    public void createPlayer(Context mContext,int resource){
        try{
            player = MediaPlayer.create(mContext,resource);
            complete = true;
        }catch (Exception ex){
            complete = false;
        }
    }

    public void prepareListener(){
        if(player!=null)
            player.setOnPreparedListener(this);
    }

    public void stop() {
        if (player != null)
            player.stop();
    }

    public void playMusic() {
        if(ValidateContext.isValidContext(mContext)) {
            player = MediaPlayer.create(mContext, R.raw.funckygroove);
            playSound(true);
            setLooping(true);
        }
    }

    public void setLooping(boolean value){
        if(player != null)
            player.setLooping(value);
    }
    public void setVolumenAudio(float value) {
        audioLevel = value;
    }

    public void resumeAudio() {
    }


    public void pauseAudio() {
        if (player != null && player.isPlaying())
            player.stop();
    }

    private void setPlayerSound() {
        if (player != null) {
            if (muted)
                player.setVolume(0f, 0f);
            else
                player.setVolume(audioLevel, audioLevel);
        }
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
        if (player != null)
            setPlayerSound();
    }

    public void playedCustomFile(File f) {
        if(ValidateContext.isValidContext(mContext)) {
            player = MediaPlayer.create(mContext, Uri.fromFile(f));
            playSound(true);
            player.setLooping(false);
        }
    }

    public MediaPlayer getPlayer() {
        return player;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        playSound(complete);
    }

    public void reset() {
        if(player!=null)
            player.reset();
    }
}

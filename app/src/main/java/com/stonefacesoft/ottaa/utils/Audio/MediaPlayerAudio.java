package com.stonefacesoft.ottaa.utils.Audio;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import com.stonefacesoft.ottaa.R;

import java.io.File;

public class MediaPlayerAudio implements MediaPlayer.OnPreparedListener {
    private MediaPlayer player;
    private final Context mContext;
    private float audioLevel=0.5f;
    private boolean muted;
    private boolean complete;
     public MediaPlayerAudio(Context mContext){
         this.mContext=mContext;

     }

     private void playSound(boolean completo){
        if(player!=null){
         setPlayerSound();
         player.start();
         if(!completo)
         player.seekTo(1100);
         player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
             @Override
             public void onCompletion(MediaPlayer mediaPlayer) {
                 if(!player.isLooping()){
                    mediaPlayer.reset();
                    mediaPlayer.release();
                    player=null;
                 }

             }
         });
        }
     }

     public void playYesSound(){
         player=MediaPlayer.create(mContext, R.raw.yay);
         complete=false;
         player.setOnPreparedListener(this);
     }

     public void playYupi1Sound(){
         player=MediaPlayer.create(mContext,R.raw.yupi_1);
         complete=true;
         player.setOnPreparedListener(this);
     }
     public void playYupi2Sound(){
         player=MediaPlayer.create(mContext,R.raw.yupi_2);
         complete=true;
         player.setOnPreparedListener(this);
     }

     public void playYouWin(){
         player=MediaPlayer.create(mContext,R.raw.you_win);
         complete=true;
         player.setOnPreparedListener(this);

     }

     public void playTadaSound(){
         player=MediaPlayer.create(mContext,R.raw.tada);
         complete=true;
         player.setOnPreparedListener(this);
     }


    public void playNoSound(){
        player=MediaPlayer.create(mContext, R.raw.wrong);
        complete=true;
        player.setOnPreparedListener(this);
    }

    public void playOhOhSound(){
        player=MediaPlayer.create(mContext, R.raw.ohoh);
        complete=true;
        player.setOnPreparedListener(this);
    }
    public void stop(){
         if(player!=null)
         player.stop();
    }

    public  void playMusic(){ player=MediaPlayer.create(mContext,R.raw.funckygroove);
        playSound(true);
        player.setLooping(true);
    }

    public void  setVolumenAudio(float value){
         audioLevel=value;
    }

    public void resumeAudio(){
    }


    public void pauseAudio(){
        if(player!=null&&player.isPlaying())
         player.stop();
    }
    private void setPlayerSound(){
        if(player!=null){
         if(muted)
             player.setVolume(0f,0f);
         else
             player.setVolume(audioLevel,audioLevel);
        }
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
        if(player!=null)
            setPlayerSound();
    }

    public void playedCustomFile(File f){
         player=MediaPlayer.create(mContext, Uri.fromFile(f));
        playSound(true);
        player.setLooping(false);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        playSound(complete);
    }
}

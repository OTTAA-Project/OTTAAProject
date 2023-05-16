package com.stonefacesoft.ottaa.utils.shareActions;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.stonefacesoft.ottaa.R;

import java.io.File;

public class ShareAction {
    protected Intent sentMessage;
    protected AppCompatActivity mContext;
    protected String phrase;
    protected File file;
    protected String TAG="ShareAction";

    public ShareAction(AppCompatActivity mContext,String phrase){
        sentMessage = new Intent(Intent.ACTION_SEND);
        this.mContext = mContext;
        this.phrase = phrase;
    }
    public void prepareFile(){

    }

    protected void share(){
        Intent.createChooser(sentMessage,mContext.getResources().getString(R.string.pref_enviar));
    }

}

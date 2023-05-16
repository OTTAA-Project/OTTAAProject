package com.stonefacesoft.ottaa.utils.shareActions;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.stonefacesoft.ottaa.R;

public class ShareText extends ShareAction{
    public ShareText(AppCompatActivity mContext, String phrase) {
        super(mContext, phrase);
    }

    @Override
    public void prepareFile() {
        sentMessage.setType("text/plain");
        sentMessage.putExtra(Intent.EXTRA_TEXT, phrase);
        share();
    }

    @Override
    public void share() {
        mContext.startActivity(Intent.createChooser(sentMessage, mContext.getResources().getString(R.string.pref_enviar)));

    }
}

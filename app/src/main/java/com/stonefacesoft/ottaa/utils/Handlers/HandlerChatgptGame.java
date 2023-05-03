package com.stonefacesoft.ottaa.utils.Handlers;

import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.stonefacesoft.ottaa.Games.TellAStory;
import com.stonefacesoft.ottaa.Principal;

public class HandlerChatgptGame extends HandlerUtils{

    private TellAStory tellAStory;
    public HandlerChatgptGame(TellAStory tellAStory){
        this.tellAStory=tellAStory;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        switch (msg.what){
            case TRANSLATEDPHRASE:
                tellAStory.setStory((String) msg.obj);
                tellAStory.talkAction();
                break;
            case SHOWTEXT:
                tellAStory.setText();
                break;
            default:
                tellAStory.setExecuteChatGPT(true);
                Log.d("TellStory", "handleMessage: "+(String) msg.obj);
        }
    }
}

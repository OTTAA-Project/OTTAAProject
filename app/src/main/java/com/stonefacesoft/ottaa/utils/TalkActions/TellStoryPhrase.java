package com.stonefacesoft.ottaa.utils.TalkActions;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.airbnb.lottie.LottieAnimationView;
import com.stonefacesoft.ottaa.Games.TellAStory;
import com.stonefacesoft.ottaa.Principal;
import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;
import com.stonefacesoft.ottaa.utils.Handlers.HandlerChatgptGame;
import com.stonefacesoft.ottaa.utils.Handlers.HandlerComunicationClass;
import com.stonefacesoft.ottaa.utils.Handlers.HandlerUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TellStoryPhrase {
    private static final String TAG = "TraducirFrase";

    private final int option;
    private TalkChatGPT talkChatGPT;



    public TellStoryPhrase(TellAStory tellAStory, LottieAnimationView animationView, Context mContext, String Oracion, final int value) {
        option = value;
        HandlerChatgptGame handlerComunicationClass = new HandlerChatgptGame(tellAStory);
        talkChatGPT = new TalkChatGPT(animationView,mContext,Oracion,Oracion,handlerComunicationClass);

    }

    public void executeChatGpt(String promt){
        talkChatGPT.executeChatGPT(chatGptObject(promt), option);
    }

    private JSONObject chatGptObject(String text){
        JSONObject object= new JSONObject();
        try {
            String promt= text;
            Log.d(TAG, "chatGptObject: "+ promt);
            object.put("model","text-davinci-003");
            object.put("prompt",promt);
            object.put("temperature",0.7);
            object.put("max_tokens",1000);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }



}

package com.stonefacesoft.ottaa.utils.TalkActions;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.stonefacesoft.ottaa.Principal;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.Handlers.HandlerComunicationClass;
import com.stonefacesoft.ottaa.utils.Handlers.HandlerUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ProcessPhrase {
    private static final String TAG = "TraducirFrase";

    private final int option;
    private TalkChatGPT talkChatGPT;


    public ProcessPhrase(Principal principal, SharedPreferences sharedPreferences, LottieAnimationView animationView, Context mContext, String Oracion,final int value) {
        option = value;
        HandlerComunicationClass handlerComunicationClass = new HandlerComunicationClass(principal);
        talkChatGPT = new TalkChatGPT(animationView,mContext,Oracion,Oracion,handlerComunicationClass);

    }




    public void executeChatGPT(JSONObject jsonObject){
        talkChatGPT.executeChatGPT(jsonObject,option);
    }

    public void executeViterbi(JSONObject jsonObject){
       talkChatGPT.executeViterbi(jsonObject,option);
    }

    /*Languages code for Google Translate.
                https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes
            -Arabic         ar
            -Catala         ca
            -Danish         da
            -French         fr
            -Guarani        gn
            -Italian        it
            -Norwegian      nb
            -Portugues      pt
            -Spanish        es
            -Swahili        sw
            */
    // automatically done on workerFirebase thread (separate from UI thread)

    public void setOracion(String oracion) {

        talkChatGPT.setOracion(oracion);
    }


}

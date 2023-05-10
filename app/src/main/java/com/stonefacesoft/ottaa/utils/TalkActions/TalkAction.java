package com.stonefacesoft.ottaa.utils.TalkActions;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.Handlers.HandlerUtils;

import org.json.JSONObject;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TalkAction {
    protected  volatile LottieAnimationView animationView;
    protected  volatile Context mContext;
    protected String Oracion = "";
    protected  String traduccion = "";
    protected volatile HandlerUtils handlerComunicationClass;
    protected String openAiURL = "";
    protected String viterviurl = "https://us-central1-ottaaproject-flutter.cloudfunctions.net/realiser/realise?PERSON=yo";

    public TalkAction(LottieAnimationView animationView, Context mContext, String oracion, String traduccion) {
        this.animationView = animationView;
        this.mContext = mContext;
        openAiURL = mContext.getString(R.string.chatGPT_api_key);
        Oracion = oracion;
        this.traduccion = traduccion;
    }

    protected void onPreExecute() {
          if(animationView!=null){
             animationView.setVisibility(View.VISIBLE);
             animationView.setAnimation("circle_loader.json");
             animationView.loop(true);
             animationView.playAnimation();
          }
        }

        public void executeChatGPT(JSONObject jsonObject){

        }

        public void executeChatGPT(JSONObject jsonObject,int option){

        }

        public void executeViterbi(JSONObject jsonObject){

        }

        public void executeViterbi(JSONObject jsonObject,int option){

        }

        protected void finishAnimationView(){
         if(animationView!=null&& animationView.isAnimating()){
            animationView.cancelAnimation();
            animationView.setVisibility(View.GONE);
          }
        }

    public String getOracion() {
        return Oracion;
    }

    public void setOracion(String oracion) {
        Oracion = oracion;
    }
}

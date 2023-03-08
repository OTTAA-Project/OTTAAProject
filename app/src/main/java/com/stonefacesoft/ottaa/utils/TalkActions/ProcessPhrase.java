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
import com.stonefacesoft.ottaa.utils.HandlerComunicationClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ProcessPhrase {
    private static final String TAG = "TraducirFrase";
    private final LottieAnimationView animationView;
    private final Context mContext;
    private final SharedPreferences sharedPrefsDefault;
    private String Oracion = "";
    private final String traduccion = "";
    private final HandlerComunicationClass handlerComunicationClass;
    private int option;

    public ProcessPhrase(Principal principal, SharedPreferences sharedPreferences, LottieAnimationView animationView, Context mContext, String Oracion,final int value) {
        this.mContext = mContext;
        this.sharedPrefsDefault = sharedPreferences;
        this.animationView = animationView;
        this.Oracion = Oracion;
        this.option = value;
        handlerComunicationClass = new HandlerComunicationClass(principal);


    }


    protected void onPreExecute() {

        animationView.setVisibility(View.VISIBLE);
        animationView.setAnimation("circle_loader.json");
        animationView.loop(true);
        animationView.playAnimation();
    }

    public void executeChatGPT(JSONObject jsonObject){
        Executor executor = Executors.newSingleThreadExecutor();
        onPreExecute();
        executor.execute(() -> {
            String url = "https://api.openai.com/v1/completions";
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(mContext);
            final JSONObject jsonBody;


            JsonObjectRequest myRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    jsonObject,

                    response -> {
                        Log.d(TAG, "onResponse: " + response);
                        try {
                            Log.e(TAG, "execute: "+ jsonObject.toString());
                            Log.e(TAG, "execute: "+ response.toString());
                            handlerComunicationClass.sendMessage(Message.obtain(handlerComunicationClass, option, response.getJSONArray("choices").getJSONObject(0).getString("text").replaceAll("\n"," ")));
                            animationView.cancelAnimation();
                            animationView.setVisibility(View.GONE);
                        } catch (Exception e) {
                            animationView.cancelAnimation();
                            animationView.setVisibility(View.GONE);
                            Log.e(TAG, "onResponse: Error: " + e.getMessage());
                            handlerComunicationClass.sendMessage(Message.obtain(handlerComunicationClass, HandlerComunicationClass.INTENTARDENUEVO));
                        }

                    },
                    error -> {
                        animationView.cancelAnimation();
                        animationView.setVisibility(View.GONE);
                        handlerComunicationClass.sendMessage(Message.obtain(handlerComunicationClass, HandlerComunicationClass.TEXTONOTRADUCIDO));
                        Log.e(TAG, "onErrorResponse: Error " + error.getMessage());
                    }) {

                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Bearer "+ mContext.getResources().getString(R.string.chatGPT_api_key));
                    return headers;
                }
            };

            queue.add(myRequest);

        });
    }
    public void executeViterbi(JSONObject jsonObject){
        Executor executor = Executors.newSingleThreadExecutor();
        onPreExecute();
        executor.execute(() -> {
            String url = "https://us-central1-ottaaproject-flutter.cloudfunctions.net/realiser/realise?PERSON=yo";
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(mContext);
            final JSONObject jsonBody;


            JsonObjectRequest myRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    jsonObject,

                    response -> {
                        Log.d(TAG, "onResponse: " + response);
                        try {
                            Log.e(TAG, "execute: "+ jsonObject.toString());
                            Log.e(TAG, "execute: "+ response.toString());
                            handlerComunicationClass.sendMessage(Message.obtain(handlerComunicationClass, option, response.getString("sentence")));
                            animationView.cancelAnimation();
                            animationView.setVisibility(View.GONE);
                        } catch (Exception e) {
                            animationView.cancelAnimation();
                            animationView.setVisibility(View.GONE);
                            Log.e(TAG, "onResponse: Error: " + e.getMessage());
                            handlerComunicationClass.sendMessage(Message.obtain(handlerComunicationClass, HandlerComunicationClass.INTENTARDENUEVO));
                        }

                    },
                    error -> {
                        animationView.cancelAnimation();
                        animationView.setVisibility(View.GONE);
                        handlerComunicationClass.sendMessage(Message.obtain(handlerComunicationClass, HandlerComunicationClass.TEXTONOTRADUCIDO));
                        Log.e(TAG, "onErrorResponse: Error " + error.getMessage());
                    }) {

                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    headers.put("Authorization", "apikey " + mContext.getResources().getString(R.string.google_translate_api_key));
                    return headers;
                }
            };

            queue.add(myRequest);

        });
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
        this.Oracion = oracion;
    }
}

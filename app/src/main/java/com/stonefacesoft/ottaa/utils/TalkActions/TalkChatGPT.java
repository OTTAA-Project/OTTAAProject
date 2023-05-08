package com.stonefacesoft.ottaa.utils.TalkActions;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.Handlers.HandlerComunicationClass;
import com.stonefacesoft.ottaa.utils.Handlers.HandlerUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TalkChatGPT extends TalkAction{
    private String TAG ="TALKCHATGPT";

    public TalkChatGPT(LottieAnimationView animationView, Context mContext, String oracion, String traduccion, HandlerUtils handlerComunicationClass) {
        super(animationView, mContext, oracion, traduccion);
        this.handlerComunicationClass = handlerComunicationClass;
    }

    @Override
    public void executeChatGPT(JSONObject jsonObject, int option) {

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

            onPreExecute();
        executor.execute(()->{
            RequestQueue queue = Volley.newRequestQueue(mContext);

            Log.e(TAG, "execute: "+ jsonObject.toString());
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    openAiURL,
                    jsonObject,
                    response -> {
                       try {
                            Log.e(TAG, "execute: "+ response.toString());
                           handlerComunicationClass.sendMessage(Message.obtain(handlerComunicationClass, option, response.getJSONArray("choices").getJSONObject(0).getString("text")));
                        } catch (JSONException e) {
                            Log.e(TAG, "onResponse: Error: " + e.getMessage());
                            handlerComunicationClass.sendMessage(Message.obtain(handlerComunicationClass, HandlerComunicationClass.TRYAGAIN));
                        }finally {
                           finishAnimationView();
                       }
                        handler.post(()->{
                            handlerComunicationClass.sendMessage(Message.obtain(handlerComunicationClass,HandlerUtils.SHOWTEXT));
                        });
                    },
                    error -> {
                        Log.e(TAG, "onErrorResponse: Error " + error.getMessage());
                        finishAnimationView();
                        handlerComunicationClass.sendMessage(Message.obtain(handlerComunicationClass, HandlerComunicationClass.TEXTNOTTRANSLATED));
                    }
                    ){
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Bearer "+ mContext.getResources().getString(R.string.chatGPT_api_key));
                    return headers;
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(request);

        });

    }



    @Override
    public void executeViterbi(JSONObject jsonObject,int option) {
        Executor executor = Executors.newSingleThreadExecutor();
        onPreExecute();
        executor.execute(() -> {
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(mContext);
            JsonObjectRequest myRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    viterviurl,
                    jsonObject,

                    response -> {
                        Log.d(TAG, "onResponse: " + response);
                        try {
                            Log.e(TAG, "execute: "+ jsonObject.toString());
                            Log.e(TAG, "execute: "+ response.toString());
                            handlerComunicationClass.sendMessage(Message.obtain(handlerComunicationClass, option, response.getString("sentence")));
                            finishAnimationView();
                        } catch (Exception e) {
                            finishAnimationView();
                            Log.e(TAG, "onResponse: Error: " + e.getMessage());
                            handlerComunicationClass.sendMessage(Message.obtain(handlerComunicationClass, HandlerComunicationClass.TRYAGAIN));
                        }

                    },
                    error -> {
                        finishAnimationView();
                        handlerComunicationClass.sendMessage(Message.obtain(handlerComunicationClass, HandlerComunicationClass.TEXTNOTTRANSLATED));
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

        });    }
}

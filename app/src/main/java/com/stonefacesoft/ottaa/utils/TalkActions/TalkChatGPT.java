package com.stonefacesoft.ottaa.utils.TalkActions;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.datatransport.cct.internal.LogEvent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.LogDescriptor;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.Handlers.HandlerComunicationClass;
import com.stonefacesoft.ottaa.utils.Handlers.HandlerUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TalkChatGPT extends TalkAction{
    private String TAG ="TALKCHATGPT";

    public TalkChatGPT(LottieAnimationView animationView, Context mContext, String oracion, String traduccion, HandlerUtils handlerComunicationClass) {
        super(animationView, mContext, oracion, traduccion);
        this.handlerComunicationClass = handlerComunicationClass;
    }

    @Override
    public void executeChatGPT(JSONObject jsonObject, int option) {
        onPreExecute();
        callFunction(jsonObject).addOnCompleteListener(new OnCompleteListener<Object>() {
            @Override
            public void onComplete(@NonNull Task<Object> task) {
                Exception e = task.getException();
                if (e instanceof FirebaseFunctionsException) {
                    FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                    FirebaseFunctionsException.Code code = ffe.getCode();
                    Object details = ffe.getDetails();
                    finishAnimationView();
                    handlerComunicationClass.sendMessage(Message.obtain(handlerComunicationClass, HandlerComunicationClass.CHATGPTERROR));
                }else{

                    try{
                        HashMap hashMap = (HashMap)task.getResult();
                        JSONObject result = new JSONObject(hashMap);
                        handlerComunicationClass.sendMessage(Message.obtain(handlerComunicationClass, option, result.getJSONArray("choices").getJSONObject(0).getString("text")));
                    }catch (Exception ex){
                        Log.e(TAG, "onComplete error: "+ex.getMessage() );
                    }finally {
                        finishAnimationView();
                        handlerComunicationClass.sendMessage(Message.obtain(handlerComunicationClass,HandlerUtils.SHOWTEXT));
                    }
                }

            }
        });


        /*Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());


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

                        });
                    },
                    error -> {
                        String message = error.getMessage();
                        Log.e(TAG, "onErrorResponse: Error " + message);
                        finishAnimationView();
                        if(message == null){
                            handlerComunicationClass.sendMessage(Message.obtain(handlerComunicationClass, HandlerComunicationClass.TEXTNOTTRANSLATED));
                        }else {
                            handlerComunicationClass.sendMessage(Message.obtain(handlerComunicationClass, HandlerComunicationClass.CHATGPTERROR));
                        }
                    }
                    ){
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(request);


        });*/

    }

    private Task<Object> callFunction(JSONObject object){
        FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();
        Map<String, Object> data = new HashMap<>();
        try {
            data.put("prompt", object.getString("prompt"));
        } catch (JSONException e) {
        }
        try {
            data.put("model", object.getString("model"));
        } catch (JSONException e) {
        }
        try {
            data.put("max_tokens", object.getInt("max_tokens"));
        } catch (JSONException e) {
        }
        try {
            data.put("temperature", object.getDouble("temperature"));
        } catch (JSONException e) {
        }
        data.put("push", true);
        return mFunctions.getHttpsCallable("openai").withTimeout(10, TimeUnit.SECONDS).call(object).continueWith(new Continuation<HttpsCallableResult, Object>() {
            @Override
            public Object then(@NonNull Task<HttpsCallableResult> task) throws Exception {

                return task.getResult().getData();

            }




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

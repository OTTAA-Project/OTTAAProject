package com.stonefacesoft.ottaa.utils;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TraducirFrase {
    private static final String TAG = "TraducirFrase";

    private final LottieAnimationView animationView;
    private final Context mContext;
    private final SharedPreferences sharedPrefsDefault;
    private String Oracion = "";
    private final String traduccion = "";
    private final HandlerComunicationClass handlerComunicationClass;

    public TraducirFrase(Principal principal, SharedPreferences sharedPreferences, LottieAnimationView animationView, Context mContext, String Oracion) {
        this.mContext = mContext;
        this.sharedPrefsDefault = sharedPreferences;
        this.animationView = animationView;
        this.Oracion = Oracion;
        handlerComunicationClass = new HandlerComunicationClass(principal);

    }


    protected void onPreExecute() {

        animationView.setVisibility(View.VISIBLE);
        animationView.setAnimation("circle_loader.json");
        animationView.loop(true);
        animationView.playAnimation();
    }

    public void execute(){
        Executor executor = Executors.newSingleThreadExecutor();
        onPreExecute();
        executor.execute(() -> {
            String url = "https://translation.googleapis.com/language/translate/v2?key="+mContext.getResources().getString(R.string.google_translate_api_key);
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(mContext);
            final JSONObject jsonBody;

            try {
                jsonBody = new JSONObject("{\"q\":\"" + Oracion + "\"," +
                        "\"target\":\"" + sharedPrefsDefault.getString(mContext.getString(R.string
                        .str_idioma), "en") + "\"" + ",\"source\":\"en\"}");

                JsonObjectRequest myRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        url,
                        jsonBody,

                        response -> {
                            Log.d(TAG, "onResponse: " + response);
                            try {
                                Log.e("Principal_Trad_GTransO1", "" + traduccion);
                                Log.d(TAG, "onResponse: Traduccion: " + traduccion);
                                handlerComunicationClass.sendMessage(Message.obtain(handlerComunicationClass, HandlerComunicationClass.FraseTraducida, response.getJSONObject("data").getJSONArray
                                        ("translations").getJSONObject(0).getString("translatedText")));
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

            } catch (JSONException e) {
                Log.e(TAG, "doInBackground: Error" + e.getMessage());
            }

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
package com.stonefacesoft.ottaa.utils;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.stonefacesoft.ottaa.Interfaces.translateInterface;
import com.stonefacesoft.ottaa.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class traducirTexto {

    private final Context mContext;

    private String texto="";
    private String mTarget="";
    private String mSource="";
    private String traduction;
    private translateInterface anInterface;
    private final String TAG="TraducirTexto";

    public traducirTexto(Context context) {
        mContext=context;
    }





    public void traducirIdioma(translateInterface interfaces,String textoATraducir, String idiomaSource, String idiomaTarget,boolean traducir){
        this.texto = textoATraducir;
        this.mSource = idiomaSource;
        this.mTarget = idiomaTarget;
        anInterface=interfaces;
        if(traducir) {
            if (!idiomaSource.equals(idiomaTarget)) {
                new traducirIdioma().execute();
            }
            else
                {
                    texto=textoATraducir;
                    anInterface.onTextoTraducido(true);
                }
        }
        else{
                texto=textoATraducir;
                anInterface.onTextoTraducido(true);
        }

    }
    // can use UI thread here

    protected void onPostExecute(final Void unused) {
        this.texto=traduction;
    }



    public String getTexto() {
        return texto;
    }

   /* @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }*/

    public String getmTarget() {
        return mTarget;
    }

    public void setmTarget(String mTarget) {
        this.mTarget = mTarget;
    }

    public String getmSource() {
        return mSource;
    }

    public void setmSource(String mSource) {
        this.mSource = mSource;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
    public class traducirIdioma
    {


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
      public void execute(){
          Executor executor = Executors.newSingleThreadExecutor();
          executor.execute(() -> {
              String url = "https://translation.googleapis.com/language/translate/v2?key="+mContext.getResources().getString(R.string.google_translate_api_key);
              // Instantiate the RequestQueue.
              RequestQueue queue = Volley.newRequestQueue(mContext);
              final JSONObject jsonBody;
              try {
                  jsonBody = new JSONObject("{\"q\":\""+texto+"\"," +
                          "\"target\":\""+mTarget+"\"" + ",\"source\":\""+mSource+"\"}");

                  JsonObjectRequest myRequest = new JsonObjectRequest(
                          Request.Method.POST,
                          url,
                          jsonBody,

                          response -> {
                              Log.d(TAG,"Principal_Trad_GTrans"+" : "+response);
                              try{
//                                  Oracion = response.getJSONArray("translations").getJSONObject(0).getString("translatedText");
                                  texto = response.getJSONObject("data").getJSONArray("translations").getJSONObject(0).getString("translatedText");
                                  anInterface.onTextoTraducido(true);
                                  Log.d("Principal_Trad_GTransOr",""+texto);

                              }catch (Exception e){
                                  Log.e(TAG,"Principal_Trad_GTransOr : "+ e);
                                  //    Toast.makeText(Principal.this, R.string.tryAgain, Toast.LENGTH_SHORT).show();
                              }

                          },
                          error -> {
                              Log.e("Principal_Trad_DoInBack","ResponseError:"+error);
                              CustomToast.getInstance(mContext).mostrarFrase(mContext.getString(R.string.str_error_translateGoogle));
                              new Handler().postDelayed(() -> anInterface.onTextoTraducido(true), 4000);
                          }) {

                      @Override
                      public Map<String, String> getHeaders() {
                          HashMap<String, String> headers = new HashMap<>();
                          headers.put("Content-Type", "application/json; charset=utf-8");
                          headers.put("Authorization", "apikey "+mContext.getResources().getString(R.string.google_translate_api_key));
                          return headers;
                      }
                  };

                  queue.add(myRequest);

              } catch (JSONException e) {
                  e.printStackTrace();
              }

          });
      }

    }
}

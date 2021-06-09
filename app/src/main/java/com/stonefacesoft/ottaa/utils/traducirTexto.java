package com.stonefacesoft.ottaa.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.stonefacesoft.ottaa.Interfaces.translateInterface;
import com.stonefacesoft.ottaa.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class traducirTexto {

    private Context mContext;

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
                new traducirIdioma().doInBackground();
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
    public class traducirIdioma extends AsyncTask<Void,Void,Void>
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
    @Override
    protected Void doInBackground(Void... voids) {
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

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG,"Principal_Trad_GTrans"+" : "+response);
                            try{
//                                  Oracion = response.getJSONArray("translations").getJSONObject(0).getString("translatedText");
                                texto = response.getJSONObject("data").getJSONArray("translations").getJSONObject(0).getString("translatedText");
                                anInterface.onTextoTraducido(true);
                                Log.d("Principal_Trad_GTransOr",""+texto);

                            }catch (Exception e){
                                Log.e(TAG,"Principal_Trad_GTransOr : "+e.toString());
                                //    Toast.makeText(Principal.this, R.string.tryAgain, Toast.LENGTH_SHORT).show();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Principal_Trad_DoInBack","ResponseError:"+error);
                            new CustomToast(mContext).mostrarFrase(mContext.getString(R.string.str_error_translateGoogle));
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    anInterface.onTextoTraducido(true);
                                }

                            }, 4000);
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    headers.put("Authorization", "apikey "+mContext.getResources().getString(R.string.google_translate_api_key));
                    return headers;
                }
            };

            queue.add(myRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    }
}

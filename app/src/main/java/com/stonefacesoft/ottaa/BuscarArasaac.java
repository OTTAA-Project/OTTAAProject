package com.stonefacesoft.ottaa;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.datatransport.cct.internal.LogEvent;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.google.api.LogDescriptor;
import com.stonefacesoft.ottaa.Interfaces.SearchAraasacPictogram;
import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;
import com.stonefacesoft.ottaa.utils.HttpsTrustManager;
import com.stonefacesoft.ottaa.utils.JSONutils;
import com.stonefacesoft.ottaa.utils.StringFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;


/**
 * Created by Cristian on 21/6/2016.
 */
public class BuscarArasaac {
    private final String TAG = "BuscarArasaac";
    SearchAraasacPictogram searchAraasacPictogram;
    private boolean useVolley;

    public void setSearchAraasacPictogram(SearchAraasacPictogram searchAraasacPictogram) {
        this.searchAraasacPictogram = searchAraasacPictogram;
    }

    public void HacerBusqueda(String texto, String lang, Context context){
        String direction;
        String key = context.getResources().getString(R.string.galeria_araasac_api_key);
        String aux =texto.replaceAll(" ","_");
        String globalsymbol = "https://globalsymbols.com/api/v1/labels/search?query="+texto+"&symbolset=arasaac&language="+ ConfigurarIdioma.getLanguageIso6393(lang);
        String araasac = "http://old.arasaac.org/api/index.php?callback=json&language="+lang+"&word="+aux+"%&catalog=colorpictos&thumbnailsize=150&TXTlocate=4&KEY="+key;
        /*if(Build.VERSION.SDK_INT<= Build.VERSION_CODES.M){
            JSONutils.setUseVolley(false);
            requestHttp(araasac);
        }
        else{*/
        HttpsTrustManager.allowAllSSL();
        JSONutils.setUseVolley(true);
        requestVolley(context,globalsymbol);
        //}
    }


    private void requestVolley(Context context,String request){
        RequestQueue requestQueue = Volley.newRequestQueue(context,new HurlStack());
        requestQueue.start();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, request, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray object = new JSONArray(response);
                    searchAraasacPictogram.findPictogramsJsonArray(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("BuscarAraasac","Error :" + error.getMessage());
            }
        });

        requestQueue.add(stringRequest);
    }




    private void requestHttp(String request){
        try {
            StringBuilder resultado = new StringBuilder();
            String encodeUrl = request.replaceAll("%","%25");
            URL url = new URL(encodeUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response;
            while ((response = reader.readLine())!=null){
                resultado.append(response);
            }
            reader.close();
            JSONObject object = new JSONObject(resultado.toString());
            searchAraasacPictogram.findPictogramsJsonObject(object);
        } catch (Exception ex) {
            Log.e("error_BuscarAraasac", ex.toString());
        }
    }

    private HostnameVerifier mVerifier(String host){
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                return hv.verify(host,session);
            }
        };
        return hostnameVerifier;
    }




}
